package uz.katm.mip.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;
import uz.katm.common.exception.BusinessException;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

/**
 * UZ-ASBO — сведения о бюджетниках (REPORT_UZ_ASBO=323). Перенос bkiService.getUzAsboData:
 * собственный HTTP GET с BasicAuth (НЕ ПЦД). subReportId=1 → url-check, иначе → url.
 * Конфиг: katm.mip.uzasbo.{url,url-check,username,password}.
 */
@Slf4j
@Service
public class UzAsboMipService {

    private static final DateTimeFormatter YYYY_MM_DD = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final RestClient restClient = RestClient.create();
    private final String url;
    private final String urlCheck;
    private final String authHeader;

    public UzAsboMipService(@Value("${katm.mip.uzasbo.url:}") String url,
                            @Value("${katm.mip.uzasbo.url-check:}") String urlCheck,
                            @Value("${katm.mip.uzasbo.username:}") String username,
                            @Value("${katm.mip.uzasbo.password:}") String password) {
        this.url = url;
        this.urlCheck = urlCheck;
        this.authHeader = "Basic " + Base64.getEncoder()
                .encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Сведения по ПИНФЛ за период (default год назад … сегодня). subReportId=1 — проверочный URL.
     * Возвращает сырой ответ (тело содержит {@code "state": 404}, если данные не найдены).
     */
    public String getUzAsbo(String pin, String startDate, String endDate, int subReportId) {
        if (!StringUtils.hasText(pin)) {
            throw new BusinessException("05048", "Не указан ПИНФЛ субъекта", HttpStatus.BAD_REQUEST);
        }
        String from = StringUtils.hasText(startDate) ? startDate : LocalDate.now().minusYears(1).format(YYYY_MM_DD);
        String to = StringUtils.hasText(endDate) ? endDate : LocalDate.now().format(YYYY_MM_DD);
        String base = subReportId == 1 ? urlCheck : url;
        String fullUrl = UriComponentsBuilder.fromUriString(base)
                .queryParam("PINFL", enc(pin))
                .queryParam("StartDate", from)
                .queryParam("EndDate", to)
                .build(true)
                .toUriString();
        log.info("UZ-ASBO: pin={}, subReport={}", pin, subReportId);
        try {
            return restClient.get()
                    .uri(fullUrl)
                    .header(HttpHeaders.AUTHORIZATION, authHeader)
                    .retrieve()
                    .body(String.class);
        } catch (Exception e) {
            log.error("Ошибка запроса в UZ-ASBO: {}", e.getMessage());
            throw new BusinessException("UZ_ASBO_UNAVAILABLE",
                    "Произошла ошибка при получении данных из UZ-ASBO", HttpStatus.BAD_GATEWAY);
        }
    }

    private static String enc(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}
