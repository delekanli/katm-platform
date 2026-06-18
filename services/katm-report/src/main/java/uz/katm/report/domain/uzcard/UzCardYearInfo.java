package uz.katm.report.domain.uzcard;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/** Годовая разбивка оборотов по карте UzCard. */
@JsonIgnoreProperties(ignoreUnknown = true)
public record UzCardYearInfo(
        @JsonProperty("year") String year,
        @JsonProperty("monthsInfo") List<UzCardMonthInfos> monthsInfo
) {
}
