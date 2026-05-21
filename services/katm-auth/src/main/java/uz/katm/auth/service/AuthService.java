package uz.katm.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.katm.auth.client.KeycloakClient;
import uz.katm.auth.domain.dto.LoginRequest;
import uz.katm.auth.domain.dto.TokenResponse;


@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final KeycloakClient keycloakClient;

    public TokenResponse login(LoginRequest request) {
        log.info("Аутентификация: login={}", request.getLogin());
        return keycloakClient.getTokenByPassword(request.getLogin(), request.getPassword());
    }

    public TokenResponse refresh(String refreshToken) {
        log.debug("Обновление токена");
        return keycloakClient.refreshToken(refreshToken);
    }

    public void logout(String refreshToken) {
        log.debug("Logout: отзыв refresh-токена");
        keycloakClient.logout(refreshToken);
    }
}
