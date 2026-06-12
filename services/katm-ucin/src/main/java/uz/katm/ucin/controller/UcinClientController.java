package uz.katm.ucin.controller;

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
import uz.katm.ucin.domain.dto.InitiateClientRequest;
import uz.katm.ucin.domain.dto.InitiateLegalClientRequest;
import uz.katm.ucin.domain.dto.KatmSirRequest;
import uz.katm.ucin.domain.record.ClientDataResult;
import uz.katm.ucin.domain.record.ClientResult;
import uz.katm.ucin.domain.record.KatmSirResult;
import uz.katm.ucin.service.UcinClientService;

/** UCIN client API (фаза 1): инициализация клиентов, данные/тариф/КАТМ СИР. */
@Tag(name = "UCIN · Client", description = "UCIN client API: инициализация клиентов и данные")
@RestController
@RequestMapping("/api/v1/ucin/clients")
@RequiredArgsConstructor
@Validated
@PreAuthorize("isAuthenticated()")
public class UcinClientController {

    private final UcinClientService service;

    @Operation(summary = "Инициализация клиента-физлица (INITIATE_CLIENT)")
    @PostMapping("/init")
    public ResponseEntity<ApiResponse<ClientResult>> initClient(@Valid @RequestBody InitiateClientRequest req) {
        return ResponseEntity.ok(ApiResponse.ok(service.initClient(req)));
    }

    @Operation(summary = "Инициализация клиента-юрлица (INITIATE_LEGAL_CLIENT)")
    @PostMapping("/init/legal")
    public ResponseEntity<ApiResponse<ClientResult>> initLegalClient(@Valid @RequestBody InitiateLegalClientRequest req) {
        return ResponseEntity.ok(ApiResponse.ok(service.initLegalClient(req)));
    }

    @Operation(summary = "Данные клиента по clientId (GET_DATA_BY_CLIENT_ID)")
    @GetMapping("/{clientId}")
    public ResponseEntity<ApiResponse<ClientDataResult>> getClient(
            @NotBlank @PathVariable String clientId,
            @RequestParam(required = false) String reportId) {
        return ResponseEntity.ok(ApiResponse.ok(service.getClientById(clientId, reportId)));
    }

    @Operation(summary = "Тариф по клиенту (get_web_claim_tariff)")
    @GetMapping("/{clientId}/tariff")
    public ResponseEntity<ApiResponse<Double>> getTariff(
            @NotBlank @PathVariable String clientId,
            @RequestParam(required = false) Integer reportId) {
        return ResponseEntity.ok(ApiResponse.ok(service.getClientTariff(clientId, reportId)));
    }

    @Operation(summary = "Поиск клиента в КАТМ СИР по данным лица (GET_CLIENT_ID)")
    @PostMapping("/katm-sir")
    public ResponseEntity<ApiResponse<KatmSirResult>> getKatmSir(@Valid @RequestBody KatmSirRequest req) {
        return ResponseEntity.ok(ApiResponse.ok(service.getKatmSir(req)));
    }
}
