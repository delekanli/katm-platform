package uz.katm.client.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.List;

/**
 * Ответ сервиса Халк банка по отчислениям ИНПС (один счёт субъекта). Перенос
 * gov.uz.katm.core.client.ClientInpsDataDto. Имена полей входного JSON сохранены из монолита.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class InpsData {

    @JsonProperty("pass_sn")
    private String passSn;
    @JsonProperty("pass_num")
    private String passNum;
    @JsonProperty("name")
    private String name;
    @JsonProperty("resident")
    private String resident;
    @JsonProperty("birth_date")
    private Date birthDate;
    @JsonProperty("birth_place")
    private String birthPlace;
    @JsonProperty("gender")
    private String gender;
    @JsonProperty("adress")
    private String address;
    @JsonProperty("inps")
    private String inps;
    @JsonProperty("result_code")
    private Integer resultCode;
    @JsonProperty("result_message")
    private String resultMessage;
    @JsonProperty("total_remain")
    private String totalRemain;
    @JsonProperty("forced_remain")
    private String forcedRemain;
    @JsonProperty("voluntary_remain")
    private String voluntaryRemain;
    @JsonProperty("invoices")
    private List<InpsDetail> invoices;

    public String getPassSn() {
        return passSn;
    }

    public String getPassNum() {
        return passNum;
    }

    public String getName() {
        return name;
    }

    public String getResident() {
        return resident;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public String getGender() {
        return gender;
    }

    public String getAddress() {
        return address;
    }

    public String getInps() {
        return inps;
    }

    public Integer getResultCode() {
        return resultCode;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public String getTotalRemain() {
        return totalRemain;
    }

    public String getForcedRemain() {
        return forcedRemain;
    }

    public String getVoluntaryRemain() {
        return voluntaryRemain;
    }

    public List<InpsDetail> getInvoices() {
        return invoices;
    }
}
