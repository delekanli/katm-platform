package uz.katm.registry.repository;

import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import uz.katm.registry.domain.record.PasswordResetResult;

import javax.sql.DataSource;
import java.sql.Types;
import java.util.Map;

/**
 * Сброс пароля организации (общая процедура DATAS.PKG_USERS.ONLINE_PASSWORD_CHANGE
 * для страховых и ритейлеров — перенос InsurancePasswordChange/OnlinePasswordChange).
 */
@Repository
public class PasswordResetRepository {

    private final SimpleJdbcCall resetCall;

    public PasswordResetRepository(DataSource dataSource) {
        this.resetCall = new SimpleJdbcCall(dataSource)
                .withSchemaName("DATAS")
                .withCatalogName("PKG_USERS")
                .withProcedureName("ONLINE_PASSWORD_CHANGE")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("P_LOGIN", Types.VARCHAR),
                        new SqlOutParameter("P_PASSWORD", Types.VARCHAR),
                        new SqlOutParameter("P_RESULT", Types.VARCHAR),
                        new SqlOutParameter("P_RET_MSG", Types.VARCHAR)
                );
    }

    public PasswordResetResult resetPassword(String login) {
        Map<String, Object> out = resetCall.execute(new MapSqlParameterSource()
                .addValue("P_LOGIN", login));
        return new PasswordResetResult(
                (String) out.get("P_PASSWORD"),
                (String) out.get("P_RESULT"),
                (String) out.get("P_RET_MSG"));
    }
}
