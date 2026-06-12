package uz.katm.system.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uz.katm.common.dto.ApiResponse;
import uz.katm.system.domain.dto.GatewayRequestLogRequest;
import uz.katm.system.domain.dto.OnlineRequestErrorRequest;
import uz.katm.system.domain.dto.ReportRequestLogRequest;
import uz.katm.system.domain.record.EgovResendItem;
import uz.katm.system.domain.record.ProcedureResult;
import uz.katm.system.service.SystemService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/** Системные инструменты КАТМ (перенос ISystemToolService монолита). */
@Tag(name = "System", description = "Системные инструменты: логи, ошибки онлайн-запросов, E-GOV resend")
@RestController
@RequestMapping("/api/v1/system")
@RequiredArgsConstructor
@Validated
@PreAuthorize("hasRole('ADMIN')")
public class SystemController {

    private final SystemService service;

    @Operation(summary = "Записать лог запроса шлюза")
    @PostMapping("/logs/gateway-request")
    public ResponseEntity<ApiResponse<ProcedureResult>> addGatewayRequestLog(
            @Valid @RequestBody GatewayRequestLogRequest req) {
        return ResponseEntity.ok(ApiResponse.ok(service.addGatewayRequestsLog(req)));
    }

    @Operation(summary = "Записать лог запроса отчёта")
    @PostMapping("/logs/report-request")
    public ResponseEntity<ApiResponse<ProcedureResult>> addReportRequestLog(
            @Valid @RequestBody ReportRequestLogRequest req) {
        return ResponseEntity.ok(ApiResponse.ok(service.addReportRequestLog(req)));
    }

    @Operation(summary = "Зарегистрировать ошибку онлайн-запроса")
    @PostMapping("/online-errors")
    public ResponseEntity<ApiResponse<ProcedureResult>> addOnlineError(
            @Valid @RequestBody OnlineRequestErrorRequest req) {
        return ResponseEntity.ok(ApiResponse.ok(service.addOnlineRequestError(req)));
    }

    @Operation(summary = "Ошибки онлайн-запросов по фильтру")
    @GetMapping("/online-errors")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getOnlineErrors(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate requestDate,
            @RequestParam(required = false) String tin,
            @RequestParam(required = false) String pin,
            @RequestParam(required = false) String docSeries,
            @RequestParam(required = false) String docNumber) {
        return ResponseEntity.ok(ApiResponse.ok(
                service.getOnlineRequestErrors(requestDate, tin, pin, docSeries, docNumber)));
    }

    @Operation(summary = "Отметить веб-заявку отправленной в E-GOV (status=1)")
    @PostMapping("/web-claims/{claimId}/status-egov")
    public ResponseEntity<ApiResponse<Integer>> updateWebClaimStatusEgov(@NotBlank @PathVariable String claimId) {
        return ResponseEntity.ok(ApiResponse.ok(service.updateWebClaimStatusEgov(claimId)));
    }

    @Operation(summary = "Отчёты на повторную отправку в E-GOV")
    @GetMapping("/egov-resend")
    public ResponseEntity<ApiResponse<List<EgovResendItem>>> getReportsForEgovResend() {
        return ResponseEntity.ok(ApiResponse.ok(service.getReportsForEgovResend()));
    }
}
