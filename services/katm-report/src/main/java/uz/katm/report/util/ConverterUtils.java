package uz.katm.report.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/** Утилиты форматирования дат для FCBK-блоков (перенос gov.uz.katm.core.util.ConverterUtils). */
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
}
