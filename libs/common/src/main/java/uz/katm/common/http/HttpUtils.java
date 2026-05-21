package uz.katm.common.http;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public final class HttpUtils {

    private HttpUtils() {}

    public static Map<String, String> basicAuthHeaders(String username, String password) {
        String encoded = Base64.getEncoder()
                .encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8));
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Basic " + encoded);
        return headers;
    }

    public static Map<String, String> bearerAuthHeaders(String token) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + token);
        return headers;
    }

    public static Map<String, String> emptyHeaders() {
        return new HashMap<>();
    }

    @SafeVarargs
    public static Map<String, String> mergeHeaders(Map<String, String>... maps) {
        Map<String, String> result = new HashMap<>();
        for (Map<String, String> m : maps) {
            if (m != null) {
                result.putAll(m);
            }
        }
        return result;
    }
}
