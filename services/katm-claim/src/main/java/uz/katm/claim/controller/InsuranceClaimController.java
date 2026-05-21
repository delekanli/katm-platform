package uz.katm.claim.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uz.katm.claim.domain.ProcedureResult;
import uz.katm.claim.domain.insurance.AddClaimRequest;
import uz.katm.claim.domain.insurance.ClaimResult;
import uz.katm.claim.domain.insurance.InitiateClaimRequest;
import uz.katm.claim.service.InsuranceClaimService;
import uz.katm.common.dto.ApiResponse;
import uz.katm.common.security.JwtUtils;

@Tag(name = "Insurance Claims", description = "Страховые случаи")
@RestController
@RequestMapping("/api/v1/insurance/claims")
@RequiredArgsConstructor
@Validated
@PreAuthorize("hasAnyRole('INSURANCE', 'ADMIN')")
public class InsuranceClaimController {

    private final InsuranceClaimService insuranceClaimService;

    @Operation(summary = "Инициировать страховой случай")
    @PostMapping("/initiate")
    public ResponseEntity<ApiResponse<ClaimResult>> initiateClaim(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody InitiateClaimRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(insuranceClaimService.initiateClaim(JwtUtils.code(jwt), request)));
    }

    @Operation(summary = "Добавить страховой случай")
    @PostMapping("/add")
    public ResponseEntity<ApiResponse<ProcedureResult>> addClaim(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody AddClaimRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(insuranceClaimService.addClaim(JwtUtils.code(jwt), request)));
    }

    @Operation(summary = "Отклонить страховой случай")
    @PostMapping("/reject/{claimId}")
    public ResponseEntity<ApiResponse<ProcedureResult>> rejectClaim(
            @AuthenticationPrincipal Jwt jwt,
            @NotBlank @PathVariable String claimId,
            @NotBlank @RequestParam String reason) {
        return ResponseEntity.ok(ApiResponse.ok(
                insuranceClaimService.rejectClaim(claimId, JwtUtils.code(jwt), reason)));
    }
}
