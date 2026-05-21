package uz.katm.claim.domain.retailer;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record ClaimRegistrationExtRequest(
        @Size(max = 9) @Pattern(regexp = "[0-9]*") String inn,
        @Size(max = 30) String firstName,
        @Size(max = 30) String lastName,
        @Size(max = 30) String middleName,
        LocalDate birthDate,
        LocalDate issueDocDate,
        LocalDate expiredDocDate,
        Integer male,
        Integer resident,
        @NotBlank @Size(min = 88, max = 90) String mrz,
        @NotBlank @Size(max = 2) @Pattern(regexp = "[0-9]+") String docType,
        @NotBlank @Size(min = 1, max = 20) String claimId,
        @NotNull LocalDate claimDate,
        @NotBlank @Size(min = 1, max = 10) String agreementId,
        @NotNull LocalDate agreementDate,
        @NotBlank @Size(min = 2, max = 2) @Pattern(regexp = "[0-9]+") String region,
        @NotBlank @Size(min = 3, max = 3) @Pattern(regexp = "[0-9]+") String localRegion,
        @Size(max = 200) String address,
        @NotBlank @Size(min = 1, max = 5) String docSeries,
        @NotBlank @Size(min = 1, max = 10) String docNumber
) {}
