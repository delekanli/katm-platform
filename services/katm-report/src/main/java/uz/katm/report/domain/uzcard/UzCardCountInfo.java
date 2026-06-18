package uz.katm.report.domain.uzcard;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/** Результат card.counts.get: число активных карт и скоринг по каждой. */
@JsonIgnoreProperties(ignoreUnknown = true)
public record UzCardCountInfo(
        @JsonProperty("activeCards") Integer activeCards,
        @JsonProperty("countInfos") List<UzCardScoringInfos> countInfos
) {
}
