package uz.katm.system.domain.record;

/** Результат хранимой процедуры: код + сообщение. */
public record ProcedureResult(
        String result,
        String message
) {
}
