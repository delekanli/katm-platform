package uz.katm.client.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ClientLegalDataDto {

    @JsonProperty("LE_ID")            private String leId;
    @JsonProperty("TIN")              private String tin;
    @JsonProperty("OKPO")             private String okpo;
    @JsonProperty("KFS_CD")           private String kfsCd;
    @JsonProperty("KFS_DESC_UZ")      private String kfsDescUz;
    @JsonProperty("KFS_DESC_RU")      private String kfsDescRu;
    @JsonProperty("KFS_DESC_EN")      private String kfsDescEn;
    @JsonProperty("KOPF_CD")          private String kopfCd;
    @JsonProperty("KOPF_DESC_UZ")     private String kopfDescUz;
    @JsonProperty("KOPF_DESC_RU")     private String kopfDescRu;
    @JsonProperty("KOPF_DESC_EN")     private String kopfDescEn;
    @JsonProperty("SOATO_CD")         private String soatoCd;
    @JsonProperty("SOATO_DESC_UZ")    private String soatoDescUz;
    @JsonProperty("SOATO_DESC_RU")    private String soatoDescRu;
    @JsonProperty("SOATO_DESC_EN")    private String soatoDescEn;
    @JsonProperty("OKONH_CD")         private String okontCd;
    @JsonProperty("OKONH_DESC_UZ")    private String okontDescUz;
    @JsonProperty("OKONH_DESC_RU")    private String okontDescRu;
    @JsonProperty("OKONH_DESC_EN")    private String okontDescEn;
    @JsonProperty("OKED_CD")          private String okedCd;
    @JsonProperty("OKED_DESC_UZ")     private String okedDescUz;
    @JsonProperty("OKED_DESC_RU")     private String okedDescRu;
    @JsonProperty("OKED_DESC_EN")     private String okedDescEn;
    @JsonProperty("SOOGU_CD")         private String sooguCd;
    @JsonProperty("SOOGU_DESC_UZ")    private String sooguDescUz;
    @JsonProperty("SOOGU_DESC_RU")    private String sooguDescRu;
    @JsonProperty("SOOGU_DESC_EN")    private String sooguDescEn;
    @JsonProperty("LE_NM_UZ")         private String leNameUz;
    @JsonProperty("ACRON_UZ")         private String acronUz;
    @JsonProperty("REG_DATE")         private String regDate;
    @JsonProperty("REG_NO")           private String regNo;
    @JsonProperty("LIQ_DATE")         private String liqDate;
    @JsonProperty("LIQ_NO")           private String liqNo;
    @JsonProperty("ZIP")              private String zip;
    @JsonProperty("ADDR")             private String address;
    @JsonProperty("HEAD_NM")          private String headName;
    @JsonProperty("HEAD_TIN")         private String headTin;
    @JsonProperty("PHONE")            private String phone;
    @JsonProperty("EMAIL")            private String email;
    @JsonProperty("AUTH_CAPITAL")     private String authCapital;
    @JsonProperty("AUTH_CAPITAL_US")  private String authCapitalUs;
    @JsonProperty("LE_TYP")           private String leType;
    @JsonProperty("SMALL_BIZ")        private String smallBiz;
    @JsonProperty("EMP_AVR")          private String empAvr;
    @JsonProperty("TAX_CD")           private String taxCd;
    @JsonProperty("LE_STATUS")        private String leStatus;
    @JsonProperty("FOUNDER_NM1")      private String founderNm1;
    @JsonProperty("FOUNDER_COUNTRY1") private String founderCountry1;
    @JsonProperty("FOUNDER_NM2")      private String founderNm2;
    @JsonProperty("FOUNDER_COUNTRY2") private String founderCountry2;
    @JsonProperty("FOUNDER_NM3")      private String founderNm3;
    @JsonProperty("FOUNDER_COUNTRY3") private String founderCountry3;
    @JsonProperty("FOUNDER_NM4")      private String founderNm4;
    @JsonProperty("FOUNDER_COUNTRY4") private String founderCountry4;
    @JsonProperty("FOUNDER_NM5")      private String founderNm5;
    @JsonProperty("FOUNDER_COUNTRY5") private String founderCountry5;
}
