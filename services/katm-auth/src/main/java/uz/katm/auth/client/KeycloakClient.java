package uz.katm.auth.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import uz.katm.auth.domain.dto.TokenResponse;
import uz.katm.auth.exception.AuthException;

import java.nio.charset.StandardCharsets;

/**
 * HTTP-клиент для работы с Keycloak OpenID Connect Token Endpoint.
 * Использует Resource Owner Password Credentials (ROPC) grant для выпуска токенов
 * и стандартный refresh/logout flow.
 */
@Slf4j
@Component
public class KeycloakClient {

    private static final String GRANT_TYPE_PASSWORD      = "password";
    private static final String GRANT_TYPE_REFRESH_TOKEN = "refresh_token";
    private static final String PARAM_GRANT_TYPE         = "grant_type";
    private static final String PARAM_CLIENT_ID          = "client_id";
    private static final String PARAM_CLIENT_SECRET      = "client_secret";
    private static final String PARAM_USERNAME           = "username";
    private static final String PARAM_PASSWORD           = "password";
    private static final String PARAM_REFRESH_TOKEN      = "refresh_token";
    private static final String PARAM_TOKEN              = "token";
    private static final String PARAM_TOKEN_TYPE_HINT    = "token_type_hint";

    private final RestClient restClient;
    private final String tokenEndpoint;
    private final String logoutEndpoint;
    private final String clientId;
    private final String clientSecret;

    public KeycloakClient(
            @Value("${katm.auth.keycloak-url}") String keycloakUrl,
            @Value("${katm.auth.realm}")         String realm,
            @Value("${katm.auth.client-id}")     String clientId,
            @Value("${katm.auth.client-secret}") String clientSecret
    ) {
        String baseUrl       = keycloakUrl + "/realms/" + realm + "/protocol/openid-connect";
        this.tokenEndpoint   = baseUrl + "/token";
        this.logoutEndpoint  = baseUrl + "/logout";
        this.clientId        = clientId;
        this.clientSecret    = clientSecret;
        this.restClient      = RestClient.builder().build();
    }

    public TokenResponse getTokenByPassword(String username, String password) {
        log.debug("Keycloak ROPC token request: username={}", username);
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add(PARAM_GRANT_TYPE,    GRANT_TYPE_PASSWORD);
        form.add(PARAM_CLIENT_ID,     clientId);
        form.add(PARAM_CLIENT_SECRET, clientSecret);
        form.add(PARAM_USERNAME,      username);
        form.add(PARAM_PASSWORD,      password);
        return exchange(form);
    }

    public TokenResponse refreshToken(String refreshToken) {
        log.debug("Keycloak refresh token request");
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add(PARAM_GRANT_TYPE,    GRANT_TYPE_REFRESH_TOKEN);
        form.add(PARAM_CLIENT_ID,     clientId);
        form.add(PARAM_CLIENT_SECRET, clientSecret);
        form.add(PARAM_REFRESH_TOKEN, refreshToken);
        return exchange(form);
    }

    public void logout(String refreshToken) {
        log.debug("Keycloak logout request");
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add(PARAM_CLIENT_ID,      clientId);
        form.add(PARAM_CLIENT_SECRET,  clientSecret);
        form.add(PARAM_REFRESH_TOKEN,  refreshToken);
        form.add(PARAM_TOKEN,          refreshToken);
        form.add(PARAM_TOKEN_TYPE_HINT, "refresh_token");

        restClient.post()
                .uri(logoutEndpoint)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(form)
                .retrieve()
                .toBodilessEntity();
    }

    private TokenResponse exchange(MultiValueMap<String, String> form) {
        KeycloakTokenResponse raw = restClient.post()
                .uri(tokenEndpoint)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(form)
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                        (req, res) -> {
                            String body = new String(res.getBody().readAllBytes(), StandardCharsets.UTF_8);
                            log.error("Keycloak вернул {} для {}. Тело: {}", res.getStatusCode(), tokenEndpoint, body);
                            throw new AuthException("Ошибка получения токена от Keycloak: HTTP "
                                    + res.getStatusCode().value() + " — " + body);
                        })
                .body(KeycloakTokenResponse.class);

        if (raw == null) {
            throw new AuthException("Пустой ответ от Keycloak");
        }
        return TokenResponse.builder()
                .accessToken(raw.access_token())
                .refreshToken(raw.refresh_token())
                .tokenType(raw.token_type())
                .expiresIn(raw.expires_in())
                .refreshExpiresIn(raw.refresh_expires_in())
                .build();
    }

    private record KeycloakTokenResponse(
            String access_token,
            String refresh_token,
            String token_type,
            long   expires_in,
            long   refresh_expires_in,
            String scope
    ) {}
}
