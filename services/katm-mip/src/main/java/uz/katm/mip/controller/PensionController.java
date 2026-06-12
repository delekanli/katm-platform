package uz.katm.mip.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uz.katm.mip.service.PensionMipService;

/**
 * Пенсионные сервисы ИНПС через МИП (фаза 4).
 */
@Tag(name = "MIP Pension", description = "Пенсионные сервисы ИНПС через МИП (SOAP)")
@RestController
@RequestMapping("/api/v1/mip/pension")
@RequiredArgsConstructor
@Validated
@PreAuthorize("isAuthenticated()")
public class PensionController {

    private final PensionMipService pensionMipService;

    @Operation(summary = "Баланс ИНПС (InpsBalance)")
    @GetMapping("/inps-balance")
    public ResponseEntity<?> getInpsBalance(
            @NotBlank @RequestParam String tin,
            @NotBlank @RequestParam String pin,
            @RequestParam(required = false, defaultValue = "") String docSeries,
            @RequestParam(required = false, defaultValue = "") String docNumber) {
        return ResponseEntity.ok(pensionMipService.getInpsBalance(tin, pin, docSeries, docNumber));
    }

    @Operation(summary = "Назначение пенсии (GetPensionAssgnService)")
    @GetMapping("/assign/{pin}")
    public ResponseEntity<?> getPensionAssign(
            @NotBlank @PathVariable String pin,
            @RequestParam(defaultValue = "1") int type) {
        return ResponseEntity.ok(pensionMipService.getPensionAssignService(pin, type));
    }

    @Operation(summary = "Размер пенсии за период (GetSizePensService)")
    @GetMapping("/size/{pin}")
    public ResponseEntity<?> getPensionSize(
            @NotBlank @PathVariable String pin,
            @RequestParam(required = false, defaultValue = "") String beginPeriod,
            @RequestParam(required = false, defaultValue = "") String endPeriod,
            @RequestParam(defaultValue = "1") int type) {
        return ResponseEntity.ok(pensionMipService.getPensionSizeService(pin, beginPeriod, endPeriod, type));
    }

    @Operation(summary = "Запрос пенсии (GetRequestPensService)")
    @GetMapping("/request/{pin}")
    public ResponseEntity<?> getPension(
            @NotBlank @PathVariable String pin,
            @RequestParam(defaultValue = "1") int type) {
        return ResponseEntity.ok(pensionMipService.getPensionService(pin, type));
    }
}
