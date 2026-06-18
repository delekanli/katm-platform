package uz.katm.mip.domain.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Параметры инициации запроса о судимости. Идентичность субъекта передаётся вызывающим
 * (как и в прочих MIP-эндпоинтах). regionId — код региона (СОАТО) места рождения.
 */
public record ConvictionRequest(
        @NotBlank String pinfl,
        String docSeries,
        String docNumber,
        Integer birthYear,
        String firstName,
        String lastName,
        @NotBlank String regionId
) {
}
