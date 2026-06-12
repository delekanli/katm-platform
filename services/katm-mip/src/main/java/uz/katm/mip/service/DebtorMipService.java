package uz.katm.mip.service;

import jakarta.xml.ws.BindingProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import uz.katm.mip.ws.MipPortConfigurer;
import uz.katm.mip.ws.debtor.DebtorIndividualArgument;
import uz.katm.mip.ws.debtor.DebtorIndividualResult;
import uz.katm.mip.ws.debtor.DebtorJuridicalArgument;
import uz.katm.mip.ws.debtor.DebtorJuridicalResult;
import uz.katm.mip.ws.debtor.DebtorWS;
import uz.katm.mip.ws.debtor.DebtorWSService;
import uz.katm.mip.ws.water.GetInfo;
import uz.katm.mip.ws.water.GetInfoResponse;
import uz.katm.mip.ws.water.WaterControlInfoServiceInfoPortType;
import uz.katm.mip.ws.water.WaterControlInfoServiceInfoService;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Сервисы МИБ: задолженности физлиц/юрлиц (DebtorWS) и водоконтроль (WaterControl) — фаза 5.
 * Учётные данные передаются из конфигурации: для DebtorWS — в теле запроса
 * (username/password), для WaterControl — транспортным Basic Auth.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DebtorMipService {

    private final MipPortConfigurer portConfigurer;

    @Value("${katm.mip.debtor-endpoint:}")
    private String debtorEndpoint;

    @Value("${katm.mip.debtor.username:}")
    private String debtorUsername;

    @Value("${katm.mip.debtor.password:}")
    private String debtorPassword;

    @Value("${katm.mip.water-endpoint:}")
    private String waterEndpoint;

    @Value("${katm.mip.water.username:}")
    private String waterUsername;

    @Value("${katm.mip.water.password:}")
    private String waterPassword;

    /** Задолженности физлица по паспорту (DebtorWS.getDebtorPhysical). */
    @Cacheable(value = "mip-debtor-individual", key = "#series + '-' + #number")
    public DebtorIndividualResult getDebtorIndividualResult(String series, String number) {
        log.info("SOAP DebtorWS.getDebtorPhysical: series={}, number={}", series, number);

        DebtorIndividualArgument argument = new DebtorIndividualArgument();
        argument.setPassportSn(series);
        argument.setPassportNumber(number);
        argument.setUsername(debtorUsername);
        argument.setPassword(debtorPassword);

        DebtorWS port = new DebtorWSService().getDebtorWSPort();
        portConfigurer.configure(port, debtorEndpoint);

        return port.getDebtorPhysical(argument);
    }

    /** Задолженности юрлица по ИНН (DebtorWS.getDebtorJuridical). */
    @Cacheable(value = "mip-debtor-juridical", key = "#tin")
    public DebtorJuridicalResult getDebtorJuridicalResult(String tin) {
        log.info("SOAP DebtorWS.getDebtorJuridical: tin={}", tin);

        DebtorJuridicalArgument argument = new DebtorJuridicalArgument();
        argument.setInn(tin);
        argument.setUsername(debtorUsername);
        argument.setPassword(debtorPassword);

        DebtorWS port = new DebtorWSService().getDebtorWSPort();
        portConfigurer.configure(port, debtorEndpoint);

        return port.getDebtorJuridical(argument);
    }

    /** Сведения водоконтроля по ПИНФЛ или ИНН (WaterControl.receive1). */
    @Cacheable(value = "mip-water", key = "(#pin != null && !#pin.isBlank()) ? #pin : #tin")
    public GetInfoResponse getWaterControlInfo(String pin, String tin) {
        log.info("SOAP WaterControl: pin={}, tin={}", pin, tin);

        GetInfo getInfo = new GetInfo();
        getInfo.setServiceType("03");
        getInfo.setId(ThreadLocalRandom.current().nextInt(Integer.MAX_VALUE));
        if (pin != null && !pin.isBlank()) {
            getInfo.setReqType("PIN");
            getInfo.setReqValue(pin);
        } else {
            getInfo.setReqType("TIN");
            getInfo.setReqValue(tin);
        }

        WaterControlInfoServiceInfoPortType port =
                new WaterControlInfoServiceInfoService().getWaterControlInfoServiceInfoSOAP11BindingPort();
        BindingProvider provider = (BindingProvider) port;
        portConfigurer.configure(port, waterEndpoint);
        provider.getRequestContext().put(BindingProvider.USERNAME_PROPERTY, waterUsername);
        provider.getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, waterPassword);

        return port.receive1(getInfo);
    }

}
