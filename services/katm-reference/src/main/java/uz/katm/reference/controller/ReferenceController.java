package uz.katm.reference.controller;

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
import uz.katm.reference.domain.RefDto;
import uz.katm.reference.service.ReferenceService;

import java.util.List;

/**
 * Справочники КАТМ (перенос ReferenceController монолита).
 */
@Tag(name = "Reference", description = "Справочники (регионы, статусы/режимы ритейлеров, форматы)")
@RestController
@RequestMapping("/api/v1/reference")
@RequiredArgsConstructor
@Validated
@PreAuthorize("isAuthenticated()")
public class ReferenceController {

    private final ReferenceService referenceService;

    @Operation(summary = "Список регионов")
    @GetMapping("/regions")
    public ResponseEntity<ApiResponse<List<RefDto>>> regions() {
        return ResponseEntity.ok(ApiResponse.ok(referenceService.getRegions()));
    }

    @Operation(summary = "Районы региона по коду")
    @GetMapping("/regions/local")
    public ResponseEntity<ApiResponse<List<RefDto>>> localRegions(@NotBlank @RequestParam String code) {
        return ResponseEntity.ok(ApiResponse.ok(referenceService.getLocalRegions(code)));
    }

    @Operation(summary = "Статусы ритейлеров")
    @GetMapping("/retailer-statuses")
    public ResponseEntity<ApiResponse<List<RefDto>>> retailerStatuses() {
        return ResponseEntity.ok(ApiResponse.ok(referenceService.getRetailerStatuses()));
    }

    @Operation(summary = "Режимы ритейлеров")
    @GetMapping("/retailer-modes")
    public ResponseEntity<ApiResponse<List<RefDto>>> retailerModes() {
        return ResponseEntity.ok(ApiResponse.ok(referenceService.getRetailerModes()));
    }

    @Operation(summary = "Форматы выгрузки")
    @GetMapping("/formats")
    public ResponseEntity<ApiResponse<List<RefDto>>> formats() {
        return ResponseEntity.ok(ApiResponse.ok(referenceService.getFormats()));
    }
}
