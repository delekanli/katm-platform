package uz.katm.auth.repository;

import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Types;
import java.util.Map;
import java.util.Objects;

@Repository
public class AuthRepository {

    private final SimpleJdbcCall checkAccessCall;

    public AuthRepository(DataSource dataSource) {
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
