package uz.katm.contract.domain.bank;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record ContractScheduleRequest(
        @NotBlank String contractId,
        @NotBlank String inn,
        @NotBlank String nibbd,
        @NotNull LocalDate date,
        Integer isUpdate,
        @NotNull @NotEmpty @Valid List<ScheduleItem> scheduleArray
) {}
