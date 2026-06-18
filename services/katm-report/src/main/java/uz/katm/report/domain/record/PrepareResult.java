package uz.katm.report.domain.record;

/** Результат подготовки отчёта (DATAS.PKG_ONLINE.PREPARE_CREDIT_REPORT): код/сообщение и выделенный demandId. */
public record PrepareResult(String code, String message, String demandId) {
}
