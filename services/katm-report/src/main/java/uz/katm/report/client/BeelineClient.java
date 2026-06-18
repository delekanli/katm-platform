package uz.katm.report.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import uz.katm.report.exception.CreditServiceException;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Клиент к сервисам Beeline (перенос вызовов из BkiServiceImpl get*Beeline*Data).
 * Авторизация OAuth (grant_type=password) → Bearer-токен, затем вызовы скоринга/телефонов/верификации.
 * Конфиг: katm.report.beeline.auth.{url,username,password,client-id,client-secret}.
 */
@Slf4j
@Component
public class BeelineClient {

    private final RestClient restClient = RestClient.create();
    private final ObjectMapper objectMapper;

    private final String authUrl;
    private final String username;
    private final String password;
    private final String clientId;
    private final String clientSecret;

    public BeelineClient(ObjectMapper objectMapper,
                         @Value("${katm.report.beeline.auth.url:}") String authUrl,
                         @Value("${katm.report.beeline.auth.username:}") String username,
                         @Value("${katm.report.beeline.auth.password:}") String password,
                         @Value("${katm.report.beeline.auth.client-id:}") String clientId,
                         @Value("${katm.report.beeline.auth.client-secret:}") String clientSecret) {
        this.objectMapper = objectMapper;
        this.authUrl = authUrl;
        this.username = username;
        this.password = password;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    /** Авторизация в Beeline, возвращает access-токен. */
    public String authenticate() {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("username", username);
        body.put("password", password);
        body.put("grant_type", "password");
        body.put("client_id", clientId);
        body.put("client_secret", clientSecret);
        try {
            String response = restClient.post()
                    .uri(authUrl)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(body)
                    .retrieve()
                    .body(String.class);
            JsonNode node = objectMapper.readTree(response);
            String token = node.path("token").asText(null);
            if (token == null || token.isBlank()) {
                throw new CreditServiceException("-1", "Произошла ошибка при авторизации в сервисе Beeline");
            }
            return token;
        } catch (CreditServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("Ошибка авторизации в Beeline: {}", e.getMessage());
            throw new CreditServiceException("-1", "Произошла ошибка при авторизации в сервисе Beeline");
        }
    }

    /** POST с Bearer-токеном Beeline, возвращает сырой ответ. */
    public String post(String url, Object body, String token) {
        try {
            return restClient.post()
                    .uri(url)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(body)
                    .retrieve()
                    .body(String.class);
        } catch (Exception e) {
            log.error("Ошибка запроса в Beeline: {}", e.getMessage());
            throw new CreditServiceException("-1", "Произошла ошибка при получении данных от сервиса Beeline");
        }
    }
}
