package uz.katm.mip.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Slf4j
@Component
public class MipHttpClient {

    private final RestClient restClient;

    public MipHttpClient(
            @Value("${mip.base-url:https://mip.uz/api}") String mipBaseUrl,
            @Value("${mip.api-key:change-me}") String apiKey
    ) {
        this.restClient = RestClient.builder()
                .baseUrl(mipBaseUrl)
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> sendRequest(String endpoint, Object body) {
        log.debug("MIP POST → {}", endpoint);
        return restClient.post()
                .uri(endpoint)
                .body(body)
                .retrieve()
                .body(Map.class);
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> checkStatus(String referenceId) {
        log.debug("MIP GET status → {}", referenceId);
        return restClient.get()
                .uri("/status/{id}", referenceId)
                .retrieve()
                .body(Map.class);
    }
}
