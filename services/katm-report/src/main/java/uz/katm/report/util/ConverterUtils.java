package uz.katm.report.util;

import org.json.JSONTokener;
import org.json.XML;

import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/** Утилиты форматирования дат и конвертации XML/JSON (перенос gov.uz.katm.core.util.ConverterUtils). */
public final class ConverterUtils {

    public static final String FORMAT_YYYY_MM_DD = "yyyy-MM-dd";

    private ConverterUtils() {}

    public static String dateToString(Date date, String format) {
        if (date == null) {
            return null;
        }
        DateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    /**
     * Преобразует XML-документ отчёта в JSON (перенос ConverterUtils.jsonFromXml монолита).
     * Сохраняет порядок (keepStrings=true), снимает внешнюю обёртку через JSONTokener.nextValue().
     */
    public static byte[] jsonFromXml(String xml) {
        var xmlJson = XML.toJSONObject(xml, true);
        if (xmlJson != null) {
            String reportJson = xmlJson.toString(4);
            if (reportJson != null && !reportJson.isEmpty()) {
                String formatted = new JSONTokener(reportJson).nextValue().toString();
                return formatted.getBytes(StandardCharsets.UTF_8);
            }
        }
        return null;
    }
}
