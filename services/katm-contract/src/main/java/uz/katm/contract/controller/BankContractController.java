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
import uz.katm.contract.domain.bank.*;
import uz.katm.contract.service.BankContractService;

@Tag(name = "Bank Contracts", description = "Управление договорами и заявками банка")
@RestController
@RequestMapping("/api/v1/bank")
@RequiredArgsConstructor
@Validated
@PreAuthorize("hasAnyRole('BANK', 'ADMIN')")
public class BankContractController {

    private final BankContractService bankContractService;

    @Operation(summary = "Отклонить заявку")
    @PostMapping("/claim/decline")
    public ResponseEntity<ApiResponse<ProcedureResult>> declineClaim(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody DeclineClaimRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(
                bankContractService.declineClaim(JwtUtils.head(jwt), JwtUtils.code(jwt), request)));
    }

    @Operation(summary = "Получить информацию по заявке")
    @PostMapping("/claim/info")
    public ResponseEntity<ApiResponse<ClaimInfoResponse>> getClaim(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody GetClaimRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(
                bankContractService.getClaim(JwtUtils.head(jwt), JwtUtils.code(jwt), request)));
    }

    @Operation(summary = "Запрос по физическому лицу (МИП)")
    @PostMapping("/claim/individual")
    public ResponseEntity<ApiResponse<String>> inquiryIndividual(@Valid @RequestBody InquiryIndividualRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(bankContractService.inquiryIndividual(request)));
    }

    @Operation(summary = "Запрос по юридическому лицу (МИП)")
    @PostMapping("/claim/entity")
    public ResponseEntity<ApiResponse<String>> inquiryEntity(@Valid @RequestBody InquiryEntityRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(bankContractService.inquiryEntity(request)));
    }

    @Operation(summary = "Регистрация кредитного договора")
    @PostMapping("/contract/loan")
    public ResponseEntity<ApiResponse<ProcedureResult>> registerLoan(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody RegisterLoanRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(
                bankContractService.registerLoan(JwtUtils.head(jwt), JwtUtils.code(jwt), request)));
    }

    @Operation(summary = "Регистрация лизингового договора")
    @PostMapping("/contract/lease")
    public ResponseEntity<ApiResponse<ProcedureResult>> registerLease(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody RegisterLeaseRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(
                bankContractService.registerLease(JwtUtils.head(jwt), JwtUtils.code(jwt), request)));
    }

    @Operation(summary = "Регистрация факторингового договора")
    @PostMapping("/contract/factoring")
    public ResponseEntity<ApiResponse<ProcedureResult>> registerFactoring(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody RegisterFactoringRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(
                bankContractService.registerFactoring(JwtUtils.head(jwt), JwtUtils.code(jwt), request)));
    }

    @Operation(summary = "Добавить погашение по кредиту")
    @PostMapping("/contract/repayment")
    public ResponseEntity<ApiResponse<ProcedureResult>> addContractRepayment(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody ContractRepaymentRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(
                bankContractService.addContractRepayment(JwtUtils.head(jwt), JwtUtils.code(jwt), request)));
    }

    @Operation(summary = "Добавить детали погашения по кредиту")
    @PostMapping("/contract/repayment/details")
    public ResponseEntity<ApiResponse<ProcedureResult>> addContractRepaymentDetails(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody ContractRepaymentDetailsRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(
                bankContractService.addContractRepaymentDetails(JwtUtils.head(jwt), JwtUtils.code(jwt), request)));
    }

    @Operation(summary = "Добавить детали погашения (субъект)")
    @PostMapping("/contract/repayment/subject")
    public ResponseEntity<ApiResponse<ProcedureResult>> addSubjectRepaymentDetails(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody SubjectRepaymentDetailsRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(
                bankContractService.addSubjectRepaymentDetails(JwtUtils.head(jwt), JwtUtils.code(jwt), request)));
    }

    @Operation(summary = "Добавить график погашения кредита")
    @PostMapping("/contract/schedule")
    public ResponseEntity<ApiResponse<ProcedureResult>> addContractSchedule(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody ContractScheduleRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(
                bankContractService.addContractSchedule(JwtUtils.head(jwt), JwtUtils.code(jwt), request)));
    }

    @Operation(summary = "Добавить информацию о статусе счетов")
    @PostMapping("/contract/account-status")
    public ResponseEntity<ApiResponse<ProcedureResult>> addAccountStatusInfo(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody AccountStatusInfoRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(
                bankContractService.addAccountStatusInfo(JwtUtils.head(jwt), JwtUtils.code(jwt), request)));
    }

    @Operation(summary = "Регистрация связанных лиц")
    @PostMapping("/contract/related-entities")
    public ResponseEntity<ApiResponse<ProcedureResult>> addRelatedEntitiesRegistration(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody RelatedEntityRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(
                bankContractService.addRelatedEntitiesRegistration(JwtUtils.head(jwt), JwtUtils.code(jwt), request)));
    }

    @Operation(summary = "Регистрация залогодателя")
    @PostMapping("/contract/pledge-owner")
    public ResponseEntity<ApiResponse<ProcedureResult>> addPledgeOwnerRegistration(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody PledgeOwnerRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(
                bankContractService.addPledgeOwnerRegistration(JwtUtils.head(jwt), JwtUtils.code(jwt), request)));
    }

    @Operation(summary = "Регистрация обеспечения по кредиту")
    @PostMapping("/contract/loan-security")
    public ResponseEntity<ApiResponse<ProcedureResult>> addLoanSecurityRegistration(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody LoanSecurityRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(
                bankContractService.addLoanSecurityRegistration(JwtUtils.head(jwt), JwtUtils.code(jwt), request)));
    }

    @Operation(summary = "Добавить график погашения лизинга")
    @PostMapping("/contract/lease-schedule")
    public ResponseEntity<ApiResponse<ProcedureResult>> addLizContractSchedule(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody ContractLeaseScheduleRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(
                bankContractService.addLizContractSchedule(JwtUtils.head(jwt), JwtUtils.code(jwt), request)));
    }

    @Operation(summary = "Добавить погашение по лизингу")
    @PostMapping("/contract/lease-repayment")
    public ResponseEntity<ApiResponse<ProcedureResult>> addContractLeaseRepayment(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody ContractLeaseRepaymentRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(
                bankContractService.addContractLeaseRepayment(JwtUtils.head(jwt), JwtUtils.code(jwt), request)));
    }
}
