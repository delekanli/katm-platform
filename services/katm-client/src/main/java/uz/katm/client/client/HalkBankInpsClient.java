package uz.katm.client.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import uz.katm.client.exception.ClientServiceException;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Клиент к сервису Халк банка для получения отчислений ИНПС (перенос вызовов
 * InternalServiceImpl.checkInps/getInpsData/getInpsDataForScor через katmHttpService + BasicAuth).
 * Три раздельных эндпоинта (у каждого свои URL/логин/пароль), как настройки INPS_SERVICE_CHECK /
 * INPS_SERVICE / INPS_SERVICE_1 в монолите. Конфиг: katm.client.inps.{check|scor|inps}.{url,username,password}.
 */
@Slf4j
@Component
public class HalkBankInpsClient {

    private final RestClient restClient = RestClient.create();

    private final Endpoint check;
    private final Endpoint scor;
    private final Endpoint inps;

    public HalkBankInpsClient(
            @Value("${katm.client.inps.check.url:}") String checkUrl,
            @Value("${katm.client.inps.check.username:}") String checkUser,
            @Value("${katm.client.inps.check.password:}") String checkPass,
            @Value("${katm.client.inps.scor.url:}") String scorUrl,
            @Value("${katm.client.inps.scor.username:}") String scorUser,
            @Value("${katm.client.inps.scor.password:}") String scorPass,
            @Value("${katm.client.inps.inps.url:}") String inpsUrl,
            @Value("${katm.client.inps.inps.username:}") String inpsUser,
            @Value("${katm.client.inps.inps.password:}") String inpsPass) {
        this.check = new Endpoint(checkUrl, basic(checkUser, checkPass));
        this.scor = new Endpoint(scorUrl, basic(scorUser, scorPass));
        this.inps = new Endpoint(inpsUrl, basic(inpsUser, inpsPass));
    }

    /** Проверка счёта ИНПС по ПИНФЛ. Возвращает сырой JSON-ответ (массив счетов). */
    public String checkInps(Object body) {
        return post(check, body, "проверки счёта ИНПС");
    }

    /** Данные ИНПС для скоринга (эндпоинт INPS_SERVICE). */
    public String getScorData(Object body) {
        return post(scor, body, "получения отчислений ИНПС (скоринг)");
    }

    /** Данные ИНПС для отчёта (эндпоинт INPS_SERVICE_1). */
    public String getInpsData(Object body) {
        return post(inps, body, "получения отчислений ИНПС");
    }

    private String post(Endpoint endpoint, Object body, String op) {
        try {
            return restClient.post()
                    .uri(endpoint.url())
                    .header(HttpHeaders.AUTHORIZATION, endpoint.authHeader())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(body)
                    .retrieve()
                    .body(String.class);
        } catch (Exception e) {
            log.error("Ошибка {} в Халк банке: {}", op, e.getMessage());
            throw new ClientServiceException("-1", "Сервис Халк банка недоступен. Повторите попытку позже");
        }
    }

    private static String basic(String username, String password) {
        return "Basic " + Base64.getEncoder()
                .encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8));
    }

    private record Endpoint(String url, String authHeader) {
    }
}
