package uz.katm.mip.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uz.katm.mip.service.CitizenMipService;

import java.util.List;

@Tag(name = "MIP Citizens", description = "Данные граждан через МИП (SOAP)")
@RestController
@RequestMapping("/api/v1/mip/citizens")
@RequiredArgsConstructor
@Validated
@PreAuthorize("isAuthenticated()")
public class CitizenController {

    private final CitizenMipService citizenMipService;

    @Operation(summary = "Данные гражданина по ПИНФЛ (GetCitizenInfoServiceNew)")
    @GetMapping("/{pin}")
    public ResponseEntity<?> getCitizenInfo(@NotBlank @PathVariable String pin) {
        return ResponseEntity.ok(citizenMipService.getCitizenInfo(pin));
    }

    @Operation(summary = "Документы гражданина по ПИНФЛ (PersonDocInfoService)")
    @GetMapping("/{pin}/documents")
    public ResponseEntity<?> getDocuments(@NotBlank @PathVariable String pin) {
        return ResponseEntity.ok(citizenMipService.getDocuments(pin));
    }

    @Operation(summary = "Пакетный запрос по списку ПИНФЛ")
    @PostMapping("/batch")
    public ResponseEntity<?> getCitizensBatch(@NotEmpty @RequestBody List<String> pins) {
        return ResponseEntity.ok(citizenMipService.getCitizensBatch(pins));
    }

    @Operation(summary = "Адрес ПИНФЛ через старый сервис (GetCitizensInfoService, legacy)")
    @GetMapping("/{pin}/address")
    public ResponseEntity<?> getPeAddress(
            @NotBlank @PathVariable String pin,
            @RequestParam(required = false, defaultValue = "") String guid) {
        return ResponseEntity.ok(citizenMipService.getPeAddress(pin, guid));
    }
}
