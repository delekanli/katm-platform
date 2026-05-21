package uz.katm.client.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigInteger;

@Data
public class ClientPersonalDataDto {

    @JsonProperty("pinppAddressResult")
    private PinppAddressResult pinppAddressResult;

    @Data
    public static class PinppAddressResult {
        private BigInteger pResult;
        @JsonProperty("AnswereMessage")
        private String answereMessage;
        @JsonProperty("AnswereComment")
        private String answereComment;
        @JsonProperty("AnswereId")
        private BigInteger answereId;
        @JsonProperty("Data")
        private String data;
        private String pComment;
    }
}
