package uz.katm.registry.domain.record;

/** Результат операции без данных: код результата + сообщение. */
public record OperationResult(
        String result,
        String message
) {
}
