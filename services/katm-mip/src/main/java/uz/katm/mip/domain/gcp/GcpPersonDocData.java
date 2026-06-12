package uz.katm.mip.domain.gcp;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GcpPersonDocData {
    @JsonProperty("document")
    private String document;

    @JsonProperty("type")
    private String type;

    @JsonProperty("docgiveplace")
    private String docGivePlace;

    @JsonProperty("docgiveplaceid")
    private String docGivePlaceId;

    @JsonProperty("datebegin")
    private String dateBegin;

    @JsonProperty("dateend")
    private String dateEnd;

    @JsonProperty("status")
    private String status;

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDocGivePlace() {
        return docGivePlace;
    }

    public void setDocGivePlace(String docGivePlace) {
        this.docGivePlace = docGivePlace;
    }

    public String getDocGivePlaceId() {
        return docGivePlaceId;
    }

    public void setDocGivePlaceId(String docGivePlaceId) {
        this.docGivePlaceId = docGivePlaceId;
    }

    public String getDateBegin() {
        return dateBegin;
    }

    public void setDateBegin(String dateBegin) {
        this.dateBegin = dateBegin;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
