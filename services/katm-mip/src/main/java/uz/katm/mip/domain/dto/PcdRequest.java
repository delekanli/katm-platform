package uz.katm.mip.domain.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.Map;

/**
 * Запрос сквозного вызова ПЦД/МИБ-сервиса.
 *
 * @param method  HTTP-метод (POST по умолчанию, если пусто)
 * @param url     полный URL целевого сервиса ПЦД
 * @param payload тело запроса (JSON), сериализуется как есть
 */
public record PcdRequest(
        String method,
        @NotBlank String url,
        Map<String, Object> payload
) {
}