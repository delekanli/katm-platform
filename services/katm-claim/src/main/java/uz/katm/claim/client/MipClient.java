package uz.katm.claim.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.math.BigInteger;

@Slf4j
@Component
public class MipClient {

    private final RestClient restClient;

    public MipClient(@Value("${katm.mip-service.url}") String baseUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public CitizenData getCitizenInfo(String pin) {
        log.info("MIP: requesting citizen data for pin={}", pin);
        try {
            return restClient.get()
                    .uri("/api/v1/mip/citizens/{pin}", pin)
                    .retrieve()
                    .body(CitizenData.class);
        } catch (Exception e) {
            log.error("MIP citizen request failed for pin={}: {}", pin, e.getMessage());
            return null;
        }
    }

    public LegalData getLegalInfo(String tin) {
        log.info("MIP: requesting legal data for tin={}", tin);
        try {
            return restClient.get()
                    .uri("/api/v1/mip/legal/{tin}", tin)
                    .retrieve()
                    .body(LegalData.class);
        } catch (Exception e) {
            log.error("MIP legal request failed for tin={}: {}", tin, e.getMessage());
            return null;
        }
    }

    @Data
    public static class CitizenData {
        @JsonProperty("pinppAddressResult")
        private PinppAddressResult pinppAddressResult;

        @Data
        public static class PinppAddressResult {
            private BigInteger pResult;
            @JsonProperty("AnswereMessage") private String answereMessage;
            @JsonProperty("AnswereComment") private String answereComment;
            @JsonProperty("AnswereId")      private BigInteger answereId;
            @JsonProperty("Data")           private String data;
            private String pComment;
        }
    }

    @Data
    public static class LegalData {
        @JsonProperty("LE_ID")    private String leId;
        @JsonProperty("TIN")      private String tin;
        @JsonProperty("OKPO")     private String okpo;
        @JsonProperty("LE_NM_UZ") private String leNameUz;
        @JsonProperty("LE_TYP")   private String leType;
        @JsonProperty("LE_STATUS") private String leStatus;
    }
}
