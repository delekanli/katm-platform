package uz.katm.registry.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import uz.katm.registry.domain.OrgDto;
import uz.katm.registry.domain.dto.RegisterLeasingRequest;
import uz.katm.registry.domain.record.RegistrationResult;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.Types;
import java.util.List;
import java.util.Map;

/**
 * Регистрация и список лизинговых организаций (DATAS.PKG_LEASING).
 * Перенос gov.uz.katm.repo.leasing.LeasingRepositoryImpl. Смена статуса в монолите
 * закомментирована (не реализована) — здесь тоже отсутствует; сброс пароля — общий
 * {@link PasswordResetRepository} (PKG_USERS.ONLINE_PASSWORD_CHANGE).
 */
@Repository
public class LeasingRegistryRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcCall addCall;

    public LeasingRegistryRepository(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.addCall = new SimpleJdbcCall(dataSource)
                .withSchemaName("DATAS")
                .withCatalogName("PKG_LEASING")
                .withProcedureName("ADD_LEASING")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("P_NAME", Types.VARCHAR),
                        new SqlParameter("P_BEGIN_DATE", Types.DATE),
                        new SqlParameter("P_END_DATE", Types.DATE),
                        new SqlParameter("P_REGION", Types.VARCHAR),
                        new SqlParameter("P_LOCAL_REGION", Types.VARCHAR),
                        new SqlParameter("P_INN", Types.VARCHAR),
                        new SqlOutParameter("O_OUT_TEXT", Types.VARCHAR),
                        new SqlOutParameter("O_RESULT", Types.VARCHAR),
                        new SqlOutParameter("O_LOGIN", Types.VARCHAR),
                        new SqlOutParameter("O_PASSWORD", Types.VARCHAR)
                );
    }

    public List<OrgDto> findAll() {
        return jdbcTemplate.query(
                "select * from table(datas.Pkg_Leasing.Get_Leasing_List)",
                (rs, rowNum) -> new OrgDto(
                        rs.getString("CODE"),
                        rs.getString("NAME"),
                        rs.getString("REGION"),
                        rs.getString("LOCAL_REGION"),
                        null,
                        rs.getDate("BEGIN_DATE") != null ? rs.getDate("BEGIN_DATE").toLocalDate() : null,
                        rs.getDate("END_DATE") != null ? rs.getDate("END_DATE").toLocalDate() : null,
                        rs.getString("STATUS"),
                        null,
                        rs.getString("INN")
                ));
    }

    public RegistrationResult register(RegisterLeasingRequest req) {
        Map<String, Object> out = addCall.execute(new MapSqlParameterSource()
                .addValue("P_NAME", req.name())
                .addValue("P_BEGIN_DATE", Date.valueOf(req.contractStartDate()))
                .addValue("P_END_DATE", req.contractEndDate() != null ? Date.valueOf(req.contractEndDate()) : null)
                .addValue("P_REGION", req.region())
                .addValue("P_LOCAL_REGION", req.localRegion())
                .addValue("P_INN", req.tin()));
        return new RegistrationResult(
                (String) out.get("O_LOGIN"),
                (String) out.get("O_PASSWORD"),
                (String) out.get("O_RESULT"),
                (String) out.get("O_OUT_TEXT"));
    }
}
