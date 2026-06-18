package uz.katm.report.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import uz.katm.report.client.MipReportClient;
import uz.katm.report.domain.record.ClaimData;
import uz.katm.report.domain.record.CreditReportRequest;
import uz.katm.report.domain.record.CreditReportResponse;
import uz.katm.report.domain.record.ReportFormat;
import uz.katm.report.domain.record.SystemReportRequest;
import uz.katm.report.exception.CreditServiceException;
import uz.katm.report.repository.CreditRepository;
import uz.katm.report.util.ConverterUtils;
import uz.katm.report.util.ReportFormatConverter;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Единый диспетчер отчётов по reportId (перенос InternalServiceImpl/BkiServiceImpl.getReport switch).
 * Валидирует запрос, резолвит субъекта по заявке (GET_DATA_BY_CLAIM) и маршрутизирует:
 * локальные источники (UzCard/Humo/Beeline/КИ) — напрямую, MIP/ПЦД-каналы — через katm-mip (MipReportClient).
 * Результат приводится к запрошенному формату (XML/JSON).
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ReportDispatchService {

    private final CreditService creditService;
    private final CreditRepository creditRepository;
    private final UzCardService uzCardService;
    private final HumoService humoService;
    private final BeelineService beelineService;
    private final MipReportClient mip;
    private final ReportFormatConverter formatConverter;

    // Отчёты, к которым монолит дописывал блок наличия КИ (salary/einvoice PE-LE/cashback).
    private static final Set<Integer> AVAILABILITY_REPORTS = Set.of(48, 326, 327, 328);

    public CreditReportResponse dispatch(String head, String code, SystemReportRequest req) {
        log.info("Диспетчер отчёта: head={}, code={}, reportId={}, claimId={}", head, code, req.reportId(), req.claimId());
        creditService.validateReportRequest(head, code, req.reportId(), req.claimId(), req.ownerId(), req.loanSubject());

        ClaimData claim = creditRepository.getClaimData(head, code, req.claimId(), String.valueOf(req.reportId()));
        if (!claim.isSuccess()) {
            return new CreditReportResponse(claim.code(), claim.message(), null, null);
        }

        ReportFormat format = ReportFormat.fromCode(req.reportFormat());
        String pin = nz(claim.pinfl());
        String tin = nz(claim.tin());
        String number = nz(req.number());
        int sub = req.subReportId() != null ? req.subReportId() : 1;

        // UzCard (40) — асинхронная инициация (card.counts.create); журналирование ошибок не применяется (как в монолите).
        if (req.reportId() == 40) {
            return uzCardService.submitUzCardScoring(head, code, toCreditRequest(claim, req));
        }

        // Внешние шлюзы (Humo/Beeline/MIP-ПЦД): при ошибке журналируем неудачную попытку
        // (перенос saveReportError → logCreditReport: повторный GET_CREDIT_REPORT с ошибкой в P_MIP_RESPONSE).
        String raw;
        try {
            raw = fetchGateway(head, code, req, claim, pin, tin, number, sub);
        } catch (RuntimeException e) {
            journalError(head, code, req, claim, e.getMessage());
            throw e;
        }

        if (raw != null) {
            // К отчётам salary/einvoice/cashback дописываем блок наличия КИ (монолит availabilityOfCreditRepC).
            if (AVAILABILITY_REPORTS.contains(req.reportId())) {
                raw = appendAvailability(head, code, req.claimId(), raw);
            }
            return wrap(raw, format);
        }

        // По умолчанию — кредитный отчёт (КИ) через процедуру (это сама процедура отчёта, не внешний шлюз).
        CreditReportResponse response = creditRepository.getCreditReport(head, code, toCreditRequest(claim, req));
        return response.withReport(formatConverter.convertXmlDoc(response.report(), format));
    }

    /** Маршрутизация на внешний шлюз (Humo/Beeline локально, остальное — katm-mip). Возвращает JSON-строку или null. */
    private String fetchGateway(String head, String code, SystemReportRequest req, ClaimData claim,
                                String pin, String tin, String number, int sub) {
        return switch (req.reportId()) {
            // --- Локальные шлюзы katm-report ---
            case 42 -> humoService.getHumo(head, code, req.claimId(), claim);
            case 97 -> beelineService.getScor(claim);
            case 98 -> beelineService.getPhones(claim);
            case 99 -> beelineService.getVerification(claim);
            case 89 -> beelineService.getRoaming(claim);
            // --- MIP core ---
            case 31 -> mip.get("/api/v1/mip/legal/entity/" + enc(tin));
            case 32 -> mip.get("/api/v1/mip/citizens/" + enc(pin));
            case 33 -> mip.get("/api/v1/mip/citizens/" + enc(pin) + "/address");
            case 34 -> mip.get("/api/v1/mip/tax/tin/" + enc(tin));
            case 35 -> mip.get("/api/v1/mip/tax/objects/" + enc(tin));
            case 44 -> mip.get("/api/v1/mip/tax/debt/" + enc(tin));
            case 39 -> mip.get("/api/v1/mip/debtor/individual?series=" + enc(nz(claim.docSeries()))
                    + "&number=" + enc(nz(claim.docNumber())));
            case 41 -> mip.get("/api/v1/mip/debtor/juridical/" + enc(tin));
            case 70 -> mip.get("/api/v1/mip/debtor/water?pin=" + enc(pin) + "&tin=" + enc(tin));
            // --- Пенсии ---
            case 75 -> mip.get("/api/v1/mip/pension/assign/" + enc(pin));
            case 76, 74 -> mip.get("/api/v1/mip/pension/size/" + enc(pin));
            case 78 -> mip.get("/api/v1/mip/pension/request/" + enc(pin));
            // --- ГНК ---
            case 48 -> mip.get(gnk("salary", "pinfl=" + enc(pin) + "&tin=" + enc(tin), req));
            case 50 -> mip.get(gnk("dividend", "pinfl=" + enc(pin) + "&tin=" + enc(tin) + year(req), req));
            case 52 -> mip.get(gnk("rent", "pinfl=" + enc(pin), req));
            case 60 -> mip.get(gnk("account-report-1", "tin=" + enc(tin) + year(req) + period(req), req));
            case 62 -> mip.get(gnk("account-report-2", "tin=" + enc(tin) + year(req) + period(req), req));
            case 63 -> mip.get(gnk("ip-info", "pinfl=" + enc(pin), req));
            case 91 -> mip.get("/api/v1/mip/gnk/info?pinfl=" + enc(pin));
            case 46 -> mip.get(gnk("pe-info", "pinfl=" + enc(pin) + "&tin=" + enc(tin), req));
            case 58 -> mip.get(gnk("juridical-info", "tin=" + enc(tin), req));
            case 54 -> mip.get(gnk("pe-subject", "pinfl=" + enc(pin) + year(req), req));
            case 64 -> mip.get(gnk("tax-report", "tin=" + enc(tin) + year(req) + period(req), req));
            case 66 -> mip.get(gnk("staff-count", "tin=" + enc(tin) + year(req), req));
            case 68 -> mip.get(gnk("nds", "tin=" + enc(tin), req));
            case 45 -> mip.get(gnk("self-employment", "pinfl=" + enc(pin) + "&tin=" + enc(tin), req));
            // --- MIP2 ---
            case 90 -> mip.get("/api/v1/mip/justice?pin=" + enc(pin) + "&subReportId=" + sub);
            case 92 -> mip.get("/api/v1/mip/labour?pin=" + enc(pin));
            case 93 -> mip.get("/api/v1/mip/aliment?pin=" + enc(pin) + "&type=" + sub);
            case 94 -> mip.get("/api/v1/mip/minzdrav?pin=" + enc(pin) + "&subReportId=" + sub);
            case 96 -> mip.get("/api/v1/mip/vehicle?plateNumber=" + enc(number));
            case 95 -> mip.post("/api/v1/mip/conviction", convictionBody(claim, number));
            // --- МИБ ---
            case 310 -> mip.get("/api/v1/mip/mib/debt-ban?pin=" + enc(pin));
            case 311 -> mip.get("/api/v1/mip/mib/avto-ban?autoNumber=" + enc(number));
            case 312 -> mip.get("/api/v1/mip/mib/realty-ban?cadNumber=" + enc(number));
            case 313 -> mip.get("/api/v1/mip/mib/doc-avto?autoNumber=" + enc(number));
            case 314 -> mip.get("/api/v1/mip/mib/doc-creditor?pin=" + enc(pin));
            case 315 -> mip.get("/api/v1/mip/mib/doc-debtor?pin=" + enc(pin) + "&tin=" + enc(tin));
            case 316 -> mip.get("/api/v1/mip/mib/doc-action?pin=" + enc(pin) + "&workNumber=" + enc(number) + "&type=" + sub);
            case 317 -> mip.get("/api/v1/mip/mib/list-pe-insolvent?pin=" + enc(pin));
            case 318 -> mip.get("/api/v1/mip/mib/list-le-insolvent?tin=" + enc(tin));
            // --- Коммуналка ---
            case 36 -> mip.get("/api/v1/mip/utility/electro?pin=" + enc(pin));
            case 37 -> mip.get("/api/v1/mip/utility/gas?pin=" + enc(pin));
            case 38 -> mip.get("/api/v1/mip/utility/ecology?pin=" + enc(pin));
            case 69 -> mip.get("/api/v1/mip/utility/toshissiqquvvat?pin=" + enc(pin));
            case 79, 80 -> mip.get("/api/v1/mip/cadastre?cadastreNumber=" + enc(number) + "&pin=" + enc(pin) + "&tin=" + enc(tin));
            // --- Прочее ---
            case 321 -> mip.get("/api/v1/mip/my-home?cadastre=" + enc(number) + "&pin=" + enc(pin));
            case 326 -> mip.post("/api/v1/mip/einvoice/pe", einvoiceBody(claim, req));
            case 327 -> mip.post("/api/v1/mip/einvoice/le", einvoiceBody(claim, req));
            case 328 -> mip.get("/api/v1/mip/cashback?pin=" + enc(pin) + year(req));
            default -> null;
        };
    }

    /**
     * Журналирование неудачной попытки получения отчёта (перенос saveReportError → logCreditReport):
     * повторный вызов GET_CREDIT_REPORT с текстом ошибки в P_MIP_RESPONSE. Сбой журналирования
     * не маскирует исходную ошибку.
     */
    private void journalError(String head, String code, SystemReportRequest req, ClaimData claim, String error) {
        try {
            int isLegal = claim.isLegal() != null ? claim.isLegal() : 0;
            CreditReportRequest errReq = new CreditReportRequest(isLegal, req.claimId(), req.reportId(), nz(req.ip()),
                    "Ошибка - " + error, 0, req.lang(), null, req.loanSubject(),
                    nz(claim.pinfl()), nz(claim.tin()), req.ownerId(), req.reportFormat());
            creditRepository.getCreditReport(head, code, errReq);
        } catch (Exception ex) {
            log.warn("Не удалось зажурналировать ошибку отчёта claimId={}: {}", req.claimId(), ex.getMessage());
        }
    }

    /**
     * Дописывает блок наличия КИ в JSON-объект отчёта (монолит: response без хвостовой "}" + ", " + блок + "}").
     * Блок — внутренние поля результата availabilityOfCreditRepC без внешних скобок.
     */
    private String appendAvailability(String head, String code, String claimId, String reportJson) {
        String block = availabilityBlock(head, code, claimId);
        String trimmed = reportJson != null ? reportJson.trim() : "";
        if (StringUtils.hasText(block) && trimmed.endsWith("}")) {
            return trimmed.substring(0, trimmed.length() - 1) + ", " + block + "}";
        }
        return reportJson;
    }

    /** Блок наличия КИ: XML-документ процедуры → JSON → снятие внешних скобок (как монолит). null при отсутствии/ошибке. */
    private String availabilityBlock(String head, String code, String claimId) {
        try {
            byte[] doc = creditRepository.getAvailabilityDoc(head, code, claimId);
            if (doc == null || doc.length == 0) {
                return null;
            }
            byte[] json = ConverterUtils.jsonFromXml(new String(doc, StandardCharsets.UTF_8));
            if (json == null || json.length <= 1) {
                return null;
            }
            String s = new String(json, StandardCharsets.UTF_8);
            return s.substring(1, s.length() - 1);
        } catch (Exception e) {
            log.warn("Не удалось получить блок наличия КИ для claimId={}: {}", claimId, e.getMessage());
            return null;
        }
    }

    /** Оборачивает JSON-строку отчёта в требуемый формат (как generateReport монолита). */
    private CreditReportResponse wrap(String reportJson, ReportFormat format) {
        byte[] report = formatConverter.wrapJsonReport(reportJson, format);
        return new CreditReportResponse("0", "OK", report, null);
    }

    private CreditReportRequest toCreditRequest(ClaimData claim, SystemReportRequest req) {
        int isLegal = claim.isLegal() != null ? claim.isLegal() : 0;
        return new CreditReportRequest(isLegal, req.claimId(), req.reportId(), nz(req.ip()), "", 0,
                req.lang(), null, req.loanSubject(), nz(claim.pinfl()), nz(claim.tin()), req.ownerId(),
                req.reportFormat());
    }

    private Map<String, Object> einvoiceBody(ClaimData claim, SystemReportRequest req) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("pin", nz(claim.pinfl()));
        body.put("tin", nz(claim.tin()));
        body.put("signature", nz(req.signature()));
        if (req.number() != null) {
            body.put("page", req.number());
        }
        body.put("lang", req.lang());
        return body;
    }

    private Map<String, Object> convictionBody(ClaimData claim, String regionId) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("pinfl", nz(claim.pinfl()));
        body.put("docSeries", nz(claim.docSeries()));
        body.put("docNumber", nz(claim.docNumber()));
        body.put("birthYear", birthYear(claim.birthDate()));
        body.put("firstName", nz(claim.firstName()));
        body.put("lastName", nz(claim.lastName()));
        body.put("regionId", nz(regionId));
        return body;
    }

    private static String gnk(String op, String query, SystemReportRequest req) {
        String lang = StringUtils.hasText(req.lang()) ? "&lang=" + enc(req.lang()) : "";
        return "/api/v1/mip/gnk/" + op + "?" + query + lang;
    }

    private static String year(SystemReportRequest req) {
        return req.year() != null ? "&year=" + req.year() : "";
    }

    private static String period(SystemReportRequest req) {
        return req.period() != null ? "&period=" + req.period() : "";
    }

    /** Год рождения из строки dd.MM.yyyy (для запроса о судимости). */
    private static Integer birthYear(String birthDate) {
        if (birthDate == null || !birthDate.contains(".")) {
            return null;
        }
        try {
            return Integer.parseInt(birthDate.substring(birthDate.lastIndexOf('.') + 1).trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static String enc(String value) {
        return URLEncoder.encode(value != null ? value : "", StandardCharsets.UTF_8);
    }

    private static String nz(String value) {
        return value != null ? value : "";
    }
}
