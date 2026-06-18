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
import uz.katm.mip.service.CadastreMipService;
import uz.katm.mip.service.UtilityMipService;

/**
 * Коммунальные задолженности и кадастр. Перенос bkiService get{Electro,Gas,Ecology,Teploener}Data
 * (через ПЦД) и getCadastreInfo (свой BasicAuth). Субъект передаётся напрямую — конвенция MIP.
 */
@RestController
@RequestMapping("/api/v1/mip")
@RequiredArgsConstructor
@Validated
@PreAuthorize("isAuthenticated()")
@Tag(name = "MIP Utility", description = "Коммунальные задолженности и кадастр")
public class UtilityController {

    private final UtilityMipService utilityMipService;
    private final CadastreMipService cadastreMipService;

    @Operation(summary = "Задолженность по электроснабжению")
    @GetMapping("/utility/electro")
    public ResponseEntity<ApiResponse<String>> getElectro(@NotBlank @RequestParam String pin) {
        return ResponseEntity.ok(ApiResponse.ok(utilityMipService.getElectro(pin)));
    }

    @Operation(summary = "Задолженность по газоснабжению")
    @GetMapping("/utility/gas")
    public ResponseEntity<ApiResponse<String>> getGas(@NotBlank @RequestParam String pin) {
        return ResponseEntity.ok(ApiResponse.ok(utilityMipService.getGas(pin)));
    }

    @Operation(summary = "Задолженность по теплоэнергии")
    @GetMapping("/utility/toshissiqquvvat")
    public ResponseEntity<ApiResponse<String>> getToshissiqquvvat(@NotBlank @RequestParam String pin) {
        return ResponseEntity.ok(ApiResponse.ok(utilityMipService.getToshissiqquvvat(pin)));
    }

    @Operation(summary = "Задолженность по бытовым отходам (экология)")
    @GetMapping("/utility/ecology")
    public ResponseEntity<ApiResponse<String>> getEcology(
            @NotBlank @RequestParam String pin,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        return ResponseEntity.ok(ApiResponse.ok(utilityMipService.getEcology(pin, startDate, endDate)));
    }

    @Operation(summary = "Кадастр недвижимости (по номеру/ПИНФЛ/ИНН)")
    @GetMapping("/cadastre")
    public ResponseEntity<ApiResponse<String>> getCadastre(
            @RequestParam(required = false) String cadastreNumber,
            @RequestParam(required = false) String pin,
            @RequestParam(required = false) String tin) {
        return ResponseEntity.ok(ApiResponse.ok(cadastreMipService.getCadastreInfo(cadastreNumber, pin, tin)));
    }
}
