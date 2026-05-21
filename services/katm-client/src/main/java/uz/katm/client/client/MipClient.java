package uz.katm.client.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import uz.katm.client.domain.dto.ClientLegalDataDto;
import uz.katm.client.domain.dto.ClientPersonalDataDto;

@Slf4j
@Component
public class MipClient {

    private final RestClient restClient;

    public MipClient(@Value("${katm.mip-service.url}") String baseUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public ClientPersonalDataDto getCitizenInfo(String pin) {
        return restClient.get()
                .uri("/api/v1/mip/citizens/{pin}", pin)
                .retrieve()
                .body(ClientPersonalDataDto.class);
    }

    public ClientLegalDataDto getLegalInfo(String tin) {
        return restClient.get()
                .uri("/api/v1/mip/legal/{tin}", tin)
                .retrieve()
                .body(ClientLegalDataDto.class);
    }

    public Object getInpsBalance(String pin) {
        return restClient.get()
                .uri("/api/v1/mip/inps/{pin}/balance", pin)
                .retrieve()
                .body(Object.class);
    }

    public Object getTaxInfoByPin(String pin) {
        return restClient.get()
                .uri("/api/v1/mip/tax/pin/{pin}", pin)
                .retrieve()
                .body(Object.class);
    }
}
