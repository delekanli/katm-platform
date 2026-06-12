package uz.katm.mip.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import uz.katm.common.http.HttpResponseInfo;
import uz.katm.common.http.HttpUtils;
import uz.katm.common.http.IHttpService;
import uz.katm.mip.domain.dto.OAuthTokenResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Хранилище OAuth2-токена доступа к ПЦД/E-GOV.
 * Заменяет монолитный {@code PcdParametersLocalStorage}: токен запрашивается
 * по схеме grant_type=password (с Basic-авторизацией клиента), кэшируется в памяти,
 * обновляется по расписанию и принудительно при истечении (HTTP 401).
 */
@Slf4j
@Component
public class PcdTokenStore {

    private final IHttpService httpService;
    private final ObjectMapper objectMapper;

    @Value("${katm.mip.pcd.oauth-url:}")
    private String oauthUrl;

    @Value("${katm.mip.pcd.oauth-username:}")
    private String oauthUsername;

    @Value("${katm.mip.pcd.oauth-password:}")
    private String oauthPassword;

    @Value("${katm.mip.pcd.client-username:}")
    private String clientUsername;

    @Value("${katm.mip.pcd.client-password:}")
    private String clientPassword;

    private final AtomicReference<String> accessToken = new AtomicReference<>();

    public PcdTokenStore(IHttpService httpService, ObjectMapper objectMapper) {
        this.httpService = httpService;
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void init() {
        if (oauthUrl == null || oauthUrl.isBlank()) {
            log.warn("PCD OAuth URL не сконфигурирован (katm.mip.pcd.oauth-url) — PCD passthrough отключён");
            return;
        }
        reload();
    }

    public String getAccessToken() {
        String token = accessToken.get();
        if (token == null || token.isBlank()) {
            reload();
            token = accessToken.get();
        }
        return token;
    }

    /** Обновление токена раз в 59 минут (типичный TTL access_token = 1 час). */
    @Scheduled(fixedRate = 59 * 60 * 1000)
    public void scheduledReload() {
        if (oauthUrl != null && !oauthUrl.isBlank()) {
            reload();
        }
    }

    /** Принудительный перезапрос токена (вызывается при 401 от ПЦД). */
    public synchronized void reload() {
        try {
            Map<String, String> form = new HashMap<>();
            form.put("grant_type", "password");
            form.put("username", oauthUsername);
            form.put("password", oauthPassword);

            Map<String, String> headers = HttpUtils.basicAuthHeaders(clientUsername, clientPassword);

            HttpResponseInfo resp = httpService.sendPostFormRequest(oauthUrl, form, headers);
            if (resp == null || !resp.isSuccess()) {
                log.error("Не удалось получить OAuth-токен ПЦД, status={}",
                        resp != null ? resp.getStatusCode() : "null");
                return;
            }

            OAuthTokenResponse token = objectMapper.readValue(resp.getBodyAsString(), OAuthTokenResponse.class);
            if (token != null && token.accessToken() != null && !token.accessToken().isBlank()) {
                accessToken.set(token.accessToken());
                log.info("OAuth-токен ПЦД успешно обновлён");
            } else {
                log.error("OAuth-эндпоинт ПЦД вернул пустой access_token");
            }
        } catch (Exception e) {
            log.error("Ошибка при получении OAuth-токена ПЦД", e);
        }
    }
}