package uz.katm.common.http;

import java.nio.charset.StandardCharsets;

public class HttpResponseInfo {

    private int statusCode;
    private byte[] body;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public String getBodyAsString() {
        return body != null && body.length > 0
                ? new String(body, StandardCharsets.UTF_8)
                : "";
    }

    public boolean isSuccess() {
        return statusCode >= 200 && statusCode < 300;
    }
}
