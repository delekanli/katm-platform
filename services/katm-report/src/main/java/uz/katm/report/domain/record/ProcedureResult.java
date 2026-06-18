package uz.katm.report.domain.record;

/** Результат хранимой процедуры с кодом и сообщением (P_RESULT / P_RET_MSG). */
public record ProcedureResult(String code, String message) {
}
