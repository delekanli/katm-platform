package uz.katm.mip.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uz.katm.mip.client.PcdClient;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Коммунальные задолженности через ПЦД (перенос bkiService getElectroData / getGasBalance /
 * getEcologyData / getTeploenerData). Тела запросов воспроизводят DTO монолита
 * (MunicipalParams / PinParams / EcologyParams) с теми же JSON-ключами.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UtilityMipService {

    private static final DateTimeFormatter DD_MM_YYYY = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private final PcdClient pcdClient;

    @Value("${katm.mip.utility.electro-url:}")
    private String electroUrl;
    @Value("${katm.mip.utility.gas-url:}")
    private String gasUrl;
    @Value("${katm.mip.utility.ecology-url:}")
    private String ecologyUrl;
    @Value("${katm.mip.utility.toshissiqquvvat-url:}")
    private String toshissiqquvvatUrl;

    /** Задолженность по электроснабжению (REPORT_ELECTRO_INFO=36, MunicipalParams). */
    public String getElectro(String pin) {
        log.info("Коммуналка электро: pin={}", pin);
        return call(electroUrl, municipal(pin));
    }

    /** Задолженность по газоснабжению (REPORT_GAS_INFO=37, PinParams{pin}). */
    public String getGas(String pin) {
        log.info("Коммуналка газ: pin={}", pin);
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("pin", nz(pin));
        return call(gasUrl, payload);
    }

    /** Задолженность по теплоэнергии (REPORT_PE_TOSHISSIQQUVVAT=69, MunicipalParams). */
    public String getToshissiqquvvat(String pin) {
        log.info("Коммуналка теплоэнерго: pin={}", pin);
        return call(toshissiqquvvatUrl, municipal(pin));
    }

    /**
     * Задолженность по бытовым отходам (REPORT_ECOLOGY=38, EcologyParams).
     * Период по умолчанию: год назад … сегодня (формат dd.MM.yyyy).
     */
    public String getEcology(String pin, String startDate, String endDate) {
        log.info("Коммуналка экология: pin={}", pin);
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("transaction_id", transactionId());
        payload.put("sender_pinfl", MibMipService.SENDER_PINFL);
        payload.put("purpose", "Scoring");
        payload.put("consent", "Yes");
        payload.put("pinfl", nz(pin));
        payload.put("startDate", hasText(startDate) ? startDate : LocalDate.now().minusYears(1).format(DD_MM_YYYY));
        payload.put("endDate", hasText(endDate) ? endDate : LocalDate.now().format(DD_MM_YYYY));
        return call(ecologyUrl, payload);
    }

    /** Тело MunicipalParams (электро/теплоэнерго): customer_type=P, purpose=Scoring, consent=Yes. */
    private Map<String, Object> municipal(String pin) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("transaction_id", transactionId());
        payload.put("customer_type", "P");
        payload.put("sender_pinfl", MibMipService.SENDER_PINFL);
        payload.put("purpose", "Scoring");
        payload.put("consent", "Yes");
        payload.put("pinfl", nz(pin));
        return payload;
    }

    private String call(String url, Object payload) {
        return pcdClient.mibState(pcdClient.execute(url, payload));
    }

    private static String transactionId() {
        return String.valueOf(System.currentTimeMillis());
    }

    private static boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

    private static String nz(String value) {
        return value != null ? value : "";
    }
}
