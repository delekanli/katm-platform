package uz.katm.mip.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import uz.katm.mip.ws.MipPortConfigurer;
import uz.katm.mip.ws.legal.GetLegalEntityInfo;
import uz.katm.mip.ws.legal.GetLegalEntityInfoPortType;
import uz.katm.mip.ws.legal.GetLegalEntityInfoService;
import uz.katm.mip.ws.legal.LEGALENTITYINFORMATION;
import uz.katm.mip.ws.legalbr.GetLegalInfoBRPortType;
import uz.katm.mip.ws.legalbr.GetLegalInfoBRService;
import uz.katm.mip.ws.legalbr.Request;
import uz.katm.mip.ws.legalbr.Response;


/**
 * Сведения о юридических лицах через МИП (фаза 3 миграции):
 * информация о юрлице по ИНН (GetLegalEntityInfo) и реестр БР (GetLegalInfoBR).
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LegalMipService {

    private final MipPortConfigurer portConfigurer;

    @Value("${katm.mip.legal-endpoint:}")
    private String legalEndpoint;

    @Value("${katm.mip.legal-br-endpoint:}")
    private String legalBrEndpoint;

    /** Информация о юрлице по ИНН (GetLegalEntityInfo). */
    @Cacheable(value = "mip-legal", key = "#tin")
    public LEGALENTITYINFORMATION getLegalInformation(String tin, String hashKey) {
        log.info("SOAP GetLegalEntityInfo: tin={}", tin);

        GetLegalEntityInfo request = new GetLegalEntityInfo();
        GetLegalEntityInfo.Msg msg = new GetLegalEntityInfo.Msg();
        msg.setTIN(tin);
        msg.setLHASHKEY(hashKey);
        request.setMsg(msg);

        GetLegalEntityInfoPortType port =
                new GetLegalEntityInfoService().getPort(GetLegalEntityInfoPortType.class);
        portConfigurer.configure(port, legalEndpoint);

        return port.receive(request);
    }

    /** Сведения о юрлице из реестра БР (GetLegalInfoBR). */
    @Cacheable(value = "mip-legal-br", key = "#tin")
    public Response getLegalInfoBR(String tin) {
        log.info("SOAP GetLegalInfoBR: tin={}", tin);

        Request request = new Request();
        request.setTin(tin);

        GetLegalInfoBRPortType port =
                new GetLegalInfoBRService().getPort(GetLegalInfoBRPortType.class);
        portConfigurer.configure(port, legalBrEndpoint);

        return port.receive2(request);
    }

}
