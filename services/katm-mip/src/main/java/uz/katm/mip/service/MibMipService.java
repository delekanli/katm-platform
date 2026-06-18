package uz.katm.mip.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import uz.katm.common.exception.BusinessException;
import uz.katm.mip.client.PcdClient;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Отчёты МИБ (Бюро принудительного исполнения) через ПЦД REST-канал. Перенос bkiService
 * getMib*Data + getList{Pe,Le}Insolvent. Все запросы наследуют базу MibParams монолита:
 * {transaction_id, sender_pinfl, purpose="Scoring", consent="Yes"} + поля конкретного отчёта.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MibMipService {

    /** Constants.RESPONSIBLE_PIN — ПИНФЛ-отправитель запросов в МИБ. */
    public static final String SENDER_PINFL = "32010840440026";

    private final PcdClient pcdClient;

    @Value("${katm.mip.mib.debt-ban-url:}")
    private String debtBanUrl;
    @Value("${katm.mip.mib.avto-ban-url:}")
    private String avtoBanUrl;
    @Value("${katm.mip.mib.realty-ban-url:}")
    private String realtyBanUrl;
    @Value("${katm.mip.mib.doc-avto-url:}")
    private String docAvtoUrl;
    @Value("${katm.mip.mib.doc-creditor-url:}")
    private String docCreditorUrl;
    @Value("${katm.mip.mib.doc-debtor-url:}")
    private String docDebtorUrl;
    @Value("${katm.mip.mib.doc-action-url:}")
    private String docActionUrl;
    @Value("${katm.mip.mib.list-pe-insolvent-url:}")
    private String listPeInsolventUrl;
    @Value("${katm.mip.mib.list-le-insolvent-url:}")
    private String listLeInsolventUrl;

    /** Запрет выезда за границу (REPORT_MIB_DEBT_BAN=310). */
    public String getDebtBan(String pin) {
        log.info("МИБ запрет выезда: pin={}", pin);
        return call(debtBanUrl, withPin(pin));
    }

    /** Запрет на использование автотранспорта (REPORT_MIB_AVTO_BAN=311). */
    public String getAvtoBan(String autoNumber) {
        log.info("МИБ запрет на авто: number={}", autoNumber);
        return call(avtoBanUrl, withAuto(autoNumber));
    }

    /** Запрет на недвижимость (REPORT_MIB_REALTY_BAN=312). */
    public String getRealtyBan(String cadNumber) {
        log.info("МИБ запрет на недвижимость: cad={}", cadNumber);
        return call(realtyBanUrl, withCad(cadNumber));
    }

    /** Исполнительный бланк по гос. номеру ТС (REPORT_MIB_DOC_AVTO=313). */
    public String getDocAvto(String autoNumber) {
        log.info("МИБ исп. бланк (авто): number={}", autoNumber);
        return call(docAvtoUrl, withAuto(autoNumber));
    }

    /** Исполнительный лист в качестве взыскателя (REPORT_MIB_DOC_CREDITOR=314). */
    public String getDocCreditor(String pin) {
        log.info("МИБ исп. лист (взыскатель): pin={}", pin);
        return call(docCreditorUrl, withPin(pin));
    }

    /** Исполнительный лист у должника (REPORT_MIB_DOC_DEBTOR=315): ПИНФЛ приоритетнее ИНН. */
    public String getDocDebtor(String pin, String tin) {
        log.info("МИБ исп. лист (должник): pin={}, tin={}", pin, tin);
        if (StringUtils.hasText(pin)) {
            return call(docDebtorUrl, withPin(pin));
        }
        if (StringUtils.hasText(tin)) {
            return call(docDebtorUrl, withTin(tin));
        }
        throw new BusinessException("MIB_NO_SUBJECT", "Не передан ПИНФЛ или ИНН субъекта",
                org.springframework.http.HttpStatus.BAD_REQUEST);
    }

    /** Ход исполнительного производства (REPORT_MIB_DOC_ACTION=316): type=subReportId, work_number. */
    public String getDocAction(String pin, String workNumber, int type) {
        log.info("МИБ ход производства: pin={}, work={}, type={}", pin, workNumber, type);
        Map<String, Object> payload = base();
        payload.put("type", type);
        payload.put("work_number", nz(workNumber));
        payload.put("pin", nz(pin));
        return call(docActionUrl, payload);
    }

    /** Физлицо в списке нечестных/неплатёжеспособных (REPORT_LIST_PE_INSOLVENT=317). */
    public String getListPeInsolvent(String pin) {
        log.info("МИБ список неплатёжеспособных (физ): pin={}", pin);
        return call(listPeInsolventUrl, withPin(pin));
    }

    /** Юрлицо в списке нечестных/неплатёжеспособных (REPORT_LIST_LE_INSOLVENT=318). */
    public String getListLeInsolvent(String tin) {
        log.info("МИБ список неплатёжеспособных (юр): tin={}", tin);
        return call(listLeInsolventUrl, withTin(tin));
    }

    /** Базовые поля всех МИБ-запросов (MibParams монолита). */
    private Map<String, Object> base() {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("transaction_id", System.currentTimeMillis());
        payload.put("sender_pinfl", SENDER_PINFL);
        payload.put("purpose", "Scoring");
        payload.put("consent", "Yes");
        return payload;
    }

    private Map<String, Object> withPin(String pin) {
        Map<String, Object> payload = base();
        payload.put("pin", nz(pin));
        return payload;
    }

    private Map<String, Object> withTin(String tin) {
        Map<String, Object> payload = base();
        payload.put("inn", nz(tin));
        return payload;
    }

    private Map<String, Object> withAuto(String autoNumber) {
        Map<String, Object> payload = base();
        payload.put("auto_number", nz(autoNumber));
        return payload;
    }

    private Map<String, Object> withCad(String cadNumber) {
        Map<String, Object> payload = base();
        payload.put("cad_number", nz(cadNumber));
        return payload;
    }

    private String call(String url, Object payload) {
        return pcdClient.mibState(pcdClient.execute(url, payload));
    }

    private static String nz(String value) {
        return value != null ? value : "";
    }
}
