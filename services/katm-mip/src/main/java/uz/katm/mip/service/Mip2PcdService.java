package uz.katm.mip.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import uz.katm.mip.client.PcdClient;
import uz.katm.mip.domain.dto.ConvictionCheckResponse;
import uz.katm.mip.domain.dto.ConvictionQueryResponse;
import uz.katm.mip.domain.dto.ConvictionRequest;
import uz.katm.mip.domain.dto.ConvictionResult;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * MIP2-отчёты через ПЦД REST-канал (перенос bkiService: getJusticeData / getLabourData /
 * getAlimentData / getMinZdravData / getVehicleData / getConvictionData + checkConvictionData).
 * Тела запросов воспроизводят DTO монолита 1:1. Судимость — асинхронная (инициация + опрос статуса).
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class Mip2PcdService {

    private static final String CONVICTION_TOKEN_PREFIX = "95"; // Constants.REPORT_CONVICTION
    private static final String CONVICTION_ORGANIZATION_ID = "04057";

    private final PcdClient pcdClient;
    private final ObjectMapper objectMapper;

    @Value("${katm.mip.mip2.justice-birth-url:}")
    private String justiceBirthUrl;
    @Value("${katm.mip.mip2.justice-marriage-url:}")
    private String justiceMarriageUrl;
    @Value("${katm.mip.mip2.justice-divorce-url:}")
    private String justiceDivorceUrl;
    @Value("${katm.mip.mip2.justice-death-url:}")
    private String justiceDeathUrl;
    @Value("${katm.mip.mip2.labour-url:}")
    private String labourUrl;
    @Value("${katm.mip.mip2.aliment-url:}")
    private String alimentUrl;
    @Value("${katm.mip.mip2.narko-url:}")
    private String narkoUrl;
    @Value("${katm.mip.mip2.psycho-url:}")
    private String psychoUrl;
    @Value("${katm.mip.mip2.vehicle-url:}")
    private String vehicleUrl;
    @Value("${katm.mip.mip2.conviction-url:}")
    private String convictionUrl;
    @Value("${katm.mip.mip2.conviction-check-url:}")
    private String convictionCheckUrl;

    /** ЗАГС (REPORT_JUSTICE=90): subReportId 1=рождение, 2=брак, 3=развод, 4=смерть (default рождение). */
    public String getJustice(String pin, int subReportId) {
        log.info("ЗАГС: pin={}, subReport={}", pin, subReportId);
        String url = switch (subReportId) {
            case 2 -> justiceMarriageUrl;
            case 3 -> justiceDivorceUrl;
            case 4 -> justiceDeathUrl;
            default -> justiceBirthUrl;
        };
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("id", requestId());
        payload.put("pin", nz(pin));
        return call(url, payload);
    }

    /** МинТруд (REPORT_LABOUR=92): {request_id, pin}. requestId монолит брал из claimId. */
    public String getLabour(String pin, String requestId) {
        log.info("МинТруд: pin={}", pin);
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("request_id", StringUtils.hasText(requestId) ? requestId : requestId());
        payload.put("pin", nz(pin));
        return call(labourUrl, payload);
    }

    /**
     * Алименты МИБ (REPORT_ALIMENT=93). AlimentParams наследует MibParams, поэтому тело включает
     * базовые поля МИБ {transaction_id, sender_pinfl, purpose, consent} + {pin, type}. type=subReportId.
     */
    public String getAliment(String pin, int type) {
        log.info("Алименты: pin={}, type={}", pin, type);
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("transaction_id", System.currentTimeMillis());
        payload.put("sender_pinfl", MibMipService.SENDER_PINFL);
        payload.put("purpose", "Scoring");
        payload.put("consent", "Yes");
        payload.put("pin", nz(pin));
        payload.put("type", type);
        return call(alimentUrl, payload);
    }

    /** МинЗдрав (REPORT_MINZDRAV=94): subReportId 1=нарко, 2=психо (default нарко). Тело {pinpp}. */
    public String getMinzdrav(String pin, int subReportId) {
        log.info("МинЗдрав: pin={}, subReport={}", pin, subReportId);
        String url = subReportId == 2 ? psychoUrl : narkoUrl;
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("pinpp", nz(pin));
        return call(url, payload);
    }

    /** Авто (REPORT_VEHICLE_INFO=96): {pRequestID, pPlateNumber}. Субъект — гос. номер ТС. */
    public String getVehicle(String plateNumber) {
        log.info("Авто: plate={}", plateNumber);
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("pRequestID", requestId());
        payload.put("pPlateNumber", nz(plateNumber));
        return call(vehicleUrl, payload);
    }

    /**
     * Судимость (REPORT_CONVICTION=95) — инициация. Возвращает токен "95_&lt;requestId&gt;" + WAIT,
     * либо FAIL. middlename/comments="-", organization_id="04057", consent=true — как в монолите.
     */
    public ConvictionResult submitConviction(ConvictionRequest req) {
        log.info("Судимость (инициация): pin={}, region={}", req.pinfl(), req.regionId());
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("pinfl", nz(req.pinfl()));
        payload.put("firstname", nz(req.firstName()));
        payload.put("lastname", nz(req.lastName()));
        payload.put("middlename", "-");
        payload.put("birth_year", req.birthYear());
        payload.put("comments", "-");
        payload.put("passport", nz(req.docSeries()) + nz(req.docNumber()));
        payload.put("organization_id", CONVICTION_ORGANIZATION_ID);
        payload.put("region_id", nz(req.regionId()));
        payload.put("consent", true);

        String response = call(convictionUrl, payload);
        ConvictionQueryResponse parsed = parse(response, ConvictionQueryResponse.class);
        if (parsed != null && Boolean.TRUE.equals(parsed.success())) {
            String token = CONVICTION_TOKEN_PREFIX + "_" + parsed.requestId();
            return new ConvictionResult("WAIT", null, token, response);
        }
        return ConvictionResult.fail(parsed != null ? parsed.message() : "Получили пустой ответ от сервиса");
    }

    /** Судимость — опрос готовности по токену "95_&lt;idQuery&gt;". */
    public ConvictionResult checkConviction(String token) {
        log.info("Судимость (опрос): token={}", token);
        String[] keys = token.split("_");
        if (keys.length < 2) {
            return ConvictionResult.fail("Некорректный токен судимости");
        }
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("id_query", keys[1]);
        String response = call(convictionCheckUrl, payload);
        ConvictionCheckResponse parsed = parse(response, ConvictionCheckResponse.class);
        if (parsed != null && Boolean.TRUE.equals(parsed.success())) {
            return ConvictionResult.ok(response);
        }
        return ConvictionResult.wait(parsed != null ? parsed.message() : "Запрос в обработке", token);
    }

    private String call(String url, Object payload) {
        return pcdClient.mibState(pcdClient.execute(url, payload));
    }

    private <T> T parse(String json, Class<T> type) {
        try {
            return objectMapper.readValue(json, type);
        } catch (Exception e) {
            log.error("Некорректный ответ ПЦД-сервиса: {}", e.getMessage());
            return null;
        }
    }

    private static String requestId() {
        return String.valueOf(System.currentTimeMillis());
    }

    private static String nz(String value) {
        return value != null ? value : "";
    }
}
