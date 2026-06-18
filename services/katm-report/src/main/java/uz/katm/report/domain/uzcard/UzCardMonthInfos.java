package uz.katm.report.domain.uzcard;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/** Помесячные обороты по карте UzCard. Перенос gov.uz.katm.core.client.uzcard.UzCardMonthInfos. */
@JsonIgnoreProperties(ignoreUnknown = true)
public record UzCardMonthInfos(
        @JsonProperty("month") String month,
        @JsonProperty("totalCash") Long totalCash,
        @JsonProperty("totalCashAtm") Long totalCashAtm,
        @JsonProperty("totalIncome") Long totalIncome,
        @JsonProperty("totalExpense") Long totalExpense,
        @JsonProperty("totalP2PIncome") Long totalP2PIncome,
        @JsonProperty("totalP2PExpense") Long totalP2PExpense,
        @JsonProperty("totalDepositIncome") Long totalDepositIncome,
        @JsonProperty("totalDepositExpense") Long totalDepositExpense,
        @JsonProperty("totalConversionIncome") Long totalConversionIncome,
        @JsonProperty("totalConversionExpense") Long totalConversionExpense
) {
}
