package uz.katm.client.domain.record;

import jakarta.validation.constraints.NotBlank;

/**
 * Запрос снятия кредитного запрета (DATAS.CREDIT_BANS.DEACTIVATE_CREDIT_BAN).
 * head/code берутся из JWT, clientIp — из запроса (как в checkBanStatus).
 */
public record DeactivateCreditBanRequest(
        String subjectId,
        @NotBlank String identifier,
        String reason
) {
}
