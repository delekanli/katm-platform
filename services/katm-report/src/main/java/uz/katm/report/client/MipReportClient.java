package uz.katm.report.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import uz.katm.report.exception.CreditServiceException;

/**
 * Клиент к katm-mip для диспетчера отчётов: вызывает MIP/ПЦД-эндпоинты (ГНК, MIP2, МИБ, коммуналка,
 * прочее, MIP-core) и распаковывает обёртку ApiResponse&lt;String&gt; (поле data).
 * Аналог межсервисного MipClient (katm-claim): RestClient с baseUrl katm.mip-service.url.
 */
@Slf4j
@Component
public class MipReportClient {

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public MipReportClient(@Value("${katm.mip-service.url}") String baseUrl, ObjectMapper objectMapper) {
        this.restClient = RestClient.builder().baseUrl(baseUrl).build();
        this.objectMapper = objectMapper;
    }

    public String get(String pathWithQuery) {
        try {
            return unwrap(restClient.get().uri(pathWithQuery).retrieve().body(String.class));
        } catch (CreditServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("Ошибка вызова MIP {}: {}", pathWithQuery, e.getMessage());
            throw new CreditServiceException("-1", "Сервис данных недоступен. Повторите попытку позже");
        }
    }

    public String post(String path, Object body) {
        try {
            return unwrap(restClient.post().uri(path).contentType(MediaType.APPLICATION_JSON)
                    .body(body).retrieve().body(String.class));
        } catch (CreditServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("Ошибка вызова MIP {}: {}", path, e.getMessage());
            throw new CreditServiceException("-1", "Сервис данных недоступен. Повторите попытку позже");
        }
    }

    /** Извлекает поле data из обёртки ApiResponse; если data — объект, возвращает его JSON. */
    private String unwrap(String apiResponseJson) {
        if (apiResponseJson == null) {
            return "{}";
        }
        try {
            JsonNode node = objectMapper.readTree(apiResponseJson);
            JsonNode data = node.path("data");
            if (data.isMissingNode() || data.isNull()) {
                return "{}";
            }
            return data.isTextual() ? data.asText() : data.toString();
        } catch (Exception e) {
            // Не ApiResponse — возвращаем как есть.
            return apiResponseJson;
        }
    }
}
