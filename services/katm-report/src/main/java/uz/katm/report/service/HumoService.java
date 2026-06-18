package uz.katm.report.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uz.katm.report.client.HumoClient;
import uz.katm.report.domain.record.ClaimData;
import uz.katm.report.exception.CreditServiceException;
import uz.katm.report.repository.CreditRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Скоринг HUMO (REPORT_HUMO=42). Перенос BkiServiceImpl.getHumoData: получаем карты субъекта,
 * по каждой карте запрашиваем помесячный скоринг, маскируем номер карты и агрегируем отчёт.
 * Источник журналируется в gateway_requests_log (source=2).
 */
@Slf4j
@Service
public class HumoService {

    private static final int SOURCE_HUMO = 2;
    private static final String GET_CARDS_OK = "getCards:200";
    private static final String GET_CARDS_FAIL = "getCards:500";
    private static final String GET_BILLING_OK = "getBilling:200";
    private static final String GET_BILLING_FAIL = "getBilling:500";
    private static final String MASK = "********";
    private static final DateTimeFormatter UTC_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    private final CreditRepository creditRepository;
    private final HumoClient humoClient;
    private final ObjectMapper objectMapper;

    private final String cardsUrl;
    private final String scoringUrl;

    public HumoService(CreditRepository creditRepository, HumoClient humoClient, ObjectMapper objectMapper,
                       @Value("${katm.report.humo.cards-url:}") String cardsUrl,
                       @Value("${katm.report.humo.scoring-url:}") String scoringUrl) {
        this.creditRepository = creditRepository;
        this.humoClient = humoClient;
        this.objectMapper = objectMapper;
        this.cardsUrl = cardsUrl;
        this.scoringUrl = scoringUrl;
    }

    public String getHumo(String head, String code, String claimId, ClaimData claim) {
        log.info("HUMO скоринг: head={}, code={}, claimId={}", head, code, claimId);
        String pin = nz(claim.pinfl());
        String dateFrom = LocalDateTime.now().minusMonths(11).format(UTC_FMT);
        String dateTo = LocalDateTime.now().format(UTC_FMT);
        int reqId = ThreadLocalRandom.current().nextInt(Integer.MAX_VALUE);

        Map<String, Object> cardsReq = humoRequest(claimId, Map.of("person_code", pin));
        String cardsReqJson = toJson(cardsReq);
        String cardsResp = humoClient.post(cardsUrl, cardsReq);

        JsonNode cardsNode = read(cardsResp);
        JsonNode cards = cardsNode.path("result").path("data");
        ArrayNode scoringArray = objectMapper.createArrayNode();

        if (cardsNode.path("error").isNull() || cardsNode.path("error").isMissingNode()) {
            if (cards.isArray() && !cards.isEmpty()) {
                creditRepository.addGatewayRequestsLog(reqId, SOURCE_HUMO, head, code, claimId,
                        GET_CARDS_OK, cardsReqJson, cardsResp);
                for (JsonNode card : cards) {
                    scoringArray.add(scoreCard(reqId, head, code, claimId, card, dateFrom, dateTo));
                }
            } else {
                creditRepository.addGatewayRequestsLog(reqId, SOURCE_HUMO, head, code, claimId,
                        GET_CARDS_FAIL, cardsReqJson, cardsResp);
            }
        } else {
            creditRepository.addGatewayRequestsLog(reqId, SOURCE_HUMO, head, code, claimId,
                    GET_CARDS_FAIL, cardsReqJson, cardsResp);
        }

        ObjectNode report = objectMapper.createObjectNode();
        report.set("scoring", scoringArray);
        return toJson(report);
    }

    private ObjectNode scoreCard(int reqId, String head, String code, String claimId,
                                 JsonNode card, String dateFrom, String dateTo) {
        String originalCard = card.path("card").asText("");
        Map<String, Object> scoringReq = humoRequest(claimId, scoringParams(originalCard, dateFrom, dateTo));
        String scoringReqJson = toJson(scoringReq);
        String scoringResp = humoClient.post(scoringUrl, scoringReq);
        JsonNode scoringNode = read(scoringResp);
        JsonNode months = scoringNode.path("result").path("data");

        ObjectNode maskedCard = objectMapper.createObjectNode();
        maskedCard.put("name", card.path("name").asText(""));
        maskedCard.put("card", maskCard(originalCard));
        maskedCard.put("expiry", "-");
        maskedCard.put("bank", originalCard.length() >= 6 ? originalCard.substring(4, 6) : "");

        ObjectNode entry = objectMapper.createObjectNode();
        entry.set("card", maskedCard);

        boolean scoringOk = (scoringNode.path("error").isNull() || scoringNode.path("error").isMissingNode())
                && months.isArray();
        if (scoringOk) {
            entry.set("data", months);
            if (!months.isEmpty()) {
                creditRepository.addGatewayRequestsLog(reqId, SOURCE_HUMO, head, code, claimId,
                        GET_BILLING_OK, scoringReqJson, scoringResp);
            }
        } else {
            entry.set("data", objectMapper.createArrayNode());
            creditRepository.addGatewayRequestsLog(reqId, SOURCE_HUMO, head, code, claimId,
                    GET_BILLING_FAIL, scoringReqJson, scoringResp);
        }
        return entry;
    }

    private Map<String, Object> humoRequest(String claimId, Map<String, Object> params) {
        Map<String, Object> req = new LinkedHashMap<>();
        req.put("id", claimId);
        req.put("params", params);
        return req;
    }

    private Map<String, Object> scoringParams(String card, String dateFrom, String dateTo) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("card", card);
        params.put("date_from", dateFrom);
        params.put("date_to", dateTo);
        return params;
    }

    /** Маскирование: первые 4 + ******** + последние 4 (как монолит). */
    private static String maskCard(String card) {
        if (card.length() >= 16) {
            return card.substring(0, 4) + MASK + card.substring(12, 16);
        }
        return MASK;
    }

    private JsonNode read(String json) {
        try {
            return objectMapper.readTree(json);
        } catch (Exception e) {
            throw new CreditServiceException("-1", "Некорректный ответ сервиса HUMO");
        }
    }

    private String toJson(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (Exception e) {
            throw new CreditServiceException("-1", "Ошибка сериализации запроса HUMO");
        }
    }

    private static String nz(String value) {
        return value != null ? value : "";
    }
}
