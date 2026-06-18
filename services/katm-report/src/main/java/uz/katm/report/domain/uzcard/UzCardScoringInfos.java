package uz.katm.report.domain.uzcard;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/** Скоринговые данные по одной карте UzCard. Ключ avarageIncome сохранён как в контракте монолита. */
@JsonIgnoreProperties(ignoreUnknown = true)
public record UzCardScoringInfos(
        @JsonProperty("pan") String pan,
        @JsonProperty("fullname") String fullname,
        @JsonProperty("avarageIncome") String avarageIncome,
        @JsonProperty("yearInfo") List<UzCardYearInfo> yearInfo
) {
}
