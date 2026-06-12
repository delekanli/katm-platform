package uz.katm.report.client;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import uz.katm.common.exception.BusinessException;
import uz.katm.common.http.HttpResponseInfo;
import uz.katm.common.http.HttpUtils;
import uz.katm.common.http.IHttpService;
import uz.katm.report.domain.fcbk.FicoScoreParams;
import uz.katm.report.domain.fcbk.ResponseBlock;

/**
 * Клиент внешнего сервиса FICO-скоринга FCBK (Basic auth).
 * Перенос gov.uz.katm.core.fcbk.service.FcbkServiceImpl (REST-часть) на общий IHttpService.
 */
@Slf4j
@Component
public class FcbkClient {

    private final IHttpService httpService;
    private final ObjectMapper objectMapper;

    @Value("${katm.fcbk.url:}")
    private String url;

    @Value("${katm.fcbk.username:}")
    private String username;

    @Value("${katm.fcbk.password:}")
    private String password;

    public FcbkClient(IHttpService httpService) {
        this.httpService = httpService;
        this.objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public ResponseBlock requestFicoScore(FicoScoreParams params) {
        if (url == null || url.isBlank()) {
            throw new BusinessException("FCBK_NOT_CONFIGURED",
                    "URL сервиса FCBK FICO не сконфигурирован (katm.fcbk.url)", HttpStatus.SERVICE_UNAVAILABLE);
        }
        try {
            HttpResponseInfo resp = httpService.sendPostRequest(url, params, HttpUtils.basicAuthHeaders(username, password));
            if (resp == null || !resp.isSuccess()) {
                throw new BusinessException("FCBK_ERROR",
                        "Ошибка сервиса FCBK FICO, status=" + (resp != null ? resp.getStatusCode() : "null"),
                        HttpStatus.BAD_GATEWAY);
            }
            return objectMapper.readValue(resp.getBodyAsString(), ResponseBlock.class);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException("FCBK_ERROR",
                    "Ошибка при формировании FICO score: " + e.getMessage(), HttpStatus.BAD_GATEWAY);
        }
    }
}
