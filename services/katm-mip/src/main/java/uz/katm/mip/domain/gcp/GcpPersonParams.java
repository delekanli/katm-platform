package uz.katm.mip.domain.gcp;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class GcpPersonParams {

    @JsonProperty("transaction_id")
    private Long transactionId = System.currentTimeMillis();

    @JsonProperty("is_consent")
    private String isConsent = "Y";

    @JsonProperty("sender_pinfl")
    private String senderPin;

    @JsonProperty("langId")
    private Integer langId;

    @JsonProperty("document")
    private String document;

    @JsonProperty("pinpp")
    private String subjectPin;

    @JsonProperty("is_photo")
    private String isPhoto = "N";

    @JsonProperty("Sender")
    private String sender = "P";

    public GcpPersonParams(String document, String subjectPin, Integer langId) {
        this.document = document;
        this.subjectPin = subjectPin;
        this.senderPin = subjectPin;
        this.langId = langId;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public String getIsConsent() {
        return isConsent;
    }

    public void setIsConsent(String isConsent) {
        this.isConsent = isConsent;
    }

    public String getSenderPin() {
        return senderPin;
    }

    public void setSenderPin(String senderPin) {
        this.senderPin = senderPin;
    }

    public Integer getLangId() {
        return langId;
    }

    public void setLangId(Integer langId) {
        this.langId = langId;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getSubjectPin() {
        return subjectPin;
    }

    public void setSubjectPin(String subjectPin) {
        this.subjectPin = subjectPin;
    }

    public String getIsPhoto() {
        return isPhoto;
    }

    public void setIsPhoto(String isPhoto) {
        this.isPhoto = isPhoto;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
