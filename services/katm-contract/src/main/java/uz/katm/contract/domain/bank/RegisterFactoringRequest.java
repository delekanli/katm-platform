package uz.katm.contract.domain.bank;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record RegisterFactoringRequest(
        @NotBlank String claimId,
        @NotBlank @Size(min = 9, max = 9) @Pattern(regexp = "[0-9]+") String inn,
        @NotBlank String nibbd,
        @NotBlank String factoringId,
        @NotNull LocalDate date,
        @NotNull LocalDate dateBegin,
        @NotNull LocalDate dateEnd,
        String bankElement,
        String factoringNumber,
        @NotBlank String currency,
        @NotNull @Positive Long summaLiability,
        @NotNull @Positive Long summaDiscount,
        @NotBlank @Size(min = 9, max = 9) @Pattern(regexp = "[0-9]+") String innDebtor
) {}
