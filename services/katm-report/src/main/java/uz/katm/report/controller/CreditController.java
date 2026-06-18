package uz.katm.report.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uz.katm.common.dto.ApiResponse;
import uz.katm.report.domain.record.CreditReportRequest;
import uz.katm.report.domain.record.CreditReportResponse;
import uz.katm.report.domain.record.FicoResult;
import uz.katm.report.domain.record.QualityReportRequest;
import uz.katm.report.domain.record.SystemReportRequest;
import uz.katm.report.service.CreditService;
import uz.katm.report.service.ReportDispatchService;

import java.util.List;
import java.util.Map;

@Tag(name = "Credits", description = "Кредитные отчёты и FICO-скоринг")
@RestController
@RequestMapping("/api/v1/credits")
@RequiredArgsConstructor
@Validated
public class CreditController {

    private final CreditService creditService;
    private final ReportDispatchService reportDispatchService;

    @Operation(summary = "Единый диспетчер отчётов по reportId")
    @PostMapping("/report/dispatch")
    @PreAuthorize("hasAnyRole('BANK', 'ADMIN')")
    public ResponseEntity<ApiResponse<CreditReportResponse>> dispatchReport(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody SystemReportRequest request) {
        Map<String, Object> attrs = jwt.getClaimAsMap("attributes");
        String head = (String) attrs.get("head");
        String code = (String) attrs.get("code");
        return ResponseEntity.ok(ApiResponse.ok(reportDispatchService.dispatch(head, code, request)));
    }

    @Operation(summary = "Запросить кредитный отчёт")
    @PostMapping("/report")
    @PreAuthorize("hasAnyRole('BANK', 'ADMIN')")
    public ResponseEntity<ApiResponse<CreditReportResponse>> getCreditReport(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody CreditReportRequest request) {
        Map<String, Object> attrs = jwt.getClaimAsMap("attributes");
        String head = (String) attrs.get("head");
        String code = (String) attrs.get("code");
        return ResponseEntity.ok(ApiResponse.ok(creditService.getCreditReport(head, code, request)));
    }

    @Operation(summary = "Получить статус асинхронного отчёта")
    @GetMapping("/report/status")
    @PreAuthorize("hasAnyRole('BANK', 'ADMIN')")
    public ResponseEntity<ApiResponse<CreditReportResponse>> getCreditReportStatus(
            @AuthenticationPrincipal Jwt jwt,
            @NotBlank @RequestParam String claimId,
            @NotBlank @RequestParam String token,
            @RequestParam(defaultValue = "0") int format) {
        Map<String, Object> attrs = jwt.getClaimAsMap("attributes");
        String head = (String) attrs.get("head");
        String code = (String) attrs.get("code");
        return ResponseEntity.ok(ApiResponse.ok(
                creditService.getCreditReportStatus(head, code, claimId, token, format)));
    }

    @Operation(summary = "Получить Infoscore-отчёт по ПИНФЛ")
    @GetMapping("/report/pin/{pin}")
    @PreAuthorize("hasAnyRole('BANK', 'ADMIN')")
    public ResponseEntity<ApiResponse<CreditReportResponse>> getCreditReportByPin(
            @NotBlank @PathVariable String pin) {
        return ResponseEntity.ok(ApiResponse.ok(creditService.getInfoscoreReport(pin)));
    }

    @Operation(summary = "Получить Infoscore-отчёт по ИНН юрлица")
    @GetMapping("/report/tin/{tin}")
    @PreAuthorize("hasAnyRole('BANK', 'ADMIN')")
    public ResponseEntity<ApiResponse<CreditReportResponse>> getCreditReportByTin(
            @NotBlank @PathVariable String tin) {
        return ResponseEntity.ok(ApiResponse.ok(creditService.getInfoscoreLegalReport(tin)));
    }

    @Operation(summary = "Отчёт по изменению качества КИ за период")
    @PostMapping("/report/quality")
    @PreAuthorize("hasAnyRole('BANK', 'ADMIN')")
    public ResponseEntity<ApiResponse<CreditReportResponse>> getQualityReport(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody QualityReportRequest request) {
        Map<String, Object> attrs = jwt.getClaimAsMap("attributes");
        String head = (String) attrs.get("head");
        String code = (String) attrs.get("code");
        return ResponseEntity.ok(ApiResponse.ok(creditService.getQualityReport(head, code, request)));
    }

    @Operation(summary = "Доступность сервиса отчётов")
    @GetMapping("/service/available")
    @PreAuthorize("hasAnyRole('BANK', 'ADMIN')")
    public ResponseEntity<ApiResponse<Boolean>> isServiceAvailable() {
        return ResponseEntity.ok(ApiResponse.ok(creditService.isReportServiceAvailable()));
    }

    @Operation(summary = "Получить FICO-скор по идентификатору клиента")
    @GetMapping("/fico/{clientId}")
    @PreAuthorize("hasAnyRole('BANK', 'ADMIN')")
    public ResponseEntity<ApiResponse<List<FicoResult>>> getFicoScore(
            @NotBlank @PathVariable String clientId) {
        return ResponseEntity.ok(ApiResponse.ok(creditService.getFicoScore(clientId)));
    }
}
