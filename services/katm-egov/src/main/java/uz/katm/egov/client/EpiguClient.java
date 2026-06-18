package uz.katm.egov.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import uz.katm.egov.domain.dto.EpiguReportRequest;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Клиент к внешнему сервису E-GOV Epigu (epigu.uz) для отправки готовых кредитных отчётов.
 * Перенос логики отправки из монолитного ResendEgovReportsScheduler: POST с Basic Auth,
 * URL шаблонизируется claimId (в монолите — {@code String.format(url, claimId)}).
 */
@Slf4j
@Component
public class EpiguClient {

    private final RestClient restClient = RestClient.create();
    private final String urlTemplate;
    private final String authHeader;

    public EpiguClient(@Value("${katm.egov.epigu.url:}") String urlTemplate,
                       @Value("${katm.egov.epigu.username:}") String username,
                       @Value("${katm.egov.epigu.password:}") String password) {
        this.urlTemplate = urlTemplate;
        this.authHeader = "Basic " + Base64.getEncoder()
                .encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8));
    }

    /** Отправляет отчёт по заявке в Epigu. Возвращает {@code true} при HTTP 2xx. */
    public boolean sendReport(String claimId, EpiguReportRequest body) {
        String url = String.format(urlTemplate, claimId);
        try {
            var response = restClient.post()
                    .uri(url)
                    .header(HttpHeaders.AUTHORIZATION, authHeader)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(body)
                    .retrieve()
                    .toBodilessEntity();
            boolean ok = response.getStatusCode().is2xxSuccessful();
            log.info("Epigu resend claimId={} -> HTTP {}", claimId, response.getStatusCode().value());
            return ok;
        } catch (Exception e) {
            log.error("Ошибка отправки отчёта в Epigu по заявке {}: {}", claimId, e.getMessage());
            return false;
        }
    }
}
