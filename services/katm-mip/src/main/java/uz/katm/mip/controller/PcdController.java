package uz.katm.mip.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.katm.common.dto.ApiResponse;
import uz.katm.common.http.HttpResponseInfo;
import uz.katm.mip.client.PcdClient;
import uz.katm.mip.domain.dto.PcdRequest;

/**
 * Сквозной (passthrough) доступ к REST-сервисам ПЦД/МИБ.
 * Фаза 7 миграции MIP: перенос {@code executePcdServiceCall} + {@code mibState}.
 */
@RestController
@RequestMapping("/api/v1/mip/pcd")
@RequiredArgsConstructor
@Tag(name = "MIP PCD/MIB", description = "Сквозной вызов REST-сервисов ПЦД/МИБ")
public class PcdController {

    private final PcdClient pcdClient;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @Operation(summary = "Вызвать сервис ПЦД и интерпретировать ответ МИБ")
    public ResponseEntity<ApiResponse<String>> call(@Valid @RequestBody PcdRequest request) {
        HttpResponseInfo response = pcdClient.execute(request.method(), request.url(), request.payload());
        String body = pcdClient.mibState(response);
        return ResponseEntity.ok(ApiResponse.ok(body));
    }
}
