package uz.katm.report.domain.uzcard;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/** Результат card.counts.create: идентификаторы созданных заданий скоринга. */
@JsonIgnoreProperties(ignoreUnknown = true)
public record UzCardCountId(@JsonProperty("jobIds") String[] jobIds) {
}
