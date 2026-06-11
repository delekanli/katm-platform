package uz.katm.mip.domain.gcp;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class GcpPersonData {
    @JsonProperty("documents")
    private List<GcpPersonDocData> documents;

    @JsonProperty("current_document")
    private String currentDocument;

    @JsonProperty("current_pinpp")
    private String currentPinpp;

    @JsonProperty("surnamelat")
    private String surnameLat;

    @JsonProperty("namelat")
    private String nameLat;

    @JsonProperty("patronymlat")
    private String patronymLat;

    @JsonProperty("surnamecyr")
    private String surnameCyr;

    @JsonProperty("namecyr")
    private String nameCyr;

    @JsonProperty("patronymcyr")
    private String patronymCyr;

    @JsonProperty("engsurname")
    private String engSurname;

    @JsonProperty("engname")
    private String engName;

    @JsonProperty("birth_date")
    private String birthDate;

    @JsonProperty("birthplace")
    private String birthPlace;

    @JsonProperty("birthcountry")
    private String birthCountry;

    @JsonProperty("birthcountryid")
    private String birthCountryId;

    @JsonProperty("livestatus")
    private Integer liveStatus;

    @JsonProperty("nationality")
    private String nationality;

    @JsonProperty("nationalityid")
    private String nationalityId;

    @JsonProperty("citizenship")
    private String citizenship;

    @JsonProperty("citizenshipid")
    private String citizenshipId;

    @JsonProperty("sex")
    private Integer sex;

    @JsonProperty("photo")
    private String photo;

    public List<GcpPersonDocData> getDocuments() {
        return documents;
    }

    public void setDocuments(List<GcpPersonDocData> documents) {
        this.documents = documents;
    }

    public String getCurrentDocument() {
        return currentDocument;
    }

    public void setCurrentDocument(String currentDocument) {
        this.currentDocument = currentDocument;
    }

    public String getCurrentPinpp() {
        return currentPinpp;
    }

    public void setCurrentPinpp(String currentPinpp) {
        this.currentPinpp = currentPinpp;
    }

    public String getSurnameLat() {
        return surnameLat;
    }

    public void setSurnameLat(String surnameLat) {
        this.surnameLat = surnameLat;
    }

    public String getNameLat() {
        return nameLat;
    }

    public void setNameLat(String nameLat) {
        this.nameLat = nameLat;
    }

    public String getPatronymLat() {
        return patronymLat;
    }

    public void setPatronymLat(String patronymLat) {
        this.patronymLat = patronymLat;
    }

    public String getSurnameCyr() {
        return surnameCyr;
    }

    public void setSurnameCyr(String surnameCyr) {
        this.surnameCyr = surnameCyr;
    }

    public String getNameCyr() {
        return nameCyr;
    }

    public void setNameCyr(String nameCyr) {
        this.nameCyr = nameCyr;
    }

    public String getPatronymCyr() {
        return patronymCyr;
    }

    public void setPatronymCyr(String patronymCyr) {
        this.patronymCyr = patronymCyr;
    }

    public String getEngSurname() {
        return engSurname;
    }

    public void setEngSurname(String engSurname) {
        this.engSurname = engSurname;
    }

    public String getEngName() {
        return engName;
    }

    public void setEngName(String engName) {
        this.engName = engName;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public String getBirthCountry() {
        return birthCountry;
    }

    public void setBirthCountry(String birthCountry) {
        this.birthCountry = birthCountry;
    }

    public String getBirthCountryId() {
        return birthCountryId;
    }

    public void setBirthCountryId(String birthCountryId) {
        this.birthCountryId = birthCountryId;
    }

    public Integer getLiveStatus() {
        return liveStatus;
    }

    public void setLiveStatus(Integer liveStatus) {
        this.liveStatus = liveStatus;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getNationalityId() {
        return nationalityId;
    }

    public void setNationalityId(String nationalityId) {
        this.nationalityId = nationalityId;
    }

    public String getCitizenship() {
        return citizenship;
    }

    public void setCitizenship(String citizenship) {
        this.citizenship = citizenship;
    }

    public String getCitizenshipId() {
        return citizenshipId;
    }

    public void setCitizenshipId(String citizenshipId) {
        this.citizenshipId = citizenshipId;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
