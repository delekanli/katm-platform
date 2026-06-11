package uz.katm.registry.controller;

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
import uz.katm.registry.domain.OrgDto;
import uz.katm.registry.domain.dto.ChangeStatusRequest;
import uz.katm.registry.domain.dto.PasswordResetRequest;
import uz.katm.registry.domain.dto.RegisterRetailerRequest;
import uz.katm.registry.domain.record.OperationResult;
import uz.katm.registry.domain.record.PasswordResetResult;
import uz.katm.registry.domain.record.RegistrationResult;
import uz.katm.registry.service.RetailerRegistryService;

import java.util.List;

/** Реестр ритейлеров (перенос RetailerController монолита). */
@Tag(name = "Registry · Retailer", description = "Реестр ритейлеров")
@RestController
@RequestMapping("/api/v1/registry/retailers")
@RequiredArgsConstructor
@Validated
@PreAuthorize("isAuthenticated()")
public class RetailerController {

    private final RetailerRegistryService service;

    @Operation(summary = "Список ритейлеров")
    @GetMapping
    public ResponseEntity<ApiResponse<List<OrgDto>>> list() {
        return ResponseEntity.ok(ApiResponse.ok(service.list()));
    }

    @Operation(summary = "Регистрация ритейлера")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<RegistrationResult>> register(@Valid @RequestBody RegisterRetailerRequest req) {
        return ResponseEntity.ok(ApiResponse.ok(service.register(req)));
    }

    @Operation(summary = "Смена статуса ритейлера")
    @PostMapping("/{code}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<OperationResult>> changeStatus(
            @NotBlank @PathVariable String code,
            @Valid @RequestBody ChangeStatusRequest req) {
        return ResponseEntity.ok(ApiResponse.ok(service.changeStatus(code, req.status(), req.mode())));
    }

    @Operation(summary = "Сброс пароля ритейлера")
    @PostMapping("/password/reset")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PasswordResetResult>> resetPassword(@Valid @RequestBody PasswordResetRequest req) {
        return ResponseEntity.ok(ApiResponse.ok(service.resetPassword(req.login())));
    }
}
