package uz.katm.mip.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import uz.katm.mip.client.PcdClient;
import uz.katm.mip.domain.dto.EInvoiceRequest;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Прочие отчёты через ПЦД: Мой дом (Минстрой), выручка по ККМ (E-Invoice ГНК физ/юр), кэшбек ГНК.
 * Перенос bkiService getMyHomeData / getEInvoiceKkm{Pe,Le}Data / getCashBackData. Тела запросов
 * воспроизводят DTO монолита (MyHomeParams / EInvoiceKkmParams / TaxTinPinParams).
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MiscMipService {

    private static final String DEFAULT_LANG = "ru";
    private static final DateTimeFormatter DD_MM_YYYY = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private final PcdClient pcdClient;

    @Value("${katm.mip.misc.my-home-url:}")
    private String myHomeUrl;
    @Value("${katm.mip.misc.einvoice-pe-url:}")
    private String einvoicePeUrl;
    @Value("${katm.mip.misc.einvoice-le-url:}")
    private String einvoiceLeUrl;
    @Value("${katm.mip.misc.cashback-url:}")
    private String cashbackUrl;

    /** Мой дом (REPORT_MY_HOME=321, MyHomeParams). cadastre — кадастровый номер (обязателен). */
    public String getMyHome(String cadastre, String pin) {
        log.info("Мой дом: cadastre={}, pin={}", cadastre, pin);
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("CUSTOMER", "");
        payload.put("CADASTRE", nz(cadastre));
        payload.put("PIN", nz(pin));
        payload.put("TIN", "");
        return call(myHomeUrl, payload);
    }

    /** Выручка по ККМ физлица (REPORT_E_INVOICE_KKM_PE=326). pinTin = ПИНФЛ, иначе ИНН. */
    public String getEInvoiceKkmPe(EInvoiceRequest req) {
        log.info("E-Invoice физ: pin={}, tin={}", req.pin(), req.tin());
        String pinTin = StringUtils.hasText(req.pin()) ? req.pin() : nz(req.tin());
        return call(einvoicePeUrl, einvoice(req, pinTin));
    }

    /** Выручка по ККМ юрлица (REPORT_E_INVOICE_KKM_LE=327). pinTin = ИНН. */
    public String getEInvoiceKkmLe(EInvoiceRequest req) {
        log.info("E-Invoice юр: tin={}", req.tin());
        return call(einvoiceLeUrl, einvoice(req, nz(req.tin())));
    }

    /** Кэшбек ГНК (REPORT_CASH_BACK=328, TaxTinPinParams). year по умолчанию — текущий. */
    public String getCashBack(String pin, Integer year, String lang) {
        log.info("Кэшбек: pin={}, year={}", pin, year);
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("lang", lang(lang));
        payload.put("tin", "");
        payload.put("year", year != null ? year : LocalDate.now().getYear());
        payload.put("pinfl", nz(pin));
        return call(cashbackUrl, payload);
    }

    private Map<String, Object> einvoice(EInvoiceRequest req, String pinTin) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("date_begin", StringUtils.hasText(req.dateBegin())
                ? req.dateBegin() : LocalDate.now().minusYears(1).format(DD_MM_YYYY));
        payload.put("date_end", StringUtils.hasText(req.dateEnd())
                ? req.dateEnd() : LocalDate.now().format(DD_MM_YYYY));
        payload.put("page", StringUtils.hasText(req.page()) ? req.page() : "0");
        payload.put("size", "100");
        payload.put("signature", nz(req.signature()));
        payload.put("tin_or_pinfl", nz(pinTin));
        payload.put("lang", lang(req.lang()));
        return payload;
    }

    private String call(String url, Object payload) {
        return pcdClient.mibState(pcdClient.execute(url, payload));
    }

    private static String lang(String lang) {
        return StringUtils.hasText(lang) ? lang : DEFAULT_LANG;
    }

    private static String nz(String value) {
        return value != null ? value : "";
    }
}
