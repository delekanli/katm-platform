package uz.katm.claim.domain.retailer;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record ClaimRegistrationRequest(
        @NotBlank @Size(min = 14, max = 14) @Pattern(regexp = "[0-9]+") String pinfl,
        @NotBlank @Size(min = 1, max = 5) String docSeries,
        @NotBlank @Size(min = 1, max = 10) String docNumber,
        @Size(max = 2) @Pattern(regexp = "[0-9]*") String docType,
        @NotBlank @Size(min = 1, max = 20) String claimId,
        @NotNull LocalDate claimDate,
        @NotBlank @Size(min = 1, max = 10) String agreementId,
        @NotNull LocalDate agreementDate,
        @NotBlank @Size(min = 2, max = 2) @Pattern(regexp = "[0-9]+") String region,
        @NotBlank @Size(min = 3, max = 3) @Pattern(regexp = "[0-9]+") String localRegion,
        @Size(max = 200) String address,
        @Size(max = 9) @Pattern(regexp = "[0-9]*") String inn
) {}
