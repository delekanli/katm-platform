package uz.katm.report.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import uz.katm.report.exception.CreditServiceException;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Клиент к мониторинговому сервису UzCard (JSON-RPC card.counts.get).
 * Перенос вызовов из CreditReportServiceImpl.getUzCardData (BasicAuth + заголовок Host).
 * Конфиг: katm.report.uzcard.{url,username,password,host}.
 */
@Slf4j
@Component
public class UzCardClient {

    private final RestClient restClient = RestClient.create();
    private final String url;
    private final String host;
    private final String authHeader;

    public UzCardClient(
            @Value("${katm.report.uzcard.url:}") String url,
            @Value("${katm.report.uzcard.host:monitoring.uzcard.uz}") String host,
            @Value("${katm.report.uzcard.username:}") String username,
            @Value("${katm.report.uzcard.password:}") String password) {
        this.url = url;
        this.host = host;
        this.authHeader = "Basic " + Base64.getEncoder()
                .encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8));
    }

    /** card.counts.get — опрос результата задания. Возвращает сырой ответ. */
    public String getCounts(Object body) {
        return post(body);
    }

    /** card.counts.create — создание заданий скоринга. Возвращает сырой ответ. */
    public String createCounts(Object body) {
        return post(body);
    }

    private String post(Object body) {
        try {
            return restClient.post()
                    .uri(url)
                    .header(HttpHeaders.AUTHORIZATION, authHeader)
                    .header(HttpHeaders.HOST, host)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(body)
                    .retrieve()
                    .body(String.class);
        } catch (Exception e) {
            log.error("Ошибка запроса в UzCard: {}", e.getMessage());
            throw new CreditServiceException("-1", "Сервис UzCard недоступен. Повторите попытку позже");
        }
    }
}
