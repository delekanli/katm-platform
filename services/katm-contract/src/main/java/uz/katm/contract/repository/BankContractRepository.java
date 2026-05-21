package uz.katm.contract.repository;

import lombok.extern.slf4j.Slf4j;
import oracle.jdbc.OracleConnection;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import uz.katm.contract.domain.bank.*;
import uz.katm.contract.exception.ContractServiceException;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
public class BankContractRepository {

    private final JdbcTemplate jdbcTemplate;
    private final DataSource dataSource;

    private final SimpleJdbcCall declineClaimCall;
    private final SimpleJdbcCall registerLoanCall;
    private final SimpleJdbcCall registerLeaseCall;
    private final SimpleJdbcCall registerFactoringCall;
    private final SimpleJdbcCall contractRepaymentCall;
    private final SimpleJdbcCall repaymentDetailsCall;
    private final SimpleJdbcCall subjectRepaymentDetailsCall;
    private final SimpleJdbcCall loanScheduleCall;
    private final SimpleJdbcCall leaseScheduleCall;
    private final SimpleJdbcCall accountStatusInfoCall;
    private final SimpleJdbcCall relatedEntitiesCall;
    private final SimpleJdbcCall pledgeOwnerCall;
    private final SimpleJdbcCall loanSecurityCall;
    private final SimpleJdbcCall leaseRepaymentCall;

    public BankContractRepository(DataSource dataSource) {
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

        this.registerLoanCall = new SimpleJdbcCall(dataSource)
                .withSchemaName("DATAS")
                .withCatalogName("PKG_ONLINE_NEW")
                .withProcedureName("CI004_REGISTER_LOAN")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("P_HEAD", Types.VARCHAR),
                        new SqlParameter("P_CODE", Types.VARCHAR),
                        new SqlParameter("P_DATE", Types.DATE),
                        new SqlParameter("P_CLAIM_ID", Types.VARCHAR),
                        new SqlParameter("P_CONTRACT_ID", Types.VARCHAR),
                        new SqlParameter("P_INN", Types.VARCHAR),
                        new SqlParameter("P_NIBBD", Types.VARCHAR),
                        new SqlParameter("P_CREDIT_TYPE", Types.VARCHAR),
                        new SqlParameter("P_CREDIT_OBJECT", Types.VARCHAR),
                        new SqlParameter("P_CREDIT_DATE_BEGIN", Types.DATE),
                        new SqlParameter("P_CREDIT_DATE_END", Types.DATE),
                        new SqlParameter("P_SUMMA", Types.BIGINT),
                        new SqlParameter("P_CURRENCY", Types.VARCHAR),
                        new SqlParameter("P_PROCENT", Types.DOUBLE),
                        new SqlParameter("P_JURIDIC_NUMBER", Types.VARCHAR),
                        new SqlParameter("P_SUPPLY", Types.VARCHAR),
                        new SqlParameter("P_QUALITY", Types.VARCHAR),
                        new SqlParameter("P_URGENCY", Types.VARCHAR),
                        new SqlParameter("P_HBRANCH", Types.VARCHAR),
                        new SqlParameter("P_CREDIT_ACTIVITY", Types.VARCHAR),
                        new SqlParameter("P_REASON", Types.VARCHAR),
                        new SqlParameter("P_FOUNDER", Types.VARCHAR),
                        new SqlOutParameter("P_RESULT", Types.VARCHAR),
                        new SqlOutParameter("P_RET_MSG", Types.VARCHAR)
                );

        this.registerLeaseCall = new SimpleJdbcCall(dataSource)
                .withSchemaName("DATAS")
                .withCatalogName("PKG_ONLINE_NEW")
                .withProcedureName("CI011_REGISTER_LEASE")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("P_HEAD", Types.VARCHAR),
                        new SqlParameter("P_CODE", Types.VARCHAR),
                        new SqlParameter("P_DATE", Types.DATE),
                        new SqlParameter("P_CLAIM_ID", Types.VARCHAR),
                        new SqlParameter("P_INN", Types.VARCHAR),
                        new SqlParameter("P_NIBBD", Types.VARCHAR),
                        new SqlParameter("P_LEASING_ID", Types.VARCHAR),
                        new SqlParameter("P_DATE_LEASING_BEGIN", Types.DATE),
                        new SqlParameter("P_DATE_LEASING_END", Types.DATE),
                        new SqlParameter("P_NOTARIUS_REG_NUMBER", Types.VARCHAR),
                        new SqlParameter("P_NOTARIUS_REG_DATE", Types.DATE),
                        new SqlParameter("P_NOTARIUS_CERT_NUMBER", Types.VARCHAR),
                        new SqlParameter("P_NOTARIUS_CERT_DATE", Types.DATE),
                        new SqlParameter("P_GOVERNMENT_REG_NUM", Types.VARCHAR),
                        new SqlParameter("P_GOVERNMENT_REG_DATE", Types.DATE),
                        new SqlParameter("P_SUMMA", Types.BIGINT),
                        new SqlParameter("P_CURRENCY", Types.VARCHAR),
                        new SqlParameter("P_PROCENT", Types.DOUBLE),
                        new SqlParameter("P_COUNT_OBJECT", Types.INTEGER),
                        new SqlOutParameter("P_RESULT", Types.VARCHAR),
                        new SqlOutParameter("P_RET_MSG", Types.VARCHAR)
                );

        this.registerFactoringCall = new SimpleJdbcCall(dataSource)
                .withSchemaName("DATAS")
                .withCatalogName("PKG_ONLINE_NEW")
                .withProcedureName("CI014_REGISTER_FACTORING")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("P_HEAD", Types.VARCHAR),
                        new SqlParameter("P_CODE", Types.VARCHAR),
                        new SqlParameter("P_DATE", Types.DATE),
                        new SqlParameter("P_CLAIM_ID", Types.VARCHAR),
                        new SqlParameter("P_INN", Types.VARCHAR),
                        new SqlParameter("P_NIBBD", Types.VARCHAR),
                        new SqlParameter("P_FACTORING_ID", Types.VARCHAR),
                        new SqlParameter("P_DATE_BEGIN", Types.DATE),
                        new SqlParameter("P_DATE_END", Types.DATE),
                        new SqlParameter("P_BANK_ELEMENT", Types.VARCHAR),
                        new SqlParameter("P_FACTORING_NUMBER", Types.VARCHAR),
                        new SqlParameter("P_CURRENCY", Types.VARCHAR),
                        new SqlParameter("P_SUMMA_LIABILITY", Types.BIGINT),
                        new SqlParameter("P_SUMMA_DISCOUNT", Types.BIGINT),
                        new SqlParameter("P_INN_DEBTOR", Types.VARCHAR),
                        new SqlOutParameter("P_RESULT", Types.VARCHAR),
                        new SqlOutParameter("P_RET_MSG", Types.VARCHAR)
                );

        this.contractRepaymentCall = new SimpleJdbcCall(dataSource)
                .withSchemaName("DATAS")
                .withCatalogName("PKG_ONLINE_NEW")
                .withProcedureName("CI015_LOAN_REPAYMENT")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("P_HEAD", Types.VARCHAR),
                        new SqlParameter("P_CODE", Types.VARCHAR),
                        new SqlParameter("P_DATE", Types.DATE),
                        new SqlParameter("P_CONTRACT_ID", Types.VARCHAR),
                        new SqlParameter("P_CONTRACT_TYPE", Types.VARCHAR),
                        new SqlParameter("P_LOAN_STATUS", Types.VARCHAR),
                        new SqlParameter("P_ACCOUNT", Types.VARCHAR),
                        new SqlParameter("P_REPAYMENT_ARRAY", Types.ARRAY, "DATAS.T_LOAN_REPAYMENT_ARRAY"),
                        new SqlParameter("P_IS_UPDATE", Types.INTEGER),
                        new SqlParameter("P_COA", Types.VARCHAR),
                        new SqlOutParameter("P_RESULT", Types.VARCHAR),
                        new SqlOutParameter("P_RET_MSG", Types.VARCHAR)
                );

        this.repaymentDetailsCall = new SimpleJdbcCall(dataSource)
                .withSchemaName("DATAS")
                .withCatalogName("PKG_ONLINE_NEW")
                .withProcedureName("CI016_LOAN_REPAYMENT_DETAILS")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("P_HEAD", Types.VARCHAR),
                        new SqlParameter("P_CODE", Types.VARCHAR),
                        new SqlParameter("P_DATE", Types.DATE),
                        new SqlParameter("P_CONTRACT_ID", Types.VARCHAR),
                        new SqlParameter("P_CONTRACT_TYPE", Types.VARCHAR),
                        new SqlParameter("P_LOAN_REPAY_DETAIL_ARRAY", Types.ARRAY, "DATAS.T_LOAN_REPAY_DETAILS_ARRAY"),
                        new SqlParameter("P_IS_UPDATE", Types.INTEGER),
                        new SqlOutParameter("P_RESULT", Types.VARCHAR),
                        new SqlOutParameter("P_RET_MSG", Types.VARCHAR)
                );

        this.subjectRepaymentDetailsCall = new SimpleJdbcCall(dataSource)
                .withSchemaName("DATAS")
                .withCatalogName("PKG_ONLINE_NEW")
                .withProcedureName("CI022_LOM_REPAYMENT_DETAILS")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("P_HEAD", Types.VARCHAR),
                        new SqlParameter("P_CODE", Types.VARCHAR),
                        new SqlParameter("P_DATE", Types.DATE),
                        new SqlParameter("P_CONTRACT_ID", Types.VARCHAR),
                        new SqlParameter("P_CONTRACT_TYPE", Types.VARCHAR),
                        new SqlParameter("P_LOM_REPAY_DETAIL_ARRAY", Types.ARRAY, "DATAS.T_LOM_REPAY_DETAILS_ARRAY"),
                        new SqlParameter("P_IS_UPDATE", Types.INTEGER),
                        new SqlOutParameter("P_RESULT", Types.VARCHAR),
                        new SqlOutParameter("P_RET_MSG", Types.VARCHAR)
                );

        this.loanScheduleCall = new SimpleJdbcCall(dataSource)
                .withSchemaName("DATAS")
                .withCatalogName("PKG_ONLINE_NEW")
                .withProcedureName("CI005_REGISTER_LOAN_SCHEDULE")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("P_HEAD", Types.VARCHAR),
                        new SqlParameter("P_CODE", Types.VARCHAR),
                        new SqlParameter("P_DATE", Types.DATE),
                        new SqlParameter("P_CONTRACT_ID", Types.VARCHAR),
                        new SqlParameter("P_INN", Types.VARCHAR),
                        new SqlParameter("P_NIBBD", Types.VARCHAR),
                        new SqlParameter("P_SCHEDULE_ARRAY", Types.ARRAY, "DATAS.T_LOAN_SCHEDULE_ARRAY"),
                        new SqlParameter("P_IS_UPDATE", Types.INTEGER),
                        new SqlOutParameter("P_RESULT", Types.VARCHAR),
                        new SqlOutParameter("P_RET_MSG", Types.VARCHAR)
                );

        this.leaseScheduleCall = new SimpleJdbcCall(dataSource)
                .withSchemaName("DATAS")
                .withCatalogName("PKG_ONLINE_NEW")
                .withProcedureName("CI012_REGISTER_LEASE_SCHEDULE")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("P_HEAD", Types.VARCHAR),
                        new SqlParameter("P_CODE", Types.VARCHAR),
                        new SqlParameter("P_DATE", Types.DATE),
                        new SqlParameter("P_CONTRACT_ID", Types.VARCHAR),
                        new SqlParameter("P_INN", Types.VARCHAR),
                        new SqlParameter("P_NIBBD", Types.VARCHAR),
                        new SqlParameter("P_SCHEDULE_ARRAY", Types.ARRAY, "DATAS.T_LEASE_SCHEDULE_ARRAY"),
                        new SqlParameter("P_IS_UPDATE", Types.INTEGER),
                        new SqlOutParameter("P_RESULT", Types.VARCHAR),
                        new SqlOutParameter("P_RET_MSG", Types.VARCHAR)
                );

        this.accountStatusInfoCall = new SimpleJdbcCall(dataSource)
                .withSchemaName("DATAS")
                .withCatalogName("PKG_ONLINE_NEW")
                .withProcedureName("CI018_ACCOUNTS_STATUS_INFO")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("P_HEAD", Types.VARCHAR),
                        new SqlParameter("P_CODE", Types.VARCHAR),
                        new SqlParameter("P_DATE", Types.DATE),
                        new SqlParameter("P_CONTRACT_ID", Types.VARCHAR),
                        new SqlParameter("P_CONTRACT_TYPE", Types.VARCHAR),
                        new SqlParameter("P_ACC_STATUS_DETAIL_ARRAY", Types.ARRAY, "DATAS.T_ACC_STATUS_DETAILS_ARRAY"),
                        new SqlParameter("P_IS_UPDATE", Types.INTEGER),
                        new SqlOutParameter("P_RESULT", Types.VARCHAR),
                        new SqlOutParameter("P_RET_MSG", Types.VARCHAR)
                );

        this.relatedEntitiesCall = new SimpleJdbcCall(dataSource)
                .withSchemaName("DATAS")
                .withCatalogName("PKG_ONLINE_NEW")
                .withProcedureName("CI023_RELATED_ENTITIES_REG")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("P_HEAD", Types.VARCHAR),
                        new SqlParameter("P_CODE", Types.VARCHAR),
                        new SqlParameter("P_DATE", Types.DATE),
                        new SqlParameter("P_CLAIM_ID", Types.VARCHAR),
                        new SqlParameter("P_OWNER_ID", Types.VARCHAR),
                        new SqlParameter("P_LOAN_SUBJECT", Types.VARCHAR),
                        new SqlParameter("P_SUBJECT_STATUS", Types.VARCHAR),
                        new SqlOutParameter("P_RESULT", Types.VARCHAR),
                        new SqlOutParameter("P_RET_MSG", Types.VARCHAR)
                );

        this.pledgeOwnerCall = new SimpleJdbcCall(dataSource)
                .withSchemaName("DATAS")
                .withCatalogName("PKG_ONLINE_NEW")
                .withProcedureName("CI020_PLEDGE_OWNER_INFO")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("P_HEAD", Types.VARCHAR),
                        new SqlParameter("P_CODE", Types.VARCHAR),
                        new SqlParameter("P_DATE", Types.DATE),
                        new SqlParameter("P_OWNER_ID", Types.VARCHAR),
                        new SqlParameter("P_INN", Types.VARCHAR),
                        new SqlParameter("P_CLIENT_ID", Types.VARCHAR),
                        new SqlParameter("P_RESIDENT", Types.VARCHAR),
                        new SqlParameter("P_SUBJECT_TYPE", Types.VARCHAR),
                        new SqlParameter("P_IDENTITY_CARD_TYPE", Types.VARCHAR),
                        new SqlParameter("P_IDENTITY_CARD_SERIAL", Types.VARCHAR),
                        new SqlParameter("P_IDENTITY_CARD_NUMBER", Types.VARCHAR),
                        new SqlParameter("P_IDENTITY_CARD_DATE", Types.DATE),
                        new SqlParameter("P_SEX", Types.VARCHAR),
                        new SqlParameter("P_FIO", Types.VARCHAR),
                        new SqlParameter("P_PERSONAL_CODE", Types.VARCHAR),
                        new SqlParameter("P_NIBBD", Types.VARCHAR),
                        new SqlParameter("P_CLIENT_TYPE", Types.VARCHAR),
                        new SqlParameter("P_FULL_NAME", Types.VARCHAR),
                        new SqlParameter("P_AGREEMENT_NUMBER", Types.VARCHAR),
                        new SqlParameter("P_AGREEMENT_DATE", Types.DATE),
                        new SqlParameter("P_LEGAL_ADDRESS", Types.VARCHAR),
                        new SqlParameter("P_DATE_BIRTHDAY", Types.DATE),
                        new SqlOutParameter("P_RESULT", Types.VARCHAR),
                        new SqlOutParameter("P_RET_MSG", Types.VARCHAR)
                );

        this.loanSecurityCall = new SimpleJdbcCall(dataSource)
                .withSchemaName("DATAS")
                .withCatalogName("PKG_ONLINE_NEW")
                .withProcedureName("CI021_LOAN_SECURITY_INFO")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("P_HEAD", Types.VARCHAR),
                        new SqlParameter("P_CODE", Types.VARCHAR),
                        new SqlParameter("P_DATE", Types.DATE),
                        new SqlParameter("P_GUARANTEE_ID", Types.VARCHAR),
                        new SqlParameter("P_CLAIM_ID", Types.VARCHAR),
                        new SqlParameter("P_OWNER_ID", Types.VARCHAR),
                        new SqlParameter("P_AGREEMENT_NUMBER", Types.VARCHAR),
                        new SqlParameter("P_AGREEMENT_DATE", Types.DATE),
                        new SqlParameter("P_GUARANTEE_TYPE", Types.VARCHAR),
                        new SqlParameter("P_CONTRACT_NUMBER", Types.VARCHAR),
                        new SqlParameter("P_CONTRACT_DATE", Types.DATE),
                        new SqlParameter("P_SUMMA", Types.BIGINT),
                        new SqlParameter("P_CURRENCY", Types.VARCHAR),
                        new SqlParameter("P_NAME", Types.VARCHAR),
                        new SqlParameter("P_DESCRIPTION", Types.VARCHAR),
                        new SqlParameter("P_STATUS", Types.VARCHAR),
                        new SqlParameter("P_LOAN_SUBJECT", Types.VARCHAR),
                        new SqlOutParameter("P_RESULT", Types.VARCHAR),
                        new SqlOutParameter("P_RET_MSG", Types.VARCHAR)
                );

        this.leaseRepaymentCall = new SimpleJdbcCall(dataSource)
                .withSchemaName("DATAS")
                .withCatalogName("PKG_ONLINE_NEW")
                .withProcedureName("CI013_LEASE_REPAYMENT")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("P_HEAD", Types.VARCHAR),
                        new SqlParameter("P_CODE", Types.VARCHAR),
                        new SqlParameter("P_DATE", Types.DATE),
                        new SqlParameter("P_INN", Types.VARCHAR),
                        new SqlParameter("P_NIBBD", Types.VARCHAR),
                        new SqlParameter("P_CLIENT_ID", Types.VARCHAR),
                        new SqlParameter("P_LEASING_ID", Types.VARCHAR),
                        new SqlParameter("P_REPAYMENT_ARRAY", Types.ARRAY, "DATAS.T_LEASE_REPAYEMENT_ARRAY"),
                        new SqlParameter("P_IS_UPDATE", Types.INTEGER),
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
        return executeCall(declineClaimCall, params);
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

    public ProcedureResult registerLoan(String head, String code, RegisterLoanRequest req) {
        var params = new MapSqlParameterSource()
                .addValue("P_HEAD", head)
                .addValue("P_CODE", code)
                .addValue("P_DATE", toSqlDate(req.date()))
                .addValue("P_CLAIM_ID", req.claimId())
                .addValue("P_CONTRACT_ID", req.contractId())
                .addValue("P_INN", req.inn())
                .addValue("P_NIBBD", req.nibbd())
                .addValue("P_CREDIT_TYPE", req.creditType())
                .addValue("P_CREDIT_OBJECT", req.creditObject())
                .addValue("P_CREDIT_DATE_BEGIN", toSqlDate(req.creditDateBegin()))
                .addValue("P_CREDIT_DATE_END", toSqlDate(req.creditDateEnd()))
                .addValue("P_SUMMA", req.summa())
                .addValue("P_CURRENCY", req.currency())
                .addValue("P_PROCENT", req.procent())
                .addValue("P_JURIDIC_NUMBER", req.juridicNumber())
                .addValue("P_SUPPLY", req.supply())
                .addValue("P_QUALITY", req.quality())
                .addValue("P_URGENCY", req.urgency())
                .addValue("P_HBRANCH", req.hbranch())
                .addValue("P_CREDIT_ACTIVITY", req.creditActivity())
                .addValue("P_REASON", req.reason())
                .addValue("P_FOUNDER", req.founder());
        return executeCall(registerLoanCall, params);
    }

    public ProcedureResult registerLease(String head, String code, RegisterLeaseRequest req) {
        var params = new MapSqlParameterSource()
                .addValue("P_HEAD", head)
                .addValue("P_CODE", code)
                .addValue("P_DATE", toSqlDate(req.date()))
                .addValue("P_CLAIM_ID", req.claimId())
                .addValue("P_INN", req.inn())
                .addValue("P_NIBBD", req.nibbd())
                .addValue("P_LEASING_ID", req.leasingId())
                .addValue("P_DATE_LEASING_BEGIN", toSqlDate(req.dateLeasingBegin()))
                .addValue("P_DATE_LEASING_END", toSqlDate(req.dateLeasingEnd()))
                .addValue("P_NOTARIUS_REG_NUMBER", req.notariusRegNumber())
                .addValue("P_NOTARIUS_REG_DATE", toSqlDate(req.notariusRegDate()))
                .addValue("P_NOTARIUS_CERT_NUMBER", req.notariusCertNumber())
                .addValue("P_NOTARIUS_CERT_DATE", toSqlDate(req.notariusCertDate()))
                .addValue("P_GOVERNMENT_REG_NUM", req.governmentRegNum())
                .addValue("P_GOVERNMENT_REG_DATE", toSqlDate(req.governmentRegDate()))
                .addValue("P_SUMMA", req.summa())
                .addValue("P_CURRENCY", req.currency())
                .addValue("P_PROCENT", req.procent())
                .addValue("P_COUNT_OBJECT", req.countObject());
        return executeCall(registerLeaseCall, params);
    }

    public ProcedureResult registerFactoring(String head, String code, RegisterFactoringRequest req) {
        var params = new MapSqlParameterSource()
                .addValue("P_HEAD", head)
                .addValue("P_CODE", code)
                .addValue("P_DATE", toSqlDate(req.date()))
                .addValue("P_CLAIM_ID", req.claimId())
                .addValue("P_INN", req.inn())
                .addValue("P_NIBBD", req.nibbd())
                .addValue("P_FACTORING_ID", req.factoringId())
                .addValue("P_DATE_BEGIN", toSqlDate(req.dateBegin()))
                .addValue("P_DATE_END", toSqlDate(req.dateEnd()))
                .addValue("P_BANK_ELEMENT", req.bankElement())
                .addValue("P_FACTORING_NUMBER", req.factoringNumber())
                .addValue("P_CURRENCY", req.currency())
                .addValue("P_SUMMA_LIABILITY", req.summaLiability())
                .addValue("P_SUMMA_DISCOUNT", req.summaDiscount())
                .addValue("P_INN_DEBTOR", req.innDebtor());
        return executeCall(registerFactoringCall, params);
    }

    public ProcedureResult addContractRepayment(String head, String code, ContractRepaymentRequest req) {
        return jdbcTemplate.execute((Connection conn) -> {
            OracleConnection oracleConn = conn.unwrap(OracleConnection.class);
            Array repaymentArray = buildRepaymentArray(oracleConn, req.repaymentArray());
            var params = new MapSqlParameterSource()
                    .addValue("P_HEAD", head)
                    .addValue("P_CODE", code)
                    .addValue("P_DATE", toSqlDate(req.date()))
                    .addValue("P_CONTRACT_ID", req.contractId())
                    .addValue("P_CONTRACT_TYPE", req.contractType())
                    .addValue("P_LOAN_STATUS", req.loanStatus())
                    .addValue("P_ACCOUNT", req.account())
                    .addValue("P_REPAYMENT_ARRAY", repaymentArray)
                    .addValue("P_IS_UPDATE", req.isUpdate())
                    .addValue("P_COA", req.coa());
            return executeCall(contractRepaymentCall, params);
        });
    }

    public ProcedureResult addContractRepaymentDetails(String head, String code, ContractRepaymentDetailsRequest req) {
        return jdbcTemplate.execute((Connection conn) -> {
            OracleConnection oracleConn = conn.unwrap(OracleConnection.class);
            Array detailArray = buildRepaymentDetailsArray(oracleConn, req.repaymentDetArray(),
                    "DATAS.T_LOAN_REPAY_DETAILS_OBJECT", "DATAS.T_LOAN_REPAY_DETAILS_ARRAY");
            var params = new MapSqlParameterSource()
                    .addValue("P_HEAD", head)
                    .addValue("P_CODE", code)
                    .addValue("P_DATE", toSqlDate(req.date()))
                    .addValue("P_CONTRACT_ID", req.contractId())
                    .addValue("P_CONTRACT_TYPE", req.contractType())
                    .addValue("P_LOAN_REPAY_DETAIL_ARRAY", detailArray)
                    .addValue("P_IS_UPDATE", req.isUpdate());
            return executeCall(repaymentDetailsCall, params);
        });
    }

    public ProcedureResult addSubjectRepaymentDetails(String head, String code, SubjectRepaymentDetailsRequest req) {
        return jdbcTemplate.execute((Connection conn) -> {
            OracleConnection oracleConn = conn.unwrap(OracleConnection.class);
            Array detailArray = buildRepaymentDetailsArray(oracleConn, req.lomRepayDetailArray(),
                    "DATAS.T_LOM_REPAY_DETAILS_OBJECT", "DATAS.T_LOM_REPAY_DETAILS_ARRAY");
            var params = new MapSqlParameterSource()
                    .addValue("P_HEAD", head)
                    .addValue("P_CODE", code)
                    .addValue("P_DATE", toSqlDate(req.date()))
                    .addValue("P_CONTRACT_ID", req.contractId())
                    .addValue("P_CONTRACT_TYPE", req.contractType())
                    .addValue("P_LOM_REPAY_DETAIL_ARRAY", detailArray)
                    .addValue("P_IS_UPDATE", req.isUpdate());
            return executeCall(subjectRepaymentDetailsCall, params);
        });
    }

    public ProcedureResult addContractSchedule(String head, String code, ContractScheduleRequest req) {
        return jdbcTemplate.execute((Connection conn) -> {
            OracleConnection oracleConn = conn.unwrap(OracleConnection.class);
            Array scheduleArray = buildScheduleArray(oracleConn, req.scheduleArray(),
                    "DATAS.T_LOAN_SCHEDULE_OBJECT", "DATAS.T_LOAN_SCHEDULE_ARRAY");
            var params = new MapSqlParameterSource()
                    .addValue("P_HEAD", head)
                    .addValue("P_CODE", code)
                    .addValue("P_DATE", toSqlDate(req.date()))
                    .addValue("P_CONTRACT_ID", req.contractId())
                    .addValue("P_INN", req.inn())
                    .addValue("P_NIBBD", req.nibbd())
                    .addValue("P_SCHEDULE_ARRAY", scheduleArray)
                    .addValue("P_IS_UPDATE", req.isUpdate());
            return executeCall(loanScheduleCall, params);
        });
    }

    public ProcedureResult addLizContractSchedule(String head, String code, ContractLeaseScheduleRequest req) {
        return jdbcTemplate.execute((Connection conn) -> {
            OracleConnection oracleConn = conn.unwrap(OracleConnection.class);
            Array scheduleArray = buildScheduleArray(oracleConn, req.scheduleArray(),
                    "DATAS.T_LEASE_SCHEDULE_OBJECT", "DATAS.T_LEASE_SCHEDULE_ARRAY");
            var params = new MapSqlParameterSource()
                    .addValue("P_HEAD", head)
                    .addValue("P_CODE", code)
                    .addValue("P_DATE", toSqlDate(req.date()))
                    .addValue("P_CONTRACT_ID", req.leasingId())
                    .addValue("P_INN", req.inn())
                    .addValue("P_NIBBD", req.nibbd())
                    .addValue("P_SCHEDULE_ARRAY", scheduleArray)
                    .addValue("P_IS_UPDATE", req.isUpdate());
            return executeCall(leaseScheduleCall, params);
        });
    }

    public ProcedureResult addAccountStatusInfo(String head, String code, AccountStatusInfoRequest req) {
        return jdbcTemplate.execute((Connection conn) -> {
            OracleConnection oracleConn = conn.unwrap(OracleConnection.class);
            Array statusArray = buildAccountStatusArray(oracleConn, req.accStatusDetailArray());
            var params = new MapSqlParameterSource()
                    .addValue("P_HEAD", head)
                    .addValue("P_CODE", code)
                    .addValue("P_DATE", toSqlDate(req.date()))
                    .addValue("P_CONTRACT_ID", req.contractId())
                    .addValue("P_CONTRACT_TYPE", req.contractType())
                    .addValue("P_ACC_STATUS_DETAIL_ARRAY", statusArray)
                    .addValue("P_IS_UPDATE", req.isUpdate());
            return executeCall(accountStatusInfoCall, params);
        });
    }

    public ProcedureResult addRelatedEntitiesRegistration(String head, String code, RelatedEntityRequest req) {
        var params = new MapSqlParameterSource()
                .addValue("P_HEAD", head)
                .addValue("P_CODE", code)
                .addValue("P_DATE", toSqlDate(req.date()))
                .addValue("P_CLAIM_ID", req.claimId())
                .addValue("P_OWNER_ID", req.ownerId())
                .addValue("P_LOAN_SUBJECT", req.loanSubject())
                .addValue("P_SUBJECT_STATUS", req.subjectStatus());
        return executeCall(relatedEntitiesCall, params);
    }

    public ProcedureResult addPledgeOwnerRegistration(String head, String code, PledgeOwnerRequest req) {
        var params = new MapSqlParameterSource()
                .addValue("P_HEAD", head)
                .addValue("P_CODE", code)
                .addValue("P_DATE", toSqlDate(req.date()))
                .addValue("P_OWNER_ID", req.ownerId())
                .addValue("P_INN", req.inn())
                .addValue("P_CLIENT_ID", req.clientId())
                .addValue("P_RESIDENT", req.resident())
                .addValue("P_SUBJECT_TYPE", req.subjectType())
                .addValue("P_IDENTITY_CARD_TYPE", req.identityCardType())
                .addValue("P_IDENTITY_CARD_SERIAL", req.identityCardSerial())
                .addValue("P_IDENTITY_CARD_NUMBER", req.identityCardNumber())
                .addValue("P_IDENTITY_CARD_DATE", toSqlDate(req.identityCardDate()))
                .addValue("P_SEX", req.sex())
                .addValue("P_FIO", req.fio())
                .addValue("P_PERSONAL_CODE", req.personalCode())
                .addValue("P_NIBBD", req.nibbd())
                .addValue("P_CLIENT_TYPE", req.clientType())
                .addValue("P_FULL_NAME", req.fullName())
                .addValue("P_AGREEMENT_NUMBER", req.agreementNumber())
                .addValue("P_AGREEMENT_DATE", toSqlDate(req.agreementDate()))
                .addValue("P_LEGAL_ADDRESS", req.legalAddress())
                .addValue("P_DATE_BIRTHDAY", toSqlDate(req.dateBirthday()));
        return executeCall(pledgeOwnerCall, params);
    }

    public ProcedureResult addLoanSecurityRegistration(String head, String code, LoanSecurityRequest req) {
        var params = new MapSqlParameterSource()
                .addValue("P_HEAD", head)
                .addValue("P_CODE", code)
                .addValue("P_DATE", toSqlDate(req.date()))
                .addValue("P_GUARANTEE_ID", req.guaranteeId())
                .addValue("P_CLAIM_ID", req.claimId())
                .addValue("P_OWNER_ID", req.ownerId())
                .addValue("P_AGREEMENT_NUMBER", req.agreementNumber())
                .addValue("P_AGREEMENT_DATE", toSqlDate(req.agreementDate()))
                .addValue("P_GUARANTEE_TYPE", req.guaranteeType())
                .addValue("P_CONTRACT_NUMBER", req.contractNumber())
                .addValue("P_CONTRACT_DATE", toSqlDate(req.contractDate()))
                .addValue("P_SUMMA", req.summa())
                .addValue("P_CURRENCY", req.currency())
                .addValue("P_NAME", req.name())
                .addValue("P_DESCRIPTION", req.description())
                .addValue("P_STATUS", req.status())
                .addValue("P_LOAN_SUBJECT", req.loanSubject());
        return executeCall(loanSecurityCall, params);
    }

    public ProcedureResult addContractLeaseRepayment(String head, String code, ContractLeaseRepaymentRequest req) {
        return jdbcTemplate.execute((Connection conn) -> {
            OracleConnection oracleConn = conn.unwrap(OracleConnection.class);
            Array leaseArray = buildLeaseRepaymentArray(oracleConn, req.repaymentArray());
            var params = new MapSqlParameterSource()
                    .addValue("P_HEAD", head)
                    .addValue("P_CODE", code)
                    .addValue("P_DATE", toSqlDate(req.date()))
                    .addValue("P_INN", req.inn())
                    .addValue("P_NIBBD", req.nibbd())
                    .addValue("P_CLIENT_ID", req.clientId())
                    .addValue("P_LEASING_ID", req.leasingId())
                    .addValue("P_REPAYMENT_ARRAY", leaseArray)
                    .addValue("P_IS_UPDATE", req.isUpdate());
            return executeCall(leaseRepaymentCall, params);
        });
    }

    private ProcedureResult executeCall(SimpleJdbcCall call, MapSqlParameterSource params) {
        Map<String, Object> result = call.execute(params);
        String code = String.valueOf(result.get("P_RESULT"));
        String message = (String) result.get("P_RET_MSG");
        log.debug("Procedure result: code={}, message={}", code, message);
        return new ProcedureResult(code, message);
    }

    private Array buildRepaymentArray(OracleConnection conn, List<RepaymentItem> items) {
        try {
            Struct[] structs = new Struct[items.size()];
            for (int i = 0; i < items.size(); i++) {
                RepaymentItem item = items.get(i);
                Object[] attrs = {toSqlDate(item.date()), item.account(), item.debit(), item.credit(),
                        item.startBalance(), item.endBalance()};
                structs[i] = conn.createStruct("DATAS.T_LOAN_REPAYMENT_OBJECT", attrs);
            }
            return conn.createARRAY("DATAS.T_LOAN_REPAYMENT_ARRAY", structs);
        } catch (Exception e) {
            throw new ContractServiceException("Failed to build repayment array: " + e.getMessage());
        }
    }

    private Array buildRepaymentDetailsArray(OracleConnection conn, List<RepaymentDetailItem> items,
                                              String structType, String arrayType) {
        try {
            Struct[] structs = new Struct[items.size()];
            for (int i = 0; i < items.size(); i++) {
                RepaymentDetailItem item = items.get(i);
                Object[] attrs = {item.payType(), item.docNum(), item.docType(), toSqlDate(item.docDate()),
                        item.currency(), item.nameA(), item.branchA(), item.accountA(),
                        item.nameB(), item.branchB(), item.accountB(), item.summa(),
                        item.purpose(), item.coaA(), item.coaB(), item.paymentId()};
                structs[i] = conn.createStruct(structType, attrs);
            }
            return conn.createARRAY(arrayType, structs);
        } catch (Exception e) {
            throw new ContractServiceException("Failed to build repayment details array: " + e.getMessage());
        }
    }

    private Array buildScheduleArray(OracleConnection conn, List<ScheduleItem> items,
                                     String structType, String arrayType) {
        try {
            Struct[] structs = new Struct[items.size()];
            for (int i = 0; i < items.size(); i++) {
                ScheduleItem item = items.get(i);
                Object[] attrs = {toSqlDate(item.date()), item.currency(), item.amount(), item.percent()};
                structs[i] = conn.createStruct(structType, attrs);
            }
            return conn.createARRAY(arrayType, structs);
        } catch (Exception e) {
            throw new ContractServiceException("Failed to build schedule array: " + e.getMessage());
        }
    }

    private Array buildAccountStatusArray(OracleConnection conn, List<AccountStatusItem> items) {
        try {
            Struct[] structs = new Struct[items.size()];
            for (int i = 0; i < items.size(); i++) {
                AccountStatusItem item = items.get(i);
                Object[] attrs = {item.coa(), item.account(), toSqlDate(item.dateOpen()),
                        toSqlDate(item.dateClose()), toSqlDate(item.date())};
                structs[i] = conn.createStruct("DATAS.T_ACC_STATUS_DETAILS_OBJECT", attrs);
            }
            return conn.createARRAY("DATAS.T_ACC_STATUS_DETAILS_ARRAY", structs);
        } catch (Exception e) {
            throw new ContractServiceException("Failed to build account status array: " + e.getMessage());
        }
    }

    private Array buildLeaseRepaymentArray(OracleConnection conn, List<LeaseRepaymentItem> items) {
        try {
            Struct[] structs = new Struct[items.size()];
            for (int i = 0; i < items.size(); i++) {
                LeaseRepaymentItem item = items.get(i);
                Object[] attrs = {toSqlDate(item.date()), item.currency(), item.leasingType(),
                        item.amortization(), item.amount(), item.name(), item.status(),
                        item.details(), item.objectId()};
                structs[i] = conn.createStruct("DATAS.T_LEASE_REPAYEMENT_OBJECT", attrs);
            }
            return conn.createARRAY("DATAS.T_LEASE_REPAYEMENT_ARRAY", structs);
        } catch (Exception e) {
            throw new ContractServiceException("Failed to build lease repayment array: " + e.getMessage());
        }
    }

    private static Date toSqlDate(LocalDate date) {
        return date != null ? Date.valueOf(date) : null;
    }
}
