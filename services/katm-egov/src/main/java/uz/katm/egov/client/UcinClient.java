package uz.katm.egov.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import uz.katm.egov.domain.record.ClientInitResult;

import java.util.Map;

/**
 * Клиент к сервису katm-ucin для инициализации клиента (registerClaim делегирует UCIN,
 * как в монолите). По образцу katm-client MipClient — REST-вызов без auth-заголовка
 * (сложившаяся inter-service конвенция платформы; см. оговорку про межсервисную авторизацию).
 */
@Slf4j
@Component
public class UcinClient {

    private final RestClient restClient;

    public UcinClient(@Value("${katm.ucin-service.url:http://localhost:8094}") String baseUrl) {
        this.restClient = RestClient.builder().baseUrl(baseUrl).build();
    }

    public ClientInitResult initClient(Map<String, Object> body) {
        log.info("UCIN initClient: pinfl={}", body.get("pinfl"));
        UcinApiResponse response = restClient.post()
                .uri("/api/v1/ucin/clients/init")
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .body(UcinApiResponse.class);
        return response != null ? response.data() : null;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record UcinApiResponse(ClientInitResult data) {
    }
}
