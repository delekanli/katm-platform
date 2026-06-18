package uz.katm.report.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.katm.report.client.UzCardClient;
import uz.katm.report.domain.record.ClaimData;
import uz.katm.report.domain.record.CreditReportRequest;
import uz.katm.report.domain.record.CreditReportResponse;
import uz.katm.report.domain.record.PrepareResult;
import uz.katm.report.domain.record.ReportFormat;
import uz.katm.report.domain.uzcard.UzCardCountId;
import uz.katm.report.domain.uzcard.UzCardCountInfo;
import uz.katm.report.domain.uzcard.UzCardGetCountIdCriteria;
import uz.katm.report.domain.uzcard.UzCardGetCountIdParams;
import uz.katm.report.domain.uzcard.UzCardGetInfosParams;
import uz.katm.report.domain.uzcard.UzCardRequest;
import uz.katm.report.domain.uzcard.UzCardResponse;
import uz.katm.report.domain.uzcard.UzCardScoringInfos;
import uz.katm.report.exception.CreditServiceException;
import uz.katm.report.repository.CreditRepository;
import uz.katm.report.util.ReportFormatConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Асинхронный опрос UzCard по токену статуса отчёта. Перенос CreditReportServiceImpl.getUzCardData:
 * для каждого незавершённого задания (count_id из gateway_requests_log) вызывается card.counts.get,
 * результаты агрегируются. Готово (RESULT_OK), только когда завершены все задания, иначе RESULT_WAIT.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UzCardService {

    private static final String JSONRPC = "2.0";
    private static final String METHOD = "card.counts.get";
    private static final String METHOD_CREATE = "card.counts.create";
    private static final String SUCCESS = "0";
    private static final String PENDING = "05050";
    private static final String UZCARD_TOKEN_PREFIX = "40"; // Constants.REPORT_UZCARD
    private static final DateTimeFormatter YYYYMMDD = DateTimeFormatter.ofPattern("yyyyMMdd");

    private final CreditRepository creditRepository;
    private final UzCardClient uzCardClient;
    private final ObjectMapper objectMapper;
    private final ReportFormatConverter formatConverter;

    /**
     * Инициация скоринга в UzCard (перенос BkiServiceImpl.getUzCardData, метод card.counts.create).
     * Выделяет demandId, создаёт задания по ПИНФЛ субъекта и сохраняет их count_id в gateway_requests_log,
     * откуда их затем читает {@link #getUzCardData}. Возвращает токен "40_&lt;reqId&gt;_&lt;demandId&gt;"
     * со статусом WAIT (05050) — отчёт забирается асинхронно по статусу.
     */
    public CreditReportResponse submitUzCardScoring(String head, String code, CreditReportRequest req) {
        log.info("Инициация скоринга UzCard: head={}, code={}, claimId={}", head, code, req.claimId());
        ClaimData claim = creditRepository.getClaimData(head, code, req.claimId(), String.valueOf(req.reportId()));
        if (!claim.isSuccess()) {
            return new CreditReportResponse(claim.code(), claim.message(), null, null);
        }
        int isLegal = claim.isLegal() != null ? claim.isLegal() : 0;

        PrepareResult prep = creditRepository.prepareCreditReport(
                head, code, isLegal, req.claimId(), req.ip(), req.reportId(),
                req.lang(), req.loanSubject(), req.ownerId());
        if (prep.demandId() == null || prep.demandId().isBlank()) {
            throw new CreditServiceException(prep.code(),
                    prep.message() != null ? prep.message() : "Не удалось подготовить отчёт UzCard");
        }
        String demandId = prep.demandId();
        int reqId = ThreadLocalRandom.current().nextInt(Integer.MAX_VALUE);

        // Период по умолчанию: с первого дня месяца 11 месяцев назад по сегодня (монолит: addMonth(-11)+getStartDate).
        String startDate = LocalDate.now().minusMonths(11).withDayOfMonth(1).format(YYYYMMDD);
        String endDate = LocalDate.now().format(YYYYMMDD);
        String pinfl = claim.pinfl() != null ? claim.pinfl() : "";
        UzCardRequest<UzCardGetCountIdParams> request = new UzCardRequest<>(reqId, JSONRPC, METHOD_CREATE,
                new UzCardGetCountIdParams(new UzCardGetCountIdCriteria(pinfl, startDate, endDate)));
        String requestJson = toJson(request);
        String rawResponse = uzCardClient.createCounts(request);

        UzCardResponse<UzCardCountId> result = parseCountId(rawResponse);
        String token = UZCARD_TOKEN_PREFIX + "_" + reqId + "_" + demandId;
        if (result != null && result.result() != null && result.error() == null
                && result.result().jobIds() != null && result.result().jobIds().length > 0) {
            // Основная строка токена и по строке на каждое задание (count_id = demandId_jobId), как в монолите.
            creditRepository.addGatewayRequestsLog(reqId, head, code, req.claimId(), token, requestJson, rawResponse);
            for (String jobId : result.result().jobIds()) {
                creditRepository.addGatewayRequestsLog(reqId, head, code, req.claimId(),
                        demandId + "_" + jobId, null, rawResponse);
            }
            return new CreditReportResponse(PENDING, "Запрос в UzCard принят в обработку", null, token);
        }

        String errorMessage = (result != null && result.error() != null)
                ? result.error().message() : "Получили пустой ответ от UzCard";
        creditRepository.addGatewayRequestsLog(reqId, head, code, req.claimId(), demandId, requestJson, errorMessage);
        throw new CreditServiceException("-1", errorMessage);
    }

    /**
     * @param token  формат "40_&lt;reqId&gt;_&lt;demandId&gt;" (префикс REPORT_UZCARD).
     * @param format формат выдачи готового отчёта.
     */
    public CreditReportResponse getUzCardData(String head, String code, String claimId, String token,
                                              ReportFormat format) {
        log.info("Опрос данных UzCard: head={}, code={}, claimId={}, token={}", head, code, claimId, token);
        String[] keys = token.split("_");
        if (keys.length < 3) {
            throw new CreditServiceException("-1", "Некорректный токен UzCard");
        }
        int reqId = parseInt(keys[1]);
        String demandId = keys[2];

        List<String> countIds = creditRepository.getCountIdsFromGatewayRequestsLog(head, code, claimId, demandId, token);

        int activeCards = 0;
        int jobEnded = 0;
        String lastError = null;
        List<UzCardScoringInfos> allInfos = new ArrayList<>();

        for (String countId : countIds) {
            String jobId = countId.substring(countId.indexOf("_") + 1);
            UzCardRequest<UzCardGetInfosParams> request =
                    new UzCardRequest<>(reqId, JSONRPC, METHOD, new UzCardGetInfosParams(jobId));
            String rawResponse = uzCardClient.getCounts(request);
            UzCardResponse<UzCardCountInfo> result = parse(rawResponse);

            if (result != null && result.result() != null && result.error() == null) {
                creditRepository.updateGatewayRequestsLog(reqId, countId, toJson(request), rawResponse, 1);
                UzCardCountInfo info = result.result();
                if (info.activeCards() != null && info.activeCards() > 0 && info.countInfos() != null) {
                    allInfos.addAll(info.countInfos());
                    activeCards += info.activeCards();
                    jobEnded++;
                }
            } else {
                creditRepository.updateGatewayRequestsLog(reqId, countId, toJson(request), rawResponse, 0);
                if (result != null && result.error() != null) {
                    lastError = result.error().message();
                }
            }
        }

        // Готово, только когда завершены все задания (включая вырожденный случай пустого списка — как в монолите).
        if (jobEnded == countIds.size()) {
            UzCardResponse<UzCardCountInfo> finalResult =
                    new UzCardResponse<>(reqId, JSONRPC, new UzCardCountInfo(activeCards, allInfos), null);
            // Отчёт UzCard — JSON-строка; оборачиваем в {"report":...} и приводим к формату (монолит generateReport).
            byte[] report = formatConverter.wrapJsonReport(toJson(finalResult), format);
            return new CreditReportResponse(SUCCESS, "OK", report, token);
        }
        return new CreditReportResponse(PENDING,
                lastError != null ? lastError : "Запрос в UzCard в обработке", null, token);
    }

    private UzCardResponse<UzCardCountInfo> parse(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<UzCardResponse<UzCardCountInfo>>() {
            });
        } catch (Exception e) {
            log.error("Некорректный ответ UzCard: {}", e.getMessage());
            throw new CreditServiceException("-1", "Некорректный ответ сервиса UzCard");
        }
    }

    private UzCardResponse<UzCardCountId> parseCountId(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<UzCardResponse<UzCardCountId>>() {
            });
        } catch (Exception e) {
            log.error("Некорректный ответ UzCard (create): {}", e.getMessage());
            throw new CreditServiceException("-1", "Некорректный ответ сервиса UzCard");
        }
    }

    private String toJson(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (Exception e) {
            throw new CreditServiceException("-1", "Ошибка сериализации запроса UzCard");
        }
    }

    private static int parseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new CreditServiceException("-1", "Некорректный токен UzCard");
        }
    }
}
