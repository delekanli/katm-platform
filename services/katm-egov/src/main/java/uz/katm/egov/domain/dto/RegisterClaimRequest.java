package uz.katm.egov.domain.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

/**
 * Регистрация заявки через E-GOV: инициализация клиента-физлица.
 * Перенос eGov registerClaim монолита (делегировал UCIN initClient).
 */
public record RegisterClaimRequest(
        @NotBlank String pinfl,
        String docSeries,
        String docNumber,
        LocalDate issueDocDate,
        String inn,
        String region,
        String localRegion,
        String lastName,
        String firstName,
        String middleName,
        LocalDate birthDate,
        String phone,
        String address,
        String sex,
        String resident,
        String ip
) {
}
