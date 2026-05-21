package uz.katm.contract.domain.bank;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record RegisterLeaseRequest(
        @NotBlank String claimId,
        @NotBlank @Size(min = 9, max = 9) @Pattern(regexp = "[0-9]+") String inn,
        @NotBlank String nibbd,
        @NotBlank String leasingId,
        @NotNull LocalDate date,
        @NotNull LocalDate dateLeasingBegin,
        @NotNull LocalDate dateLeasingEnd,
        String notariusRegNumber,
        LocalDate notariusRegDate,
        String notariusCertNumber,
        LocalDate notariusCertDate,
        String governmentRegNum,
        LocalDate governmentRegDate,
        @NotNull @Positive Long summa,
        @NotBlank String currency,
        Double procent,
        Integer countObject
) {}
