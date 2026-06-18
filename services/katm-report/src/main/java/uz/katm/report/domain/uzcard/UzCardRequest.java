package uz.katm.report.domain.uzcard;

import com.fasterxml.jackson.annotation.JsonProperty;

/** Обёртка JSON-RPC запроса UzCard (перенос UzCardCommonParams). */
public record UzCardRequest<P>(
        @JsonProperty("id") Integer id,
        @JsonProperty("jsonrpc") String jsonrpc,
        @JsonProperty("method") String method,
        @JsonProperty("params") P params
) {
}
