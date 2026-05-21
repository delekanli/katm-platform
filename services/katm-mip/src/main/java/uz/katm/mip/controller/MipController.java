package uz.katm.mip.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.katm.common.dto.ApiResponse;
import uz.katm.mip.service.MipService;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/mip")
@RequiredArgsConstructor
@Tag(name = "MIP Integration", description = "MIP (State Information System) integration API")
public class MipController {

    private final MipService mipService;

    @PostMapping("/request")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> sendRequest(
            @RequestParam String requestType,
            @RequestBody Map<String, Object> data) {
        return ResponseEntity.ok(ApiResponse.ok(mipService.sendSyncRequest(requestType, data)));
    }
}
