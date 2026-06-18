package uz.katm.notification.repository;

import oracle.jdbc.OracleConnection;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.SqlTypeValue;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import uz.katm.notification.domain.record.FreezeStatus;
import uz.katm.notification.domain.record.NotificationHistoryItem;
import uz.katm.notification.domain.record.NotificationType;
import uz.katm.notification.domain.record.OtpResponse;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Struct;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.SimpleDateFormat;
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
    private final SimpleJdbcCall subscriptionChangeLangCall;
    private final SimpleJdbcCall subscriptionChangeMsisdnCall;
    private final SimpleJdbcCall massSendCall;
    private final SimpleJdbcCall sendSmsOtpCall;
    private final SimpleJdbcCall checkSubscriptionStatusDateCall;
    private final SimpleJdbcCall creditActivateCall;
    private final SimpleJdbcCall creditStopCall;
    private final SimpleJdbcCall creditStatusCall;

    private static final String UTC_DATE = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";

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

        this.subscriptionChangeLangCall = new SimpleJdbcCall(dataSource)
                .withSchemaName("DATAS")
                .withCatalogName("SMS_NOTIFICATION")
                .withFunctionName("subscribtion_change_lang")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("pClientid", Types.VARCHAR),
                        new SqlParameter("plang", Types.INTEGER)
                );

        this.subscriptionChangeMsisdnCall = new SimpleJdbcCall(dataSource)
                .withSchemaName("DATAS")
                .withCatalogName("SMS_NOTIFICATION")
                .withFunctionName("subscribtion_change_MSISDN")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("pClientid", Types.VARCHAR),
                        new SqlParameter("pMSISDN", Types.VARCHAR)
                );

        this.massSendCall = new SimpleJdbcCall(dataSource)
                .withSchemaName("DATAS")
                .withCatalogName("SMS_NOTIFICATION")
                .withProcedureName("MASS_SEND")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("P_CLIENT_ID_ARRAY", Types.ARRAY, "DATAS.T_CLIENT_ID_ARRAY"),
                        new SqlParameter("P_SMS_TYPE_ID", Types.INTEGER),
                        new SqlOutParameter("SUB_STATUS", Types.INTEGER)
                );

        // OTP для привязки карты: DATAS.SEND_OTP_SMS. OUT-параметры объявлены первыми (как в монолите).
        this.sendSmsOtpCall = new SimpleJdbcCall(dataSource)
                .withSchemaName("DATAS")
                .withProcedureName("SEND_OTP_SMS")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlOutParameter("P_RESULT", Types.VARCHAR),
                        new SqlOutParameter("P_RET_MSG", Types.VARCHAR),
                        new SqlParameter("P_PHONE", Types.VARCHAR),
                        new SqlParameter("P_TYPE", Types.INTEGER),
                        new SqlParameter("P_LANG", Types.INTEGER)
                );

        // Статус FREEZE с датой: функция PKG_FREEZE.Get_Subscription_State2 (возврат + OUT O_DATE).
        // Используется доступ к метаданным БД (как в монолите) — функция с возвратом и OUT-параметром.
        SimpleJdbcCall statusDateCall = new SimpleJdbcCall(dataSource)
                .withSchemaName("DATAS")
                .withCatalogName("PKG_FREEZE")
                .withFunctionName("Get_Subscription_State2")
                .useInParameterNames("p_Client_Id");
        statusDateCall.setReturnValueRequired(true);
        this.checkSubscriptionStatusDateCall = statusDateCall;

        // Подписки на уведомления об оформлении кредита (DATAS.SMS_NOTIFY_CONTRACT.*) —
        // ОТДЕЛЬНЫЕ от SMS-подписок (SMS_NOTIFICATION.SUBSCRIBTION_*). Перенос
        // gov.uz.katm.db.orcl.credit.notification.{Activate,Stop,Status}.
        this.creditActivateCall = new SimpleJdbcCall(dataSource)
                .withSchemaName("DATAS")
                .withCatalogName("SMS_NOTIFY_CONTRACT")
                .withFunctionName("activate")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(new SqlParameter("p_Client_Id", Types.VARCHAR));

        this.creditStopCall = new SimpleJdbcCall(dataSource)
                .withSchemaName("DATAS")
                .withCatalogName("SMS_NOTIFY_CONTRACT")
                .withFunctionName("stop")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(new SqlParameter("p_Client_Id", Types.VARCHAR));

        this.creditStatusCall = new SimpleJdbcCall(dataSource)
                .withSchemaName("DATAS")
                .withCatalogName("SMS_NOTIFY_CONTRACT")
                .withFunctionName("status")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(new SqlParameter("p_Client_Id", Types.VARCHAR));
    }

    /** Подключение подписки на уведомления об оформлении кредита (SMS_NOTIFY_CONTRACT.activate). */
    public int creditNotificationActivate(String clientId) {
        return callIntFunction(creditActivateCall, new MapSqlParameterSource().addValue("p_Client_Id", clientId));
    }

    /** Приостановка кредитной подписки (SMS_NOTIFY_CONTRACT.stop). */
    public int creditNotificationSuspend(String clientId) {
        return callIntFunction(creditStopCall, new MapSqlParameterSource().addValue("p_Client_Id", clientId));
    }

    /** Статус кредитной подписки (SMS_NOTIFY_CONTRACT.status): 1=подключена, иначе отключена. */
    public int creditNotificationStatus(String clientId) {
        return callIntFunction(creditStatusCall, new MapSqlParameterSource().addValue("p_Client_Id", clientId));
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

    public int subscriptionChangeLang(String clientId, int lang) {
        var params = new MapSqlParameterSource()
                .addValue("pClientid", clientId)
                .addValue("plang", lang);
        return callIntFunction(subscriptionChangeLangCall, params);
    }

    public int subscriptionChangeMsisdn(String clientId, String msisdn) {
        var params = new MapSqlParameterSource()
                .addValue("pClientid", clientId)
                .addValue("pMSISDN", msisdn);
        return callIntFunction(subscriptionChangeMsisdnCall, params);
    }

    /**
     * Массовая рассылка (DATAS.SMS_NOTIFICATION.MASS_SEND): clientIds передаётся Oracle-массивом
     * структур DATAS.O_CLIENT_ID (тип DATAS.T_CLIENT_ID_ARRAY). Возвращает SUB_STATUS.
     */
    public int massSend(List<String> clientIds, int notificationType) {
        SqlTypeValue arrayValue = (ps, paramIndex, sqlType, typeName) -> {
            OracleConnection oracle = ps.getConnection().unwrap(OracleConnection.class);
            Struct[] structs = new Struct[clientIds.size()];
            for (int i = 0; i < clientIds.size(); i++) {
                structs[i] = oracle.createStruct("DATAS.O_CLIENT_ID", new Object[]{clientIds.get(i)});
            }
            ps.setArray(paramIndex, oracle.createARRAY(typeName, structs));
        };
        var params = new MapSqlParameterSource()
                .addValue("P_CLIENT_ID_ARRAY", arrayValue)
                .addValue("P_SMS_TYPE_ID", notificationType);
        Map<String, Object> out = massSendCall.execute(params);
        Object status = out.get("SUB_STATUS");
        return status != null ? ((Number) status).intValue() : -1;
    }

    public OtpResponse sendSmsOtp(String phone, int type, int lang) {
        var params = new MapSqlParameterSource()
                .addValue("P_PHONE", phone)
                .addValue("P_TYPE", type)
                .addValue("P_LANG", lang);
        Map<String, Object> out = sendSmsOtpCall.execute(params);
        String result = String.valueOf(out.get("P_RESULT"));
        // Успех (P_RESULT==1): OTP возвращается в P_RET_MSG.
        return new OtpResponse(result, "1".equals(result) ? (String) out.get("P_RET_MSG") : null);
    }

    public FreezeStatus checkSubscriptionStatusDate(String clientId) {
        Map<String, Object> out = checkSubscriptionStatusDateCall.execute(
                new MapSqlParameterSource().addValue("p_Client_Id", clientId));
        Integer status = null;
        String date = null;
        for (Map.Entry<String, Object> e : out.entrySet()) {
            if (e.getKey().equalsIgnoreCase("O_DATE")) {
                if (e.getValue() instanceof java.util.Date d) {
                    date = new SimpleDateFormat(UTC_DATE).format(d);
                }
            } else if (e.getValue() instanceof Number n) {
                status = n.intValue();
            }
        }
        return new FreezeStatus(status, date);
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
