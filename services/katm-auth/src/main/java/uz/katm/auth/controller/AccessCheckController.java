package uz.katm.auth.controller;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uz.katm.auth.domain.dto.AccessCheckRequest;
import uz.katm.auth.domain.dto.AccessCheckResponse;
import uz.katm.auth.repository.AuthRepository;
import uz.katm.auth.repository.AuthRepository.AccessCheckResult;

/**
 * Внутренний эндпоинт для проверки соответствия IP-адреса и кода организации.
 * Вызывается только из katm-gateway — не должен быть доступен извне.
 */
@Slf4j
@Hidden
@RestController
@RequestMapping("/internal")
@RequiredArgsConstructor
@Validated
public class AccessCheckController {

    private final AuthRepository authRepository;

    @PostMapping("/access-check")
    public ResponseEntity<AccessCheckResponse> checkAccess(@Valid @RequestBody AccessCheckRequest request) {
        log.debug("Проверка доступа: head={}, code={}, ip={}", request.getHead(), request.getCode(), request.getIp());

        AccessCheckResult result = authRepository.checkAccess(
                request.getHead(), request.getCode(), request.getIp());

        if (!result.allowed()) {
            log.warn("Доступ запрещён: head={}, code={}, ip={}, message={}",
                    request.getHead(), request.getCode(), request.getIp(), result.message());
        }

        return ResponseEntity.ok(new AccessCheckResponse(result.allowed(), result.message()));
    }
}
