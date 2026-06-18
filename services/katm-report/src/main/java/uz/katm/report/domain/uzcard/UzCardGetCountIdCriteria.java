package uz.katm.report.domain.uzcard;

import com.fasterxml.jackson.annotation.JsonProperty;

/** Критерий запроса card.counts.create: ПИНФЛ и период (yyyyMMdd). */
public record UzCardGetCountIdCriteria(
        @JsonProperty("pinfl") String pinfl,
        @JsonProperty("startDate") String startDate,
        @JsonProperty("endDate") String endDate
) {
}
