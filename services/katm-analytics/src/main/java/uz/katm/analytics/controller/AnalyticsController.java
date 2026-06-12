package uz.katm.analytics.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uz.katm.analytics.service.AnalyticsService;
import uz.katm.common.dto.ApiResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/** Аналитические отчёты КАТМ (перенос ReportController/AnalyticService монолита, фаза 1). */
@Tag(name = "Analytics", description = "Аналитические отчёты (портфель, COA, report04/06/09, статистика)")
@RestController
@RequestMapping("/api/v1/analytics")
@RequiredArgsConstructor
@Validated
@PreAuthorize("hasAnyRole('ADMIN', 'ANALYST')")
public class AnalyticsController {

    private final AnalyticsService service;

    @Operation(summary = "Детальный портфель по банку")
    @GetMapping("/portfolio/detail")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> detailPortfolio(
            @NotBlank @RequestParam String bank,
            @RequestParam(required = false) Integer pos,
            @RequestParam(defaultValue = "false") boolean allCoa) {
        return ResponseEntity.ok(ApiResponse.ok(service.detailPortfolio(bank, pos, allCoa)));
    }

    @Operation(summary = "Сводка портфеля по COA")
    @GetMapping("/portfolio/coa-summary")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> coaSummary(
            @RequestParam(required = false) Integer pos,
            @RequestParam(defaultValue = "false") boolean allCoa) {
        return ResponseEntity.ok(ApiResponse.ok(service.coaSummaryPortfolio(pos, allCoa)));
    }

    @Operation(summary = "Расширенная сводка портфеля по COA")
    @GetMapping("/portfolio/coa-ext-summary")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> coaExtSummary(
            @RequestParam(required = false) Integer pos,
            @RequestParam(defaultValue = "false") boolean allCoa) {
        return ResponseEntity.ok(ApiResponse.ok(service.coaExtSummaryPortfolio(pos, allCoa)));
    }

    @Operation(summary = "Отчёт 04 (фиксы)")
    @GetMapping("/report04")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> report04() {
        return ResponseEntity.ok(ApiResponse.ok(service.report04()));
    }

    @Operation(summary = "Отчёт 06 (классы активов) за период")
    @GetMapping("/report06")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> report06(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return ResponseEntity.ok(ApiResponse.ok(service.report06(start, end)));
    }

    @Operation(summary = "Отчёт 09 (покрытие КО) за период")
    @GetMapping("/report09")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> report09(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return ResponseEntity.ok(ApiResponse.ok(service.report09(start, end)));
    }

    @Operation(summary = "Отчёт 09 расширенный за период")
    @GetMapping("/report09/ext")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> extReport09(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return ResponseEntity.ok(ApiResponse.ok(service.extReport09(start, end)));
    }

    @Operation(summary = "Дубликаты договоров по банку")
    @GetMapping("/duplicates")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> duplicates(
            @NotBlank @RequestParam String bank) {
        return ResponseEntity.ok(ApiResponse.ok(service.duplicates(bank)));
    }

    @Operation(summary = "Статистика использования отчётов за период")
    @GetMapping("/uses-credit-reports")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> usesCreditReports(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return ResponseEntity.ok(ApiResponse.ok(service.usesCreditReports(start, end)));
    }

    @Operation(summary = "Статистика веб-обращений mygov за период")
    @GetMapping("/web-demands-stat")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> webDemandsStat(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return ResponseEntity.ok(ApiResponse.ok(service.webDemandsStat(start, end)));
    }

    @Operation(summary = "Детали скоринга по заявке")
    @GetMapping("/score-details")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> scoreDetails(
            @NotBlank @RequestParam String demandId) {
        return ResponseEntity.ok(ApiResponse.ok(service.scoreReportDetails(demandId)));
    }

    @Operation(summary = "Сводная статистика по всем отчётам за период (head/code='-1' для всех)")
    @GetMapping("/all-reports-stat")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> allReportsStat(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
            @RequestParam(defaultValue = "-1") String head,
            @RequestParam(defaultValue = "-1") String code) {
        return ResponseEntity.ok(ApiResponse.ok(service.allReportsStat(start, end, head, code)));
    }

    @Operation(summary = "Изменения договора (discover_ci015)")
    @GetMapping("/discover-ci015")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> discoverCi015(
            @NotBlank @RequestParam String head,
            @NotBlank @RequestParam String code,
            @NotBlank @RequestParam String contractId) {
        return ResponseEntity.ok(ApiResponse.ok(service.discoverCi015(head, code, contractId)));
    }

    @Operation(summary = "Статистика ЦБ РУз по производителям за период")
    @GetMapping("/cb-uz-stat")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> cbUzStat(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return ResponseEntity.ok(ApiResponse.ok(service.cbUzStat(start, end)));
    }

    @Operation(summary = "Минимальный набор за период")
    @GetMapping("/min-nabor")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> minNabor(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return ResponseEntity.ok(ApiResponse.ok(service.minNabor(start, end)));
    }

    @Operation(summary = "Заявки без отчётов за период")
    @GetMapping("/claims-wo-reports")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> claimsWoReports(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return ResponseEntity.ok(ApiResponse.ok(service.claimsWoReports(start, end)));
    }
}
