package uz.katm.client.domain.record;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

/**
 * Запрос установки кредитного запрета (DATAS.CREDIT_BANS.ADD_CREDIT_BAN).
 * head/code берутся из JWT, clientIp — из запроса (как в checkBanStatus).
 */
public record AddCreditBanRequest(
        String subjectId,
        @NotBlank String identifier,
        LocalDate idenDate,
        String fullName,
        String reason,
        LocalDate endDate
) {
}
