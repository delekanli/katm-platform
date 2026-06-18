package uz.katm.client.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uz.katm.client.domain.record.AddCreditBanRequest;
import uz.katm.client.domain.record.BanStatusResponse;
import uz.katm.client.domain.record.ClientUserInfo;
import uz.katm.client.domain.record.CreditBanHistoryItem;
import uz.katm.client.domain.record.CreditBanInfo;
import uz.katm.client.domain.record.DeactivateCreditBanRequest;
import uz.katm.client.domain.record.InpsReportResult;
import uz.katm.client.domain.record.PassportDataRequest;
import uz.katm.client.domain.record.ProcedureResult;
import uz.katm.client.service.ClientService;
import uz.katm.client.service.InpsService;
import uz.katm.common.dto.ApiResponse;
import uz.katm.common.security.JwtUtils;

import java.util.List;

@Tag(name = "Clients", description = "Управление клиентскими данными")
@RestController
@RequestMapping("/api/v1/clients")
@RequiredArgsConstructor
@Validated
public class ClientController {

    private final ClientService clientService;
    private final InpsService inpsService;

    @Operation(summary = "Сохранить паспортные данные из МИП")
    @PostMapping("/passport")
    @PreAuthorize("hasAnyRole('BANK', 'ADMIN')")
    public ResponseEntity<ApiResponse<ProcedureResult>> savePassportData(
            @Valid @RequestBody PassportDataRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(clientService.savePassportData(request)));
    }

    @Operation(summary = "Сменить пароль клиента")
    @PostMapping("/{clientId}/password")
    @PreAuthorize("hasAnyRole('CLIENT', 'ADMIN')")
    public ResponseEntity<ApiResponse<ProcedureResult>> updatePassword(
            @NotBlank @PathVariable String clientId,
            @NotBlank @RequestParam String newPassword) {
        return ResponseEntity.ok(ApiResponse.ok(clientService.updatePassword(clientId, newPassword)));
    }

    @Operation(summary = "Проверить наличие запрета на кредит")
    @GetMapping("/ban-status")
    @PreAuthorize("hasAnyRole('BANK', 'INSURANCE', 'ADMIN')")
    public ResponseEntity<ApiResponse<BanStatusResponse>> checkBanStatus(
            @AuthenticationPrincipal Jwt jwt,
            @NotBlank @RequestParam String subjectId,
            @NotBlank @RequestParam String identifier,
            @RequestParam(required = false, defaultValue = "") String additionalInfo,
            HttpServletRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(
                clientService.checkCreditBanStatus(
                        JwtUtils.head(jwt), JwtUtils.code(jwt), subjectId, identifier, clientIp(request), additionalInfo)));
    }

    @Operation(summary = "Установить запрет на кредит")
    @PostMapping("/credit-ban")
    @PreAuthorize("hasAnyRole('BANK', 'ADMIN')")
    public ResponseEntity<ApiResponse<ProcedureResult>> addCreditBan(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody AddCreditBanRequest body,
            HttpServletRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(
                clientService.addCreditBan(JwtUtils.head(jwt), JwtUtils.code(jwt), clientIp(request), body)));
    }

    @Operation(summary = "Снять запрет на кредит")
    @PostMapping("/credit-ban/deactivate")
    @PreAuthorize("hasAnyRole('BANK', 'ADMIN')")
    public ResponseEntity<ApiResponse<ProcedureResult>> deactivateCreditBan(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody DeactivateCreditBanRequest body,
            HttpServletRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(
                clientService.deactivateCreditBan(JwtUtils.head(jwt), JwtUtils.code(jwt), clientIp(request), body)));
    }

    @Operation(summary = "История запретов на кредит")
    @GetMapping("/credit-ban/history")
    @PreAuthorize("hasAnyRole('BANK', 'INSURANCE', 'ADMIN')")
    public ResponseEntity<ApiResponse<List<CreditBanHistoryItem>>> getCreditBanHistory(
            @NotBlank @RequestParam String identifier,
            @NotBlank @RequestParam String subjectId) {
        return ResponseEntity.ok(ApiResponse.ok(clientService.getCreditBanHistory(identifier, subjectId)));
    }

    @Operation(summary = "Информация о запрете по хэшу")
    @GetMapping("/credit-ban/{hash}")
    @PreAuthorize("hasAnyRole('BANK', 'INSURANCE', 'ADMIN')")
    public ResponseEntity<ApiResponse<CreditBanInfo>> getCreditBanInfoByHash(@NotBlank @PathVariable String hash) {
        return ResponseEntity.ok(ApiResponse.ok(clientService.getCreditBanInfoByHash(hash)));
    }

    @Operation(summary = "Отчисления ИНПС для скоринга (Халк банк)")
    @GetMapping("/inps/scoring")
    @PreAuthorize("hasAnyRole('BANK', 'ADMIN')")
    public ResponseEntity<ApiResponse<InpsReportResult>> getScoringInps(
            @AuthenticationPrincipal Jwt jwt,
            @NotBlank @RequestParam String claimId,
            @NotBlank @RequestParam String reportId) {
        return ResponseEntity.ok(ApiResponse.ok(
                inpsService.getScoringInps(JwtUtils.head(jwt), JwtUtils.code(jwt), claimId, reportId)));
    }

    @Operation(summary = "Отчисления ИНПС для отчёта (Халк банк)")
    @GetMapping("/inps")
    @PreAuthorize("hasAnyRole('BANK', 'ADMIN')")
    public ResponseEntity<ApiResponse<InpsReportResult>> getInps(
            @AuthenticationPrincipal Jwt jwt,
            @NotBlank @RequestParam String claimId,
            @NotBlank @RequestParam String reportId) {
        return ResponseEntity.ok(ApiResponse.ok(
                inpsService.getInps(JwtUtils.head(jwt), JwtUtils.code(jwt), claimId, reportId)));
    }

    @Operation(summary = "Найти пользователя клиент-портала по логину")
    @GetMapping("/users/{login}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ClientUserInfo>> getClientUser(@NotBlank @PathVariable String login) {
        return ResponseEntity.ok(ApiResponse.ok(clientService.getClientUserByLogin(login)));
    }

    private static String clientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        return (forwarded != null && !forwarded.isBlank())
                ? forwarded.split(",")[0].trim()
                : request.getRemoteAddr();
    }
}
