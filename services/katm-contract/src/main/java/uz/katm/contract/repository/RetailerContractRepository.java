package uz.katm.contract.repository;

import lombok.extern.slf4j.Slf4j;
import oracle.jdbc.OracleConnection;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import uz.katm.contract.domain.retailer.*;
import uz.katm.contract.exception.ContractServiceException;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
public class RetailerContractRepository {

    private final JdbcTemplate jdbcTemplate;

    private final SimpleJdbcCall addContractCall;
    private final SimpleJdbcCall addContractPlanCall;
    private final SimpleJdbcCall addContractRepaymentCall;

    public RetailerContractRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);

        this.addContractCall = new SimpleJdbcCall(dataSource)
                .withSchemaName("DATAS")
                .withCatalogName("PKG_RETAILERS")
                .withProcedureName("ADD_RETAILER_CONTRACT")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("P_CODE", Types.VARCHAR),
                        new SqlParameter("P_CLAIM_ID", Types.VARCHAR),
                        new SqlParameter("P_CONTRACT_ID", Types.VARCHAR),
                        new SqlParameter("P_OBJECT", Types.VARCHAR),
                        new SqlParameter("P_BIRTH_DATE", Types.DATE),
                        new SqlParameter("P_END_DATE", Types.DATE),
                        new SqlParameter("P_CRED_AMOUNT", Types.BIGINT),
                        new SqlParameter("P_CRED_CURRENCY", Types.VARCHAR),
                        new SqlParameter("P_CONTRACT_STATUS", Types.INTEGER),
                        new SqlParameter("P_IS_UPDATE", Types.INTEGER),
                        new SqlOutParameter("P_RESULT", Types.VARCHAR),
                        new SqlOutParameter("P_RET_MSG", Types.VARCHAR)
                );

        this.addContractPlanCall = new SimpleJdbcCall(dataSource)
                .withSchemaName("DATAS")
                .withCatalogName("PKG_RETAILERS")
                .withProcedureName("ADD_CONTRACT_PLAN")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("P_CODE", Types.VARCHAR),
                        new SqlParameter("P_CLAIM_ID", Types.VARCHAR),
                        new SqlParameter("P_CONTRACT_ID", Types.VARCHAR),
                        new SqlParameter("P_PLAN_ARRAY", Types.ARRAY, "DATAS.T_PLAN_ARRAY"),
                        new SqlParameter("P_IS_UPDATE", Types.INTEGER),
                        new SqlOutParameter("P_RESULT", Types.VARCHAR),
                        new SqlOutParameter("P_RET_MSG", Types.VARCHAR)
                );

        this.addContractRepaymentCall = new SimpleJdbcCall(dataSource)
                .withSchemaName("DATAS")
                .withCatalogName("PKG_RETAILERS")
                .withProcedureName("ADD_CONTRACT_REPAYMENT")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("P_CODE", Types.VARCHAR),
                        new SqlParameter("P_CLAIM_ID", Types.VARCHAR),
                        new SqlParameter("P_CONTRACT_ID", Types.VARCHAR),
                        new SqlParameter("P_REPAYMENT_ARRAY", Types.ARRAY, "DATAS.T_REPAYMENT_ARRAY"),
                        new SqlParameter("P_IS_UPDATE", Types.INTEGER),
                        new SqlOutParameter("P_RESULT", Types.VARCHAR),
                        new SqlOutParameter("P_RET_MSG", Types.VARCHAR)
                );
    }

    public OperationResult addContract(String code, ContractRegistrationRequest req) {
        var params = new MapSqlParameterSource()
                .addValue("P_CODE", code)
                .addValue("P_CLAIM_ID", req.claimId())
                .addValue("P_CONTRACT_ID", req.contractId())
                .addValue("P_OBJECT", req.object())
                .addValue("P_BIRTH_DATE", toSqlDate(req.startDate()))
                .addValue("P_END_DATE", toSqlDate(req.endDate()))
                .addValue("P_CRED_AMOUNT", req.creditAmount())
                .addValue("P_CRED_CURRENCY", req.currency())
                .addValue("P_CONTRACT_STATUS", req.contractStatus() != null ? Integer.parseInt(req.contractStatus()) : null)
                .addValue("P_IS_UPDATE", req.isUpdate());
        return executeResult(addContractCall, params);
    }

    public OperationResult addContractPlan(String code, ScheduleAddRequest req) {
        return jdbcTemplate.execute((Connection conn) -> {
            OracleConnection oracleConn = conn.unwrap(OracleConnection.class);
            Array planArray = buildPlanArray(oracleConn, req.planArray());
            var params = new MapSqlParameterSource()
                    .addValue("P_CODE", code)
                    .addValue("P_CLAIM_ID", req.claimId())
                    .addValue("P_CONTRACT_ID", req.contractId())
                    .addValue("P_PLAN_ARRAY", planArray)
                    .addValue("P_IS_UPDATE", req.isUpdate());
            return executeResult(addContractPlanCall, params);
        });
    }

    public OperationResult addContractRepayment(String code, RepaymentAddRequest req) {
        return jdbcTemplate.execute((Connection conn) -> {
            OracleConnection oracleConn = conn.unwrap(OracleConnection.class);
            Array repaymentArray = buildRepaymentArray(oracleConn, req.repaymentArray());
            var params = new MapSqlParameterSource()
                    .addValue("P_CODE", code)
                    .addValue("P_CLAIM_ID", req.claimId())
                    .addValue("P_CONTRACT_ID", req.contractId())
                    .addValue("P_REPAYMENT_ARRAY", repaymentArray)
                    .addValue("P_IS_UPDATE", req.isUpdate());
            return executeResult(addContractRepaymentCall, params);
        });
    }

    public List<Map<String, Object>> getRepayments(String head, String code, ContractQueryRequest req) {
        return jdbcTemplate.queryForList(
                "SELECT * FROM TABLE(DATAS.PKG_RETAILERS.GET_CONTRACT_REPAYMENTS(?,?,?))",
                head, code, req.contractId()
        );
    }

    public List<Map<String, Object>> getRepaymentsSchedule(String head, String code, ContractQueryRequest req) {
        return jdbcTemplate.queryForList(
                "SELECT * FROM TABLE(DATAS.PKG_RETAILERS.GET_CONTRACT_PLAN(?,?,?))",
                head, code, req.contractId()
        );
    }

    public Map<String, Object> getContractInfo(String head, String code, ContractQueryRequest req) {
        var rows = jdbcTemplate.queryForList(
                "SELECT * FROM TABLE(DATAS.PKG_RETAILERS.GET_CONTRACT_INFO(?,?,?))",
                head, code, req.contractId()
        );
        return rows.isEmpty() ? Map.of() : rows.get(0);
    }

    private OperationResult executeResult(SimpleJdbcCall call, MapSqlParameterSource params) {
        Map<String, Object> result = call.execute(params);
        String code = String.valueOf(result.get("P_RESULT"));
        String message = (String) result.get("P_RET_MSG");
        log.debug("Procedure result: code={}, message={}", code, message);
        return new OperationResult(code, message, null);
    }

    private Array buildPlanArray(OracleConnection conn, List<PlanItem> items) {
        try {
            Struct[] structs = new Struct[items.size()];
            for (int i = 0; i < items.size(); i++) {
                PlanItem item = items.get(i);
                Object[] attrs = {toSqlDate(item.payDate()), item.mainAmount(), item.percentAmount()};
                structs[i] = conn.createStruct("DATAS.T_PLAN_OBJECT", attrs);
            }
            return conn.createARRAY("DATAS.T_PLAN_ARRAY", structs);
        } catch (Exception e) {
            throw new ContractServiceException("Failed to build plan array: " + e.getMessage());
        }
    }

    private Array buildRepaymentArray(OracleConnection conn, List<RepaymentItem> items) {
        try {
            Struct[] structs = new Struct[items.size()];
            for (int i = 0; i < items.size(); i++) {
                RepaymentItem item = items.get(i);
                Object[] attrs = {toSqlDate(item.payDate()), item.mainAmount(), item.percentAmount()};
                structs[i] = conn.createStruct("DATAS.T_REPAYMENT_OBJECT", attrs);
            }
            return conn.createARRAY("DATAS.T_REPAYMENT_ARRAY", structs);
        } catch (Exception e) {
            throw new ContractServiceException("Failed to build repayment array: " + e.getMessage());
        }
    }

    private static Date toSqlDate(LocalDate date) {
        return date != null ? Date.valueOf(date) : null;
    }
}
