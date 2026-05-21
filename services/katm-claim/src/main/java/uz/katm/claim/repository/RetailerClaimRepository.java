package uz.katm.claim.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import uz.katm.claim.domain.retailer.ClaimLegalRegistrationRequest;
import uz.katm.claim.domain.retailer.ClaimRegistrationRequest;
import uz.katm.claim.domain.retailer.OperationResult;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.Types;
import java.time.LocalDate;
import java.util.Map;

@Slf4j
@Repository
public class RetailerClaimRepository {

    private final SimpleJdbcCall initiateClaimCall;
    private final SimpleJdbcCall initiateIndClaimCall;
    private final SimpleJdbcCall addClaimCall;
    private final SimpleJdbcCall initiateLegalClaimCall;
    private final SimpleJdbcCall addLegalClaimCall;
    private final SimpleJdbcCall addOrgClaimCall;

    public RetailerClaimRepository(DataSource dataSource) {
        this.initiateClaimCall = new SimpleJdbcCall(dataSource)
                .withSchemaName("DATAS")
                .withCatalogName("PKG_RETAILERS")
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

        this.initiateIndClaimCall = new SimpleJdbcCall(dataSource)
                .withSchemaName("DATAS")
                .withCatalogName("PKG_RETAILERS_EXP")
                .withProcedureName("INITIATE_CLAIM")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("P_CODE", Types.VARCHAR),
                        new SqlParameter("P_CLAIM_ID", Types.VARCHAR),
                        new SqlParameter("P_CLAIM_DATE", Types.DATE),
                        new SqlParameter("P_AGREEMENT_ID", Types.VARCHAR),
                        new SqlParameter("P_AGREEMENT_DATE", Types.DATE),
                        new SqlParameter("P_PINFL", Types.VARCHAR),
                        new SqlOutParameter("P_RESULT", Types.VARCHAR),
                        new SqlOutParameter("P_RET_MSG", Types.VARCHAR),
                        new SqlOutParameter("P_INITIAL_ID", Types.INTEGER)
                );

        this.addClaimCall = new SimpleJdbcCall(dataSource)
                .withSchemaName("DATAS")
                .withCatalogName("PKG_RETAILERS")
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
                        new SqlParameter("P_INN", Types.VARCHAR),
                        new SqlParameter("P_IS_UPDATE", Types.INTEGER),
                        new SqlParameter("P_MIP_STATE", Types.VARCHAR),
                        new SqlParameter("P_MIP_RESULT", Types.VARCHAR),
                        new SqlParameter("P_IDENTITY_CARD_TYPE", Types.VARCHAR),
                        new SqlOutParameter("P_RESULT", Types.VARCHAR),
                        new SqlOutParameter("P_RET_MSG", Types.VARCHAR),
                        new SqlOutParameter("P_CLIENT_ID", Types.VARCHAR)
                );

        this.initiateLegalClaimCall = new SimpleJdbcCall(dataSource)
                .withSchemaName("DATAS")
                .withCatalogName("PKG_RETAILERS")
                .withProcedureName("INITIATE_LEGAL_CLAIM")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("P_HEAD", Types.VARCHAR),
                        new SqlParameter("P_CODE", Types.VARCHAR),
                        new SqlParameter("P_CLAIM_ID", Types.VARCHAR),
                        new SqlParameter("P_CLAIM_DATE", Types.DATE),
                        new SqlParameter("P_AGREEMENT_ID", Types.VARCHAR),
                        new SqlParameter("P_AGREEMENT_DATE", Types.DATE),
                        new SqlParameter("P_INN", Types.VARCHAR),
                        new SqlParameter("P_REGION", Types.VARCHAR),
                        new SqlParameter("P_LOCAL_REGION", Types.VARCHAR),
                        new SqlParameter("P_ADDRESS", Types.VARCHAR),
                        new SqlParameter("P_PHONE", Types.VARCHAR),
                        new SqlParameter("P_CRED_AMOUNT", Types.BIGINT),
                        new SqlParameter("P_CRED_CURRENCY", Types.VARCHAR),
                        new SqlParameter("P_CRED_END", Types.DATE),
                        new SqlOutParameter("P_RESULT", Types.VARCHAR),
                        new SqlOutParameter("P_RET_MSG", Types.VARCHAR),
                        new SqlOutParameter("P_INITIAL_ID", Types.INTEGER)
                );

        this.addLegalClaimCall = new SimpleJdbcCall(dataSource)
                .withSchemaName("DATAS")
                .withCatalogName("PKG_RETAILERS")
                .withProcedureName("ADD_LEGAL_CLAIM")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("P_INITIAL_ID", Types.INTEGER),
                        new SqlParameter("P_HEAD", Types.VARCHAR),
                        new SqlParameter("P_CODE", Types.VARCHAR),
                        new SqlParameter("P_CLAIM_ID", Types.VARCHAR),
                        new SqlParameter("P_FULL_NAME", Types.VARCHAR),
                        new SqlParameter("P_INN", Types.VARCHAR),
                        new SqlParameter("P_IS_UPDATE", Types.INTEGER),
                        new SqlParameter("P_MIP_STATE", Types.VARCHAR),
                        new SqlParameter("P_MIP_RESULT", Types.VARCHAR),
                        new SqlParameter("P_RESIDENT", Types.INTEGER),
                        new SqlParameter("P_CLIENT_TYPE_ID", Types.INTEGER),
                        new SqlParameter("P_OWNER_FORM", Types.VARCHAR),
                        new SqlParameter("P_GOVERMENT", Types.INTEGER),
                        new SqlParameter("P_HBRANCH", Types.VARCHAR),
                        new SqlParameter("P_INDUSTRY", Types.VARCHAR),
                        new SqlParameter("P_OKPO", Types.VARCHAR),
                        new SqlOutParameter("P_RESULT", Types.VARCHAR),
                        new SqlOutParameter("P_RET_MSG", Types.VARCHAR),
                        new SqlOutParameter("P_CLIENT_ID", Types.VARCHAR)
                );

        this.addOrgClaimCall = new SimpleJdbcCall(dataSource)
                .withSchemaName("DATAS")
                .withCatalogName("PKG_RETAILERS")
                .withProcedureName("ADD_LEGAL_CLAIM")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("P_INITIAL_ID", Types.INTEGER),
                        new SqlParameter("P_HEAD", Types.VARCHAR),
                        new SqlParameter("P_CODE", Types.VARCHAR),
                        new SqlParameter("P_CLAIM_ID", Types.VARCHAR),
                        new SqlParameter("P_FULL_NAME", Types.VARCHAR),
                        new SqlParameter("P_INN", Types.VARCHAR),
                        new SqlParameter("P_IS_UPDATE", Types.INTEGER),
                        new SqlParameter("P_MIP_STATE", Types.VARCHAR),
                        new SqlParameter("P_MIP_RESULT", Types.VARCHAR),
                        new SqlParameter("P_RESIDENT", Types.INTEGER),
                        new SqlParameter("P_CLIENT_TYPE_ID", Types.INTEGER),
                        new SqlParameter("P_OWNER_FORM", Types.VARCHAR),
                        new SqlParameter("P_GOVERMENT", Types.INTEGER),
                        new SqlParameter("P_HBRANCH", Types.VARCHAR),
                        new SqlParameter("P_INDUSTRY", Types.VARCHAR),
                        new SqlParameter("P_OKPO", Types.VARCHAR),
                        new SqlOutParameter("P_RESULT", Types.VARCHAR),
                        new SqlOutParameter("P_RET_MSG", Types.VARCHAR),
                        new SqlOutParameter("P_CLIENT_ID", Types.VARCHAR)
                );
    }

    public InitiateResult initiateClaim(String head, String code, ClaimRegistrationRequest req) {
        var params = new MapSqlParameterSource()
                .addValue("P_CODE", code)
                .addValue("P_CLAIM_ID", req.claimId())
                .addValue("P_CLAIM_DATE", toSqlDate(req.claimDate()))
                .addValue("P_AGREEMENT_ID", req.agreementId())
                .addValue("P_AGREEMENT_DATE", toSqlDate(req.agreementDate()))
                .addValue("P_IDENTITY_CARD_SERIAL", req.docSeries())
                .addValue("P_IDENTITY_CARD_NUMBER", req.docNumber())
                .addValue("P_REGION", req.region())
                .addValue("P_LOCAL_REGION", req.localRegion())
                .addValue("P_ADDRESS", req.address())
                .addValue("P_PINFL", req.pinfl())
                .addValue("P_PHONE", null)
                .addValue("P_CRED_AMOUNT", null)
                .addValue("P_CRED_CURRENCY", null)
                .addValue("P_CRED_END", null);
        return executeInitiate(initiateClaimCall, params);
    }

    public InitiateResult initiateIndClaim(String code, ClaimRegistrationRequest req) {
        var params = new MapSqlParameterSource()
                .addValue("P_CODE", code)
                .addValue("P_CLAIM_ID", req.claimId())
                .addValue("P_CLAIM_DATE", toSqlDate(req.claimDate()))
                .addValue("P_AGREEMENT_ID", req.agreementId())
                .addValue("P_AGREEMENT_DATE", toSqlDate(req.agreementDate()))
                .addValue("P_PINFL", req.pinfl());
        return executeInitiate(initiateIndClaimCall, params);
    }

    public OperationResult addClaim(Integer initialId, String code, String claimId,
                                    String lastName, String firstName, String middleName,
                                    Integer sex, LocalDate birthDate, LocalDate issueDate, LocalDate expireDate,
                                    String inn, Integer isUpdate, String mipState, String mipResult, String docType) {
        var params = new MapSqlParameterSource()
                .addValue("P_INITIAL_ID", initialId)
                .addValue("P_CODE", code)
                .addValue("P_CLAIM_ID", claimId)
                .addValue("P_LAST_NAME", lastName)
                .addValue("P_NAME", firstName)
                .addValue("P_PATRONYMIC_NAME", middleName)
                .addValue("P_SEX", sex)
                .addValue("P_BIRTH_DATE", toSqlDate(birthDate))
                .addValue("P_IDENTITY_CARD_ISSUE_DATE", toSqlDate(issueDate))
                .addValue("P_IDENTITY_CARD_EXPIRE_DATE", toSqlDate(expireDate))
                .addValue("P_INN", inn)
                .addValue("P_IS_UPDATE", isUpdate)
                .addValue("P_MIP_STATE", mipState)
                .addValue("P_MIP_RESULT", mipResult)
                .addValue("P_IDENTITY_CARD_TYPE", docType);
        return executeResult(addClaimCall, params);
    }

    public InitiateResult initiateLegalClaim(String head, String code, ClaimLegalRegistrationRequest req) {
        var params = new MapSqlParameterSource()
                .addValue("P_HEAD", head)
                .addValue("P_CODE", code)
                .addValue("P_CLAIM_ID", req.claimId())
                .addValue("P_CLAIM_DATE", toSqlDate(req.claimDate()))
                .addValue("P_AGREEMENT_ID", req.agreementId())
                .addValue("P_AGREEMENT_DATE", toSqlDate(req.agreementDate()))
                .addValue("P_INN", req.inn())
                .addValue("P_REGION", req.region())
                .addValue("P_LOCAL_REGION", req.localRegion())
                .addValue("P_ADDRESS", req.address())
                .addValue("P_PHONE", null)
                .addValue("P_CRED_AMOUNT", null)
                .addValue("P_CRED_CURRENCY", null)
                .addValue("P_CRED_END", null);
        return executeInitiate(initiateLegalClaimCall, params);
    }

    public OperationResult addLegalClaim(Integer initialId, String head, String code,
                                         ClaimLegalRegistrationRequest req,
                                         String fullName, String mipState, String mipResult) {
        return executeResult(addLegalClaimCall, buildLegalParams(initialId, head, code, req, fullName, mipState, mipResult));
    }

    public OperationResult addOrgClaim(Integer initialId, String head, String code,
                                       ClaimLegalRegistrationRequest req,
                                       String fullName, String mipState, String mipResult) {
        return executeResult(addOrgClaimCall, buildLegalParams(initialId, head, code, req, fullName, mipState, mipResult));
    }

    private MapSqlParameterSource buildLegalParams(Integer initialId, String head, String code,
                                                    ClaimLegalRegistrationRequest req,
                                                    String fullName, String mipState, String mipResult) {
        return new MapSqlParameterSource()
                .addValue("P_INITIAL_ID", initialId)
                .addValue("P_HEAD", head)
                .addValue("P_CODE", code)
                .addValue("P_CLAIM_ID", req.claimId())
                .addValue("P_FULL_NAME", fullName)
                .addValue("P_INN", req.inn())
                .addValue("P_IS_UPDATE", 0)
                .addValue("P_MIP_STATE", mipState)
                .addValue("P_MIP_RESULT", mipResult)
                .addValue("P_RESIDENT", req.resident())
                .addValue("P_CLIENT_TYPE_ID", req.clientType() != null ? Integer.parseInt(req.clientType()) : null)
                .addValue("P_OWNER_FORM", null)
                .addValue("P_GOVERMENT", req.government())
                .addValue("P_HBRANCH", req.headCode())
                .addValue("P_INDUSTRY", null)
                .addValue("P_OKPO", null);
    }

    private InitiateResult executeInitiate(SimpleJdbcCall call, MapSqlParameterSource params) {
        Map<String, Object> result = call.execute(params);
        return new InitiateResult(
                String.valueOf(result.get("P_RESULT")),
                (String) result.get("P_RET_MSG"),
                (Integer) result.get("P_INITIAL_ID")
        );
    }

    private OperationResult executeResult(SimpleJdbcCall call, MapSqlParameterSource params) {
        Map<String, Object> result = call.execute(params);
        return new OperationResult(
                String.valueOf(result.get("P_RESULT")),
                (String) result.get("P_RET_MSG"),
                (String) result.get("P_CLIENT_ID")
        );
    }

    private static Date toSqlDate(LocalDate date) {
        return date != null ? Date.valueOf(date) : null;
    }

    public record InitiateResult(String code, String message, Integer initialId) {}
}
