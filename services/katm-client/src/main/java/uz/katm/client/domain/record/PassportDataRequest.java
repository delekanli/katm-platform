package uz.katm.client.domain.record;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record PassportDataRequest(
        @NotBlank String tin,
        @NotBlank String firstName,
        @NotBlank String lastName,
        String middleName,
        @NotBlank String docSeries,
        @NotBlank String docNumber,
        @NotNull LocalDate docIssueDate,
        @NotNull LocalDate docExpireDate,
        int resident,
        int sex,
        int isAlive,
        String birthCountry,
        String birthPlace,
        @NotNull LocalDate birthDate,
        @NotBlank String pinfl
) {}
