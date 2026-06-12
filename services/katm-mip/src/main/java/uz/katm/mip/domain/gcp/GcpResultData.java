package uz.katm.mip.domain.gcp;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class GcpResultData {
    @JsonProperty("code")
    private String code;

    @JsonProperty("message")
    private String message;

    @JsonProperty("description")
    private String description;

    @JsonProperty("result")
    private String result;

    @JsonProperty("comments")
    private String comments;

    @JsonProperty("data")
    private List<GcpPersonData> data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public List<GcpPersonData> getData() {
        return data;
    }

    public void setData(List<GcpPersonData> data) {
        this.data = data;
    }
}
