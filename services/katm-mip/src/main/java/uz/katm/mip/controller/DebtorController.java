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
import uz.katm.mip.service.DebtorMipService;

/**
 * Сервисы МИБ: задолженности (DebtorWS) и водоконтроль (WaterControl) — фаза 5.
 */
@Tag(name = "MIP Debtor/Water", description = "Задолженности и водоконтроль через МИБ (SOAP)")
@RestController
@RequestMapping("/api/v1/mip/debtor")
@RequiredArgsConstructor
@Validated
@PreAuthorize("isAuthenticated()")
public class DebtorController {

    private final DebtorMipService debtorMipService;

    @Operation(summary = "Задолженности физлица по паспорту (DebtorWS.getDebtorPhysical)")
    @GetMapping("/individual")
    public ResponseEntity<?> getDebtorIndividual(
            @NotBlank @RequestParam String series,
            @NotBlank @RequestParam String number) {
        return ResponseEntity.ok(debtorMipService.getDebtorIndividualResult(series, number));
    }

    @Operation(summary = "Задолженности юрлица по ИНН (DebtorWS.getDebtorJuridical)")
    @GetMapping("/juridical/{tin}")
    public ResponseEntity<?> getDebtorJuridical(@NotBlank @PathVariable String tin) {
        return ResponseEntity.ok(debtorMipService.getDebtorJuridicalResult(tin));
    }

    @Operation(summary = "Сведения водоконтроля по ПИНФЛ или ИНН (WaterControl)")
    @GetMapping("/water")
    public ResponseEntity<?> getWaterControl(
            @RequestParam(required = false, defaultValue = "") String pin,
            @RequestParam(required = false, defaultValue = "") String tin) {
        return ResponseEntity.ok(debtorMipService.getWaterControlInfo(pin, tin));
    }
}
