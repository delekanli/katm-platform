package uz.katm.ucin.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import uz.katm.ucin.domain.dto.CreditReportRequest;
import uz.katm.ucin.domain.record.UcinReportResult;

import javax.sql.DataSource;
import java.sql.Clob;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Map;

/**
 * UCIN credit-report домен (фаза 2): получение кредитного отчёта, статус, по хэшу,
 * готовый отчёт, проверка активной выгрузки. Перенос UcinCreditReportRepositoryImpl.
 */
@Repository
public class UcinReportRepository {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedJdbcTemplate;
    private final SimpleJdbcCall getCreditReportCall;
    private final SimpleJdbcCall getCreditReportStatusCall;
    private final SimpleJdbcCall getReportCall;

    public UcinReportRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedJdbcTemplate,
                                DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedJdbcTemplate = namedJdbcTemplate;
        this.getCreditReportCall = new SimpleJdbcCall(dataSource)
                .withSchemaName("DATAS").withCatalogName("PKG_WEB_INDIVIDUAL")
                .withProcedureName("GET_CREDIT_REPORT")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("P_CLIENT_ID", Types.VARCHAR),
                        new SqlParameter("P_CLAIM_ID", Types.VARCHAR),
                        new SqlParameter("P_CLAIM_DATE", Types.DATE),
                        new SqlParameter("P_REPORT_ID", Types.INTEGER),
                        new SqlParameter("P_IP", Types.VARCHAR),
                        new SqlParameter("P_LANG", Types.VARCHAR),
                        new SqlOutParameter("P_TARIF", Types.DOUBLE),
                        new SqlOutParameter("P_RESULT", Types.VARCHAR),
                        new SqlOutParameter("P_RET_MSG", Types.VARCHAR),
                        new SqlOutParameter("P_REPORT", Types.CLOB),
                        new SqlOutParameter("P_TOKEN", Types.VARCHAR)
                );
        this.getCreditReportStatusCall = new SimpleJdbcCall(dataSource)
                .withSchemaName("DATAS").withCatalogName("PKG_WEB_INDIVIDUAL")
                .withProcedureName("GET_CREDIT_REPORT_STATUS")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("P_CLAIM_ID", Types.VARCHAR),
                        new SqlParameter("P_TOKEN", Types.VARCHAR),
                        new SqlOutParameter("P_RESULT", Types.VARCHAR),
                        new SqlOutParameter("P_RET_MSG", Types.VARCHAR),
                        new SqlOutParameter("P_REPORT", Types.CLOB)
                );
        this.getReportCall = new SimpleJdbcCall(dataSource)
                .withSchemaName("DATAS").withCatalogName("NEW_REPORTS_UCIN")
                .withProcedureName("GET_REPORT")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("P_CLIENT_ID", Types.VARCHAR),
                        new SqlOutParameter("P_RESULT", Types.VARCHAR),
                        new SqlOutParameter("P_RET_MSG", Types.VARCHAR),
                        new SqlOutParameter("P_REPORT", Types.CLOB)
                );
    }

    public UcinReportResult getCreditReport(CreditReportRequest r) {
        Map<String, Object> out = getCreditReportCall.execute(new MapSqlParameterSource()
                .addValue("P_CLIENT_ID", r.clientId())
                .addValue("P_CLAIM_ID", r.claimId())
                .addValue("P_CLAIM_DATE", r.claimDate() != null ? Date.valueOf(r.claimDate()) : null)
                .addValue("P_REPORT_ID", r.reportId())
                .addValue("P_IP", r.ip())
                .addValue("P_LANG", r.lang()));
        Object tarif = out.get("P_TARIF");
        return new UcinReportResult(
                (String) out.get("P_RESULT"),
                (String) out.get("P_RET_MSG"),
                clobToString(out.get("P_REPORT")),
                (String) out.get("P_TOKEN"),
                tarif != null ? ((Number) tarif).doubleValue() : null);
    }

    public UcinReportResult getCreditReportStatus(String claimId, String token) {
        Map<String, Object> out = getCreditReportStatusCall.execute(new MapSqlParameterSource()
                .addValue("P_CLAIM_ID", claimId)
                .addValue("P_TOKEN", token));
        return new UcinReportResult(
                (String) out.get("P_RESULT"),
                (String) out.get("P_RET_MSG"),
                clobToString(out.get("P_REPORT")),
                null, null);
    }

    public UcinReportResult getReport(String clientId) {
        Map<String, Object> out = getReportCall.execute(new MapSqlParameterSource()
                .addValue("P_CLIENT_ID", clientId));
        return new UcinReportResult(
                (String) out.get("P_RESULT"),
                (String) out.get("P_RET_MSG"),
                clobToString(out.get("P_REPORT")),
                null, null);
    }

    /** Содержимое кредитного отчёта по хэшу заявки (datas.demands_cr/demands). null если не найдено. */
    public String getCreditReportByHash(String token) {
        try {
            Clob clob = namedJdbcTemplate.queryForObject(
                    "select dcr.credit_report from datas.demands_cr dcr " +
                            "left join datas.demands d on d.id_demand = dcr.id_demand " +
                            "where d.hash_value = :token",
                    new MapSqlParameterSource("token", token), Clob.class);
            return clobToString(clob);
        } catch (Exception e) {
            return null;
        }
    }

    /** Активна ли фоновая выгрузка (Loader.x_Open_Session, state=1, id=3). */
    public int checkActiveDemandWork() {
        Integer count = jdbcTemplate.queryForObject(
                "Select Count(*) From Loader.x_Open_Session t Where t.State = 1 And t.Id = 3",
                Integer.class);
        return count != null ? count : 0;
    }

    private static String clobToString(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof String s) {
            return s;
        }
        if (value instanceof Clob clob) {
            try {
                return clob.getSubString(1, (int) clob.length());
            } catch (SQLException e) {
                throw new IllegalStateException("Не удалось прочитать CLOB отчёта", e);
            }
        }
        return value.toString();
    }
}
