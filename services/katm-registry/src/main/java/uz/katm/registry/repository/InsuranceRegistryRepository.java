package uz.katm.registry.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import uz.katm.registry.domain.OrgDto;
import uz.katm.registry.domain.dto.RegisterInsuranceRequest;
import uz.katm.registry.domain.record.OperationResult;
import uz.katm.registry.domain.record.RegistrationResult;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.Types;
import java.util.List;
import java.util.Map;

/**
 * Регистрация и управление страховыми организациями (DATAS.PKG_INSURANCES).
 * Перенос gov.uz.katm.repo.insurance.InsuranceRepositoryImpl (reg/edit-часть).
 */
@Repository
public class InsuranceRegistryRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcCall addCall;
    private final SimpleJdbcCall changeStatusCall;

    public InsuranceRegistryRepository(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.addCall = new SimpleJdbcCall(dataSource)
                .withSchemaName("DATAS")
                .withCatalogName("PKG_INSURANCES")
                .withProcedureName("ADD_INSURANCE")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("P_SUG_TYPE", Types.INTEGER),
                        new SqlParameter("P_SUG_NAME", Types.VARCHAR),
                        new SqlParameter("P_START_DATE", Types.DATE),
                        new SqlParameter("P_REGION", Types.VARCHAR),
                        new SqlParameter("P_LOCAL_REGION", Types.VARCHAR),
                        new SqlParameter("P_ADDRESS", Types.VARCHAR),
                        new SqlOutParameter("P_RESULT", Types.VARCHAR),
                        new SqlOutParameter("P_RET_MSG", Types.VARCHAR),
                        new SqlOutParameter("P_LOGIN", Types.VARCHAR),
                        new SqlOutParameter("P_PASSWORD", Types.VARCHAR)
                );
        this.changeStatusCall = new SimpleJdbcCall(dataSource)
                .withSchemaName("DATAS")
                .withCatalogName("PKG_INSURANCES")
                .withProcedureName("CHANGE_INSURANCE_STATUS")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("P_CODE", Types.VARCHAR),
                        new SqlParameter("P_IN_TEST", Types.INTEGER),
                        new SqlParameter("P_STATE", Types.INTEGER),
                        new SqlOutParameter("P_RESULT", Types.VARCHAR),
                        new SqlOutParameter("P_RET_MSG", Types.VARCHAR)
                );
    }

    public List<OrgDto> findAll() {
        return jdbcTemplate.query(
                "select * from table(datas.pkg_insurances.get_insurances_list)",
                (rs, rowNum) -> new OrgDto(
                        rs.getString("CODE"),
                        rs.getString("NAME"),
                        rs.getString("REGION"),
                        rs.getString("LOCAL_REGION"),
                        rs.getString("ADDRESS"),
                        rs.getDate("START_DATE") != null ? rs.getDate("START_DATE").toLocalDate() : null,
                        rs.getDate("END_DATE") != null ? rs.getDate("END_DATE").toLocalDate() : null,
                        rs.getString("STATUS"),
                        rs.getString("IN_TEST"),
                        null
                ));
    }

    public RegistrationResult register(RegisterInsuranceRequest req) {
        Map<String, Object> out = addCall.execute(new MapSqlParameterSource()
                .addValue("P_SUG_TYPE", req.type())
                .addValue("P_SUG_NAME", req.name())
                .addValue("P_START_DATE", Date.valueOf(req.contractStartDate()))
                .addValue("P_REGION", req.region())
                .addValue("P_LOCAL_REGION", req.localRegion())
                .addValue("P_ADDRESS", req.address()));
        return new RegistrationResult(
                (String) out.get("P_LOGIN"),
                (String) out.get("P_PASSWORD"),
                (String) out.get("P_RESULT"),
                (String) out.get("P_RET_MSG"));
    }

    public OperationResult changeStatus(String code, int status, int mode) {
        Map<String, Object> out = changeStatusCall.execute(new MapSqlParameterSource()
                .addValue("P_CODE", code)
                .addValue("P_IN_TEST", mode)
                .addValue("P_STATE", status));
        return new OperationResult((String) out.get("P_RESULT"), (String) out.get("P_RET_MSG"));
    }
}
