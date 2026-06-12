package uz.katm.common.security;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import uz.katm.common.http.HttpResponseInfo;
import uz.katm.common.http.HttpUtils;
import uz.katm.common.http.IHttpService;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Поставщик сервисного (M2M) access-токена для межсервисных вызовов.
 * Получает токен по client_credentials у Keycloak (клиент {@code katm-service}),
 * кэширует до истечения и обновляет автоматически. Если не сконфигурирован —
 * {@link #getToken()} возвращает {@code null}, а {@link #authInterceptor()} не добавляет заголовок
 * (вызовы остаются без авторизации, как раньше).
 */
public class ServiceTokenProvider {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceTokenProvider.class);

    private final IHttpService httpService;
    private final ObjectMapper objectMapper;
    private final String tokenUri;
    private final String clientId;
    private final String clientSecret;

    private final AtomicReference<CachedToken> cache = new AtomicReference<>();

    public ServiceTokenProvider(IHttpService httpService, ObjectMapper objectMapper,
                                String tokenUri, String clientId, String clientSecret) {
        this.httpService = httpService;
        this.objectMapper = objectMapper;
        this.tokenUri = tokenUri;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    private boolean configured() {
        return tokenUri != null && !tokenUri.isBlank()
                && clientId != null && !clientId.isBlank();
    }

    /** Текущий сервисный токен (из кэша или свежий). {@code null}, если провайдер не сконфигурирован. */
    public String getToken() {
        if (!configured()) {
            return null;
        }
        CachedToken token = cache.get();
        if (token != null && Instant.now().isBefore(token.expiresAt().minusSeconds(30))) {
            return token.value();
        }
        return refresh();
    }

    private synchronized String refresh() {
        CachedToken existing = cache.get();
        if (existing != null && Instant.now().isBefore(existing.expiresAt().minusSeconds(30))) {
            return existing.value();
        }
        try {
            Map<String, String> form = new HashMap<>();
            form.put("grant_type", "client_credentials");
            Map<String, String> headers = HttpUtils.basicAuthHeaders(clientId, clientSecret);

            HttpResponseInfo resp = httpService.sendPostFormRequest(tokenUri, form, headers);
            if (resp == null || !resp.isSuccess()) {
                LOG.error("Не удалось получить сервисный токен, status={}",
                        resp != null ? resp.getStatusCode() : "null");
                return null;
            }
            TokenResponse tr = objectMapper.readValue(resp.getBodyAsString(), TokenResponse.class);
            if (tr == null || tr.accessToken() == null || tr.accessToken().isBlank()) {
                LOG.error("Token endpoint вернул пустой access_token");
                return null;
            }
            long ttl = tr.expiresIn() != null ? tr.expiresIn() : 60L;
            cache.set(new CachedToken(tr.accessToken(), Instant.now().plusSeconds(ttl)));
            LOG.debug("Сервисный токен обновлён (ttl={}s)", ttl);
            return tr.accessToken();
        } catch (Exception e) {
            LOG.error("Ошибка при получении сервисного токена", e);
            return null;
        }
    }

    /** Интерцептор для RestClient/RestTemplate: добавляет Bearer-токен, если он доступен. */
    public ClientHttpRequestInterceptor authInterceptor() {
        return (request, body, execution) -> {
            String token = getToken();
            if (token != null) {
                request.getHeaders().setBearerAuth(token);
            }
            return execution.execute(request, body);
        };
    }

    private record CachedToken(String value, Instant expiresAt) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record TokenResponse(
            @JsonProperty("access_token") String accessToken,
            @JsonProperty("expires_in") Long expiresIn
    ) {
    }
}
