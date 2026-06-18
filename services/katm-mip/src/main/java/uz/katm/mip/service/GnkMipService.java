package uz.katm.mip.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import uz.katm.mip.client.PcdClient;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Отчёты ГНК (налоговый комитет) через ПЦД REST-канал. Перенос bkiService.get*Data
 * (BkiServiceImpl) — каждый отчёт строил payload и звал govUzWebService.executePcdServiceCall + mibState.
 * Тела запросов воспроизводят DTO монолита (TaxParams / TaxTinPinParams / TaxAccountReportParams /
 * TaxPinParams / PinflParams) с теми же JSON-ключами.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GnkMipService {

    private static final String DEFAULT_LANG = "ru";

    private final PcdClient pcdClient;

    @Value("${katm.mip.gnk.salary-url:}")
    private String salaryUrl;
    @Value("${katm.mip.gnk.dividend-url:}")
    private String dividendUrl;
    @Value("${katm.mip.gnk.rent-url:}")
    private String rentUrl;
    @Value("${katm.mip.gnk.account-report-1-url:}")
    private String accountReport1Url;
    @Value("${katm.mip.gnk.account-report-2-url:}")
    private String accountReport2Url;
    @Value("${katm.mip.gnk.ip-info-url:}")
    private String ipInfoUrl;
    @Value("${katm.mip.gnk.info-url:}")
    private String gnkInfoUrl;
    @Value("${katm.mip.gnk.pe-info-url:}")
    private String peInfoUrl;
    @Value("${katm.mip.gnk.juridical-info-url:}")
    private String juridicalInfoUrl;
    @Value("${katm.mip.gnk.pe-subject-url:}")
    private String peSubjectUrl;
    @Value("${katm.mip.gnk.tax-report-url:}")
    private String taxReportUrl;
    @Value("${katm.mip.gnk.staff-count-url:}")
    private String staffCountUrl;
    @Value("${katm.mip.gnk.nds-url:}")
    private String ndsUrl;
    @Value("${katm.mip.gnk.self-employment-url:}")
    private String selfEmploymentUrl;

    /** Зарплата (REPORT_GNK_SALARY=48, TaxParams). tin отправляется только при пустом ПИНФЛ — как в монолите. */
    public String getSalary(String pinfl, String tin, String lang) {
        log.info("ГНК зарплата: pinfl={}, tin={}", pinfl, tin);
        String effectiveTin = StringUtils.hasText(pinfl) ? "" : nz(tin);
        return call(salaryUrl, taxParams(lang, effectiveTin, pinfl));
    }

    /** Дивиденды (REPORT_GNK_DIVIDEND=50, TaxTinPinParams: lang/tin/year/pinfl). */
    public String getDividend(String pinfl, String tin, String lang, int year) {
        log.info("ГНК дивиденды: pinfl={}, tin={}, year={}", pinfl, tin, year);
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("lang", lang(lang));
        payload.put("tin", nz(tin));
        payload.put("year", year);
        payload.put("pinfl", nz(pinfl));
        return call(dividendUrl, payload);
    }

    /** Аренда (REPORT_GNK_RENT=52, TaxParams — монолит задаёт только ПИНФЛ). */
    public String getRent(String pinfl, String lang) {
        log.info("ГНК аренда: pinfl={}", pinfl);
        return call(rentUrl, taxParams(lang, "", pinfl));
    }

    /** Бухбаланс форма 1 (REPORT_GNK_ACCOUNT_REPORT_1=60, TaxAccountReportParams). */
    public String getAccountReport1(String tin, String lang, int year, int period) {
        log.info("ГНК бухотчёт ф.1: tin={}, year={}, period={}", tin, year, period);
        return call(accountReport1Url, accountReportParams(lang, tin, year, period));
    }

    /** Финотчётность форма 2 (REPORT_GNK_ACCOUNT_REPORT_2=62, TaxAccountReportParams). */
    public String getAccountReport2(String tin, String lang, int year, int period) {
        log.info("ГНК бухотчёт ф.2: tin={}, year={}, period={}", tin, year, period);
        return call(accountReport2Url, accountReportParams(lang, tin, year, period));
    }

    /** Сведения об ИП (REPORT_GNK_IP_INFO=63, TaxPinParams: lang/pinfl). */
    public String getIpInfo(String pinfl, String lang) {
        log.info("ГНК сведения об ИП: pinfl={}", pinfl);
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("lang", lang(lang));
        payload.put("pinfl", nz(pinfl));
        return call(ipInfoUrl, payload);
    }

    /** Общие сведения ГНК (REPORT_GNK=91, PinflParams: ключ pinfl). */
    public String getGnkInfo(String pinfl) {
        log.info("ГНК общие сведения: pinfl={}", pinfl);
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("pinfl", nz(pinfl));
        return call(gnkInfoUrl, payload);
    }

    /** Информация о физлице (REPORT_GNK_PE_INFO=46, TaxParams: lang/tin/pinfl). */
    public String getPeInfo(String pinfl, String tin, String lang) {
        log.info("ГНК инфо физлица: pinfl={}, tin={}", pinfl, tin);
        return call(peInfoUrl, taxParams(lang, tin, pinfl));
    }

    /** Информация о юрлице (REPORT_GNK_JURIDICAL_INFO=58, TaxLegalTinParams: lang/company_tin). */
    public String getJuridicalInfo(String tin, String lang) {
        log.info("ГНК инфо юрлица: tin={}", tin);
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("lang", lang(lang));
        payload.put("company_tin", nz(tin));
        return call(juridicalInfoUrl, payload);
    }

    /** Имущество физлица (REPORT_GNK_PE_SUBJECT=54, TaxTinPinParams: lang/tin=""/year/pinfl). */
    public String getPeSubject(String pinfl, String lang, int year) {
        log.info("ГНК имущество физлица: pinfl={}", pinfl);
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("lang", lang(lang));
        payload.put("tin", "");
        payload.put("year", year);
        payload.put("pinfl", nz(pinfl));
        return call(peSubjectUrl, payload);
    }

    /** Отчёт по ЕНП (REPORT_GNK_TAX_REPORT=64, TaxAccountReportParams: lang/tin/year/period=quarter). */
    public String getTaxReport(String tin, String lang, int year, int period) {
        log.info("ГНК отчёт ЕНП: tin={}, year={}, period={}", tin, year, period);
        return call(taxReportUrl, accountReportParams(lang, tin, year, period));
    }

    /** Количество работников (REPORT_GNK_STAFF_REPORT=66, TaxTinYearParams: lang/tin/year). */
    public String getStaffCount(String tin, String lang, int year) {
        log.info("ГНК число работников: tin={}, year={}", tin, year);
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("lang", lang(lang));
        payload.put("tin", nz(tin));
        payload.put("year", year);
        return call(staffCountUrl, payload);
    }

    /** База НДС (REPORT_GNK_NDS_REPORT=68, TaxTinParams: lang/tin). */
    public String getNds(String tin, String lang) {
        log.info("ГНК НДС: tin={}", tin);
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("lang", lang(lang));
        payload.put("tin", nz(tin));
        return call(ndsUrl, payload);
    }

    /** Самозанятость (REPORT_GNK_SELF_EMPLOYMENT=45, TaxParams: lang/tin/pinfl). */
    public String getSelfEmployment(String pinfl, String tin, String lang) {
        log.info("ГНК самозанятость: pinfl={}, tin={}", pinfl, tin);
        return call(selfEmploymentUrl, taxParams(lang, tin, pinfl));
    }

    /** Полная форма TaxParams (lang/tin/pinfl + пустые series_passport/number_passport, как сериализует монолит). */
    private Map<String, Object> taxParams(String lang, String tin, String pinfl) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("lang", lang(lang));
        payload.put("tin", nz(tin));
        payload.put("pinfl", nz(pinfl));
        payload.put("series_passport", "");
        payload.put("number_passport", "");
        return payload;
    }

    private Map<String, Object> accountReportParams(String lang, String tin, int year, int period) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("lang", lang(lang));
        payload.put("tin", nz(tin));
        payload.put("year", year);
        payload.put("period", period);
        return payload;
    }

    private String call(String url, Object payload) {
        return pcdClient.mibState(pcdClient.execute(url, payload));
    }

    private String lang() {
        return DEFAULT_LANG;
    }

    private String lang(String lang) {
        return StringUtils.hasText(lang) ? lang : DEFAULT_LANG;
    }

    private static String nz(String value) {
        return value != null ? value : "";
    }
}
