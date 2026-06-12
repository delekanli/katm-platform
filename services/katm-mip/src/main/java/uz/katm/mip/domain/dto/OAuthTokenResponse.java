package uz.katm.mip.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Ответ OAuth2-эндпоинта ПЦД/E-GOV (grant_type=password).
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record OAuthTokenResponse(
        @JsonProperty("access_token") String accessToken,
        @JsonProperty("token_type") String tokenType,
        @JsonProperty("expires_in") Long expiresIn,
        @JsonProperty("refresh_token") String refreshToken
) {
}