package uz.katm.report.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uz.katm.common.dto.ApiResponse;
import uz.katm.report.domain.record.ClaimData;
import uz.katm.report.exception.CreditServiceException;
import uz.katm.report.repository.CreditRepository;
import uz.katm.report.service.BeelineService;
import uz.katm.report.service.HumoService;

import java.util.Map;

/**
 * Телеком-шлюзы скоринга (HUMO, Beeline). Перенос BkiServiceImpl.getHumoData / getBeeline*Data.
 * Субъект резолвится по claimId через GET_DATA_BY_CLAIM; head/code — из JWT.
 */
@Tag(name = "Telecom", description = "Скоринг HUMO и Beeline")
@RestController
@RequestMapping("/api/v1/credits")
@RequiredArgsConstructor
@Validated
public class TelecomController {

    private static final int REPORT_HUMO = 42;
    private static final int REPORT_BEELINE_SCOR = 97;
    private static final int REPORT_BEELINE_PHONES = 98;
    private static final int REPORT_BEELINE_VERIFICATION = 99;
    private static final int REPORT_BEELINE_ROAMING = 89;

    private final CreditRepository creditRepository;
    private final HumoService humoService;
    private final BeelineService beelineService;

    @Operation(summary = "Скоринг HUMO по картам субъекта")
    @GetMapping("/humo")
    @PreAuthorize("hasAnyRole('BANK', 'ADMIN')")
    public ResponseEntity<ApiResponse<String>> getHumo(
            @AuthenticationPrincipal Jwt jwt, @NotBlank @RequestParam String claimId) {
        String head = head(jwt);
        String code = code(jwt);
        ClaimData claim = resolveClaim(head, code, claimId, REPORT_HUMO);
        return ResponseEntity.ok(ApiResponse.ok(humoService.getHumo(head, code, claimId, claim)));
    }

    @Operation(summary = "Скоринг Beeline")
    @GetMapping("/beeline/scor")
    @PreAuthorize("hasAnyRole('BANK', 'ADMIN')")
    public ResponseEntity<ApiResponse<String>> getBeelineScor(
            @AuthenticationPrincipal Jwt jwt, @NotBlank @RequestParam String claimId) {
        return ResponseEntity.ok(ApiResponse.ok(
                beelineService.getScor(resolveClaim(head(jwt), code(jwt), claimId, REPORT_BEELINE_SCOR))));
    }

    @Operation(summary = "Телефоны Beeline")
    @GetMapping("/beeline/phones")
    @PreAuthorize("hasAnyRole('BANK', 'ADMIN')")
    public ResponseEntity<ApiResponse<String>> getBeelinePhones(
            @AuthenticationPrincipal Jwt jwt, @NotBlank @RequestParam String claimId) {
        return ResponseEntity.ok(ApiResponse.ok(
                beelineService.getPhones(resolveClaim(head(jwt), code(jwt), claimId, REPORT_BEELINE_PHONES))));
    }

    @Operation(summary = "Верификация Beeline")
    @GetMapping("/beeline/verification")
    @PreAuthorize("hasAnyRole('BANK', 'ADMIN')")
    public ResponseEntity<ApiResponse<String>> getBeelineVerification(
            @AuthenticationPrincipal Jwt jwt, @NotBlank @RequestParam String claimId) {
        return ResponseEntity.ok(ApiResponse.ok(
                beelineService.getVerification(resolveClaim(head(jwt), code(jwt), claimId, REPORT_BEELINE_VERIFICATION))));
    }

    @Operation(summary = "Верификация роуминга Beeline")
    @GetMapping("/beeline/roaming")
    @PreAuthorize("hasAnyRole('BANK', 'ADMIN')")
    public ResponseEntity<ApiResponse<String>> getBeelineRoaming(
            @AuthenticationPrincipal Jwt jwt, @NotBlank @RequestParam String claimId) {
        return ResponseEntity.ok(ApiResponse.ok(
                beelineService.getRoaming(resolveClaim(head(jwt), code(jwt), claimId, REPORT_BEELINE_ROAMING))));
    }

    private ClaimData resolveClaim(String head, String code, String claimId, int reportId) {
        ClaimData claim = creditRepository.getClaimData(head, code, claimId, String.valueOf(reportId));
        if (!claim.isSuccess()) {
            throw new CreditServiceException(claim.code(), claim.message());
        }
        return claim;
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> attrs(Jwt jwt) {
        return jwt.getClaimAsMap("attributes");
    }

    private static String head(Jwt jwt) {
        return (String) attrs(jwt).get("head");
    }

    private static String code(Jwt jwt) {
        return (String) attrs(jwt).get("code");
    }
}
