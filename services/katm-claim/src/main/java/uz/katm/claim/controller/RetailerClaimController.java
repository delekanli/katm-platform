package uz.katm.claim.controller;

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
import uz.katm.claim.domain.retailer.*;
import uz.katm.claim.service.RetailerClaimService;
import uz.katm.common.dto.ApiResponse;
import uz.katm.common.security.JwtUtils;

@Tag(name = "Retailer Claims", description = "Регистрация заявок от ритейлеров")
@RestController
@RequestMapping("/api/v1/retailer/claim")
@RequiredArgsConstructor
@Validated
@PreAuthorize("hasAnyRole('RETAILER', 'ADMIN')")
public class RetailerClaimController {

    private final RetailerClaimService retailerClaimService;

    @Operation(summary = "Регистрация заявки физ.лица (по ПИНФЛ через МИП)")
    @PostMapping("/registration")
    public ResponseEntity<ApiResponse<OperationResult>> registerClaim(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody ClaimRegistrationRequest req) {
        return ResponseEntity.ok(ApiResponse.ok(
                retailerClaimService.registerClaim(JwtUtils.head(jwt), JwtUtils.code(jwt), req)));
    }

    @Operation(summary = "Регистрация заявки физ.лица (ручной ввод данных)")
    @PostMapping("/registration/ext")
    public ResponseEntity<ApiResponse<OperationResult>> registerClaimExt(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody ClaimRegistrationExtRequest req) {
        return ResponseEntity.ok(ApiResponse.ok(
                retailerClaimService.registerClaimExt(JwtUtils.head(jwt), JwtUtils.code(jwt), req)));
    }

    @Operation(summary = "Регистрация доверенной заявки физ.лица")
    @PostMapping("/registration/trusted")
    public ResponseEntity<ApiResponse<OperationResult>> registerClaimTrusted(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody ClaimRegistrationTrustRequest req) {
        return ResponseEntity.ok(ApiResponse.ok(
                retailerClaimService.registerClaimTrusted(JwtUtils.head(jwt), JwtUtils.code(jwt), req)));
    }

    @Operation(summary = "Регистрация заявки юр.лица (через МИП)")
    @PostMapping("/legal/registration")
    public ResponseEntity<ApiResponse<OperationResult>> registerLegalClaim(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody ClaimLegalRegistrationRequest req) {
        return ResponseEntity.ok(ApiResponse.ok(
                retailerClaimService.registerLegalClaim(JwtUtils.head(jwt), JwtUtils.code(jwt), req)));
    }

    @Operation(summary = "Регистрация заявки юр.лица (ручной ввод данных)")
    @PostMapping("/legal/registration/ext")
    public ResponseEntity<ApiResponse<OperationResult>> registerLegalClaimExt(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody ClaimLegalRegistrationRequest req) {
        return ResponseEntity.ok(ApiResponse.ok(
                retailerClaimService.registerLegalClaimExt(JwtUtils.head(jwt), JwtUtils.code(jwt), req)));
    }

    @Operation(summary = "Регистрация заявки организации (через МИП)")
    @PostMapping("/org/registration")
    public ResponseEntity<ApiResponse<OperationResult>> registerOrgClaim(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody ClaimLegalRegistrationRequest req) {
        return ResponseEntity.ok(ApiResponse.ok(
                retailerClaimService.registerOrgClaim(JwtUtils.head(jwt), JwtUtils.code(jwt), req)));
    }

    @Operation(summary = "Регистрация заявки организации (ручной ввод данных)")
    @PostMapping("/org/registration/ext")
    public ResponseEntity<ApiResponse<OperationResult>> registerOrgClaimExt(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody ClaimLegalRegistrationRequest req) {
        return ResponseEntity.ok(ApiResponse.ok(
                retailerClaimService.registerOrgClaimExt(JwtUtils.head(jwt), JwtUtils.code(jwt), req)));
    }
}
