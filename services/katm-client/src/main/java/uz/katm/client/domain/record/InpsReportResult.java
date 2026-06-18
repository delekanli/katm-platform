package uz.katm.client.domain.record;

/** Результат запроса отчёта по отчислениям ИНПС: код/сообщение и JSON-строка отчёта. */
public record InpsReportResult(String code, String message, String report) {
}
