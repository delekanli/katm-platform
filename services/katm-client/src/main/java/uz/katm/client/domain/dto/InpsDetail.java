package uz.katm.client.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * Деталь отчисления ИНПС (одна организация-плательщик). Перенос
 * gov.uz.katm.core.client.ClientInpsDetailsDto. Имена полей входного JSON Халк банка сохранены.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class InpsDetail {

    @JsonProperty("inn")
    private String inn;
    @JsonProperty("orgName")
    private String orgName;
    @JsonProperty("orgAddress")
    private String orgAddress;
    @JsonProperty("orgType")
    private String orgType;
    @JsonProperty("period")
    private Date period;
    @JsonProperty("sendDate")
    private Date sendDate;
    @JsonProperty("operDate")
    private Date operDate;
    @JsonProperty("totalInvoice")
    private java.math.BigDecimal totalInvoices;

    public String getInn() {
        return inn;
    }

    public String getOrgName() {
        return orgName;
    }

    public String getOrgAddress() {
        return orgAddress;
    }

    public String getOrgType() {
        return orgType;
    }

    public Date getPeriod() {
        return period;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public Date getOperDate() {
        return operDate;
    }

    public java.math.BigDecimal getTotalInvoices() {
        return totalInvoices;
    }
}
