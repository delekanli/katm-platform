package uz.katm.mip.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uz.katm.mip.service.TaxMipService;

@Tag(name = "MIP Tax", description = "Налоговая информация через МИП (SOAP)")
@RestController
@RequestMapping("/api/v1/mip/tax")
@RequiredArgsConstructor
@Validated
@PreAuthorize("isAuthenticated()")
public class TaxController {

    private final TaxMipService taxMipService;

    @Operation(summary = "Налоговая информация по ПИНФЛ (GetTaxInfobyPin)")
    @GetMapping("/pin/{pin}")
    public ResponseEntity<?> getTaxByPin(@NotBlank @PathVariable String pin) {
        return ResponseEntity.ok(taxMipService.getTaxInfoByPin(pin));
    }

    @Operation(summary = "Налоговая информация по ИНН (GetTaxInfobyTin)")
    @GetMapping("/tin/{tin}")
    public ResponseEntity<?> getTaxByTin(@NotBlank @PathVariable String tin) {
        return ResponseEntity.ok(taxMipService.getTaxInfoByTin(tin));
    }

    @Operation(summary = "ИНН по серии и номеру паспорта (GetTinbyPasNumNew)")
    @GetMapping("/tin-by-passport")
    public ResponseEntity<?> getTinByPassport(
            @NotBlank @RequestParam String series,
            @NotBlank @RequestParam String number) {
        return ResponseEntity.ok(taxMipService.getTinByPassport(series, number));
    }

    @Operation(summary = "Задолженности по налогам (GetDeptTaxInfo)")
    @GetMapping("/debt/{tin}")
    public ResponseEntity<?> getDeptTax(@NotBlank @PathVariable String tin) {
        return ResponseEntity.ok(taxMipService.getDeptTaxInfo(tin));
    }
}
