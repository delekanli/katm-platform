package uz.katm.report.util;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.stereotype.Component;
import uz.katm.report.domain.record.ReportFormat;

import java.nio.charset.StandardCharsets;

/**
 * Конвертация тела отчёта в запрошенный формат. Перенос логики из CreditReportServiceImpl
 * (getCreditReport/getCreditReportStatus) и InternalServiceImpl.generateReport.
 * Base64-кодирование байтов выполняет Jackson при сериализации (поле byte[] → base64), как
 * монолитный reportBase64.
 */
@Slf4j
@Component
public class ReportFormatConverter {

    private static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"WINDOWS-1251\"?>";

    /**
     * Тело отчёта из процедуры — это XML-документ. Перенос ветки getCreditReport:
     * XML → как есть; JSON → ConverterUtils.jsonFromXml. HTML-рендеринг не перенесён (отдельный гэп) —
     * возвращаем XML как есть.
     */
    public byte[] convertXmlDoc(byte[] xmlDoc, ReportFormat format) {
        if (xmlDoc == null) {
            return null;
        }
        if (format == ReportFormat.JSON) {
            return ConverterUtils.jsonFromXml(new String(xmlDoc, StandardCharsets.UTF_8));
        }
        if (format == ReportFormat.HTML) {
            log.warn("HTML-рендеринг отчёта пока не перенесён — отдаём XML как есть");
        }
        return xmlDoc;
    }

    /**
     * Тело отчёта — это JSON-строка (UzCard / системные отчёты). Перенос generateReport:
     * оборачивается в {@code {"report": ...}}, затем JSON → как есть; XML → XML.toString с
     * WINDOWS-1251 заголовком.
     */
    public byte[] wrapJsonReport(String reportJson, ReportFormat format) {
        String wrapped = "{\"report\":" + reportJson + "}";
        if (format == ReportFormat.JSON) {
            return wrapped.getBytes(StandardCharsets.UTF_8);
        }
        // XML и HTML: монолит для системных отчётов формировал XML (WINDOWS-1251).
        JSONObject jsonObject = new JSONObject(wrapped);
        String xml = XML_HEADER + XML.toString(jsonObject);
        return xml.getBytes(StandardCharsets.UTF_8);
    }
}
