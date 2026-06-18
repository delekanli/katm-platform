package uz.katm.report.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import uz.katm.report.exception.CreditServiceException;

/**
 * Клиент к HUMO (перенос вызовов из BkiServiceImpl.getHumoData). Авторизация — статический
 * Bearer-токен из конфигурации. Конфиг: katm.report.humo.{cards-url,scoring-url,token}.
 */
@Slf4j
@Component
public class HumoClient {

    private final RestClient restClient = RestClient.create();
    private final String token;

    public HumoClient(@Value("${katm.report.humo.token:}") String token) {
        this.token = token;
    }

    /** POST с Bearer-токеном HUMO, возвращает сырой ответ. */
    public String post(String url, Object body) {
        try {
            return restClient.post()
                    .uri(url)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(body)
                    .retrieve()
                    .body(String.class);
        } catch (Exception e) {
            log.error("Ошибка запроса в HUMO: {}", e.getMessage());
            throw new CreditServiceException("-1", "Получили пустой ответ от HUMO");
        }
    }
}
