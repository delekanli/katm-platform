package uz.katm.contract.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uz.katm.common.dto.ApiResponse;
import uz.katm.common.security.JwtUtils;
import uz.katm.contract.domain.retailer.*;
import uz.katm.contract.service.RetailerContractService;

import java.util.List;
import java.util.Map;

@Tag(name = "Retailer Contracts", description = "Управление договорами ритейлеров")
@RestController
@RequestMapping("/api/v1/retailer/contract")
@RequiredArgsConstructor
@Validated
@PreAuthorize("hasAnyRole('RETAILER', 'ADMIN')")
public class RetailerContractController {

    private final RetailerContractService retailerContractService;

    @Operation(summary = "Регистрация договора")
    @PostMapping("/registration")
    public ResponseEntity<ApiResponse<OperationResult>> registerContract(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody ContractRegistrationRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(retailerContractService.registerContract(JwtUtils.code(jwt), request)));
    }

    @Operation(summary = "Добавить график погашения")
    @PostMapping("/schedule/add")
    public ResponseEntity<ApiResponse<OperationResult>> addContractSchedule(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody ScheduleAddRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(retailerContractService.addContractSchedule(JwtUtils.code(jwt), request)));
    }

    @Operation(summary = "Добавить погашение")
    @PostMapping("/repayment/add")
    public ResponseEntity<ApiResponse<OperationResult>> addContractRepayment(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody RepaymentAddRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(retailerContractService.addContractRepayment(JwtUtils.code(jwt), request)));
    }

    @Operation(summary = "Получить список погашений")
    @PostMapping("/repayment/list")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getRepayments(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody ContractQueryRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(retailerContractService.getRepayments(
                JwtUtils.head(jwt), JwtUtils.code(jwt), request)));
    }

    @Operation(summary = "Получить график погашений")
    @PostMapping("/repayment/schedule")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getRepaymentsSchedule(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody ContractQueryRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(retailerContractService.getRepaymentsSchedule(
                JwtUtils.head(jwt), JwtUtils.code(jwt), request)));
    }

    @Operation(summary = "Получить информацию по договору")
    @PostMapping("/info")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getContractInfo(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody ContractQueryRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(retailerContractService.getContractInfo(
                JwtUtils.head(jwt), JwtUtils.code(jwt), request)));
    }
}
