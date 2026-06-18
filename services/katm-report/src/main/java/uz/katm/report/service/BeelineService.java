package uz.katm.report.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import uz.katm.report.client.BeelineClient;
import uz.katm.report.domain.record.ClaimData;
import uz.katm.report.exception.CreditServiceException;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Отчёты Beeline (скоринг/телефоны/верификация/роуминг). Перенос BkiServiceImpl.getBeeline*Data.
 * Каждый отчёт: авторизация → запрос by-pinfl, при отсутствии данных — fallback by-doc (паспорт/ФИО).
 * Возвращает сырой JSON-ответ Beeline.
 */
@Slf4j
@Service
public class BeelineService {

    private static final String SUCCESS = "SUCCESS";

    private final BeelineClient beelineClient;
    private final ObjectMapper objectMapper;

    private final String scorUrl;
    private final String scorUrlCheck;
    private final String phonesUrl;
    private final String phonesUrlCheck;
    private final String verifUrl;
    private final String verifUrlCheck;

    public BeelineService(BeelineClient beelineClient, ObjectMapper objectMapper,
                          @Value("${katm.report.beeline.scor.url:}") String scorUrl,
                          @Value("${katm.report.beeline.scor.url-check:}") String scorUrlCheck,
                          @Value("${katm.report.beeline.phones.url:}") String phonesUrl,
                          @Value("${katm.report.beeline.phones.url-check:}") String phonesUrlCheck,
                          @Value("${katm.report.beeline.verif.url:}") String verifUrl,
                          @Value("${katm.report.beeline.verif.url-check:}") String verifUrlCheck) {
        this.beelineClient = beelineClient;
        this.objectMapper = objectMapper;
        this.scorUrl = scorUrl;
        this.scorUrlCheck = scorUrlCheck;
        this.phonesUrl = phonesUrl;
        this.phonesUrlCheck = phonesUrlCheck;
        this.verifUrl = verifUrl;
        this.verifUrlCheck = verifUrlCheck;
    }

    /** Скоринг Beeline (REPORT_BEELINE_SCOR=97). Успех = message=SUCCESS. */
    public String getScor(ClaimData c) {
        requirePhone(c);
        String token = beelineClient.authenticate();
        if (StringUtils.hasText(c.pinfl())) {
            String byPin = beelineClient.post(scorUrl, scorByPin(c), token);
            if (SUCCESS.equalsIgnoreCase(message(byPin))) {
                return byPin;
            }
        }
        String byDoc = beelineClient.post(scorUrlCheck, scorByDoc(c), token);
        if (!SUCCESS.equalsIgnoreCase(message(byDoc))) {
            throw new CreditServiceException("-1",
                    "Произошла ошибка при получении данных по скорингу Beeline. Данные не найдены");
        }
        return byDoc;
    }

    /** Телефоны Beeline (REPORT_BEELINE_PHONES=98). Успех = непустой список phones. */
    public String getPhones(ClaimData c) {
        requirePhone(c);
        String token = beelineClient.authenticate();
        if (StringUtils.hasText(c.pinfl())) {
            String byPin = beelineClient.post(phonesUrl, phonesByPin(c), token);
            if (hasPhones(byPin)) {
                return byPin;
            }
        }
        String byDoc = beelineClient.post(phonesUrlCheck, phonesByDoc(c), token);
        if (!hasPhones(byDoc)) {
            throw new CreditServiceException("-1",
                    "Произошла ошибка при получении данных Beeline. Данные не найдены");
        }
        return byDoc;
    }

    /** Верификация Beeline (REPORT_BEELINE_VERIFICATION=99). Успех = пустое поле error. */
    public String getVerification(ClaimData c) {
        requirePhone(c);
        String token = beelineClient.authenticate();
        String response = beelineClient.post(verifUrlCheck, verificationParams(c), token);
        return assertNoError(response);
    }

    /** Верификация роуминга Beeline (REPORT_BEELINE_VERIFICATION_ROAMING=89). Успех = пустое поле error. */
    public String getRoaming(ClaimData c) {
        requirePhone(c);
        String token = beelineClient.authenticate();
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("phone_number", nz(c.phone()));
        String response = beelineClient.post(verifUrl, body, token);
        return assertNoError(response);
    }

    private Map<String, Object> scorByPin(ClaimData c) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("phone_number", nz(c.phone()));
        m.put("pinfl", nz(c.pinfl()));
        return m;
    }

    private Map<String, Object> scorByDoc(ClaimData c) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("phone_number", nz(c.phone()));
        m.put("first_name", nz(c.firstName()));
        m.put("last_name", nz(c.lastName()));
        m.put("pass_serial", nz(c.docSeries()));
        m.put("pass_number", nz(c.docNumber()));
        return m;
    }

    private Map<String, Object> phonesByPin(ClaimData c) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("pinfl", nz(c.pinfl()));
        return m;
    }

    private Map<String, Object> phonesByDoc(ClaimData c) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("first_name", nz(c.firstName()));
        m.put("last_name", nz(c.lastName()));
        m.put("pass_serial", nz(c.docSeries()));
        m.put("pass_number", nz(c.docNumber()));
        return m;
    }

    private Map<String, Object> verificationParams(ClaimData c) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("phone_number", nz(c.phone()));
        m.put("first_name", nz(c.firstName()));
        m.put("last_name", nz(c.lastName()));
        m.put("pass_serial", nz(c.docSeries()));
        m.put("pass_number", nz(c.docNumber()));
        m.put("birthdate", nz(c.birthDate()));
        m.put("extra_phone_number", "");
        return m;
    }

    private String message(String json) {
        return read(json).path("message").asText(null);
    }

    private boolean hasPhones(String json) {
        JsonNode phones = read(json).path("phones");
        return phones.isArray() && !phones.isEmpty();
    }

    private String assertNoError(String json) {
        String error = read(json).path("error").asText("");
        if (StringUtils.hasText(error)) {
            throw new CreditServiceException("-1", error);
        }
        return json;
    }

    private JsonNode read(String json) {
        try {
            return objectMapper.readTree(json);
        } catch (Exception e) {
            throw new CreditServiceException("-1", "Некорректный ответ сервиса Beeline");
        }
    }

    private static void requirePhone(ClaimData c) {
        if (!StringUtils.hasText(c.phone())) {
            throw new CreditServiceException("05555", "Не указан номер телефона в заявке!");
        }
    }

    private static String nz(String value) {
        return value != null ? value : "";
    }
}
