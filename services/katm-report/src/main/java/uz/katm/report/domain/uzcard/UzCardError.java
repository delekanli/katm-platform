package uz.katm.report.domain.uzcard;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/** Ошибка JSON-RPC ответа UzCard. */
@JsonIgnoreProperties(ignoreUnknown = true)
public record UzCardError(
        @JsonProperty("code") String code,
        @JsonProperty("message") String message
) {
}
