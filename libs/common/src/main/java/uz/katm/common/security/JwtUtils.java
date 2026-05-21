package uz.katm.common.security;

import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Map;

public final class JwtUtils {

    private JwtUtils() {}

    public static String head(Jwt jwt) {
        return attr(jwt, "head");
    }

    public static String code(Jwt jwt) {
        return attr(jwt, "code");
    }

    private static String attr(Jwt jwt, String key) {
        if (jwt == null) return null;
        Map<String, Object> attrs = jwt.getClaimAsMap("attributes");
        return attrs != null ? (String) attrs.get(key) : null;
    }
}
