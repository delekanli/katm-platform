package uz.katm.registry.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uz.katm.common.dto.ApiResponse;
import uz.katm.registry.domain.OrgDto;
import uz.katm.registry.domain.dto.PasswordResetRequest;
import uz.katm.registry.domain.dto.RegisterLeasingRequest;
import uz.katm.registry.domain.record.PasswordResetResult;
import uz.katm.registry.domain.record.RegistrationResult;
import uz.katm.registry.service.LeasingRegistryService;

import java.util.List;

/** Реестр лизинговых организаций (перенос LeasingController монолита). */
@Tag(name = "Registry · Leasing", description = "Реестр лизинговых организаций")
@RestController
@RequestMapping("/api/v1/registry/leasings")
@RequiredArgsConstructor
@Validated
@PreAuthorize("isAuthenticated()")
public class LeasingController {

    private final LeasingRegistryService service;

    @Operation(summary = "Список лизинговых организаций")
    @GetMapping
    public ResponseEntity<ApiResponse<List<OrgDto>>> list() {
        return ResponseEntity.ok(ApiResponse.ok(service.list()));
    }

    @Operation(summary = "Регистрация лизинговой организации")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<RegistrationResult>> register(@Valid @RequestBody RegisterLeasingRequest req) {
        return ResponseEntity.ok(ApiResponse.ok(service.register(req)));
    }

    @Operation(summary = "Сброс пароля лизинговой организации")
    @PostMapping("/password/reset")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PasswordResetResult>> resetPassword(@Valid @RequestBody PasswordResetRequest req) {
        return ResponseEntity.ok(ApiResponse.ok(service.resetPassword(req.login())));
    }
}
