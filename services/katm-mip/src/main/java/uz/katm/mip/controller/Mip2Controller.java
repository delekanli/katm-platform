package uz.katm.mip.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.katm.common.dto.ApiResponse;
import uz.katm.mip.domain.gcp.GcpResultData;
import uz.katm.mip.service.Mip2Service;

/**
 * MIP2 / ГЦП REST-сервисы (фаза 6).
 */
@Tag(name = "MIP2 / GCP", description = "ГЦП REST-сервисы через ПЦД")
@RestController
@RequestMapping("/api/v1/mip/gcp")
@RequiredArgsConstructor
@Validated
@PreAuthorize("isAuthenticated()")
public class Mip2Controller {

    private final Mip2Service mip2Service;

    @Operation(summary = "Данные о документах физлица через ГЦП")
    @GetMapping("/person-doc")
    public ResponseEntity<ApiResponse<GcpResultData>> getPersonDoc(
            @NotBlank @RequestParam String pin,
            @NotBlank @RequestParam String document,
            @RequestParam(defaultValue = "1") int langId) {
        return ResponseEntity.ok(ApiResponse.ok(mip2Service.getPersonDocInfo(pin, document, langId)));
    }
}
