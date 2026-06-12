package uz.katm.auth.domain.record;

/** Доступ сотрудника к отчёту (DATAS.PKG_USERS.GET_REPORTS_LIST). */
public record ReportAccess(
        Integer reportId,
        String reportName
) {
}
