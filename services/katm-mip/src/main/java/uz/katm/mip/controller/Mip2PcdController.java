package uz.katm.mip.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uz.katm.common.dto.ApiResponse;
import uz.katm.mip.domain.dto.ConvictionRequest;
import uz.katm.mip.domain.dto.ConvictionResult;
import uz.katm.mip.service.Mip2PcdService;

/**
 * MIP2-отчёты через ПЦД (ЗАГС, МинТруд, алименты, МинЗдрав, авто, судимость).
 * Перенос bkiService MIP2-методов. Субъект (ПИНФЛ/номер) передаётся напрямую — конвенция MIP.
 */
@RestController
@RequestMapping("/api/v1/mip")
@RequiredArgsConstructor
@Validated
@PreAuthorize("isAuthenticated()")
@Tag(name = "MIP2 PCD", description = "ЗАГС, МинТруд, алименты, МинЗдрав, авто, судимость через ПЦД")
public class Mip2PcdController {

    private final Mip2PcdService mip2PcdService;

    @Operation(summary = "ЗАГС (1=рождение, 2=брак, 3=развод, 4=смерть)")
    @GetMapping("/justice")
    public ResponseEntity<ApiResponse<String>> getJustice(
            @NotBlank @RequestParam String pin,
            @RequestParam(defaultValue = "1") int subReportId) {
        return ResponseEntity.ok(ApiResponse.ok(mip2PcdService.getJustice(pin, subReportId)));
    }

    @Operation(summary = "МинТруд (сведения о трудовой деятельности)")
    @GetMapping("/labour")
    public ResponseEntity<ApiResponse<String>> getLabour(
            @NotBlank @RequestParam String pin,
            @RequestParam(required = false) String requestId) {
        return ResponseEntity.ok(ApiResponse.ok(mip2PcdService.getLabour(pin, requestId)));
    }

    @Operation(summary = "Алименты (МИБ)")
    @GetMapping("/aliment")
    public ResponseEntity<ApiResponse<String>> getAliment(
            @NotBlank @RequestParam String pin,
            @RequestParam(defaultValue = "1") int type) {
        return ResponseEntity.ok(ApiResponse.ok(mip2PcdService.getAliment(pin, type)));
    }

    @Operation(summary = "МинЗдрав (1=наркологический, 2=психоневрологический учёт)")
    @GetMapping("/minzdrav")
    public ResponseEntity<ApiResponse<String>> getMinzdrav(
            @NotBlank @RequestParam String pin,
            @RequestParam(defaultValue = "1") int subReportId) {
        return ResponseEntity.ok(ApiResponse.ok(mip2PcdService.getMinzdrav(pin, subReportId)));
    }

    @Operation(summary = "Сведения об автотранспорте по гос. номеру")
    @GetMapping("/vehicle")
    public ResponseEntity<ApiResponse<String>> getVehicle(
            @NotBlank @RequestParam String plateNumber) {
        return ResponseEntity.ok(ApiResponse.ok(mip2PcdService.getVehicle(plateNumber)));
    }

    @Operation(summary = "Судимость — инициация запроса (асинхронно)")
    @PostMapping("/conviction")
    public ResponseEntity<ApiResponse<ConvictionResult>> submitConviction(
            @Valid @RequestBody ConvictionRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(mip2PcdService.submitConviction(request)));
    }

    @Operation(summary = "Судимость — опрос готовности по токену")
    @GetMapping("/conviction/status")
    public ResponseEntity<ApiResponse<ConvictionResult>> checkConviction(
            @NotBlank @RequestParam String token) {
        return ResponseEntity.ok(ApiResponse.ok(mip2PcdService.checkConviction(token)));
    }
}
