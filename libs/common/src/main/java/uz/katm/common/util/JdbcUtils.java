package uz.katm.common.util;

import java.sql.Clob;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Map;

public final class JdbcUtils {
    private JdbcUtils() {
    }

    /**
     * Читает CLOB из карты выходных параметров и возвращает содержимое как байты UTF-8.
     * Если ключ отсутствует или значение {@code null} — возвращает {@code null}.
     */
    public static byte[] readClob(Map<String, Object> out, String key) {
        if (key == null || out.get(key) == null) return null;
        try {
            Clob clob = (Clob) out.get(key);
            return clob.getSubString(1, (int) clob.length()).getBytes(java.nio.charset.StandardCharsets.UTF_8);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Конвертирует {@link Date SQL Date} в {@link LocalDate}.
     * Если дата {@code null} — возвращает {@code null}.
     */
    public static LocalDate toLocalDate(Date date) {
        return date != null ? date.toLocalDate() : null;
    }
}
