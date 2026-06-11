package uz.katm.egov.domain.record;

/**
 * Результат запроса кредитного отчёта через канал E-GOV.
 * token — идентификатор для последующего получения документа отчёта; tarif — стоимость.
 */
public record CreditReportResult(
        String result,
        String message,
        String token,
        Double tarif
) {
}
