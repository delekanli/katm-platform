package uz.katm.ucin.controller;

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
import uz.katm.ucin.domain.dto.CreditReportRequest;
import uz.katm.ucin.domain.dto.CreditReportStatusRequest;
import uz.katm.ucin.domain.record.UcinReportResult;
import uz.katm.ucin.service.UcinReportService;

/** UCIN credit-report API (фаза 2). */
@Tag(name = "UCIN · Credit Report", description = "UCIN credit report API")
@RestController
@RequestMapping("/api/v1/ucin/reports")
@RequiredArgsConstructor
@Validated
@PreAuthorize("isAuthenticated()")
public class UcinReportController {

    private final UcinReportService service;

    @Operation(summary = "Получить кредитный отчёт (GET_CREDIT_REPORT)")
    @PostMapping
    public ResponseEntity<ApiResponse<UcinReportResult>> getCreditReport(@Valid @RequestBody CreditReportRequest req) {
        return ResponseEntity.ok(ApiResponse.ok(service.getCreditReport(req)));
    }

    @Operation(summary = "Статус/получение готового отчёта (GET_CREDIT_REPORT_STATUS)")
    @PostMapping("/status")
    public ResponseEntity<ApiResponse<UcinReportResult>> getCreditReportStatus(
            @Valid @RequestBody CreditReportStatusRequest req) {
        return ResponseEntity.ok(ApiResponse.ok(service.getCreditReportStatus(req.claimId(), req.token())));
    }

    @Operation(summary = "Содержимое отчёта по хэшу заявки")
    @GetMapping("/by-hash/{token}")
    public ResponseEntity<ApiResponse<String>> getByHash(@NotBlank @PathVariable String token) {
        return ResponseEntity.ok(ApiResponse.ok(service.getCreditReportByHash(token)));
    }

    @Operation(summary = "Готовый отчёт по клиенту (NEW_REPORTS_UCIN.GET_REPORT)")
    @GetMapping("/client/{clientId}")
    public ResponseEntity<ApiResponse<UcinReportResult>> getReport(@NotBlank @PathVariable String clientId) {
        return ResponseEntity.ok(ApiResponse.ok(service.getReport(clientId)));
    }

    @Operation(summary = "Признак активной фоновой выгрузки")
    @GetMapping("/active-demand-work")
    public ResponseEntity<ApiResponse<Integer>> checkActiveDemandWork() {
        return ResponseEntity.ok(ApiResponse.ok(service.checkActiveDemandWork()));
    }
}
