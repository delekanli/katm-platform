package uz.katm.client.repository;

import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import uz.katm.client.domain.record.AddCreditBanRequest;
import uz.katm.client.domain.record.BanStatusResponse;
import uz.katm.client.domain.record.CreditBanHistoryItem;
import uz.katm.client.domain.record.CreditBanInfo;
import uz.katm.client.domain.record.DeactivateCreditBanRequest;
import uz.katm.client.domain.record.PassportDataRequest;
import uz.katm.client.domain.record.ProcedureResult;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Repository
public class ClientRepository {


    private final NamedParameterJdbcTemplate namedJdbcTemplate;
    private final SimpleJdbcCall savePassportCall;
    private final SimpleJdbcCall updatePasswordCall;
    private final SimpleJdbcCall checkBanStatusCall;
    private final SimpleJdbcCall addCreditBanCall;
    private final SimpleJdbcCall deactivateCreditBanCall;

    public ClientRepository(DataSource dataSource, NamedParameterJdbcTemplate namedJdbcTemplate) {
        this.namedJdbcTemplate = namedJdbcTemplate;
        this.savePassportCall = new SimpleJdbcCall(dataSource)
                .withSchemaName("DATAS")
                .withCatalogName("PKG_CLIENTS")
                .withProcedureName("ADD_MIP_PASSPORT_DATA")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("P_INN", Types.VARCHAR),
                        new SqlParameter("P_NAME", Types.VARCHAR),
                        new SqlParameter("P_LAST_NAME", Types.VARCHAR),
                        new SqlParameter("P_PATRONYMIC", Types.VARCHAR),
                        new SqlParameter("P_IDENTITY_CARD_SERIAL", Types.VARCHAR),
                        new SqlParameter("P_IDENTITY_CARD_NUMBER", Types.VARCHAR),
                        new SqlParameter("P_IDENTITY_CARD_DATE", Types.DATE),
                        new SqlParameter("P_IDENTITY_CARD_EXPIRE", Types.DATE),
                        new SqlParameter("P_RESIDENT", Types.INTEGER),
                        new SqlParameter("P_SEX", Types.INTEGER),
                        new SqlParameter("P_IS_ALIVE", Types.INTEGER),
                        new SqlParameter("P_BIRTH_COUNTRY", Types.VARCHAR),
                        new SqlParameter("P_BIRTH_PLACE", Types.VARCHAR),
                        new SqlParameter("P_BIRTH_DATE", Types.DATE),
                        new SqlParameter("P_PINFL", Types.VARCHAR),
                        new SqlOutParameter("P_RESULT", Types.VARCHAR),
                        new SqlOutParameter("P_RET_MSG", Types.VARCHAR)
                );

        this.updatePasswordCall = new SimpleJdbcCall(dataSource)
                .withSchemaName("DATAS")
                .withCatalogName("PKG_CLIENTS")
                .withProcedureName("CHANGE_CLIENT_PASSWORD")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("P_CLIENT_ID", Types.VARCHAR),
                        new SqlParameter("P_NEW_PASSWORD", Types.VARCHAR),
                        new SqlOutParameter("P_RESULT", Types.VARCHAR),
                        new SqlOutParameter("P_RET_MSG", Types.VARCHAR)
                );

        this.checkBanStatusCall = new SimpleJdbcCall(dataSource)
                .withSchemaName("DATAS")
                .withCatalogName("CREDIT_BANS")
                .withProcedureName("CHECK_CREDIT_BAN_STATUS")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("P_HEAD", Types.VARCHAR),
                        new SqlParameter("P_CODE", Types.VARCHAR),
                        new SqlParameter("P_SUBJECT_ID", Types.VARCHAR),
                        new SqlParameter("P_IDENTIFIER", Types.VARCHAR),
                        new SqlParameter("P_CLIENT_IP", Types.VARCHAR),
                        new SqlParameter("P_ADDITIONAL_INFO", Types.VARCHAR),
                        new SqlOutParameter("P_IS_BANNED", Types.INTEGER),
                        new SqlOutParameter("P_RESULT", Types.VARCHAR),
                        new SqlOutParameter("P_RET_MSG", Types.VARCHAR)
                );

        this.addCreditBanCall = new SimpleJdbcCall(dataSource)
                .withSchemaName("DATAS")
                .withCatalogName("CREDIT_BANS")
                .withProcedureName("ADD_CREDIT_BAN")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("P_HEAD", Types.VARCHAR),
                        new SqlParameter("P_CODE", Types.VARCHAR),
                        new SqlParameter("P_SUBJECT_ID", Types.VARCHAR),
                        new SqlParameter("P_IDENTIFIER", Types.VARCHAR),
                        new SqlParameter("P_IDEN_DATE", Types.DATE),
                        new SqlParameter("P_FULL_NAME", Types.VARCHAR),
                        new SqlParameter("P_REASON", Types.VARCHAR),
                        new SqlParameter("P_END_DATE", Types.DATE),
                        new SqlParameter("P_CLIENT_IP", Types.VARCHAR),
                        new SqlOutParameter("P_RESULT", Types.VARCHAR),
                        new SqlOutParameter("P_RET_MSG", Types.VARCHAR)
                );

        this.deactivateCreditBanCall = new SimpleJdbcCall(dataSource)
                .withSchemaName("DATAS")
                .withCatalogName("CREDIT_BANS")
                .withProcedureName("DEACTIVATE_CREDIT_BAN")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("P_HEAD", Types.VARCHAR),
                        new SqlParameter("P_CODE", Types.VARCHAR),
                        new SqlParameter("P_SUBJECT_ID", Types.VARCHAR),
                        new SqlParameter("P_IDENTIFIER", Types.VARCHAR),
                        new SqlParameter("P_REASON", Types.VARCHAR),
                        new SqlParameter("P_CLIENT_IP", Types.VARCHAR),
                        new SqlOutParameter("P_RESULT", Types.VARCHAR),
                        new SqlOutParameter("P_RET_MSG", Types.VARCHAR)
                );
    }

    public ProcedureResult savePassportData(PassportDataRequest req) {
        var params = new MapSqlParameterSource()
                .addValue("P_INN", req.tin())
                .addValue("P_NAME", req.firstName())
                .addValue("P_LAST_NAME", req.lastName())
                .addValue("P_PATRONYMIC", req.middleName())
                .addValue("P_IDENTITY_CARD_SERIAL", req.docSeries())
                .addValue("P_IDENTITY_CARD_NUMBER", req.docNumber())
                .addValue("P_IDENTITY_CARD_DATE", Date.valueOf(req.docIssueDate()))
                .addValue("P_IDENTITY_CARD_EXPIRE", Date.valueOf(req.docExpireDate()))
                .addValue("P_RESIDENT", req.resident())
                .addValue("P_SEX", req.sex())
                .addValue("P_IS_ALIVE", req.isAlive())
                .addValue("P_BIRTH_COUNTRY", req.birthCountry())
                .addValue("P_BIRTH_PLACE", req.birthPlace())
                .addValue("P_BIRTH_DATE", Date.valueOf(req.birthDate()))
                .addValue("P_PINFL", req.pinfl());
        return toResult(savePassportCall.execute(params));
    }

    public ProcedureResult updatePassword(String clientId, String newPassword) {
        var params = new MapSqlParameterSource()
                .addValue("P_CLIENT_ID", clientId)
                .addValue("P_NEW_PASSWORD", newPassword);
        return toResult(updatePasswordCall.execute(params));
    }

    public BanStatusResponse checkCreditBanStatus(
            String head, String code,
            String subjectId, String identifier, String clientIp, String additionalInfo) {
        var params = new MapSqlParameterSource()
                .addValue("P_HEAD", head)
                .addValue("P_CODE", code)
                .addValue("P_SUBJECT_ID", subjectId)
                .addValue("P_IDENTIFIER", identifier)
                .addValue("P_CLIENT_IP", clientIp)
                .addValue("P_ADDITIONAL_INFO", additionalInfo);
        Map<String, Object> out = checkBanStatusCall.execute(params);
        return new BanStatusResponse(
                String.valueOf(out.getOrDefault("P_RESULT", "-1")),
                (String) out.get("P_RET_MSG"),
                (Integer) out.get("P_IS_BANNED")
        );
    }

    public ProcedureResult addCreditBan(String head, String code, String clientIp, AddCreditBanRequest req) {
        var params = new MapSqlParameterSource()
                .addValue("P_HEAD", head)
                .addValue("P_CODE", code)
                .addValue("P_SUBJECT_ID", req.subjectId())
                .addValue("P_IDENTIFIER", req.identifier())
                .addValue("P_IDEN_DATE", req.idenDate() != null ? Date.valueOf(req.idenDate()) : null)
                .addValue("P_FULL_NAME", req.fullName())
                .addValue("P_REASON", req.reason())
                .addValue("P_END_DATE", req.endDate() != null ? Date.valueOf(req.endDate()) : null)
                .addValue("P_CLIENT_IP", clientIp);
        return toResult(addCreditBanCall.execute(params));
    }

    public ProcedureResult deactivateCreditBan(String head, String code, String clientIp, DeactivateCreditBanRequest req) {
        var params = new MapSqlParameterSource()
                .addValue("P_HEAD", head)
                .addValue("P_CODE", code)
                .addValue("P_SUBJECT_ID", req.subjectId())
                .addValue("P_IDENTIFIER", req.identifier())
                .addValue("P_REASON", req.reason())
                .addValue("P_CLIENT_IP", clientIp);
        return toResult(deactivateCreditBanCall.execute(params));
    }

    public List<CreditBanHistoryItem> getCreditBanHistory(String identifier, String subjectId) {
        return namedJdbcTemplate.query(
                "select cb.head, cb.code, cbl.hash_val, cbl.action_at, " +
                        "case when cbl.action_type = 'INSERT' then 1 else 0 end as is_active " +
                        "from datas.credit_ban cb " +
                        "left join datas.credit_ban_log cbl on cbl.ban_id = cb.ban_id " +
                        "where cb.identifier = :identifier and cb.subject_id = :subjectId " +
                        "order by cbl.action_at desc",
                new MapSqlParameterSource()
                        .addValue("identifier", identifier)
                        .addValue("subjectId", subjectId),
                (rs, rowNum) -> new CreditBanHistoryItem(
                        rs.getString("HEAD"),
                        rs.getString("CODE"),
                        rs.getString("HASH_VAL"),
                        toLdt(rs.getTimestamp("ACTION_AT")),
                        rs.getInt("IS_ACTIVE")));
    }

    public CreditBanInfo getCreditBanInfoByHash(String hash) {
        try {
            return namedJdbcTemplate.queryForObject(
                    "select cb.identifier, cb.full_name, cbl.action_at as action_date, " +
                            "case when cbl.action_type = 'INSERT' then 1 else 0 end as status " +
                            "from datas.credit_ban cb " +
                            "left join datas.credit_ban_log cbl on cbl.ban_id = cb.ban_id " +
                            "where cbl.hash_val = :hash",
                    new MapSqlParameterSource("hash", hash),
                    (rs, rowNum) -> new CreditBanInfo(
                            rs.getString("IDENTIFIER"),
                            rs.getString("FULL_NAME"),
                            toLdt(rs.getTimestamp("ACTION_DATE")),
                            rs.getInt("STATUS")));
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            return null;
        }
    }

    private static LocalDateTime toLdt(Timestamp ts) {
        return ts != null ? ts.toLocalDateTime() : null;
    }

    private ProcedureResult toResult(Map<String, Object> out) {
        return new ProcedureResult(
                String.valueOf(out.getOrDefault("P_RESULT", "-1")),
                (String) out.get("P_RET_MSG")
        );
    }
}
