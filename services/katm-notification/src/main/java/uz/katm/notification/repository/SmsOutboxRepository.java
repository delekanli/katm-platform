package uz.katm.notification.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import uz.katm.notification.domain.record.NotifyToSend;

import javax.sql.DataSource;
import java.sql.Types;
import java.util.List;

@Slf4j
@Repository
public class SmsOutboxRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcCall setInProgressCall;
    private final SimpleJdbcCall setSentCall;
    private final SimpleJdbcCall setFailedCall;

    public SmsOutboxRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);

        this.setInProgressCall = new SimpleJdbcCall(dataSource)
                .withCatalogName("notif_pkg")
                .withProcedureName("set_in_progress")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(new SqlParameter("p_notification_id", Types.INTEGER));

        this.setSentCall = new SimpleJdbcCall(dataSource)
                .withCatalogName("notif_pkg")
                .withProcedureName("set_sent")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(new SqlParameter("p_notification_id", Types.INTEGER));

        this.setFailedCall = new SimpleJdbcCall(dataSource)
                .withCatalogName("notif_pkg")
                .withProcedureName("set_failed")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(new SqlParameter("p_notification_id", Types.INTEGER));
    }

    public List<NotifyToSend> getPending() {
        return jdbcTemplate.query(
                "SELECT * FROM v_Notif_To_Send",
                (rs, rowNum) -> new NotifyToSend(
                        rs.getLong("ID"),
                        rs.getString("RECIPIENT"),
                        rs.getString("METHOD_SENDER"),
                        rs.getString("TYPE_SHORT_NAME"),
                        rs.getString("TEXT"),
                        rs.getString("ENTITY_TYPE"),
                        rs.getLong("ENTITY_ID"),
                        rs.getString("METHOD_ID"),
                        rs.getString("TYPE_ID"),
                        rs.getString("TYPE_TRANSLIT_NAME"),
                        rs.getString("SUBMETHOD_TYPE")
                )
        );
    }

    public void setInProgress(long id) {
        setInProgressCall.execute(new MapSqlParameterSource("p_notification_id", id));
    }

    public void setSent(long id) {
        setSentCall.execute(new MapSqlParameterSource("p_notification_id", id));
    }

    public void setFailed(long id) {
        setFailedCall.execute(new MapSqlParameterSource("p_notification_id", id));
    }
}
