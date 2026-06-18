package uz.katm.report.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import uz.katm.report.domain.record.CreditReportRequest;
import uz.katm.report.domain.record.CreditReportResponse;
import uz.katm.report.domain.record.FicoResult;
import uz.katm.report.domain.record.ProcedureResult;
import uz.katm.report.domain.record.QualityReportRequest;
import uz.katm.report.domain.record.ReportFormat;
import uz.katm.report.exception.CreditServiceException;
import uz.katm.report.repository.CreditRepository;
import uz.katm.report.util.ReportFormatConverter;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreditService {

    private final CreditRepository creditRepository;
    private final UzCardService uzCardService;
    private final ReportFormatConverter formatConverter;

    // Успешный код процедуры CLAIM_LOAN_SUBJECT_CHECK (монолит: Constants.RESPONSE_OK).
    private static final String LOAN_SUBJECT_OK = "05000";
    // Тип/префикс токена отчёта UzCard (Constants.REPORT_UZCARD = 40).
    private static final int REPORT_UZCARD = 40;
    private static final String UZCARD_TOKEN_PREFIX = "40";

    public CreditReportResponse getCreditReport(String head, String code, CreditReportRequest request) {
        log.info("Запрос кредитного отчёта: head={}, code={}, claimId={}, isLegal={}",
                head, code, request.claimId(), request.isLegal());
        validateReportRequest(head, code, request.reportId(), request.claimId(), request.ownerId(), request.loanSubject());
        // UzCard (reportId=40) — асинхронная инициация скоринга (card.counts.create), а не обычная процедура.
        if (request.reportId() == REPORT_UZCARD) {
            return uzCardService.submitUzCardScoring(head, code, request);
        }
        CreditReportResponse response = creditRepository.getCreditReport(head, code, request);
        assertOkOrPending(response);
        // Процедура возвращает XML-документ; приводим к запрошенному формату (XML/JSON).
        ReportFormat format = ReportFormat.fromCode(request.reportFormat());
        return response.withReport(formatConverter.convertXmlDoc(response.report(), format));
    }

    /**
     * Валидация запроса отчёта перед вызовом процедуры — перенос
     * CreditReportServiceImpl.validateCreditReportRequest. Порядок проверок и коды ошибок сохранены.
     * Опущен checkLAuthOrgRequest (учётные данные удалены при миграции: идентичность организации
     * обеспечивают gateway access-check по IP и JWT-атрибуты head/code).
     */
    public void validateReportRequest(String head, String code, int reportIdNum, String claimId,
                                      String ownerId, String loanSubject) {
        // reportId передаётся в legacy-процедуры строкой с дополнением нулями до 3 знаков (монолит: leftPad).
        String reportId = String.format("%03d", reportIdNum);

        if (creditRepository.checkActiveDemandWork() < 1) {
            throw new CreditServiceException("05503",
                    "Временно приостановлен приём запросов на получение отчётов. Повторите запрос позже.");
        }
        if ("ORG".equalsIgnoreCase(head) && creditRepository.getReportCorrectness(head, code, reportId) < 1) {
            throw new CreditServiceException("05403", "У Вас нет доступа к запрашиваемому отчёту");
        }
        // subReportId в новой модели запроса отсутствует — передаём null (legacy: pSubReportId был nullable).
        if (creditRepository.checkLastDemandTime(head, code, claimId, reportId, ownerId, null) > 0) {
            throw new CreditServiceException("05003", "Идентичный запрос был отправлен менее чем за 5 минут");
        }
        creditRepository.logCreditReportRequest(head, code, claimId, reportId, ownerId, null);

        if (StringUtils.hasText(loanSubject)) {
            ProcedureResult ls = creditRepository.checkClaimLoanSubject(head, code, claimId, loanSubject, ownerId);
            if (!LOAN_SUBJECT_OK.equals(ls.code())) {
                throw new CreditServiceException(ls.code(), ls.message());
            }
        }
        if (creditRepository.checkFreezeSubscription(head, code, claimId, reportId) == 1) {
            throw new CreditServiceException("05555",
                    "Субъект не дает согласия на получение кредитной истории, подключена услуга Freeze. " +
                            "Субъекту необходимо отключить услугу Freeze.");
        }
    }

    /**
     * Доступность сервиса отчётов (перенос /credit/service/available). В монолите эндпоинт был
     * пустым ping'ом; здесь возвращаем реальную доступность — приём запросов разрешён, если активна
     * сессия загрузчика (checkActiveDemandWork &gt;= 1).
     */
    public boolean isReportServiceAvailable() {
        return creditRepository.checkActiveDemandWork() >= 1;
    }

    public CreditReportResponse getCreditReportStatus(String head, String code, String claimId,
                                                      String token, int reportFormat) {
        log.info("Запрос статуса кредитного отчёта: head={}, code={}, claimId={}, token={}, format={}",
                head, code, claimId, token, reportFormat);
        ReportFormat format = ReportFormat.fromCode(reportFormat);
        // Токены UzCard (префикс 40) опрашиваются по внешнему сервису, минуя процедуру статуса.
        // Ветка REPORT_CONVICTION (95) монолита здесь не переносится — это MIP-канал (отдельный гэп).
        if (isUzCardToken(token)) {
            return uzCardService.getUzCardData(head, code, claimId, token, format);
        }
        CreditReportResponse response = creditRepository.getCreditReportStatus(head, code, claimId, token);
        assertOkOrPending(response);
        // Процедура статуса возвращает XML-документ; приводим к запрошенному формату.
        return response.withReport(formatConverter.convertXmlDoc(response.report(), format));
    }

    private static boolean isUzCardToken(String token) {
        if (token == null) {
            return false;
        }
        String[] keys = token.split("_");
        return keys.length > 1 && UZCARD_TOKEN_PREFIX.equals(keys[0]);
    }

    public CreditReportResponse getInfoscoreReport(String pin) {
        log.info("Запрос Infoscore по ПИНФЛ: pin={}", pin);
        CreditReportResponse response = creditRepository.getInfoscoreReport(pin);
        assertOkOrPending(response);
        return response;
    }

    public CreditReportResponse getInfoscoreLegalReport(String tin) {
        log.info("Запрос Infoscore (юрлицо) по ИНН: tin={}", tin);
        CreditReportResponse response = creditRepository.getInfoscoreLegalReport(tin);
        assertOkOrPending(response);
        return response;
    }

    public List<FicoResult> getFicoScore(String clientId) {
        log.info("Запрос FICO-скора: clientId={}", clientId);
        return creditRepository.getFicoScore(clientId);
    }

    public CreditReportResponse getQualityReport(String head, String code, QualityReportRequest request) {
        log.info("Запрос отчёта по изменению качества КИ: head={}, code={}, dateFrom={}, dateTo={}",
                head, code, request.dateFrom(), request.dateTo());
        CreditReportResponse response = creditRepository.getQualityReport(
                request.login(), request.password(), head, code, request.dateFrom(), request.dateTo());
        assertOkOrPending(response);
        return response;
    }

    private void assertOkOrPending(CreditReportResponse response) {
        if (!response.isSuccess() && !response.isPending()) {
            log.warn("Процедура вернула код ошибки: code={}, message={}", response.code(), response.message());
            throw new CreditServiceException(response.code(), response.message());
        }
    }
}
