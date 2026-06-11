package uz.katm.mip.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import uz.katm.common.exception.BusinessException;
import uz.katm.common.http.HttpResponseInfo;
import uz.katm.mip.client.PcdClient;
import uz.katm.mip.domain.gcp.GcpPersonParams;
import uz.katm.mip.domain.gcp.GcpResultData;

/**
 * MIP2 / ГЦП REST-сервисы (фаза 6): получение данных о документах физлица через ПЦД.
 * Переиспользует OAuth-инфраструктуру ПЦД из фазы 7 ({@link PcdClient}).
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class Mip2Service {

    private final PcdClient pcdClient;
    private final ObjectMapper objectMapper;

    @Value("${katm.mip.gcp.person-doc-url:}")
    private String gcpPersonDocUrl;

    /** Данные о документах физлица через ГЦП (аналог getPersonDocInformation3 монолита). */
    public GcpResultData getPersonDocInfo(String pin, String document, int langId) {
        log.info("GCP getPersonDoc: pin={}, document={}, langId={}", pin, document, langId);

        if (gcpPersonDocUrl == null || gcpPersonDocUrl.isBlank()) {
            throw new BusinessException("MIP_GCP_NOT_CONFIGURED",
                    "URL сервиса ГЦП не сконфигурирован (katm.mip.gcp.person-doc-url)",
                    HttpStatus.SERVICE_UNAVAILABLE);
        }

        GcpPersonParams params = new GcpPersonParams(document, pin, langId);
        HttpResponseInfo response = pcdClient.execute(gcpPersonDocUrl, params);
        String body = pcdClient.mibState(response);

        try {
            return objectMapper.readValue(body, GcpResultData.class);
        } catch (Exception e) {
            throw new BusinessException("MIP_GCP_PARSE_ERROR",
                    "Не удалось разобрать ответ ГЦП: " + e.getMessage(),
                    HttpStatus.BAD_GATEWAY);
        }
    }
}
