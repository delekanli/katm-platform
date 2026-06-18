package uz.katm.notification.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import uz.katm.notification.domain.record.UcinNotificationResult;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Ретрансляция уведомлений (freeze / оформление кредита) во внешний UCIN NOTIFICATION SERVICE.
 * Перенос InternalServiceImpl.sendFreezeNotification/sendCreditNotification (POST + BasicAuth).
 * В монолите оба уведомления уходили на один сервис UCIN_NOTIFICATION_FREEZE_SERVICE — поведение сохранено.
 * Конфиг: katm.notification.ucin.{url,username,password}.
 */
@Slf4j
@Component
public class UcinNotificationClient {

    private final RestClient restClient = RestClient.create();
    private final String url;
    private final String authHeader;

    public UcinNotificationClient(
            @Value("${katm.notification.ucin.url:}") String url,
            @Value("${katm.notification.ucin.username:}") String username,
            @Value("${katm.notification.ucin.password:}") String password) {
        this.url = url;
        this.authHeader = "Basic " + Base64.getEncoder()
                .encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8));
    }

    /** Отправляет тело уведомления в UCIN. Возвращает результат доставки (без выброса исключений). */
    public UcinNotificationResult send(Object body, String pinfl) {
        try {
            String response = restClient.post()
                    .uri(url)
                    .header(HttpHeaders.AUTHORIZATION, authHeader)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(body)
                    .retrieve()
                    .body(String.class);
            log.info("UCIN-уведомление по ПИНФЛ {} доставлено", pinfl);
            return UcinNotificationResult.ok();
        } catch (RestClientResponseException e) {
            String errorBody = e.getResponseBodyAsString(StandardCharsets.UTF_8);
            log.error("UCIN NOTIFICATION SERVICE вернул ошибку по ПИНФЛ {}: {}", pinfl, errorBody);
            return UcinNotificationResult.failed("Сервис UCIN NOTIFICATION SERVICE вернул ошибку: " + errorBody);
        } catch (Exception e) {
            log.error("UCIN NOTIFICATION SERVICE недоступен по ПИНФЛ {}: {}", pinfl, e.getMessage());
            return UcinNotificationResult.failed("Сервис UCIN NOTIFICATION SERVICE недоступен. Повторите попытку позже");
        }
    }
}
