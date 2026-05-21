package uz.katm.claim.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import uz.katm.claim.domain.ProcedureResult;
import uz.katm.claim.domain.bank.*;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.Types;
import java.time.LocalDate;
import java.util.Map;

@Slf4j
@Repository
public class BankClaimRepository {

    private final JdbcTemplate jdbcTemplate;
    private final DataSource dataSource;
    private final SimpleJdbcCall declineClaimCall;

    public BankClaimRepository(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);

        this.declineClaimCall = new SimpleJdbcCall(dataSource)
                .withSchemaName("DATAS")
                .withCatalogName("PKG_ONLINE")
                .withProcedureName("REJECT_CLAIM")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("P_HEAD", Types.VARCHAR),
                        new SqlParameter("P_CODE", Types.VARCHAR),
                        new SqlParameter("P_DATE", Types.DATE),
                        new SqlParameter("P_CLAIM_ID", Types.VARCHAR),
                        new SqlParameter("P_DECLINE_NUMBER", Types.VARCHAR),
                        new SqlParameter("P_DECLINE_REASON", Types.VARCHAR),
                        new SqlParameter("P_DECLINE_REASON_NOTE", Types.VARCHAR),
                        new SqlOutParameter("P_RESULT", Types.VARCHAR),
                        new SqlOutParameter("P_RET_MSG", Types.VARCHAR)
                );
    }

    public ProcedureResult declineClaim(String head, String code, DeclineClaimRequest req) {
        var params = new MapSqlParameterSource()
                .addValue("P_HEAD", head)
                .addValue("P_CODE", code)
                .addValue("P_DATE", toSqlDate(req.date()))
                .addValue("P_CLAIM_ID", req.claimId())
                .addValue("P_DECLINE_NUMBER", req.declineNumber())
                .addValue("P_DECLINE_REASON", req.declineReason())
                .addValue("P_DECLINE_REASON_NOTE", req.declineReasonNote());
        Map<String, Object> out = declineClaimCall.execute(params);
        return toResult(out);
    }

    public ClaimInfoResponse getClaim(String head, String code, GetClaimRequest req) {
        return jdbcTemplate.queryForObject(
                "select * from table(DATAS.PKG_ONLINE.Get_Claim(?, ?, ?))",
                (rs, rowNum) -> new ClaimInfoResponse(
                        rs.getString("CODE"),
                        rs.getString("CLAIM_ID"),
                        rs.getString("CLAIM_DATE"),
                        rs.getString("CLAIM_STATUS"),
                        rs.getString("AGREEMENT_NUMBER"),
                        rs.getString("AGREEMENT_DATE"),
                        rs.getString("CLIENT_ID"),
                        rs.getString("PASSPORT_SERIAL"),
                        rs.getString("PASSPORT_NUMBER"),
                        rs.getString("PINFL"),
                        rs.getString("PHONE"),
                        rs.getString("CREDIT_AMOUNT"),
                        rs.getString("CREDIT_CURRENCY"),
                        rs.getString("CREDIT_END"),
                        rs.getString("REGION"),
                        rs.getString("LOCAL_REGION"),
                        rs.getString("ADDRESS"),
                        rs.getString("FAMILY_NAME"),
                        rs.getString("NAME"),
                        rs.getString("PATRONYMIC"),
                        rs.getString("FULL_NAME"),
                        rs.getString("SEX"),
                        rs.getString("DATE_BIRTH"),
                        rs.getString("INN")
                ),
                head, code, req.claimId()
        );
    }

    public String inquiryIndividual(InquiryIndividualRequest req) {
        SimpleJdbcCall call = new SimpleJdbcCall(dataSource)
                .withSchemaName("POSTMAN")
                .withCatalogName("WS")
                .withFunctionName("individual")
                .withoutProcedureColumnMetaDataAccess();
        call.setReturnValueRequired(true);
        var params = new MapSqlParameterSource()
                .addValue("pIp", req.ip())
                .addValue("pMethod", req.method())
                .addValue("pContentType", req.contentType())
                .addValue("pMipPin", req.mipPin())
                .addValue("pMipSurname", req.mipSurname())
                .addValue("pMipName", req.mipName())
                .addValue("pMipPatronymic", req.mipPatronymic())
                .addValue("pMipBirthdate", toSqlDate(req.mipBirthdate()))
                .addValue("pMipSex", req.mipSex())
                .addValue("pQueryRequestDateTime", req.queryRequestDateTime())
                .addValue("pQueryRequestContent", req.queryRequestContent())
                .addValue("pMipState", req.mipState())
                .addValue("pMipRequestTime", req.mipRequestTime())
                .addValue("pMipResponseTime", req.mipResponseTime())
                .addValue("pMipRequestContent", req.mipRequestContent())
                .addValue("pMipResponseContent", req.mipResponseContent());
        return call.executeFunction(String.class, params);
    }

    public String inquiryEntity(InquiryEntityRequest req) {
        SimpleJdbcCall call = new SimpleJdbcCall(dataSource)
                .withSchemaName("POSTMAN")
                .withCatalogName("WS")
                .withFunctionName("entity")
                .withoutProcedureColumnMetaDataAccess();
        call.setReturnValueRequired(true);
        var params = new MapSqlParameterSource()
                .addValue("pIp", req.ip())
                .addValue("pMethod", req.method())
                .addValue("pContentType", req.contentType())
                .addValue("pMipFullName", req.mipFullName())
                .addValue("pMipInn", req.mipInn())
                .addValue("pMipOkpo", req.mipOkpo())
                .addValue("pQueryRequestDateTime", req.queryRequestDateTime())
                .addValue("pQueryRequestContent", req.queryRequestContent())
                .addValue("pMipState", req.mipState())
                .addValue("pMipRequestTime", req.mipRequestTime())
                .addValue("pMipResponseTime", req.mipResponseTime())
                .addValue("pMipRequestContent", req.mipRequestContent())
                .addValue("pMipResponseContent", req.mipResponseContent());
        return call.executeFunction(String.class, params);
    }

    private ProcedureResult toResult(Map<String, Object> out) {
        String code = String.valueOf(out.get("P_RESULT"));
        String message = (String) out.get("P_RET_MSG");
        log.debug("Procedure result: code={}, message={}", code, message);
        return new ProcedureResult(code, message);
    }

    private static Date toSqlDate(LocalDate date) {
        return date != null ? Date.valueOf(date) : null;
    }
}
