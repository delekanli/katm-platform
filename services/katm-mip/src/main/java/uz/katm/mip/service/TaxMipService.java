package uz.katm.mip.service;

import jakarta.xml.ws.BindingProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import uz.katm.mip.ws.tax.GetTaxInfobyPinPortType;
import uz.katm.mip.ws.tax.GetTaxInfobyPinReq;
import uz.katm.mip.ws.tax.GetTaxInfobyPinRes;
import uz.katm.mip.ws.tax.GetTaxInfobyPinService;
import uz.katm.mip.ws.tin.FormData;
import uz.katm.mip.ws.tin.GetTinbyPas;
import uz.katm.mip.ws.tin.GetTinbyPasNumNewPortType;
import uz.katm.mip.ws.tin.GetTinbyPasNumNewService;

import java.util.Map;

@Slf4j
@Service
public class TaxMipService {

    @Value("${katm.mip.connect-timeout:10000}")
    private int connectTimeout;

    @Value("${katm.mip.read-timeout:30000}")
    private int readTimeout;

    @Value("${katm.mip.ws-id:KATM}")
    private String wsId;

    @Value("${katm.mip.tax-endpoint:}")
    private String taxEndpoint;

    @Value("${katm.mip.tin-endpoint:}")
    private String tinEndpoint;

    @Value("${katm.mip.debt-endpoint:}")
    private String debtEndpoint;

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
        configurePort(port, taxEndpoint);

        return port.receive1(req);
    }

    public Object getTaxInfoByTin(String tin) {
        log.warn("getTaxInfoByTin: SOAP-сервис GetTaxInfobyTin не реализован (нет WSDL), tin={}", tin);
        throw new UnsupportedOperationException("GetTaxInfobyTin SOAP-сервис не реализован");
    }

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
        configurePort(port, tinEndpoint);

        return port.rEQ(req);
    }

    public Object getDeptTaxInfo(String tin) {
        log.warn("getDeptTaxInfo: SOAP-сервис GetDeptTaxInfo не реализован (нет WSDL), tin={}", tin);
        throw new UnsupportedOperationException("GetDeptTaxInfo SOAP-сервис не реализован");
    }

    private void configurePort(Object port, String endpointUrl) {
        BindingProvider provider = (BindingProvider) port;
        Map<String, Object> ctx = provider.getRequestContext();

        if (endpointUrl != null && !endpointUrl.isBlank()) {
            ctx.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpointUrl);
        }
        ctx.put("com.sun.xml.ws.connect.timeout", connectTimeout);
        ctx.put("com.sun.xml.ws.request.timeout", readTimeout);
        ctx.put("com.sun.xml.internal.ws.connect.timeout", connectTimeout);
        ctx.put("com.sun.xml.internal.ws.request.timeout", readTimeout);
    }
}
