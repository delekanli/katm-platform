package uz.katm.contract.domain.bank;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record PledgeOwnerRequest(
        @NotBlank String ownerId,
        String inn,
        String clientId,
        String resident,
        String subjectType,
        String identityCardType,
        String identityCardSerial,
        String identityCardNumber,
        LocalDate identityCardDate,
        String sex,
        String fio,
        String personalCode,
        String nibbd,
        String clientType,
        String fullName,
        String agreementNumber,
        LocalDate agreementDate,
        String legalAddress,
        LocalDate dateBirthday,
        @NotNull LocalDate date
) {}
