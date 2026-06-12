package uz.katm.report.domain.fcbk;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseBlock {
    @JsonProperty("uniqueID")
    private String uniqueID;

    @JsonProperty("dateOfBirth")
    private String dateOfBirth;

    @JsonProperty("dateOfRequest")
    private String dateOfRequest;

    @JsonProperty("errorCode")
    private Integer errorCode;

    @JsonProperty("exclusionCode")
    private Integer exclusionCode;

    @JsonProperty("score")
    private Double score;

    @JsonProperty("badRate")
    private String badRate;

    @JsonProperty("reasonCode")
    private String[] reasonCode;

    public String getUniqueID() {
        return uniqueID;
    }

    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getDateOfRequest() {
        return dateOfRequest;
    }

    public void setDateOfRequest(String dateOfRequest) {
        this.dateOfRequest = dateOfRequest;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public Integer getExclusionCode() {
        return exclusionCode;
    }

    public void setExclusionCode(Integer exclusionCode) {
        this.exclusionCode = exclusionCode;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public String getBadRate() {
        return badRate;
    }

    public void setBadRate(String badRate) {
        this.badRate = badRate;
    }

    public String[] getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(String[] reasonCode) {
        this.reasonCode = reasonCode;
    }
}
