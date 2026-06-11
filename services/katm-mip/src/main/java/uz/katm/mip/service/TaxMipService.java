package uz.katm.mip.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import uz.katm.mip.ws.MipPortConfigurer;
import uz.katm.mip.ws.tax.GetTaxInfobyPinPortType;
import uz.katm.mip.ws.tax.GetTaxInfobyPinReq;
import uz.katm.mip.ws.tax.GetTaxInfobyPinRes;
import uz.katm.mip.ws.tax.GetTaxInfobyPinService;
import uz.katm.mip.ws.taxtin.GetTaxInfobyTinPortType;
import uz.katm.mip.ws.taxtin.GetTaxInfobyTinReq;
import uz.katm.mip.ws.taxtin.GetTaxInfobyTinRes;
import uz.katm.mip.ws.taxtin.GetTaxInfobyTinService;
import uz.katm.mip.ws.tin.FormData;
import uz.katm.mip.ws.tin.GetTinbyPas;
import uz.katm.mip.ws.tin.GetTinbyPasNumNewPortType;
import uz.katm.mip.ws.tin.GetTinbyPasNumNewService;
import uz.katm.mip.ws.tinpas.Gettin;
import uz.katm.mip.ws.tinpas.GetTinbyPasNumPortType;
import uz.katm.mip.ws.tinpas.GetTinbyPasNumService;
import uz.katm.mip.ws.fulltin.Getfulltinbypassport;
import uz.katm.mip.ws.fulltin.GetFullTinByPassportPortType;
import uz.katm.mip.ws.fulltin.GetFullTinByPassportService;
import uz.katm.mip.ws.fulltin.Res;
import uz.katm.mip.ws.taxobjects.Gettaxobjects;
import uz.katm.mip.ws.taxobjects.GetTaxInfoIndividualPortType;
import uz.katm.mip.ws.taxobjects.GetTaxInfoIndividualService;
import uz.katm.mip.ws.payinsur.Getdatabytin;
import uz.katm.mip.ws.payinsur.GetPayInsurTaxInfoPortType;
import uz.katm.mip.ws.payinsur.GetPayInsurTaxInfoService;
import uz.katm.mip.ws.debt.GetDeptTaxInfoPortType;
import uz.katm.mip.ws.debt.GetDeptTaxInfoService;
import uz.katm.mip.ws.debt.Req;


/**
 * Налоговые и ИНН-сервисы МИП (фаза 1 миграции).
 * Тонкие обёртки над JAX-WS портами; конструкция запросов перенесена 1:1
 * из монолита {@code GovUzWebServiceImpl}.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TaxMipService {

    private final MipPortConfigurer portConfigurer;

    @Value("${katm.mip.ws-id:KATM}")
    private String wsId;

    @Value("${katm.mip.tax-endpoint:}")
    private String taxEndpoint;

    @Value("${katm.mip.tax-by-tin-endpoint:}")
    private String taxByTinEndpoint;

    @Value("${katm.mip.tin-endpoint:}")
    private String tinEndpoint;

    @Value("${katm.mip.tin-old-endpoint:}")
    private String tinOldEndpoint;

    @Value("${katm.mip.full-tin-endpoint:}")
    private String fullTinEndpoint;

    @Value("${katm.mip.tax-objects-endpoint:}")
    private String taxObjectsEndpoint;

    @Value("${katm.mip.pay-insur-endpoint:}")
    private String payInsurEndpoint;

    @Value("${katm.mip.debt-endpoint:}")
    private String debtEndpoint;

    /** Налоговая информация по ПИНФЛ (GetTaxInfobyPin). */
    @Cacheable(value = "mip-tax-pin", key = "#pin")
    public GetTaxInfobyPinRes getTaxInfoByPin(String pin) {
        log.info("SOAP GetTaxInfobyPin: pin={}", pin);

        GetTaxInfobyPinReq req = new GetTaxInfobyPinReq();
        req.setAction("getTaxInfoByPin");
        req.setPin(pin);
        req.setLang("ru");

        GetTaxInfobyPinReq.AuthInfo auth = new GetTaxInfobyPinReq.AuthInfo();
        auth.setUserSessionId("");
        auth.setWSID(wsId);
        auth.setLEID("");
        req.setAuthInfo(auth);

        GetTaxInfobyPinPortType port =
                new GetTaxInfobyPinService().getPort(GetTaxInfobyPinPortType.class);
        portConfigurer.configure(port, taxEndpoint);

        return port.receive1(req);
    }

    /** Налоговая информация по ИНН (GetTaxInfobyTin). */
    @Cacheable(value = "mip-tax-tin", key = "#tin")
    public GetTaxInfobyTinRes getTaxInfoByTin(String tin) {
        log.info("SOAP GetTaxInfobyTin: tin={}", tin);

        GetTaxInfobyTinReq req = new GetTaxInfobyTinReq();
        req.setAction("get_by_tin");
        req.setTin(tin);

        GetTaxInfobyTinPortType port =
                new GetTaxInfobyTinService().getPort(GetTaxInfobyTinPortType.class);
        portConfigurer.configure(port, taxByTinEndpoint);

        return port.receive2(req);
    }

    /** ИНН по серии/номеру паспорта, новый сервис (GetTinbyPasNumNew). */
    @Cacheable(value = "mip-tin", key = "#series + '-' + #number")
    public FormData getTinByPassport(String series, String number) {
        log.info("SOAP GetTinbyPasNumNew: series={}, number={}", series, number);

        GetTinbyPas req = new GetTinbyPas();
        req.setAction("GetTinbyPasNumNew");
        req.setPassSer(series);
        req.setPassNum(number);
        req.setLang("ru");

        GetTinbyPasNumNewPortType port =
                new GetTinbyPasNumNewService().getPort(GetTinbyPasNumNewPortType.class);
        portConfigurer.configure(port, tinEndpoint);

        return port.rEQ(req);
    }

    /** ИНН по серии/номеру паспорта, старый сервис (GetTinbyPasNum). */
    @Cacheable(value = "mip-tin-old", key = "#series + '-' + #number")
    public uz.katm.mip.ws.tinpas.Respond getTinByPassNum(String series, String number) {
        log.info("SOAP GetTinbyPasNum: series={}, number={}", series, number);

        Gettin req = new Gettin();
        Gettin.Message message = new Gettin.Message();
        message.setPasSer(series);
        message.setPasNum(number);
        req.setMessage(message);

        GetTinbyPasNumPortType port =
                new GetTinbyPasNumService().getPort(GetTinbyPasNumPortType.class);
        portConfigurer.configure(port, tinOldEndpoint);

        return port.rEQ(req);
    }

    /** Полный ИНН по паспорту (GetFullTinByPassport). */
    @Cacheable(value = "mip-full-tin", key = "#series + '-' + #number")
    public Res getFullTinByPassNum(String series, String number) {
        log.info("SOAP GetFullTinByPassport: series={}, number={}", series, number);

        Getfulltinbypassport req = new Getfulltinbypassport();
        req.setPasSer(series);
        req.setPasNum(number);

        GetFullTinByPassportPortType port =
                new GetFullTinByPassportService().getPort(GetFullTinByPassportPortType.class);
        portConfigurer.configure(port, fullTinEndpoint);

        return port.receive(req);
    }

    /** Налоговые объекты по ИНН (GetTaxInfoIndividual). */
    @Cacheable(value = "mip-tax-objects", key = "#tin")
    public uz.katm.mip.ws.taxobjects.Respond getTaxObjects(String tin) {
        log.info("SOAP GetTaxInfoIndividual: tin={}", tin);

        Gettaxobjects req = new Gettaxobjects();
        Gettaxobjects.Message message = new Gettaxobjects.Message();
        message.setTin(tin);
        req.setMessage(message);

        GetTaxInfoIndividualPortType port =
                new GetTaxInfoIndividualService().getPort(GetTaxInfoIndividualPortType.class);
        portConfigurer.configure(port, taxObjectsEndpoint);

        return port.req(req);
    }

    /** Сведения об уплате страховых налогов по ИНН (GetPayInsurTaxInfo). */
    @Cacheable(value = "mip-pay-insur", key = "#tin")
    public uz.katm.mip.ws.payinsur.Respond getPayInsurTaxInfo(String tin) {
        log.info("SOAP GetPayInsurTaxInfo: tin={}", tin);

        Getdatabytin req = new Getdatabytin();
        Getdatabytin.Message message = new Getdatabytin.Message();
        message.setTin(tin);
        req.setMessage(message);

        GetPayInsurTaxInfoPortType port =
                new GetPayInsurTaxInfoService().getPort(GetPayInsurTaxInfoPortType.class);
        portConfigurer.configure(port, payInsurEndpoint);

        return port.req(req);
    }

    /** Задолженности по налогам (GetDeptTaxInfo). */
    @Cacheable(value = "mip-tax-debt", key = "#tin")
    public Req getDeptTaxInfo(String tin) {
        return callDeptTaxInfo(tin, null);
    }

    /** Долги по налогам с признаком url=49 (GetDeptTaxInfo, getDebtTaxInfo в монолите). */
    @Cacheable(value = "mip-tax-debt-49", key = "#tin")
    public Req getDebtTaxInfo(String tin) {
        return callDeptTaxInfo(tin, "49");
    }

    private Req callDeptTaxInfo(String tin, String url) {
        log.info("SOAP GetDeptTaxInfo: tin={}, url={}", tin, url);

        uz.katm.mip.ws.debt.Objects objects = new uz.katm.mip.ws.debt.Objects();
        objects.setTin(tin);
        if (url != null) {
            objects.setUrl(url);
        }

        GetDeptTaxInfoPortType port =
                new GetDeptTaxInfoService().getPort(GetDeptTaxInfoPortType.class);
        portConfigurer.configure(port, debtEndpoint);

        return port.receive(objects);
    }

}
