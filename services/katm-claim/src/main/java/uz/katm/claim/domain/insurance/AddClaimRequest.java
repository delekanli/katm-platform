package uz.katm.claim.domain.insurance;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record AddClaimRequest(
        Integer initialId,
        @NotBlank String claimId,
        @NotBlank String lastName,
        @NotBlank String firstName,
        String middleName,
        int sex,
        @NotNull LocalDate birthDate,
        @NotNull LocalDate docIssueDate,
        @NotNull LocalDate docExpireDate,
        int isUpdate,
        int mipState,
        String mipResult,
        @NotBlank String tin,
        int docType
) {}
