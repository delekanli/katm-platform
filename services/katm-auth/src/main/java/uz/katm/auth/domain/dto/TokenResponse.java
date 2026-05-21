package uz.katm.auth.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class TokenResponse {

    @JsonProperty("access_token")
    String accessToken;

    @JsonProperty("refresh_token")
    String refreshToken;

    @JsonProperty("token_type")
    String tokenType;

    @JsonProperty("expires_in")
    long expiresIn;

    @JsonProperty("refresh_expires_in")
    long refreshExpiresIn;
}
