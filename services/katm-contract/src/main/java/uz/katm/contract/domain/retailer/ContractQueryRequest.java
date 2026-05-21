package uz.katm.contract.domain.retailer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ContractQueryRequest(
        @NotBlank @Size(max = 20) String contractId
) {}
