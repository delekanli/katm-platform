package uz.katm.report.domain.record;

/**
 * Формат выдачи отчёта. Коды совпадают с монолитом (Constants.REPORT_XML/JSON/HTML).
 * HTML-рендеринг (через шаблоны getFileDemandPath/convertReport) пока не перенесён — отдельный гэп.
 */
public enum ReportFormat {
    XML(0),
    JSON(1),
    HTML(2);

    private final int code;

    ReportFormat(int code) {
        this.code = code;
    }

    public int code() {
        return code;
    }

    /** По числовому коду монолита; неизвестное/отрицательное → XML (поведение по умолчанию). */
    public static ReportFormat fromCode(int code) {
        for (ReportFormat f : values()) {
            if (f.code == code) {
                return f;
            }
        }
        return XML;
    }
}
