package uz.katm.notification.repository;

import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import uz.katm.notification.domain.record.OtpResponse;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Types;
import java.util.Map;

@Repository
public class NotificationRepository {

    private final SimpleJdbcCall sendOtpCall;
    private final SimpleJdbcCall subscriptionActivateCall;
    private final SimpleJdbcCall subscriptionSuspendCall;
    private final SimpleJdbcCall subscriptionStatusCall;
    private final SimpleJdbcCall activateFreezedCall;
    private final SimpleJdbcCall cancelFreezedCall;

    public NotificationRepository(DataSource dataSource) {
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

    private int callIntFunction(SimpleJdbcCall call, MapSqlParameterSource params) {
        BigDecimal result = call.executeFunction(BigDecimal.class, params);
        return result != null ? result.intValue() : -1;
    }
}
