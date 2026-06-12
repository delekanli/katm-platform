package uz.katm.egov.domain.record;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/** Результат инициализации клиента в UCIN (ответ katm-ucin /clients/init). */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ClientInitResult(
        String clientId,
        String result,
        String message
) {
}
