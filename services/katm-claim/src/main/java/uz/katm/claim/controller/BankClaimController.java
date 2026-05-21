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
import uz.katm.claim.domain.ProcedureResult;
import uz.katm.claim.domain.bank.*;
import uz.katm.claim.service.BankClaimService;
import uz.katm.common.dto.ApiResponse;
import uz.katm.common.security.JwtUtils;

@Tag(name = "Bank Claims", description = "Управление заявками: отклонение, получение информации, запросы по физическим и юридическим лицам")
@RestController
@RequestMapping("/api/v1/bank/claim")
@RequiredArgsConstructor
@Validated
@PreAuthorize("hasAnyRole('BANK', 'ADMIN')")
public class BankClaimController {

    private final BankClaimService bankClaimService;

    @Operation(summary = "Отклонить заявку")
    @PostMapping("/decline")
    public ResponseEntity<ApiResponse<ProcedureResult>> declineClaim(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody DeclineClaimRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(
                bankClaimService.declineClaim(JwtUtils.head(jwt), JwtUtils.code(jwt), request)));
    }

    @Operation(summary = "Получить информацию по заявке")
    @PostMapping("/info")
    public ResponseEntity<ApiResponse<ClaimInfoResponse>> getClaim(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody GetClaimRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(
                bankClaimService.getClaim(JwtUtils.head(jwt), JwtUtils.code(jwt), request)));
    }

    @Operation(summary = "Запрос по физическому лицу (МИП)")
    @PostMapping("/individual")
    public ResponseEntity<ApiResponse<String>> inquiryIndividual(@Valid @RequestBody InquiryIndividualRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(bankClaimService.inquiryIndividual(request)));
    }

    @Operation(summary = "Запрос по юридическому лицу (МИП)")
    @PostMapping("/entity")
    public ResponseEntity<ApiResponse<String>> inquiryEntity(@Valid @RequestBody InquiryEntityRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(bankClaimService.inquiryEntity(request)));
    }
}
