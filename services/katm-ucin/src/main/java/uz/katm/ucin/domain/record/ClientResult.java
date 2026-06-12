package uz.katm.ucin.domain.record;

/** Результат инициализации клиента UCIN: clientId + код/сообщение операции. */
public record ClientResult(
        String clientId,
        String result,
        String message
) {
}
