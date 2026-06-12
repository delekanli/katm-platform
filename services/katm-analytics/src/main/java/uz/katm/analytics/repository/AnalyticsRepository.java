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

    /** Детали скоринга по заявке (vladimir.pkg_test.get_score_report_ind_mod). */
    public List<Map<String, Object>> scoreReportDetails(String demandId) {
        return namedJdbcTemplate.queryForList(
                "select * from table(vladimir.pkg_test.get_score_report_ind_mod(:demandId))",
                new MapSqlParameterSource().addValue("demandId", demandId));
    }

    /** Сводная статистика по всем отчётам за период (head/code = '-1' для всех). */
    public List<Map<String, Object>> allReportsStat(LocalDate start, LocalDate end, String head, String code) {
        String sql = """
                select * from (Select d.bank,
                       d.branch,
                       t.code,
                       t.name,
                       t.extension,
                       count(d.Claim_Id) as cnt
                  From Loader.r_Information_Output t
                  Left Join datas.Demands d
                    On d.Report_Type_Id = t.Code
                   And d.Date_Time Between :startDate And :endDate
                   And d.Is_Show = 1
                   And d.Is_Test = 0
                   And (:head = '-1' or d.bank = :head)
                   And (:code = '-1' or d.branch = :code)
                 Group By d.bank,
                       d.branch,
                       t.code,
                       t.name,
                       t.extension
                Order by d.bank, d.branch, t.code) t where t.bank is not null""";
        return namedJdbcTemplate.queryForList(sql, dateRange(start, end)
                .addValue("head", head).addValue("code", code));
    }

    /** Изменения договора (discover_ci015) по head/code/contractId. */
    public List<Map<String, Object>> discoverCi015(String head, String code, String contractId) {
        String sql = """
                select to_char(date_change, 'dd.MM.yyyy') as date_change,
                loan_status, account, begin_, debit, credit, end_
                from DATAS.discover_ci015
                where head = :head
                and code = :code
                and contract_id = :contractId
                order by coa, date_change, time_""";
        return namedJdbcTemplate.queryForList(sql, new MapSqlParameterSource()
                .addValue("head", head).addValue("code", code).addValue("contractId", contractId));
    }

    /** Статистика ЦБ РУз по производителям за период (большой агрегат). */
    public List<Map<String, Object>> cbUzStat(LocalDate start, LocalDate end) {
        String sql = """
                With Claim As
                 (Select t.*
                    From Datas.Discover_Ci001 t
                   Where t.Claim_Date Between :startDate And :endDate
                  Union All
                  Select t.*
                    From Datas.Discover_Ci002 t
                   Where t.Claim_Date Between :startDate And :endDate),
                De As (
                   Select d.*
                     From Datas.Demands d
                    Where d.Report_Type_Id In ('001','002','003','004','005','007','008','021','022','023', '123', '026', '126', '027', '127', '122', '055', '155', '077', '177', '088', '188', '222', '322', '333')
                ),
                Cl As
                 (Select c.Head, c.Code, c.Claim_Id, c.Claim_Status
                    From Claim c, De d
                   Where d.Bank = c.Head
                     And d.Branch = c.Code
                     And d.Claim_Id = c.Claim_Id
                   Group By c.Head, c.Code, c.Claim_Id, c.Claim_Status),
                Fk As
                 (Select c.Head, c.Code, c.Claim_Id, c.Claim_Status
                    From Claim c
                    Left Join Datas.r_Clients_Group g On g.Client_Id = c.Client_Id
                    Left Join Datas.r_Clients_Group g1 On g1.Group_Info_Id = g.Group_Info_Id
                   Where c.Claim_Status = 1
                     And Not Exists (
                         Select 1
                           From De d
                          Where d.Bank = c.Head
                           And d.Branch = c.Code
                           And d.Claim_Id = c.Claim_Id)
                     And Exists (
                         Select 1
                           From De d
                          Where (d.Bank = c.Head or (d.Bank = 'ORG' and d.Branch = '40003'))
                           And d.Date_Time Between c.Claim_Date - 15 And c.Claim_Date +15
                           And d.Client_Id = Nvl(g1.Client_Id, c.Client_Id))
                   Group By c.Head, c.Code, c.Claim_Id, c.Claim_Status)
                Select t.Bank_Code,
                       t.Branch_Code,
                       t.Bank_Name,
                       Count(x.Code) Total_Qty,
                       Count(Case When x.Claim_Status = 0 Then x.Claim_Id End) New_Claims_Qty,
                       Count(Case When x.Claim_Status = 2 Then x.Claim_Id End) Declined_Claims_Qty,
                       Count(Case When x.Claim_Status = 1 Then x.Claim_Id End) Granted_Claims_Qty,
                       Count(y.Claim_Id) Reports_Taken,
                       Count(Case When y.Claim_Status <> 2 Then y.Claim_Id End) Reports_Taken_y,
                       Count(Case When y.Claim_Status = 2 Then y.Claim_Id End) Reports_Taken_n,
                       Count(Case When y.Claim_Status = 1 Then y.Claim_Id End) + Count(z.Claim_Id) Reports_Contract_g
                  From Site.v_Producers_All t
                  Join Claim x
                    On x.Head = t.Bank_Code
                   And x.Code = t.Branch_Code
                  Left Join Cl y
                    On y.Head = x.Head
                   And y.Code = x.Code
                   And y.Claim_Id = x.Claim_Id
                  Left Join Fk z
                    On z.Head = x.Head
                   And z.Code = x.Code
                   And z.Claim_Id = x.Claim_Id
                 Where t.Section_Type In (1, 7)
                   And t.Bank_Code Not In ('RET', 'SUG', 'LIZ', 'WEB', 'ORG')
                   And t.Closed Is Null
                 Group By t.Bank_Code, t.Branch_Code, t.Bank_Name
                 order by t.Bank_Code, t.Branch_Code""";
        return namedJdbcTemplate.queryForList(sql, dateRange(start, end));
    }

    /** Минимальный набор (datas.Report.Get_Minnabor). */
    public List<Map<String, Object>> minNabor(LocalDate start, LocalDate end) {
        return namedJdbcTemplate.queryForList(
                "Select * From Table(datas.Report.Get_Minnabor(:startDate, :endDate))",
                dateRange(start, end));
    }

    /** Заявки без отчётов за период (v_Claims_3). */
    public List<Map<String, Object>> claimsWoReports(LocalDate start, LocalDate end) {
        String sql = """
                select vc.head, vc.code, rc.client_type, vc.claim_id, to_char(vc.claim_date, 'dd.MM.yyyy') as claim_date
                from datas.v_Claims_3 vc
                left join DATAS.r_clients rc on rc.id = vc.client_id
                where not exists (select 1 from datas.demands d where d.bank = vc.head and d.branch = vc.code and d.claim_id = vc.claim_id and d.report_type_id in  ('001','002','003','004','005','007','008','021','022','023', '123', '026', '126', '027', '127', '122', '055', '155', '077', '177', '088', '188', '222', '322', '333'))
                and trunc(vc.claim_date) between :startDate and :endDate
                and vc.head Not In ('RET', 'SUG', 'LIZ', 'WEB', 'ORG') and vc.claim_status = 1
                order by vc.head, vc.code, vc.claim_date""";
        return namedJdbcTemplate.queryForList(sql, dateRange(start, end));
    }

    private static MapSqlParameterSource dateRange(LocalDate start, LocalDate end) {
        return new MapSqlParameterSource()
                .addValue("startDate", start != null ? Date.valueOf(start) : null)
                .addValue("endDate", end != null ? Date.valueOf(end) : null);
    }
}
