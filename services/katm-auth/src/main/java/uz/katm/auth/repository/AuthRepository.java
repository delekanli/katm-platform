package uz.katm.auth.repository;

import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import uz.katm.auth.domain.record.EmployeeLoginResult;
import uz.katm.auth.domain.record.OperationResult;
import uz.katm.auth.domain.record.ReportAccess;

import javax.sql.DataSource;
import java.sql.Types;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Repository
public class AuthRepository {

    private final NamedParameterJdbcTemplate namedJdbcTemplate;
    private final SimpleJdbcCall checkAccessCall;
    private final SimpleJdbcCall employeeLoginCall;
    private final SimpleJdbcCall employeeLogoutCall;

    public AuthRepository(DataSource dataSource, NamedParameterJdbcTemplate namedJdbcTemplate) {
        this.namedJdbcTemplate = namedJdbcTemplate;
        this.checkAccessCall = new SimpleJdbcCall(dataSource)
                .withSchemaName("DATAS")
                .withCatalogName("PKG_USERS")
                .withProcedureName("USER_ACCESS_CHECK")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("P_HEAD",       Types.VARCHAR),
                        new SqlParameter("P_CODE",       Types.VARCHAR),
                        new SqlParameter("P_IP_ADDRESS", Types.VARCHAR),
                        new SqlOutParameter("P_RESULT",  Types.INTEGER),
                        new SqlOutParameter("P_RET_MSG", Types.VARCHAR)
                );
        this.employeeLoginCall = new SimpleJdbcCall(dataSource)
                .withSchemaName("DATAS")
                .withCatalogName("PKG_USERS")
                .withProcedureName("USER_AUTHORIZATION")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("P_LOGIN",     Types.VARCHAR),
                        new SqlParameter("P_PASSWORD",  Types.VARCHAR),
                        new SqlOutParameter("P_RESULT",    Types.VARCHAR),
                        new SqlOutParameter("P_RET_MSG",   Types.VARCHAR),
                        new SqlOutParameter("P_USER_ID",   Types.INTEGER),
                        new SqlOutParameter("P_ROLE_ID",   Types.INTEGER),
                        new SqlOutParameter("P_ROLE_NAME", Types.VARCHAR)
                );
        this.employeeLogoutCall = new SimpleJdbcCall(dataSource)
                .withSchemaName("DATAS")
                .withCatalogName("PKG_USERS")
                .withProcedureName("USER_SESSION_LOGOUT")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("P_SESSION_ID", Types.VARCHAR),
                        new SqlParameter("P_USER_ID",    Types.INTEGER),
                        new SqlParameter("P_INTERNAL",   Types.INTEGER),
                        new SqlOutParameter("P_RESULT",  Types.VARCHAR),
                        new SqlOutParameter("P_RET_MSG", Types.VARCHAR)
                );
    }

    public EmployeeLoginResult employeeLogin(String login, String password) {
        Map<String, Object> out = employeeLoginCall.execute(new MapSqlParameterSource()
                .addValue("P_LOGIN", login)
                .addValue("P_PASSWORD", password));
        return new EmployeeLoginResult(
                (String) out.get("P_RESULT"),
                (String) out.get("P_RET_MSG"),
                toInt(out.get("P_USER_ID")),
                toInt(out.get("P_ROLE_ID")),
                (String) out.get("P_ROLE_NAME"));
    }

    public OperationResult employeeLogout(Integer userId, String sessionId) {
        Map<String, Object> out = employeeLogoutCall.execute(new MapSqlParameterSource()
                .addValue("P_SESSION_ID", sessionId)
                .addValue("P_USER_ID", userId)
                .addValue("P_INTERNAL", null));
        return new OperationResult((String) out.get("P_RESULT"), (String) out.get("P_RET_MSG"));
    }

    public List<ReportAccess> getReportAccess(Integer userId) {
        return namedJdbcTemplate.query(
                "select * from table(DATAS.PKG_USERS.GET_REPORTS_LIST(:userId))",
                new MapSqlParameterSource("userId", userId),
                (rs, rowNum) -> new ReportAccess(
                        (Integer) rs.getObject("REPORT_ID"),
                        rs.getString("REPORT_NAME")));
    }

    private static Integer toInt(Object v) {
        return v != null ? ((Number) v).intValue() : null;
    }

    public AccessCheckResult checkAccess(String head, String code, String ip) {
        var params = new MapSqlParameterSource()
                .addValue("P_HEAD",       head)
                .addValue("P_CODE",       code)
                .addValue("P_IP_ADDRESS", ip);

        Map<String, Object> out = checkAccessCall.execute(params);
        int result = ((Number) Objects.requireNonNull(out.get("P_RESULT"), "P_RESULT is null")).intValue();
        return new AccessCheckResult(result != 0, (String) out.get("P_RET_MSG"));
    }

    public record AccessCheckResult(boolean allowed, String message) {}
}
