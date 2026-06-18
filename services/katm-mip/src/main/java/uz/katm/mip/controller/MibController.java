package uz.katm.mip.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uz.katm.common.dto.ApiResponse;
import uz.katm.mip.service.MibMipService;

/**
 * Отчёты МИБ (Бюро принудительного исполнения) через ПЦД. Перенос bkiService MIB-методов.
 * Субъект (ПИНФЛ/ИНН/гос. номер) передаётся напрямую — конвенция MIP.
 */
@RestController
@RequestMapping("/api/v1/mip/mib")
@RequiredArgsConstructor
@Validated
@PreAuthorize("isAuthenticated()")
@Tag(name = "MIP MIB", description = "Запреты и исполнительные производства МИБ через ПЦД")
public class MibController {

    private final MibMipService mibMipService;

    @Operation(summary = "Запрет выезда за границу")
    @GetMapping("/debt-ban")
    public ResponseEntity<ApiResponse<String>> getDebtBan(@NotBlank @RequestParam String pin) {
        return ResponseEntity.ok(ApiResponse.ok(mibMipService.getDebtBan(pin)));
    }

    @Operation(summary = "Запрет на использование автотранспорта")
    @GetMapping("/avto-ban")
    public ResponseEntity<ApiResponse<String>> getAvtoBan(@NotBlank @RequestParam String autoNumber) {
        return ResponseEntity.ok(ApiResponse.ok(mibMipService.getAvtoBan(autoNumber)));
    }

    @Operation(summary = "Запрет на недвижимость")
    @GetMapping("/realty-ban")
    public ResponseEntity<ApiResponse<String>> getRealtyBan(@NotBlank @RequestParam String cadNumber) {
        return ResponseEntity.ok(ApiResponse.ok(mibMipService.getRealtyBan(cadNumber)));
    }

    @Operation(summary = "Исполнительный бланк по гос. номеру ТС")
    @GetMapping("/doc-avto")
    public ResponseEntity<ApiResponse<String>> getDocAvto(@NotBlank @RequestParam String autoNumber) {
        return ResponseEntity.ok(ApiResponse.ok(mibMipService.getDocAvto(autoNumber)));
    }

    @Operation(summary = "Исполнительный лист в качестве взыскателя")
    @GetMapping("/doc-creditor")
    public ResponseEntity<ApiResponse<String>> getDocCreditor(@NotBlank @RequestParam String pin) {
        return ResponseEntity.ok(ApiResponse.ok(mibMipService.getDocCreditor(pin)));
    }

    @Operation(summary = "Исполнительный лист у должника (ПИНФЛ или ИНН)")
    @GetMapping("/doc-debtor")
    public ResponseEntity<ApiResponse<String>> getDocDebtor(
            @RequestParam(required = false) String pin,
            @RequestParam(required = false) String tin) {
        return ResponseEntity.ok(ApiResponse.ok(mibMipService.getDocDebtor(pin, tin)));
    }

    @Operation(summary = "Ход исполнительного производства")
    @GetMapping("/doc-action")
    public ResponseEntity<ApiResponse<String>> getDocAction(
            @NotBlank @RequestParam String pin,
            @NotBlank @RequestParam String workNumber,
            @RequestParam(defaultValue = "1") int type) {
        return ResponseEntity.ok(ApiResponse.ok(mibMipService.getDocAction(pin, workNumber, type)));
    }

    @Operation(summary = "Физлицо в списке неплатёжеспособных")
    @GetMapping("/list-pe-insolvent")
    public ResponseEntity<ApiResponse<String>> getListPeInsolvent(@NotBlank @RequestParam String pin) {
        return ResponseEntity.ok(ApiResponse.ok(mibMipService.getListPeInsolvent(pin)));
    }

    @Operation(summary = "Юрлицо в списке неплатёжеспособных")
    @GetMapping("/list-le-insolvent")
    public ResponseEntity<ApiResponse<String>> getListLeInsolvent(@NotBlank @RequestParam String tin) {
        return ResponseEntity.ok(ApiResponse.ok(mibMipService.getListLeInsolvent(tin)));
    }
}
