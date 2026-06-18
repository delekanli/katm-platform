package uz.katm.report.domain.uzcard;

import com.fasterxml.jackson.annotation.JsonProperty;

/** Параметры запроса card.counts.get: идентификатор задания. */
public record UzCardGetInfosParams(@JsonProperty("jobId") String jobId) {
}
