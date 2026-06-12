package uz.katm.analytics.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Аналитические отчёты (DATAS.PKG_REPORTS, фаза 1 — pipelined-функции).
 * Перенос gov.uz.katm.repo.analytic.AnalyticRepositoryImpl. Возвращает строки как
 * {@code Map<column, value>} (BI-стиль) — точные запросы сохранены, типизированный
 * широкий ReportDto монолита не переносится.
 */
@Repository
public class AnalyticsRepository {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    public AnalyticsRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedJdbcTemplate = namedJdbcTemplate;
    }

    /** Детальный портфель по банку (GET_BRIEFCASE_DATA[_ALL]). */
    public List<Map<String, Object>> detailPortfolio(String bank, Integer pos, boolean allCoa) {
        String fn = allCoa ? "GET_BRIEFCASE_DATA_ALL" : "GET_BRIEFCASE_DATA";
        return namedJdbcTemplate.queryForList(
                "select * from table(DATAS.PKG_REPORTS." + fn + "(:bank, :pos))",
                new MapSqlParameterSource().addValue("bank", bank).addValue("pos", pos));
    }

    /** Сводка по COA (GET_BRIEFCASE_SUMMARY[_ALL]). */
    public List<Map<String, Object>> coaSummaryPortfolio(Integer pos, boolean allCoa) {
        String fn = allCoa ? "GET_BRIEFCASE_SUMMARY_ALL" : "GET_BRIEFCASE_SUMMARY";
        return namedJdbcTemplate.queryForList(
                "select * from table(DATAS.PKG_REPORTS." + fn + "(:pos))",
                new MapSqlParameterSource().addValue("pos", pos));
    }

    /** Расширенная сводка по COA (GET_BRIEFCASE_EXP_SUMMARY[_ALL]). */
    public List<Map<String, Object>> coaExtSummaryPortfolio(Integer pos, boolean allCoa) {
        String fn = allCoa ? "GET_BRIEFCASE_EXP_SUMMARY_ALL" : "GET_BRIEFCASE_EXP_SUMMARY";
        return namedJdbcTemplate.queryForList(
                "select * from table(DATAS.PKG_REPORTS." + fn + "(:pos))",
                new MapSqlParameterSource().addValue("pos", pos));
    }

    /** Отчёт 04 — фиксы (GET_REPORT_04_FIXES). */
    public List<Map<String, Object>> report04() {
        return jdbcTemplate.queryForList("select * from table(DATAS.PKG_REPORTS.GET_REPORT_04_FIXES)");
    }

    /** Отчёт 06 — классы активов (GET_REPORT_06_ASSETS_CLASSES). */
    public List<Map<String, Object>> report06(LocalDate start, LocalDate end) {
        return namedJdbcTemplate.queryForList(
                "select * from table(DATAS.PKG_REPORTS.GET_REPORT_06_ASSETS_CLASSES(:startDate, :endDate))",
                dateRange(start, end));
    }

    /** Отчёт 09 — покрытие КО (GET_REPORT_09_KO_COVERAGE). */
    public List<Map<String, Object>> report09(LocalDate start, LocalDate end) {
        return namedJdbcTemplate.queryForList(
                "select * from table(DATAS.PKG_REPORTS.GET_REPORT_09_KO_COVERAGE(:startDate, :endDate))",
                dateRange(start, end));
    }

    /** Отчёт 09 расширенный (GET_REPORT_09_KO_COVERAGE_EXP). */
    public List<Map<String, Object>> extReport09(LocalDate start, LocalDate end) {
        return namedJdbcTemplate.queryForList(
                "select * from table(DATAS.PKG_REPORTS.GET_REPORT_09_KO_COVERAGE_EXP(:startDate, :endDate))",
                dateRange(start, end));
    }

    /** Дубликаты договоров по банку (GET_CONTRACT_DUPLICATES). */
    public List<Map<String, Object>> duplicates(String bank) {
        return namedJdbcTemplate.queryForList(
                "select * from table(DATAS.PKG_REPORTS.GET_CONTRACT_DUPLICATES(:bank))",
                new MapSqlParameterSource().addValue("bank", bank));
    }

    /**
     * Статистика использования отчётов за период / по клиентам / по МКО-ломбардам.
     * NB: в монолите все три метода вызывают GET_CONTRACT_DUPLICATES(:startDate,:endDate)
     * — вероятная copy-paste ошибка; перенесено как есть.
     */
    public List<Map<String, Object>> usesCreditReports(LocalDate start, LocalDate end) {
        return namedJdbcTemplate.queryForList(
                "select * from table(DATAS.PKG_REPORTS.GET_CONTRACT_DUPLICATES(:startDate, :endDate))",
                dateRange(start, end));
    }

    /** Статистика веб-обращений mygov (Report.Report_Mygov_Ukin). */
    public List<Map<String, Object>> webDemandsStat(LocalDate start, LocalDate end) {
        return namedJdbcTemplate.queryForList(
                "Select * From Table(datas.Report.Report_Mygov_Ukin(:startDate, :endDate))",
                dateRange(start, end));
    }

    private static MapSqlParameterSource dateRange(LocalDate start, LocalDate end) {
        return new MapSqlParameterSource()
                .addValue("startDate", start != null ? Date.valueOf(start) : null)
                .addValue("endDate", end != null ? Date.valueOf(end) : null);
    }
}
