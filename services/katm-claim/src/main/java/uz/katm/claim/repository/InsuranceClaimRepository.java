package uz.katm.claim.repository;

import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import uz.katm.claim.domain.ProcedureResult;
import uz.katm.claim.domain.insurance.AddClaimRequest;
import uz.katm.claim.domain.insurance.ClaimResult;
import uz.katm.claim.domain.insurance.InitiateClaimRequest;

import javax.sql.DataSource;
import java.sql.Types;
import java.util.Map;

@Repository
public class InsuranceClaimRepository {

    private final SimpleJdbcCall initiateClaimCall;
    private final SimpleJdbcCall addClaimCall;
    private final SimpleJdbcCall rejectClaimCall;

    public InsuranceClaimRepository(DataSource dataSource) {
        this.initiateClaimCall = new SimpleJdbcCall(dataSource)
                .withSchemaName("DATAS")
                .withCatalogName("PKG_INSURANCES")
                .withProcedureName("INITIATE_CLAIM")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("P_CODE", Types.VARCHAR),
                        new SqlParameter("P_CLAIM_ID", Types.VARCHAR),
                        new SqlParameter("P_CLAIM_DATE", Types.DATE),
                        new SqlParameter("P_AGREEMENT_ID", Types.VARCHAR),
                        new SqlParameter("P_AGREEMENT_DATE", Types.DATE),
                        new SqlParameter("P_IDENTITY_CARD_SERIAL", Types.VARCHAR),
                        new SqlParameter("P_IDENTITY_CARD_NUMBER", Types.VARCHAR),
                        new SqlParameter("P_REGION", Types.VARCHAR),
                        new SqlParameter("P_LOCAL_REGION", Types.VARCHAR),
                        new SqlParameter("P_ADDRESS", Types.VARCHAR),
                        new SqlParameter("P_PINFL", Types.VARCHAR),
                        new SqlParameter("P_PHONE", Types.VARCHAR),
                        new SqlParameter("P_CRED_AMOUNT", Types.BIGINT),
                        new SqlParameter("P_CRED_CURRENCY", Types.VARCHAR),
                        new SqlParameter("P_CRED_END", Types.DATE),
                        new SqlOutParameter("P_RESULT", Types.VARCHAR),
                        new SqlOutParameter("P_RET_MSG", Types.VARCHAR),
                        new SqlOutParameter("P_INITIAL_ID", Types.INTEGER)
                );

        this.addClaimCall = new SimpleJdbcCall(dataSource)
                .withSchemaName("DATAS")
                .withCatalogName("PKG_INSURANCES")
                .withProcedureName("ADD_CLAIM")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("P_INITIAL_ID", Types.INTEGER),
                        new SqlParameter("P_CODE", Types.VARCHAR),
                        new SqlParameter("P_CLAIM_ID", Types.VARCHAR),
                        new SqlParameter("P_LAST_NAME", Types.VARCHAR),
                        new SqlParameter("P_NAME", Types.VARCHAR),
                        new SqlParameter("P_PATRONYMIC_NAME", Types.VARCHAR),
                        new SqlParameter("P_SEX", Types.INTEGER),
                        new SqlParameter("P_BIRTH_DATE", Types.DATE),
                        new SqlParameter("P_IDENTITY_CARD_ISSUE_DATE", Types.DATE),
                        new SqlParameter("P_IDENTITY_CARD_EXPIRE_DATE", Types.DATE),
                        new SqlParameter("P_IS_UPDATE", Types.INTEGER),
                        new SqlParameter("P_MIP_STATE", Types.INTEGER),
                        new SqlParameter("P_MIP_RESULT", Types.VARCHAR),
                        new SqlParameter("P_INN", Types.VARCHAR),
                        new SqlParameter("P_IDENTITY_CARD_TYPE", Types.INTEGER),
                        new SqlOutParameter("P_RESULT", Types.VARCHAR),
                        new SqlOutParameter("P_RET_MSG", Types.VARCHAR)
                );

        this.rejectClaimCall = new SimpleJdbcCall(dataSource)
                .withSchemaName("DATAS")
                .withCatalogName("PKG_INSURANCES")
                .withProcedureName("REJECT_CLAIM")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("P_CLAIM_ID", Types.VARCHAR),
                        new SqlParameter("P_CODE", Types.VARCHAR),
                        new SqlParameter("P_REASON", Types.VARCHAR),
                        new SqlOutParameter("P_RESULT", Types.VARCHAR),
                        new SqlOutParameter("P_RET_MSG", Types.VARCHAR)
                );
    }

    public ClaimResult initiateClaim(String code, InitiateClaimRequest req) {
        var params = new MapSqlParameterSource()
                .addValue("P_CODE", code)
                .addValue("P_CLAIM_ID", req.claimId())
                .addValue("P_CLAIM_DATE", java.sql.Date.valueOf(req.claimDate()))
                .addValue("P_AGREEMENT_ID", req.agreementId())
                .addValue("P_AGREEMENT_DATE", java.sql.Date.valueOf(req.agreementDate()))
                .addValue("P_IDENTITY_CARD_SERIAL", req.docSeries())
                .addValue("P_IDENTITY_CARD_NUMBER", req.docNumber())
                .addValue("P_REGION", req.region())
                .addValue("P_LOCAL_REGION", req.localRegion())
                .addValue("P_ADDRESS", req.address())
                .addValue("P_PINFL", req.pinfl())
                .addValue("P_PHONE", req.phone())
                .addValue("P_CRED_AMOUNT", req.creditAmount())
                .addValue("P_CRED_CURRENCY", req.currency())
                .addValue("P_CRED_END", java.sql.Date.valueOf(req.creditEndDate()));
        Map<String, Object> out = initiateClaimCall.execute(params);
        return new ClaimResult(
                String.valueOf(out.getOrDefault("P_RESULT", "-1")),
                (String) out.get("P_RET_MSG"),
                (Integer) out.get("P_INITIAL_ID")
        );
    }

    public ProcedureResult addClaim(String code, AddClaimRequest req) {
        var params = new MapSqlParameterSource()
                .addValue("P_INITIAL_ID", req.initialId())
                .addValue("P_CODE", code)
                .addValue("P_CLAIM_ID", req.claimId())
                .addValue("P_LAST_NAME", req.lastName())
                .addValue("P_NAME", req.firstName())
                .addValue("P_PATRONYMIC_NAME", req.middleName())
                .addValue("P_SEX", req.sex())
                .addValue("P_BIRTH_DATE", java.sql.Date.valueOf(req.birthDate()))
                .addValue("P_IDENTITY_CARD_ISSUE_DATE", java.sql.Date.valueOf(req.docIssueDate()))
                .addValue("P_IDENTITY_CARD_EXPIRE_DATE", java.sql.Date.valueOf(req.docExpireDate()))
                .addValue("P_IS_UPDATE", req.isUpdate())
                .addValue("P_MIP_STATE", req.mipState())
                .addValue("P_MIP_RESULT", req.mipResult())
                .addValue("P_INN", req.tin())
                .addValue("P_IDENTITY_CARD_TYPE", req.docType());
        return toResult(addClaimCall.execute(params));
    }

    public ProcedureResult rejectClaim(String claimId, String code, String reason) {
        var params = new MapSqlParameterSource()
                .addValue("P_CLAIM_ID", claimId)
                .addValue("P_CODE", code)
                .addValue("P_REASON", reason);
        return toResult(rejectClaimCall.execute(params));
    }

    private ProcedureResult toResult(Map<String, Object> out) {
        return new ProcedureResult(
                String.valueOf(out.getOrDefault("P_RESULT", "-1")),
                (String) out.get("P_RET_MSG")
        );
    }
}
