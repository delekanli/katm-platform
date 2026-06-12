package uz.katm.system.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import uz.katm.system.domain.dto.GatewayRequestLogRequest;
import uz.katm.system.domain.dto.OnlineRequestErrorRequest;
import uz.katm.system.domain.dto.ReportRequestLogRequest;
import uz.katm.system.domain.record.EgovResendItem;
import uz.katm.system.domain.record.ProcedureResult;

import javax.sql.DataSource;
import java.sql.Clob;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Системные инструменты (DATAS.PKG_ONLINE): логи шлюза/отчётов, ошибки онлайн-запросов,
 * статус веб-заявок E-GOV и отчёты на повторную отправку. Перенос
 * gov.uz.katm.repo.system.SystemToolRepositoryImpl.
 */
@Repository
public class SystemRepository {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedJdbcTemplate;
    private final SimpleJdbcCall gatewayLogCall;
    private final SimpleJdbcCall reportLogCall;
    private final SimpleJdbcCall onlineErrorCall;

    public SystemRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedJdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedJdbcTemplate = namedJdbcTemplate;
        this.gatewayLogCall = new SimpleJdbcCall(dataSource)
                .withSchemaName("DATAS").withCatalogName("PKG_ONLINE")
                .withProcedureName("ADD_GATEWAY_REQUESTS_LOG")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("P_ID", Types.INTEGER),
                        new SqlParameter("P_SOURCE", Types.INTEGER),
                        new SqlParameter("P_HEAD", Types.VARCHAR),
                        new SqlParameter("P_CODE", Types.VARCHAR),
                        new SqlParameter("P_CLAIM_ID", Types.VARCHAR),
                        new SqlParameter("P_COUNT_ID", Types.VARCHAR),
                        new SqlParameter("P_REQUEST_DATE", Types.DATE),
                        new SqlParameter("P_REQUEST", Types.VARCHAR),
                        new SqlParameter("P_RESPONSE", Types.VARCHAR),
                        new SqlOutParameter("P_RESULT", Types.VARCHAR),
                        new SqlOutParameter("P_RET_MSG", Types.VARCHAR)
                );
        this.reportLogCall = new SimpleJdbcCall(dataSource)
                .withSchemaName("DATAS").withCatalogName("PKG_ONLINE")
                .withProcedureName("ONLINE_REPORTS_REQUEST")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("P_HEAD", Types.VARCHAR),
                        new SqlParameter("P_CODE", Types.VARCHAR),
                        new SqlParameter("P_REPORT_TYPE", Types.VARCHAR),
                        new SqlParameter("P_AGREEMENT_ID", Types.VARCHAR),
                        new SqlParameter("P_AGREEMENT_DATE", Types.DATE),
                        new SqlParameter("P_CLAIM_ID", Types.VARCHAR),
                        new SqlParameter("P_STATUS", Types.VARCHAR),
                        new SqlParameter("P_RESPONSE", Types.VARCHAR),
                        new SqlOutParameter("P_RESULT", Types.VARCHAR),
                        new SqlOutParameter("P_RET_MSG", Types.VARCHAR)
                );
        this.onlineErrorCall = new SimpleJdbcCall(dataSource)
                .withSchemaName("DATAS").withCatalogName("PKG_ONLINE")
                .withProcedureName("SET_ONLINE_REQUEST_ERROR")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("P_TIN", Types.VARCHAR),
                        new SqlParameter("P_PIN", Types.VARCHAR),
                        new SqlParameter("P_DOC_SERIES", Types.VARCHAR),
                        new SqlParameter("P_DOC_NUMBER", Types.VARCHAR),
                        new SqlParameter("P_STATUS", Types.VARCHAR),
                        new SqlParameter("P_ERROR", Types.VARCHAR),
                        new SqlOutParameter("P_RESULT", Types.VARCHAR),
                        new SqlOutParameter("P_RET_MSG", Types.VARCHAR)
                );
    }

    public ProcedureResult addGatewayRequestsLog(GatewayRequestLogRequest r) {
        Map<String, Object> out = gatewayLogCall.execute(new MapSqlParameterSource()
                .addValue("P_ID", r.id())
                .addValue("P_SOURCE", r.source())
                .addValue("P_HEAD", r.head())
                .addValue("P_CODE", r.code())
                .addValue("P_CLAIM_ID", r.claimId())
                .addValue("P_COUNT_ID", r.countId())
                .addValue("P_REQUEST_DATE", r.requestDate() != null ? Date.valueOf(r.requestDate()) : null)
                .addValue("P_REQUEST", r.request())
                .addValue("P_RESPONSE", r.response()));
        return result(out);
    }

    public ProcedureResult addReportRequestLog(ReportRequestLogRequest r) {
        Map<String, Object> out = reportLogCall.execute(new MapSqlParameterSource()
                .addValue("P_HEAD", r.head())
                .addValue("P_CODE", r.code())
                .addValue("P_REPORT_TYPE", r.reportType())
                .addValue("P_AGREEMENT_ID", r.agreementId())
                .addValue("P_AGREEMENT_DATE", r.agreementDate() != null ? Date.valueOf(r.agreementDate()) : null)
                .addValue("P_CLAIM_ID", r.claimId())
                .addValue("P_STATUS", r.status())
                .addValue("P_RESPONSE", r.response()));
        return result(out);
    }

    public ProcedureResult addOnlineRequestError(OnlineRequestErrorRequest r) {
        Map<String, Object> out = onlineErrorCall.execute(new MapSqlParameterSource()
                .addValue("P_TIN", r.tin())
                .addValue("P_PIN", r.pin())
                .addValue("P_DOC_SERIES", r.docSeries())
                .addValue("P_DOC_NUMBER", r.docNumber())
                .addValue("P_STATUS", r.errorCode())
                .addValue("P_ERROR", r.errorMessage()));
        return result(out);
    }

    public List<Map<String, Object>> getOnlineRequestErrors(LocalDate requestDate, String tin, String pin,
                                                            String docSeries, String docNumber) {
        return namedJdbcTemplate.queryForList(
                "select * from table(DATAS.PKG_ONLINE.GET_ONLINE_REQUEST_ERROR(:requestDate, :tin, :pin, :docSeries, :docNumber))",
                new MapSqlParameterSource()
                        .addValue("requestDate", requestDate != null ? Date.valueOf(requestDate) : null)
                        .addValue("tin", tin).addValue("pin", pin)
                        .addValue("docSeries", docSeries).addValue("docNumber", docNumber));
    }

    public int updateWebClaimStatusEgov(String claimId) {
        return namedJdbcTemplate.update(
                "UPDATE datas.web_claims SET status = 1 WHERE claim_id = :claimId",
                new MapSqlParameterSource("claimId", claimId));
    }

    public List<EgovResendItem> getReportsForEgovResend() {
        return jdbcTemplate.query(
                "select wc.claim_id, dcr.credit_report " +
                        "from datas.demands_cr dcr " +
                        "join datas.web_claims wc on wc.id_demand = dcr.id_demand " +
                        "where wc.status = 0 and trunc(wc.date_add) >= trunc(sysdate-1) " +
                        "and wc.ip = '194.93.25.237' and rownum <= 100 order by date_add asc",
                (rs, rowNum) -> new EgovResendItem(rs.getString("CLAIM_ID"), clobToString(rs.getClob("CREDIT_REPORT"))));
    }

    private static ProcedureResult result(Map<String, Object> out) {
        return new ProcedureResult(
                String.valueOf(out.getOrDefault("P_RESULT", "-1")),
                (String) out.get("P_RET_MSG"));
    }

    private static String clobToString(Clob clob) {
        if (clob == null) {
            return null;
        }
        try {
            return clob.getSubString(1, (int) clob.length());
        } catch (SQLException e) {
            throw new IllegalStateException("Не удалось прочитать CLOB отчёта", e);
        }
    }
}
