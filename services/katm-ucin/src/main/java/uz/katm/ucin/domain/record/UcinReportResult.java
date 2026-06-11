package uz.katm.ucin.domain.record;

/**
 * Результат запроса кредитного отчёта UCIN.
 * report — содержимое отчёта (CLOB → строка); token — для последующей сверки статуса; tariff — стоимость.
 */
public record UcinReportResult(
        String result,
        String message,
        String report,
        String token,
        Double tariff
) {
}
