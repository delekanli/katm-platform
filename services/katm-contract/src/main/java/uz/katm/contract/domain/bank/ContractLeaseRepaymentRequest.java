package uz.katm.contract.domain.bank;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record ContractLeaseRepaymentRequest(
        @NotBlank String inn,
        @NotBlank String nibbd,
        @NotBlank String clientId,
        @NotBlank String leasingId,
        @NotNull LocalDate date,
        Integer isUpdate,
        @NotNull @NotEmpty @Valid List<LeaseRepaymentItem> repaymentArray
) {}
