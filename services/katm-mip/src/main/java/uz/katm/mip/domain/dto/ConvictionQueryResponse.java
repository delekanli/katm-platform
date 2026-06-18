package uz.katm.mip.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/** Ответ на инициацию запроса о судимости (перенос ConvictionQueryResponse монолита). */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ConvictionQueryResponse(
        @JsonProperty("success") Boolean success,
        @JsonProperty("request_id") String requestId,
        @JsonProperty("message") String message
) {
}
