package uz.katm.mip.controller;

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
import uz.katm.mip.domain.dto.EInvoiceRequest;
import uz.katm.mip.service.MiscMipService;
import uz.katm.mip.service.UzAsboMipService;

/**
 * Прочие отчёты: Мой дом, выручка по ККМ (E-Invoice), кэшбек (через ПЦД) и UZ-ASBO (свой BasicAuth).
 * Перенос bkiService getMyHomeData / getEInvoiceKkm*Data / getCashBackData / getUzAsboData.
 */
@RestController
@RequestMapping("/api/v1/mip")
@RequiredArgsConstructor
@Validated
@PreAuthorize("isAuthenticated()")
@Tag(name = "MIP Misc", description = "Мой дом, E-Invoice ККМ, кэшбек, UZ-ASBO")
public class MiscController {

    private final MiscMipService miscMipService;
    private final UzAsboMipService uzAsboMipService;

    @Operation(summary = "Мой дом (Минстрой и ЖКХ) по кадастровому номеру")
    @GetMapping("/my-home")
    public ResponseEntity<ApiResponse<String>> getMyHome(
            @NotBlank @RequestParam String cadastre,
            @RequestParam(required = false) String pin) {
        return ResponseEntity.ok(ApiResponse.ok(miscMipService.getMyHome(cadastre, pin)));
    }

    @Operation(summary = "Выручка по ККМ физлица (E-Invoice)")
    @PostMapping("/einvoice/pe")
    public ResponseEntity<ApiResponse<String>> getEInvoicePe(@Valid @RequestBody EInvoiceRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(miscMipService.getEInvoiceKkmPe(request)));
    }

    @Operation(summary = "Выручка по ККМ юрлица (E-Invoice)")
    @PostMapping("/einvoice/le")
    public ResponseEntity<ApiResponse<String>> getEInvoiceLe(@Valid @RequestBody EInvoiceRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(miscMipService.getEInvoiceKkmLe(request)));
    }

    @Operation(summary = "Кэшбек ГНК")
    @GetMapping("/cashback")
    public ResponseEntity<ApiResponse<String>> getCashBack(
            @NotBlank @RequestParam String pin,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) String lang) {
        return ResponseEntity.ok(ApiResponse.ok(miscMipService.getCashBack(pin, year, lang)));
    }

    @Operation(summary = "UZ-ASBO (сведения о бюджетниках)")
    @GetMapping("/uz-asbo")
    public ResponseEntity<ApiResponse<String>> getUzAsbo(
            @NotBlank @RequestParam String pin,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(defaultValue = "1") int subReportId) {
        return ResponseEntity.ok(ApiResponse.ok(uzAsboMipService.getUzAsbo(pin, startDate, endDate, subReportId)));
    }
}
