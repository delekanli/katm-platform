package uz.katm.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uz.katm.auth.domain.dto.LoginRequest;
import uz.katm.auth.domain.dto.TokenResponse;
import uz.katm.auth.service.AuthService;

@Tag(name = "Authentication", description = "Аутентификация и управление токенами")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Вход по логину и паролю", description = "Возвращает access и refresh токены Keycloak")
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @Operation(summary = "Обновить токен", description = "Обменивает refresh-токен на новую пару токенов")
    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(@RequestHeader("X-Refresh-Token") String refreshToken) {
        return ResponseEntity.ok(authService.refresh(refreshToken));
    }

    @Operation(summary = "Выход", description = "Инвалидирует refresh-токен в Keycloak")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("X-Refresh-Token") String refreshToken) {
        authService.logout(refreshToken);
        return ResponseEntity.noContent().build();
    }
}
