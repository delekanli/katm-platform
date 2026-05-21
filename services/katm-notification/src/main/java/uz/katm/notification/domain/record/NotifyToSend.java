package uz.katm.notification.domain.record;

public record NotifyToSend(
        long id,
        String recipient,
        String methodSender,
        String typeShortName,
        String text,
        String entityType,
        long entityId,
        String methodId,
        String typeId,
        String typeTranslitName,
        String submethodType
) {}
