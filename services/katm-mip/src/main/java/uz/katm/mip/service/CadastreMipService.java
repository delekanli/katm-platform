package uz.katm.mip.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;
import uz.katm.common.exception.BusinessException;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Кадастр недвижимости (REPORT_CADASTRE_LIST=79 / DETAILS=80). Перенос bkiService.getCadastreInfo:
 * собственный HTTP с BasicAuth (НЕ ПЦД). Запрос по кадастровому номеру (приоритет), иначе по ПИНФЛ,
 * иначе по ИНН. Тело: {id, time, cad_num|pinfl|org_tin} (CadastreParams монолита).
 * Конфиг: katm.mip.cadastre.{url,username,password}.
 */
@Slf4j
@Service
public class CadastreMipService {

    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final RestClient restClient = RestClient.create();
    private final String url;
    private final String authHeader;

    public CadastreMipService(@Value("${katm.mip.cadastre.url:}") String url,
                              @Value("${katm.mip.cadastre.username:}") String username,
                              @Value("${katm.mip.cadastre.password:}") String password) {
        this.url = url;
        this.authHeader = "Basic " + Base64.getEncoder()
                .encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Кадастровая информация: по номеру (если задан), иначе по ПИНФЛ, иначе по ИНН.
     * Возвращает сырой ответ или "{}" при пустом теле.
     */
    public String getCadastreInfo(String cadastreNumber, String pin, String tin) {
        log.info("Кадастр: number={}, pin={}, tin={}", cadastreNumber, pin, tin);
        Map<String, Object> payload = base();
        if (StringUtils.hasText(cadastreNumber)) {
            payload.put("cad_num", cadastreNumber);
        } else if (StringUtils.hasText(pin)) {
            payload.put("pinfl", pin);
        } else if (StringUtils.hasText(tin)) {
            payload.put("org_tin", tin);
        } else {
            throw new BusinessException("CADASTRE_NO_SUBJECT",
                    "Не передан кадастровый номер, ПИНФЛ или ИНН", HttpStatus.BAD_REQUEST);
        }
        try {
            String response = restClient.post()
                    .uri(url)
                    .header(HttpHeaders.AUTHORIZATION, authHeader)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(payload)
                    .retrieve()
                    .body(String.class);
            return StringUtils.hasText(response) ? response : "{}";
        } catch (Exception e) {
            log.error("Ошибка запроса в Кадастр: {}", e.getMessage());
            throw new BusinessException("CADASTRE_UNAVAILABLE",
                    "Сервис Кадастрового Агентства недоступен. Повторите попытку позже", HttpStatus.BAD_GATEWAY);
        }
    }

    private Map<String, Object> base() {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("id", ThreadLocalRandom.current().nextInt(Integer.MAX_VALUE));
        payload.put("time", LocalDateTime.now().format(TIME_FMT));
        return payload;
    }
}
