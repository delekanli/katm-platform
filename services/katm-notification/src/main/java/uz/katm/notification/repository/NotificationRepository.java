package uz.katm.notification.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import uz.katm.notification.domain.record.NotificationHistoryItem;
import uz.katm.notification.domain.record.NotificationType;
import uz.katm.notification.domain.record.OtpResponse;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Repository
public class NotificationRepository {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedJdbcTemplate;
    private final SimpleJdbcCall sendOtpCall;
    private final SimpleJdbcCall subscriptionActivateCall;
    private final SimpleJdbcCall subscriptionSuspendCall;
    private final SimpleJdbcCall subscriptionStatusCall;
    private final SimpleJdbcCall activateFreezedCall;
    private final SimpleJdbcCall cancelFreezedCall;

    public NotificationRepository(DataSource dataSource, JdbcTemplate jdbcTemplate,
                                  NamedParameterJdbcTemplate namedJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedJdbcTemplate = namedJdbcTemplate;
        this.sendOtpCall = new SimpleJdbcCall(dataSource)
                .withSchemaName("DATAS")
                .withCatalogName("SMS_NOTIFICATION")
                .withProcedureName("SEND_OTP")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("PMSISDN", Types.VARCHAR),
                        new SqlOutParameter("OTP", Types.VARCHAR),
                        new SqlOutParameter("SUB_STATUS", Types.INTEGER)
                );

        this.subscriptionActivateCall = new SimpleJdbcCall(dataSource)
                .withSchemaName("DATAS")
                .withCatalogName("SMS_NOTIFICATION")
                .withFunctionName("SUBSCRIBTION_ACTIVATE")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("pClientid", Types.VARCHAR),
                        new SqlParameter("pmsisdn", Types.VARCHAR),
                        new SqlParameter("plang", Types.INTEGER)
                );

        this.subscriptionSuspendCall = new SimpleJdbcCall(dataSource)
                .withSchemaName("DATAS")
                .withCatalogName("SMS_NOTIFICATION")
                .withFunctionName("SUBSCRIBTION_SUSPEND")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("CLIENT_ID", Types.VARCHAR)
                );

        this.subscriptionStatusCall = new SimpleJdbcCall(dataSource)
                .withSchemaName("DATAS")
                .withCatalogName("SMS_NOTIFICATION")
                .withFunctionName("SUBSCRIBTION_CHK_STATUS")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("CLIENT_ID", Types.VARCHAR)
                );

        this.activateFreezedCall = new SimpleJdbcCall(dataSource)
                .withSchemaName("DATAS")
                .withCatalogName("PKG_FREEZE")
                .withFunctionName("ACTIVATE_SUBSCRIPTION")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("P_CLIENT_ID", Types.VARCHAR)
                );

        this.cancelFreezedCall = new SimpleJdbcCall(dataSource)
                .withSchemaName("DATAS")
                .withCatalogName("PKG_FREEZE")
                .withFunctionName("CANCEL_SUBSCRIPTION")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("P_CLIENT_ID", Types.VARCHAR)
                );
    }

    public OtpResponse sendOtp(String msisdn) {
        var params = new MapSqlParameterSource().addValue("PMSISDN", msisdn);
        Map<String, Object> out = sendOtpCall.execute(params);
        return new OtpResponse(
                String.valueOf(out.getOrDefault("SUB_STATUS", -1)),
                (String) out.get("OTP")
        );
    }

    public int activateSubscription(String clientId, String msisdn, int lang) {
        var params = new MapSqlParameterSource()
                .addValue("pClientid", clientId)
                .addValue("pmsisdn", msisdn)
                .addValue("plang", lang);
        return callIntFunction(subscriptionActivateCall, params);
    }

    public int suspendSubscription(String clientId) {
        var params = new MapSqlParameterSource().addValue("CLIENT_ID", clientId);
        return callIntFunction(subscriptionSuspendCall, params);
    }

    public int checkSubscriptionStatus(String clientId) {
        var params = new MapSqlParameterSource().addValue("CLIENT_ID", clientId);
        return callIntFunction(subscriptionStatusCall, params);
    }

    public int reactivateFreezed(String clientId) {
        var params = new MapSqlParameterSource().addValue("P_CLIENT_ID", clientId);
        return callIntFunction(activateFreezedCall, params);
    }

    public int cancelFreezed(String clientId) {
        var params = new MapSqlParameterSource().addValue("P_CLIENT_ID", clientId);
        return callIntFunction(cancelFreezedCall, params);
    }

    public List<NotificationHistoryItem> subscriptionHistory(String clientId, LocalDate dateFrom, LocalDate dateTo) {
        return namedJdbcTemplate.query(
                "SELECT * FROM table(DATAS.SMS_NOTIFICATION.subscribtion_history(:clientId, :dateFrom, :dateTo))",
                new MapSqlParameterSource()
                        .addValue("clientId", clientId)
                        .addValue("dateFrom", dateFrom != null ? Date.valueOf(dateFrom) : null)
                        .addValue("dateTo", dateTo != null ? Date.valueOf(dateTo) : null),
                (rs, rowNum) -> new NotificationHistoryItem(
                        toLdt(rs.getTimestamp("D_DATE")),
                        rs.getString("MSISDN"),
                        rs.getString("SMS_TEXT")));
    }

    public List<NotificationType> notificationTypes() {
        return jdbcTemplate.query(
                "select distinct sms_type, comment_t from datas.SMS_TEMPLATE order by sms_type",
                (rs, rowNum) -> new NotificationType(rs.getInt("SMS_TYPE"), rs.getString("COMMENT_T")));
    }

    private static LocalDateTime toLdt(Timestamp ts) {
        return ts != null ? ts.toLocalDateTime() : null;
    }

    private int callIntFunction(SimpleJdbcCall call, MapSqlParameterSource params) {
        BigDecimal result = call.executeFunction(BigDecimal.class, params);
        return result != null ? result.intValue() : -1;
    }
}
