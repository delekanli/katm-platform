package uz.katm.egov.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uz.katm.common.dto.ApiResponse;
import uz.katm.egov.domain.dto.ClaimStatusUpdateRequest;
import uz.katm.egov.domain.dto.IndividualReportRequest;
import uz.katm.egov.domain.dto.LegalReportRequest;
import uz.katm.egov.domain.record.CreditReportResult;
import uz.katm.egov.domain.record.OperationResult;
import uz.katm.egov.service.EgovService;

/** Канал E-GOV: кредитные отчёты и статусы заявок (перенос eGov-домена монолита). */
@Tag(name = "E-GOV", description = "Канал E-GOV: кредитные отчёты и статусы заявок")
@RestController
@RequestMapping("/api/v1/egov")
@RequiredArgsConstructor
@Validated
@PreAuthorize("isAuthenticated()")
public class EgovController {

    private final EgovService service;

    @Operation(summary = "Кредитный отчёт по физлицу (EGOV_GET_CREDIT_REPORT)")
    @PostMapping("/reports/individual")
    public ResponseEntity<ApiResponse<CreditReportResult>> individualReport(
            @Valid @RequestBody IndividualReportRequest req) {
        return ResponseEntity.ok(ApiResponse.ok(service.getIndividualReport(req)));
    }

    @Operation(summary = "Кредитный отчёт по юрлицу (EGOV_LEGAL_GET_CREDIT_REPORT)")
    @PostMapping("/reports/legal")
    public ResponseEntity<ApiResponse<CreditReportResult>> legalReport(
            @Valid @RequestBody LegalReportRequest req) {
        return ResponseEntity.ok(ApiResponse.ok(service.getLegalReport(req)));
    }

    @Operation(summary = "Обновление статуса заявки (EGOV_CLAIM_UPDATE_STATUS)")
    @PostMapping("/claims/{claimId}/status")
    public ResponseEntity<ApiResponse<OperationResult>> updateClaimStatus(
            @NotBlank @PathVariable String claimId,
            @Valid @RequestBody ClaimStatusUpdateRequest req) {
        return ResponseEntity.ok(ApiResponse.ok(
                service.updateClaimStatus(claimId, req.claimStatus(), req.currentNode())));
    }
}
