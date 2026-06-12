package uz.katm.mip.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import uz.katm.common.exception.BusinessException;
import uz.katm.common.http.HttpResponseInfo;
import uz.katm.common.http.HttpUtils;
import uz.katm.common.http.IHttpService;
import uz.katm.mip.exception.MipTransientException;

/**
 * Сквозной (passthrough) клиент к REST-сервисам ПЦД/МИБ.
 * Перенос методов {@code executePcdServiceCall} и {@code mibState} из монолита
 * (GovUzWebServiceImpl) на современный {@link IHttpService}.
 *
 * <p>Авторизация — Bearer-токен из {@link PcdTokenStore}; при 401 токен
 * принудительно обновляется и запрос повторяется один раз. Resilience4j
 * добавляет ретраи и circuit breaker поверх нестабильных госсервисов.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PcdClient {

    private static final String RESILIENCE_NAME = "pcd";

    private final IHttpService httpService;
    private final PcdTokenStore tokenStore;

    public HttpResponseInfo execute(String url, Object jsonBody) {
        return execute("POST", url, jsonBody);
    }

    @CircuitBreaker(name = RESILIENCE_NAME)
    @Retry(name = RESILIENCE_NAME)
    public HttpResponseInfo execute(String method, String url, Object jsonBody) {
        // Транзиентные сбои (MipTransientException) пробрасываются наружу,
        // чтобы их перехватил Resilience4j (@Retry/@CircuitBreaker).
        HttpResponseInfo response = call(method, url, jsonBody);

        // Токен протух — обновляем и повторяем один раз (не транзиентный сбой)
        if (response != null && response.getStatusCode() == HttpStatus.UNAUTHORIZED.value()) {
            log.info("ПЦД вернул 401 — обновляю токен и повторяю запрос");
            tokenStore.reload();
            response = call(method, url, jsonBody);
        }
        return response;
    }

    private HttpResponseInfo call(String method, String url, Object jsonBody) {
        String token = tokenStore.getAccessToken();
        var headers = HttpUtils.bearerAuthHeaders(token != null ? token : "");
        try {
            if (method == null || method.isBlank() || "POST".equalsIgnoreCase(method)) {
                return httpService.sendPostRequest(url, jsonBody, headers);
            }
            return httpService.sendGetRequest(url, headers);
        } catch (Exception e) {
            throw new MipTransientException("Сбой связи с сервисом ПЦД: " + e.getMessage(), e);
        }
    }

    /**
     * Интерпретация HTTP-кода ответа сервиса МИБ (перенос {@code mibState} из монолита).
     * Возвращает тело при 200/204, иначе кидает {@link BusinessException} с понятным сообщением.
     */
    public String mibState(HttpResponseInfo response) {
        if (response == null) {
            throw new BusinessException("MIP_MIB_EMPTY", "Пустой ответ от сервиса МИБ.", HttpStatus.BAD_GATEWAY);
        }
        return switch (response.getStatusCode()) {
            case 200 -> {
                if (response.getBody() != null && response.getBody().length > 0) {
                    yield response.getBodyAsString();
                }
                throw new BusinessException("MIP_MIB_EMPTY", "Получили пустой ответ от сервиса МИБ.", HttpStatus.BAD_GATEWAY);
            }
            case 204 -> "{\"resultCode\":204,\"resultMessage\":\"Запрос выполнен успешно. Данные не найдены.\"}";
            case 301 -> throw mib("Адрес службы изменился.", HttpStatus.BAD_GATEWAY);
            case 302 -> throw mib("Обслуживающий Node находится по другому адресу.", HttpStatus.BAD_GATEWAY);
            case 400 -> throw mib("Неверный запрос.", HttpStatus.BAD_REQUEST);
            case 401 -> throw mib("Для подключения к сервису требуется авторизация.", HttpStatus.UNAUTHORIZED);
            case 403 -> throw mib("Доступ к запрашиваемой информации закрыт.", HttpStatus.FORBIDDEN);
            case 404 -> throw mib("Не найдено информации по указанным параметрам.", HttpStatus.NOT_FOUND);
            case 408 -> throw mib("Время ожидания запроса истекло.", HttpStatus.REQUEST_TIMEOUT);
            case 423 -> throw mib("Данные заблокированы в OneID (PIN/TIN).", HttpStatus.LOCKED);
            case 500 -> throw mib("Внутренняя ошибка сервера МИБ.", HttpStatus.BAD_GATEWAY);
            case 503 -> throw mib("Сервис МИБ недоступен.", HttpStatus.SERVICE_UNAVAILABLE);
            default -> throw mib("Неопределённая ошибка сервиса МИБ.", HttpStatus.BAD_GATEWAY);
        };
    }

    private static BusinessException mib(String message, HttpStatus status) {
        return new BusinessException("MIP_MIB_ERROR", message, status);
    }
}