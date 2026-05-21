package uz.katm.report.repository;

import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import uz.katm.common.util.JdbcUtils;
import uz.katm.report.domain.record.CreditReportRequest;
import uz.katm.report.domain.record.CreditReportResponse;
import uz.katm.report.domain.record.FicoResult;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class CreditRepository {

    private final SimpleJdbcCall getCreditReportCall;
    private final SimpleJdbcCall getCreditReportStatusCall;
    private final SimpleJdbcCall getInfoscoreCall;
    private final SimpleJdbcCall getInfoscoreLegalCall;
    private final SimpleJdbcCall getFicoScoreCall;

    public CreditRepository(DataSource dataSource) {
        this.getCreditReportCall = new SimpleJdbcCall(dataSource)
                .withSchemaName("DATAS")
                .withCatalogName("PKG_ONLINE")
                .withProcedureName("GET_CREDIT_REPORT")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("P_HEAD", Types.VARCHAR),
                        new SqlParameter("P_CODE", Types.VARCHAR),
                        new SqlParameter("P_IS_LEGAL", Types.INTEGER),
                        new SqlParameter("P_CLAIM_ID", Types.VARCHAR),
                        new SqlParameter("P_IP", Types.VARCHAR),
                        new SqlParameter("P_REPORT_ID", Types.INTEGER),
                        new SqlParameter("P_MIP_RESPONSE", Types.VARCHAR),
                        new SqlParameter("P_IS_SHOW", Types.INTEGER),
                        new SqlParameter("P_LANG", Types.VARCHAR),
                        new SqlParameter("P_JURIDICAL_STATUS", Types.VARCHAR),
                        new SqlParameter("P_LOAN_SUBJECT", Types.VARCHAR),
                        new SqlParameter("P_PERSONAL_CODE", Types.VARCHAR),
                        new SqlParameter("P_INN", Types.VARCHAR),
                        new SqlParameter("P_OWNER_ID", Types.VARCHAR),
                        new SqlOutParameter("P_RESULT", Types.VARCHAR),
                        new SqlOutParameter("P_RET_MSG", Types.VARCHAR),
                        new SqlOutParameter("P_REPORT", Types.CLOB),
                        new SqlOutParameter("P_TOKEN", Types.VARCHAR)
                );

        this.getCreditReportStatusCall = new SimpleJdbcCall(dataSource)
                .withSchemaName("DATAS")
                .withCatalogName("PKG_ONLINE")
                .withProcedureName("GET_CREDIT_REPORT_STATUS")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("P_HEAD", Types.VARCHAR),
                        new SqlParameter("P_CODE", Types.VARCHAR),
                        new SqlParameter("P_CLAIM_ID", Types.VARCHAR),
                        new SqlParameter("P_TOKEN", Types.VARCHAR),
                        new SqlOutParameter("P_RESULT", Types.VARCHAR),
                        new SqlOutParameter("P_RET_MSG", Types.VARCHAR),
                        new SqlOutParameter("P_REPORT", Types.CLOB)
                );

        this.getInfoscoreCall = new SimpleJdbcCall(dataSource)
                .withSchemaName("DATAS")
                .withCatalogName("PKG_ONLINE")
                .withProcedureName("GET_INFOSCORE_REPORT")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("P_PIN", Types.VARCHAR),
                        new SqlOutParameter("P_RESULT", Types.VARCHAR),
                        new SqlOutParameter("P_RET_MSG", Types.VARCHAR),
                        new SqlOutParameter("P_REPORT", Types.CLOB)
                );

        this.getInfoscoreLegalCall = new SimpleJdbcCall(dataSource)
                .withSchemaName("DATAS")
                .withCatalogName("PKG_ONLINE")
                .withProcedureName("GET_INFOSCORE_LEGAL_REPORT")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("P_TIN", Types.VARCHAR),
                        new SqlOutParameter("P_RESULT", Types.VARCHAR),
                        new SqlOutParameter("P_RET_MSG", Types.VARCHAR),
                        new SqlOutParameter("P_REPORT", Types.CLOB)
                );

        this.getFicoScoreCall = new SimpleJdbcCall(dataSource)
                .withSchemaName("DATAS")
                .withCatalogName("PKG_FICO")
                .withProcedureName("GET_FICO_SCORE")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("P_CLIENT_ID", Types.VARCHAR),
                        new SqlOutParameter("P_RESULT", Types.VARCHAR),
                        new SqlOutParameter("P_RET_MSG", Types.VARCHAR),
                        new SqlOutParameter("P_SCORE", Types.NUMERIC),
                        new SqlOutParameter("P_SCORE_DATE", Types.VARCHAR)
                );
    }

    public CreditReportResponse getCreditReport(String head, String code, CreditReportRequest req) {
        var params = new MapSqlParameterSource()
                .addValue("P_HEAD", head)
                .addValue("P_CODE", code)
                .addValue("P_IS_LEGAL", req.isLegal())
                .addValue("P_CLAIM_ID", req.claimId())
                .addValue("P_IP", req.ip())
                .addValue("P_REPORT_ID", req.reportId())
                .addValue("P_MIP_RESPONSE", req.mipResponse())
                .addValue("P_IS_SHOW", req.isShow())
                .addValue("P_LANG", req.lang())
                .addValue("P_JURIDICAL_STATUS", req.juridicalStatus())
                .addValue("P_LOAN_SUBJECT", req.loanSubject())
                .addValue("P_PERSONAL_CODE", req.personalCode())
                .addValue("P_INN", req.inn())
                .addValue("P_OWNER_ID", req.ownerId());
        return toResponse(getCreditReportCall.execute(params), "P_REPORT", "P_TOKEN");
    }

    public CreditReportResponse getCreditReportStatus(String head, String code, String claimId, String token) {
        var params = new MapSqlParameterSource()
                .addValue("P_HEAD", head)
                .addValue("P_CODE", code)
                .addValue("P_CLAIM_ID", claimId)
                .addValue("P_TOKEN", token);
        return toResponse(getCreditReportStatusCall.execute(params), "P_REPORT", null);
    }

    public CreditReportResponse getInfoscoreReport(String pin) {
        var params = new MapSqlParameterSource().addValue("P_PIN", pin);
        return toResponse(getInfoscoreCall.execute(params), "P_REPORT", null);
    }

    public CreditReportResponse getInfoscoreLegalReport(String tin) {
        var params = new MapSqlParameterSource().addValue("P_TIN", tin);
        return toResponse(getInfoscoreLegalCall.execute(params), "P_REPORT", null);
    }

    public List<FicoResult> getFicoScore(String clientId) {
        var params = new MapSqlParameterSource().addValue("P_CLIENT_ID", clientId);
        Map<String, Object> out = getFicoScoreCall.execute(params);
        List<FicoResult> results = new ArrayList<>();
        if (out.get("P_SCORE") != null) {
            results.add(new FicoResult(
                    clientId,
                    (BigDecimal) out.get("P_SCORE"),
                    (String) out.get("P_SCORE_DATE")
            ));
        }
        return results;
    }

    private CreditReportResponse toResponse(Map<String, Object> out, String reportKey, String tokenKey) {
        String code    = String.valueOf(out.getOrDefault("P_RESULT", "-1"));
        String message = (String) out.get("P_RET_MSG");
        byte[] report  = JdbcUtils.readClob(out, reportKey);
        String token   = tokenKey != null ? (String) out.get(tokenKey) : null;
        return new CreditReportResponse(code, message, report, token);
    }
}
