package uz.katm.contract.domain.bank;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record RelatedEntityRequest(
        @NotBlank String claimId,
        @NotBlank String ownerId,
        @NotBlank String loanSubject,
        @NotBlank String subjectStatus,
        @NotNull LocalDate date
) {}
