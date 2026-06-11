package uz.katm.ucin.repository;

import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import uz.katm.ucin.domain.dto.InitiateClientRequest;
import uz.katm.ucin.domain.dto.InitiateLegalClientRequest;
import uz.katm.ucin.domain.dto.KatmSirRequest;
import uz.katm.ucin.domain.record.ClientDataResult;
import uz.katm.ucin.domain.record.ClientResult;
import uz.katm.ucin.domain.record.KatmSirResult;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.Types;
import java.time.LocalDate;
import java.util.Map;

/**
 * UCIN client-домен (DATAS.PKG_WEB_INDIVIDUAL, фаза 1).
 * Перенос gov.uz.katm.repo.ucin.UcinClientRepositoryImpl (client-часть).
 */
@Repository
public class UcinClientRepository {

    private final NamedParameterJdbcTemplate namedJdbcTemplate;
    private final SimpleJdbcCall initClientCall;
    private final SimpleJdbcCall initLegalClientCall;
    private final SimpleJdbcCall getDataByClientCall;
    private final SimpleJdbcCall katmSirCall;

    public UcinClientRepository(NamedParameterJdbcTemplate namedJdbcTemplate, DataSource dataSource) {
        this.namedJdbcTemplate = namedJdbcTemplate;
        this.initClientCall = new SimpleJdbcCall(dataSource)
                .withSchemaName("DATAS").withCatalogName("PKG_WEB_INDIVIDUAL")
                .withProcedureName("INITIATE_CLIENT")
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
                        new SqlOutParameter("P_RESULT", Types.VARCHAR),
                        new SqlOutParameter("P_RET_MSG", Types.VARCHAR),
                        new SqlOutParameter("P_CLIENT_ID", Types.VARCHAR)
                );
        this.initLegalClientCall = new SimpleJdbcCall(dataSource)
                .withSchemaName("DATAS").withCatalogName("PKG_WEB_INDIVIDUAL")
                .withProcedureName("INITIATE_LEGAL_CLIENT")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("P_INN", Types.VARCHAR),
                        new SqlParameter("P_REGION", Types.VARCHAR),
                        new SqlParameter("P_LOCAL_REGION", Types.VARCHAR),
                        new SqlParameter("P_FULL_NAME", Types.VARCHAR),
                        new SqlParameter("P_POST_ADDRESS", Types.VARCHAR),
                        new SqlParameter("P_PHONE", Types.VARCHAR),
                        new SqlParameter("P_RESIDENT", Types.INTEGER),
                        new SqlParameter("P_CLIENT_TYPE_ID", Types.INTEGER),
                        new SqlParameter("P_OWNER_FORM", Types.VARCHAR),
                        new SqlParameter("P_GOVERMENT", Types.INTEGER),
                        new SqlParameter("P_OKPO", Types.VARCHAR),
                        new SqlParameter("P_HBRANCH", Types.VARCHAR),
                        new SqlParameter("P_INDUSTRY", Types.VARCHAR),
                        new SqlParameter("P_IP", Types.VARCHAR),
                        new SqlOutParameter("P_RESULT", Types.VARCHAR),
                        new SqlOutParameter("P_RET_MSG", Types.VARCHAR),
                        new SqlOutParameter("P_CLIENT_ID", Types.VARCHAR)
                );
        this.getDataByClientCall = new SimpleJdbcCall(dataSource)
                .withSchemaName("DATAS").withCatalogName("PKG_WEB_INDIVIDUAL")
                .withProcedureName("GET_DATA_BY_CLIENT_ID")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("P_CLIENT_ID", Types.VARCHAR),
                        new SqlParameter("P_REPORT_ID", Types.VARCHAR),
                        new SqlOutParameter("P_RESULT", Types.VARCHAR),
                        new SqlOutParameter("P_RET_MSG", Types.VARCHAR),
                        new SqlOutParameter("P_PINFL", Types.VARCHAR),
                        new SqlOutParameter("P_INN", Types.VARCHAR),
                        new SqlOutParameter("P_SERIE", Types.VARCHAR),
                        new SqlOutParameter("P_NUMBER", Types.VARCHAR),
                        new SqlOutParameter("P_IS_LEGAL", Types.INTEGER)
                );
        this.katmSirCall = new SimpleJdbcCall(dataSource)
                .withSchemaName("DATAS").withCatalogName("SMS_NOTIFICATION")
                .withProcedureName("GET_CLIENT_ID")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("P_IDENTITY_CARD_SERIAL", Types.VARCHAR),
                        new SqlParameter("P_IDENTITY_CARD_NUMBER", Types.VARCHAR),
                        new SqlParameter("P_PINFL", Types.VARCHAR),
                        new SqlParameter("P_DATE_BIRTH", Types.DATE),
                        new SqlOutParameter("P_CLIENT_ID", Types.VARCHAR),
                        new SqlOutParameter("P_ON_OFF_FLAG", Types.INTEGER),
                        new SqlOutParameter("P_LANG", Types.INTEGER),
                        new SqlOutParameter("P_MSISDN", Types.VARCHAR),
                        new SqlOutParameter("P_ERR_CODE", Types.INTEGER)
                );
    }

    public ClientResult initClient(InitiateClientRequest r) {
        Map<String, Object> out = initClientCall.execute(new MapSqlParameterSource()
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
                .addValue("P_IP", r.ip()));
        return new ClientResult((String) out.get("P_CLIENT_ID"),
                (String) out.get("P_RESULT"), (String) out.get("P_RET_MSG"));
    }

    public ClientResult initLegalClient(InitiateLegalClientRequest r) {
        Map<String, Object> out = initLegalClientCall.execute(new MapSqlParameterSource()
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
        return new ClientResult((String) out.get("P_CLIENT_ID"),
                (String) out.get("P_RESULT"), (String) out.get("P_RET_MSG"));
    }

    public ClientDataResult getClientById(String clientId, String reportId) {
        Map<String, Object> out = getDataByClientCall.execute(new MapSqlParameterSource()
                .addValue("P_CLIENT_ID", clientId)
                .addValue("P_REPORT_ID", reportId));
        Object legal = out.get("P_IS_LEGAL");
        return new ClientDataResult(
                (String) out.get("P_PINFL"),
                (String) out.get("P_INN"),
                (String) out.get("P_SERIE"),
                (String) out.get("P_NUMBER"),
                legal != null ? ((Number) legal).intValue() : null,
                (String) out.get("P_RESULT"),
                (String) out.get("P_RET_MSG"));
    }

    public Double getClientTariff(String clientId, Integer reportId) {
        return namedJdbcTemplate.queryForObject(
                "SELECT DATAS.PKG_WEB_INDIVIDUAL.get_web_claim_tariff(:clientId, :reportId) FROM DUAL",
                new MapSqlParameterSource()
                        .addValue("clientId", clientId)
                        .addValue("reportId", reportId),
                Double.class);
    }

    public KatmSirResult getKatmSir(KatmSirRequest r) {
        Map<String, Object> out = katmSirCall.execute(new MapSqlParameterSource()
                .addValue("P_IDENTITY_CARD_SERIAL", r.identityCardSerial())
                .addValue("P_IDENTITY_CARD_NUMBER", r.identityCardNumber())
                .addValue("P_PINFL", r.pinfl())
                .addValue("P_DATE_BIRTH", toSqlDate(r.dateBirth())));
        return new KatmSirResult(
                (String) out.get("P_CLIENT_ID"),
                toInt(out.get("P_ON_OFF_FLAG")),
                toInt(out.get("P_LANG")),
                (String) out.get("P_MSISDN"),
                toInt(out.get("P_ERR_CODE")));
    }

    private static Integer toInt(Object v) {
        return v != null ? ((Number) v).intValue() : null;
    }

    private static Date toSqlDate(LocalDate date) {
        return date != null ? Date.valueOf(date) : null;
    }
}
