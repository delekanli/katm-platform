package uz.katm.claim.domain.retailer;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record ClaimLegalRegistrationRequest(
        @NotBlank @Size(min = 9, max = 9) @Pattern(regexp = "[0-9]+") String inn,
        Integer resident,
        Integer government,
        @NotBlank @Size(min = 2, max = 2) @Pattern(regexp = "[0-9]+") String clientType,
        @Size(max = 5) @Pattern(regexp = "[0-9]*") String headCode,
        @NotBlank @Size(min = 1, max = 20) String claimId,
        @NotNull LocalDate claimDate,
        @NotBlank @Size(min = 1, max = 10) String agreementId,
        @NotNull LocalDate agreementDate,
        @NotBlank @Size(min = 2, max = 2) @Pattern(regexp = "[0-9]+") String region,
        @NotBlank @Size(min = 3, max = 3) @Pattern(regexp = "[0-9]+") String localRegion,
        @Size(max = 200) String address
) {}
