package uz.katm.report.controller;

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
import org.springframework.web.bind.annotation.RestController;
import uz.katm.common.dto.ApiResponse;
import uz.katm.report.domain.fcbk.ResponseBlock;
import uz.katm.report.service.FcbkService;

/** FCBK FICO-скоринг (внешний сервис скоринга по данным DWH_FICO). */
@Tag(name = "FCBK FICO", description = "FICO-скоринг через внешний сервис FCBK")
@RestController
@RequestMapping("/api/v1/credits/fcbk")
@RequiredArgsConstructor
@Validated
public class FcbkController {

    private final FcbkService fcbkService;

    @Operation(summary = "Запросить FICO-скор FCBK по идентификатору клиента")
    @GetMapping("/fico-score/{clientId}")
    @PreAuthorize("hasAnyRole('BANK', 'ADMIN')")
    public ResponseEntity<ApiResponse<ResponseBlock>> getFicoScore(@NotBlank @PathVariable String clientId) {
        return ResponseEntity.ok(ApiResponse.ok(fcbkService.getFicoScore(clientId)));
    }
}
