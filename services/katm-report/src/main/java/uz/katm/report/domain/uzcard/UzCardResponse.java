package uz.katm.report.domain.uzcard;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/** Обёртка JSON-RPC ответа UzCard (перенос UzCardCommonResult). */
@JsonIgnoreProperties(ignoreUnknown = true)
public record UzCardResponse<R>(
        @JsonProperty("id") Integer id,
        @JsonProperty("jsonrpc") String jsonrpc,
        @JsonProperty("result") R result,
        @JsonProperty("error") UzCardError error
) {
}
