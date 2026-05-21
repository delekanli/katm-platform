package uz.katm.contract.domain.retailer;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record RepaymentAddRequest(
        @Size(max = 20) String claimId,
        @NotBlank @Size(max = 20) String contractId,
        Integer isUpdate,
        @NotNull @NotEmpty @Valid List<RepaymentItem> repaymentArray
) {}
