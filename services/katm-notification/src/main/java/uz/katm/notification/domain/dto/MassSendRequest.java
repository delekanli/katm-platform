package uz.katm.notification.domain.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/** Запрос массовой рассылки уведомлений (перенос /notification/pool монолита). */
public record MassSendRequest(
        @NotEmpty List<String> clientIds,
        @NotNull Integer notificationType
) {
}
