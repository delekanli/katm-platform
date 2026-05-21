package uz.katm.contract.domain.retailer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record ContractRegistrationRequest(
        @NotBlank @Size(max = 20) String claimId,
        @NotBlank @Size(max = 20) String contractId,
        @NotBlank @Size(max = 6) @Pattern(regexp = "[0-9]+") String object,
        @NotNull LocalDate startDate,
        @NotNull LocalDate endDate,
        @NotNull Long creditAmount,
        @NotBlank @Size(max = 3) @Pattern(regexp = "[0-9]+") String currency,
        Integer isUpdate,
        String contractStatus
) {}
