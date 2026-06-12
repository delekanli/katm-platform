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
import uz.katm.mip.service.LegalMipService;

/**
 * Сведения о юридических лицах через МИП (фаза 3).
 */
@Tag(name = "MIP Legal", description = "Информация о юрлицах через МИП (SOAP)")
@RestController
@RequestMapping("/api/v1/mip/legal")
@RequiredArgsConstructor
@Validated
@PreAuthorize("isAuthenticated()")
public class LegalController {

    private final LegalMipService legalMipService;

    @Operation(summary = "Информация о юрлице по ИНН (GetLegalEntityInfo)")
    @GetMapping("/entity/{tin}")
    public ResponseEntity<?> getLegalEntity(
            @NotBlank @PathVariable String tin,
            @RequestParam(required = false, defaultValue = "") String hashKey) {
        return ResponseEntity.ok(legalMipService.getLegalInformation(tin, hashKey));
    }

    @Operation(summary = "Сведения о юрлице из реестра БР (GetLegalInfoBR)")
    @GetMapping("/br/{tin}")
    public ResponseEntity<?> getLegalInfoBR(@NotBlank @PathVariable String tin) {
        return ResponseEntity.ok(legalMipService.getLegalInfoBR(tin));
    }
}
