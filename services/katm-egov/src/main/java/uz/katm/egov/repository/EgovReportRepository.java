package uz.katm.egov.repository;

import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import uz.katm.egov.domain.dto.IndividualReportRequest;
import uz.katm.egov.domain.dto.LegalReportRequest;
import uz.katm.egov.domain.record.CreditReportResult;
import uz.katm.egov.domain.record.OperationResult;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.Types;
import java.time.LocalDate;
import java.util.Map;

/**
 * Канал кредитных отчётов E-GOV (DATAS.PKG_WEB_INDIVIDUAL).
 * Перенос gov.uz.katm.repo.egov.EgovCreditReportRepository (без registerClaim — тот зависит от UCIN).
 */
@Repository
public class EgovReportRepository {

    private final SimpleJdbcCall individualReportCall;
    private final SimpleJdbcCall legalReportCall;
    private final SimpleJdbcCall updateStatusCall;

    public EgovReportRepository(DataSource dataSource) {
        this.individualReportCall = new SimpleJdbcCall(dataSource)
                .withSchemaName("DATAS")
                .withCatalogName("PKG_WEB_INDIVIDUAL")
                .withProcedureName("EGOV_GET_CREDIT_REPORT")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("P_PINFL", Types.VARCHAR),
                        new SqlParameter("P_IDENTITY_CARD_SERIAL", Types.VARCHAR),
                        new SqlParameter("P_IDENTITY_CARD_NUMBER", Types.VARCHAR),
                        new SqlParameter("P_IDENTITY_CARD_DATE", Types.DATE),
                        new SqlParameter("P_INN", Types.VARCHAR),
                        new SqlParameter("P_REGION", Types.VARCHAR),
                        new SqlParameter("P_LOCAL_REGION", Types.VARCHAR),
                        new SqlParameter("P_FAMILY_NAME", Types.VARCHAR),
                        new SqlParameter("P_NAME", Types.VARCHAR),
                        new SqlParameter("P_PATRONYMIC", Types.VARCHAR),
                        new SqlParameter("P_DATE_BIRTH", Types.DATE),
                        new SqlParameter("P_PHONE", Types.VARCHAR),
                        new SqlParameter("P_POST_ADDRESS", Types.VARCHAR),
                        new SqlParameter("P_LIVE_ADDRESS", Types.VARCHAR),
                        new SqlParameter("P_RESIDENT", Types.VARCHAR),
                        new SqlParameter("P_SEX", Types.VARCHAR),
                        new SqlParameter("P_IP", Types.VARCHAR),
                        new SqlParameter("P_CLAIM_ID", Types.VARCHAR),
                        new SqlParameter("P_CLAIM_DATE", Types.DATE),
                        new SqlOutParameter("P_TARIF", Types.DOUBLE),
                        new SqlOutParameter("P_RESULT", Types.VARCHAR),
                        new SqlOutParameter("P_RET_MSG", Types.VARCHAR),
                        new SqlOutParameter("P_TOKEN", Types.VARCHAR)
                );
        this.legalReportCall = new SimpleJdbcCall(dataSource)
                .withSchemaName("DATAS")
                .withCatalogName("PKG_WEB_INDIVIDUAL")
                .withProcedureName("EGOV_LEGAL_GET_CREDIT_REPORT")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("P_INN", Types.VARCHAR),
                        new SqlParameter("P_REGION", Types.VARCHAR),
                        new SqlParameter("P_LOCAL_REGION", Types.VARCHAR),
                        new SqlParameter("P_FULL_NAME", Types.VARCHAR),
                        new SqlParameter("P_POST_ADDRESS", Types.VARCHAR),
                        new SqlParameter("P_PHONE", Types.VARCHAR),
                        new SqlParameter("P_RESIDENT", Types.VARCHAR),
                        new SqlParameter("P_CLIENT_TYPE_ID", Types.INTEGER),
                        new SqlParameter("P_OWNER_FORM", Types.VARCHAR),
                        new SqlParameter("P_GOVERMENT", Types.INTEGER),
                        new SqlParameter("P_OKPO", Types.VARCHAR),
                        new SqlParameter("P_HBRANCH", Types.VARCHAR),
                        new SqlParameter("P_INDUSTRY", Types.VARCHAR),
                        new SqlParameter("P_IP", Types.VARCHAR),
                        new SqlOutParameter("P_TARIF", Types.DOUBLE),
                        new SqlOutParameter("P_RESULT", Types.VARCHAR),
                        new SqlOutParameter("P_RET_MSG", Types.VARCHAR),
                        new SqlOutParameter("P_TOKEN", Types.VARCHAR)
                );
        this.updateStatusCall = new SimpleJdbcCall(dataSource)
                .withSchemaName("DATAS")
                .withCatalogName("PKG_WEB_INDIVIDUAL")
                .withProcedureName("EGOV_CLAIM_UPDATE_STATUS")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("P_CLAIM_ID", Types.VARCHAR),
                        new SqlParameter("P_CLAIM_STATUS", Types.VARCHAR),
                        new SqlParameter("P_CURRENT_NODE", Types.VARCHAR),
                        new SqlOutParameter("P_RESULT", Types.VARCHAR),
                        new SqlOutParameter("P_RET_MSG", Types.VARCHAR)
                );
    }

    public CreditReportResult getIndividualReport(IndividualReportRequest r) {
        Map<String, Object> out = individualReportCall.execute(new MapSqlParameterSource()
                .addValue("P_PINFL", r.pinfl())
                .addValue("P_IDENTITY_CARD_SERIAL", r.identityCardSerial())
                .addValue("P_IDENTITY_CARD_NUMBER", r.identityCardNumber())
                .addValue("P_IDENTITY_CARD_DATE", toSqlDate(r.identityCardDate()))
                .addValue("P_INN", r.inn())
                .addValue("P_REGION", r.region())
                .addValue("P_LOCAL_REGION", r.localRegion())
                .addValue("P_FAMILY_NAME", r.familyName())
                .addValue("P_NAME", r.name())
                .addValue("P_PATRONYMIC", r.patronymic())
                .addValue("P_DATE_BIRTH", toSqlDate(r.dateBirth()))
                .addValue("P_PHONE", r.phone())
                .addValue("P_POST_ADDRESS", r.postAddress())
                .addValue("P_LIVE_ADDRESS", r.liveAddress())
                .addValue("P_RESIDENT", r.resident())
                .addValue("P_SEX", r.sex())
                .addValue("P_IP", r.ip())
                .addValue("P_CLAIM_ID", r.claimId())
                .addValue("P_CLAIM_DATE", toSqlDate(r.claimDate())));
        return toReportResult(out);
    }

    public CreditReportResult getLegalReport(LegalReportRequest r) {
        Map<String, Object> out = legalReportCall.execute(new MapSqlParameterSource()
                .addValue("P_INN", r.inn())
                .addValue("P_REGION", r.region())
                .addValue("P_LOCAL_REGION", r.localRegion())
                .addValue("P_FULL_NAME", r.fullName())
                .addValue("P_POST_ADDRESS", r.postAddress())
                .addValue("P_PHONE", r.phone())
                .addValue("P_RESIDENT", r.resident())
                .addValue("P_CLIENT_TYPE_ID", r.clientTypeId())
                .addValue("P_OWNER_FORM", r.ownerForm())
                .addValue("P_GOVERMENT", r.government())
                .addValue("P_OKPO", r.okpo())
                .addValue("P_HBRANCH", r.hbranch())
                .addValue("P_INDUSTRY", r.industry())
                .addValue("P_IP", r.ip()));
        return toReportResult(out);
    }

    public OperationResult updateClaimStatus(String claimId, String status, String node) {
        Map<String, Object> out = updateStatusCall.execute(new MapSqlParameterSource()
                .addValue("P_CLAIM_ID", claimId)
                .addValue("P_CLAIM_STATUS", status)
                .addValue("P_CURRENT_NODE", node));
        return new OperationResult((String) out.get("P_RESULT"), (String) out.get("P_RET_MSG"));
    }

    private static CreditReportResult toReportResult(Map<String, Object> out) {
        Object tarif = out.get("P_TARIF");
        return new CreditReportResult(
                (String) out.get("P_RESULT"),
                (String) out.get("P_RET_MSG"),
                (String) out.get("P_TOKEN"),
                tarif != null ? ((Number) tarif).doubleValue() : null);
    }

    private static Date toSqlDate(LocalDate date) {
        return date != null ? Date.valueOf(date) : null;
    }
}
