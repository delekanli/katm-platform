package uz.katm.mip.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import uz.katm.mip.ws.MipPortConfigurer;
import uz.katm.mip.ws.inps.InpsBalancePortType;
import uz.katm.mip.ws.inps.InpsBalanceService;
import uz.katm.mip.ws.inps.Request;
import uz.katm.mip.ws.inps.Response;
import uz.katm.mip.ws.pensionassign.GetPensionAssgnServicePortType;
import uz.katm.mip.ws.pensionassign.GetPensionAssgnServiceService;
import uz.katm.mip.ws.pensionassign.PensionAssingRequest;
import uz.katm.mip.ws.pensionreq.GetRequestPensServicePortType;
import uz.katm.mip.ws.pensionreq.GetRequestPensServiceService;
import uz.katm.mip.ws.pensionreq.RequestPensRequest;
import uz.katm.mip.ws.pensionsize.GetSizePensServicePortType;
import uz.katm.mip.ws.pensionsize.GetSizePensServiceService;
import uz.katm.mip.ws.pensionsize.SizePensRequest;


/**
 * Пенсионные сервисы ИНПС через МИП (фаза 4 миграции):
 * баланс ИНПС, назначение/размер/запрос пенсии.
 * Запросы трёх пенсионных сервисов несут один XML (datacepresponse) — см. {@link #pensionData}.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PensionMipService {

    private final MipPortConfigurer portConfigurer;

    @Value("${katm.mip.inps-endpoint:}")
    private String inpsEndpoint;

    @Value("${katm.mip.pension-assign-endpoint:}")
    private String pensionAssignEndpoint;

    @Value("${katm.mip.pension-size-endpoint:}")
    private String pensionSizeEndpoint;

    @Value("${katm.mip.pension-req-endpoint:}")
    private String pensionReqEndpoint;

    /** Баланс ИНПС (InpsBalance). */
    @Cacheable(value = "mip-inps", key = "#pin + '-' + #tin")
    public Response getInpsBalance(String tin, String pin, String docSeries, String docNumber) {
        log.info("SOAP InpsBalance: pin={}, tin={}", pin, tin);

        Request request = new Request();
        request.setDocSeria(docSeries);
        request.setDocNum(docNumber);
        request.setInps(pin);
        request.setInn(tin);

        InpsBalancePortType port =
                new InpsBalanceService().getPort(InpsBalancePortType.class);
        portConfigurer.configure(port, inpsEndpoint);

        return port.balanceRecieve(request);
    }

    /** Назначение пенсии (GetPensionAssgnService). */
    @Cacheable(value = "mip-pension-assign", key = "#pin + '-' + #type")
    public uz.katm.mip.ws.pensionassign.GetLegalEntityInfo getPensionAssignService(String pin, int type) {
        log.info("SOAP GetPensionAssgnService: pin={}, type={}", pin, type);

        PensionAssingRequest request = new PensionAssingRequest();
        request.setData(pensionData(pin, null, null, type));

        GetPensionAssgnServicePortType port =
                new GetPensionAssgnServiceService().getPort(GetPensionAssgnServicePortType.class);
        portConfigurer.configure(port, pensionAssignEndpoint);

        return port.rEQ(request);
    }

    /** Размер пенсии за период (GetSizePensService). */
    @Cacheable(value = "mip-pension-size", key = "#pin + '-' + #beginPeriod + '-' + #endPeriod + '-' + #type")
    public uz.katm.mip.ws.pensionsize.GetLegalEntityInfo getPensionSizeService(
            String pin, String beginPeriod, String endPeriod, int type) {
        log.info("SOAP GetSizePensService: pin={}, period={}..{}, type={}", pin, beginPeriod, endPeriod, type);

        SizePensRequest request = new SizePensRequest();
        request.setData(pensionData(pin, beginPeriod, endPeriod, type));

        GetSizePensServicePortType port =
                new GetSizePensServiceService().getPort(GetSizePensServicePortType.class);
        portConfigurer.configure(port, pensionSizeEndpoint);

        return port.rEQ(request);
    }

    /** Запрос пенсии (GetRequestPensService). */
    @Cacheable(value = "mip-pension-req", key = "#pin + '-' + #type")
    public uz.katm.mip.ws.pensionreq.GetLegalEntityInfo getPensionService(String pin, int type) {
        log.info("SOAP GetRequestPensService: pin={}, type={}", pin, type);

        RequestPensRequest request = new RequestPensRequest();
        request.setData(pensionData(pin, null, null, type));

        GetRequestPensServicePortType port =
                new GetRequestPensServiceService().getPort(GetRequestPensServicePortType.class);
        portConfigurer.configure(port, pensionReqEndpoint);

        return port.rEQ(request);
    }

    private String pensionData(String pin, String beginPeriod, String endPeriod, int type) {
        StringBuilder row = new StringBuilder()
                .append("<type>").append(type).append("</type>")
                .append("<lang>RU</lang>")
                .append("<pinpp>").append(pin).append("</pinpp>");
        if (beginPeriod != null && !beginPeriod.isBlank() && endPeriod != null && !endPeriod.isBlank()) {
            row.append("<begin_period>").append(beginPeriod).append("</begin_period>")
               .append("<end_period>").append(endPeriod).append("</end_period>");
        }
        return "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
               "<datacepresponse><row>" + row + "</row></datacepresponse>";
    }

}
