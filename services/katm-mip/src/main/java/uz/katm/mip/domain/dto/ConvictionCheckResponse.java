package uz.katm.mip.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/** Ответ на проверку готовности отчёта о судимости (перенос ConvictionCheckResponse монолита). */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ConvictionCheckResponse(
        @JsonProperty("success") Boolean success,
        @JsonProperty("message") String message
) {
}
