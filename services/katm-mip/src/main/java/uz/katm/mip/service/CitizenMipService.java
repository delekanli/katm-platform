package uz.katm.mip.service;

import jakarta.xml.ws.BindingProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import uz.katm.mip.ws.citizen.GetCitizenInfoServiceNewPortType;
import uz.katm.mip.ws.citizen.GetCitizenInfoServiceNewService;
import uz.katm.mip.ws.citizen.PinppAddress;
import uz.katm.mip.ws.citizen.PinppAddressResponse;
import uz.katm.mip.ws.doc.CEPRequest;
import uz.katm.mip.ws.doc.CEPResponse;
import uz.katm.mip.ws.doc.PersonDocInfoServicePortType;
import uz.katm.mip.ws.doc.PersonDocInfoServiceService;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CitizenMipService {

    @Value("${katm.mip.connect-timeout:10000}")
    private int connectTimeout;

    @Value("${katm.mip.read-timeout:30000}")
    private int readTimeout;

    @Value("${katm.mip.citizen-endpoint:}")
    private String citizenEndpoint;

    @Value("${katm.mip.doc-endpoint:}")
    private String docEndpoint;

    @Cacheable(value = "mip-citizen", key = "#pin")
    public PinppAddressResponse getCitizenInfo(String pin) {
        log.info("SOAP GetCitizenInfoServiceNew: pin={}", pin);

        PinppAddress request = new PinppAddress();
        request.setPinpp(pin);

        GetCitizenInfoServiceNewPortType port =
                new GetCitizenInfoServiceNewService().getPort(GetCitizenInfoServiceNewPortType.class);
        configurePort(port, citizenEndpoint);

        return port.receive2(request);
    }

    @Cacheable(value = "mip-doc", key = "#pin")
    public CEPResponse getDocuments(String pin) {
        log.info("SOAP PersonDocInfoService: pin={}", pin);

        CEPRequest request = new CEPRequest();
        request.setData(buildDocRequestData(pin));

        PersonDocInfoServicePortType port =
                new PersonDocInfoServiceService().getPort(PersonDocInfoServicePortType.class);
        configurePort(port, docEndpoint);

        return port.soapMsgReceive(request);
    }

    public Map<String, PinppAddressResponse> getCitizensBatch(List<String> pins) {
        log.info("Пакетный запрос CitizenInfo: {} ПИНФЛ", pins.size());
        return pins.stream()
                .collect(Collectors.toMap(pin -> pin, this::getCitizenInfo));
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

    private String buildDocRequestData(String pin) {
        String safe = pin.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
        return "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
               "<DataCEPRequest>" +
               "<pinpp>" + safe + "</pinpp>" +
               "<langId>1</langId>" +
               "</DataCEPRequest>";
    }
}
