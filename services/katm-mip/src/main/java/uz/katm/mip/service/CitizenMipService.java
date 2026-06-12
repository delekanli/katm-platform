package uz.katm.mip.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import uz.katm.mip.ws.MipPortConfigurer;
import uz.katm.mip.ws.batch.GetCitizensInfoServicePortType;
import uz.katm.mip.ws.batch.GetCitizensInfoServiceService;
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
@RequiredArgsConstructor
public class CitizenMipService {

    private final MipPortConfigurer portConfigurer;

    @Value("${katm.mip.citizen-endpoint:}")
    private String citizenEndpoint;

    @Value("${katm.mip.doc-endpoint:}")
    private String docEndpoint;

    @Value("${katm.mip.citizens-batch-endpoint:}")
    private String citizensBatchEndpoint;

    @Cacheable(value = "mip-citizen", key = "#pin")
    public PinppAddressResponse getCitizenInfo(String pin) {
        log.info("SOAP GetCitizenInfoServiceNew: pin={}", pin);

        PinppAddress request = new PinppAddress();
        request.setPinpp(pin);

        GetCitizenInfoServiceNewPortType port =
                new GetCitizenInfoServiceNewService().getPort(GetCitizenInfoServiceNewPortType.class);
        portConfigurer.configure(port, citizenEndpoint);

        return port.receive2(request);
    }

    @Cacheable(value = "mip-doc", key = "#pin")
    public CEPResponse getDocuments(String pin) {
        log.info("SOAP PersonDocInfoService: pin={}", pin);

        CEPRequest request = new CEPRequest();
        request.setData(buildDocRequestData(pin));

        PersonDocInfoServicePortType port =
                new PersonDocInfoServiceService().getPort(PersonDocInfoServicePortType.class);
        portConfigurer.configure(port, docEndpoint);

        return port.soapMsgReceive(request);
    }

    public Map<String, PinppAddressResponse> getCitizensBatch(List<String> pins) {
        log.info("Пакетный запрос CitizenInfo: {} ПИНФЛ", pins.size());
        return pins.stream()
                .collect(Collectors.toMap(pin -> pin, this::getCitizenInfo));
    }

    /** Адрес ПИНФЛ через старый сервис GetCitizensInfoService (фаза 6, legacy). */
    @Cacheable(value = "mip-citizen-legacy", key = "#pin + '-' + #guid")
    public uz.katm.mip.ws.batch.PinppAddressResponse getPeAddress(String pin, String guid) {
        log.info("SOAP GetCitizensInfoService: pin={}, guid={}", pin, guid);

        uz.katm.mip.ws.batch.PinppAddress request = new uz.katm.mip.ws.batch.PinppAddress();
        request.setPinpp(pin);
        request.setGuid(guid);

        GetCitizensInfoServicePortType port =
                new GetCitizensInfoServiceService().getPort(GetCitizensInfoServicePortType.class);
        portConfigurer.configure(port, citizensBatchEndpoint);

        return port.rEQ(request);
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
