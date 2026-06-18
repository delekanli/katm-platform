package uz.katm.report.domain.uzcard;

import com.fasterxml.jackson.annotation.JsonProperty;

/** Параметры запроса card.counts.create. */
public record UzCardGetCountIdParams(@JsonProperty("criteria") UzCardGetCountIdCriteria criteria) {
}
