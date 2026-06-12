package uz.katm.report.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import uz.katm.report.domain.fcbk.FicoResultEntity;

import javax.sql.DataSource;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Данные для FICO-скоринга FCBK: clientId -> external_id (DATAS.FICO_SCORE),
 * затем выборка из DWH_FICO_MAIN/DWH_FICO_CONTRACTS. Перенос
 * gov.uz.katm.repo.credit.history.CreditReportRepositoryImpl.getFicoScore.
 */
@Repository
public class FcbkRepository {

    private static final String FICO_SQL =
            "SELECT * FROM datas.DWH_FICO_MAIN dfm " +
            "LEFT JOIN datas.DWH_FICO_CONTRACTS dfc ON dfc.external_id = dfm.external_id " +
            "WHERE dfm.external_id = :externalId";

    private final NamedParameterJdbcTemplate namedJdbcTemplate;
    private final SimpleJdbcCall ficoScoreCall;

    public FcbkRepository(NamedParameterJdbcTemplate namedJdbcTemplate, DataSource dataSource) {
        this.namedJdbcTemplate = namedJdbcTemplate;
        this.ficoScoreCall = new SimpleJdbcCall(dataSource)
                .withSchemaName("DATAS")
                .withProcedureName("FICO_SCORE")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("P_CLIENT_ID", Types.VARCHAR),
                        new SqlOutParameter("L_EXTERNAL_ID", Types.VARCHAR),
                        new SqlOutParameter("L_STATUS", Types.VARCHAR)
                );
    }

    public List<FicoResultEntity> getFicoData(String clientId) {
        Map<String, Object> out = ficoScoreCall.execute(new MapSqlParameterSource().addValue("P_CLIENT_ID", clientId));
        String externalId = (String) out.get("L_EXTERNAL_ID");
        if (externalId == null || externalId.isBlank()) {
            return List.of();
        }
        List<FicoResultEntity> resultList = new ArrayList<>();
        namedJdbcTemplate.query(FICO_SQL, new MapSqlParameterSource("externalId", externalId), rs -> {
            do {
                FicoResultEntity ficoResultEntity = new FicoResultEntity();
                ficoResultEntity.setDateOfBirth(rs.getDate("DATEOFBIRTH"));
                ficoResultEntity.setNegativeStatus(rs.getString("NEGATIVESTATUS"));
                ficoResultEntity.setCreditReportDate(rs.getDate("CREDITREPORTDATE"));
                ficoResultEntity.setExternal_Id(rs.getString("EXTERNAL_ID"));
                ficoResultEntity.setFundingType(rs.getString("FUNDINGTYPE"));
                ficoResultEntity.setCreditPurpose(rs.getString("CREDITPURPOSE"));
                ficoResultEntity.setCreditPurpose2(rs.getString("CREDITPURPOSE2"));
                ficoResultEntity.setCreditObject(rs.getString("CREDITOBJECT"));
                ficoResultEntity.setContractPhase(rs.getString("CONTRACTPHASE"));
                ficoResultEntity.setContractStatus_Month_00(rs.getString("CONTRACTSTATUS_MONTH_00"));
                ficoResultEntity.setContractStatus_Month_01(rs.getString("CONTRACTSTATUS_MONTH_01"));
                ficoResultEntity.setContractStatus_Month_02(rs.getString("CONTRACTSTATUS_MONTH_02"));
                ficoResultEntity.setContractStatus_Month_03(rs.getString("CONTRACTSTATUS_MONTH_03"));
                ficoResultEntity.setContractStatus_Month_04(rs.getString("CONTRACTSTATUS_MONTH_04"));
                ficoResultEntity.setContractStatus_Month_05(rs.getString("CONTRACTSTATUS_MONTH_05"));
                ficoResultEntity.setContractStatus_Month_06(rs.getString("CONTRACTSTATUS_MONTH_06"));
                ficoResultEntity.setContractStatus_Month_07(rs.getString("CONTRACTSTATUS_MONTH_07"));
                ficoResultEntity.setContractStatus_Month_08(rs.getString("CONTRACTSTATUS_MONTH_08"));
                ficoResultEntity.setContractStatus_Month_09(rs.getString("CONTRACTSTATUS_MONTH_09"));
                ficoResultEntity.setContractStatus_Month_10(rs.getString("CONTRACTSTATUS_MONTH_10"));
                ficoResultEntity.setContractStatus_Month_11(rs.getString("CONTRACTSTATUS_MONTH_11"));
                ficoResultEntity.setContractStatus_Month_12(rs.getString("CONTRACTSTATUS_MONTH_12"));
                ficoResultEntity.setContractStatus_Month_13(rs.getString("CONTRACTSTATUS_MONTH_13"));
                ficoResultEntity.setContractStatus_Month_14(rs.getString("CONTRACTSTATUS_MONTH_14"));
                ficoResultEntity.setContractStatus_Month_15(rs.getString("CONTRACTSTATUS_MONTH_15"));
                ficoResultEntity.setContractStatus_Month_16(rs.getString("CONTRACTSTATUS_MONTH_16"));
                ficoResultEntity.setContractStatus_Month_17(rs.getString("CONTRACTSTATUS_MONTH_17"));
                ficoResultEntity.setContractStatus_Month_18(rs.getString("CONTRACTSTATUS_MONTH_18"));
                ficoResultEntity.setContractStatus_Month_19(rs.getString("CONTRACTSTATUS_MONTH_19"));
                ficoResultEntity.setContractStatus_Month_20(rs.getString("CONTRACTSTATUS_MONTH_20"));
                ficoResultEntity.setContractStatus_Month_21(rs.getString("CONTRACTSTATUS_MONTH_21"));
                ficoResultEntity.setContractStatus_Month_22(rs.getString("CONTRACTSTATUS_MONTH_22"));
                ficoResultEntity.setContractStatus_Month_23(rs.getString("CONTRACTSTATUS_MONTH_23"));
                ficoResultEntity.setApplicationDate(rs.getDate("APPLICATIONDATE"));
                ficoResultEntity.setStartDate(rs.getDate("STARTDATE"));
                ficoResultEntity.setEndDate(rs.getDate("ENDDATE"));
                ficoResultEntity.setActualDate(rs.getDate("ACTUALDATE"));
                ficoResultEntity.setRealPaymentDate(rs.getDate("REALPAYMENTDATE"));
                ficoResultEntity.setSpecial_Relationship(rs.getString("SPECIAL_RELATIONSHIP"));
                ficoResultEntity.setGrace_Principal(rs.getString("GRACE_PRINCIPAL"));
                ficoResultEntity.setGrace_Pay(rs.getString("GRACE_PAY"));
                ficoResultEntity.setTypeOfPp(rs.getString("TYPEOFPP"));
                ficoResultEntity.setProductClassification(rs.getString("PRODUCTCLASSIFICATION"));
                ficoResultEntity.setTotalAmount_Instalment(rs.getString("TOTALAMOUNT_INSTALMENT"));
                ficoResultEntity.setInstalmentAmount(rs.getString("INSTALMENTAMOUNT"));
                ficoResultEntity.setInstalmentCount(rs.getString("INSTALMENTCOUNT"));
                ficoResultEntity.setPaymentPeriod_Instalment(rs.getString("PAYMENTPERIOD_INSTALMENT"));
                ficoResultEntity.setAccountingDate_Instalment(rs.getDate("ACCOUNTINGDATE_INSTALMENT"));
                ficoResultEntity.setOutstandingInstalmentCount(rs.getString("OUTSTANDINGINSTALMENTCOUNT"));
                ficoResultEntity.setOutstandingAmount_Instalment(rs.getString("OUTSTANDINGAMOUNT_INSTALMENT"));
                ficoResultEntity.setOverdueInstalmentCount_Month_0(rs.getString("OVERDUEINSTALMENTCOUNT_MONTH_0"));
                ficoResultEntity.setOverdueInstalmentCount_Month_1(rs.getString("OVERDUEINSTALMENTCOUNT_MONTH_1"));
                ficoResultEntity.setOverdueInstalmentCount_Month_2(rs.getString("OVERDUEINSTALMENTCOUNT_MONTH_2"));
                ficoResultEntity.setOverdueInstalmentCount_Month_3(rs.getString("OVERDUEINSTALMENTCOUNT_MONTH_3"));
                ficoResultEntity.setOverdueInstalmentCount_Month_4(rs.getString("OVERDUEINSTALMENTCOUNT_MONTH_4"));
                ficoResultEntity.setOverdueInstalmentCount_Month_5(rs.getString("OVERDUEINSTALMENTCOUNT_MONTH_5"));
                ficoResultEntity.setOverdueInstalmentCount_Month_6(rs.getString("OVERDUEINSTALMENTCOUNT_MONTH_6"));
                ficoResultEntity.setOverdueInstalmentCount_Month_7(rs.getString("OVERDUEINSTALMENTCOUNT_MONTH_7"));
                ficoResultEntity.setOverdueInstalmentCount_Month_8(rs.getString("OVERDUEINSTALMENTCOUNT_MONTH_8"));
                ficoResultEntity.setOverdueInstalmentCount_Month_9(rs.getString("OVERDUEINSTALMENTCOUNT_MONTH_9"));
                ficoResultEntity.setOverdueInstalmentCount_Month_10(rs.getString("OVERDUEINSTALMENTCOUNT_MONTH_10"));
                ficoResultEntity.setOverdueInstalmentCount_Month_11(rs.getString("OVERDUEINSTALMENTCOUNT_MONTH_11"));
                ficoResultEntity.setOverdueInstalmentCount_Month_12(rs.getString("OVERDUEINSTALMENTCOUNT_MONTH_12"));
                ficoResultEntity.setOverdueInstalmentCount_Month_13(rs.getString("OVERDUEINSTALMENTCOUNT_MONTH_13"));
                ficoResultEntity.setOverdueInstalmentCount_Month_14(rs.getString("OVERDUEINSTALMENTCOUNT_MONTH_14"));
                ficoResultEntity.setOverdueInstalmentCount_Month_15(rs.getString("OVERDUEINSTALMENTCOUNT_MONTH_15"));
                ficoResultEntity.setOverdueInstalmentCount_Month_16(rs.getString("OVERDUEINSTALMENTCOUNT_MONTH_16"));
                ficoResultEntity.setOverdueInstalmentCount_Month_17(rs.getString("OVERDUEINSTALMENTCOUNT_MONTH_17"));
                ficoResultEntity.setOverdueInstalmentCount_Month_18(rs.getString("OVERDUEINSTALMENTCOUNT_MONTH_18"));
                ficoResultEntity.setOverdueInstalmentCount_Month_19(rs.getString("OVERDUEINSTALMENTCOUNT_MONTH_19"));
                ficoResultEntity.setOverdueInstalmentCount_Month_20(rs.getString("OVERDUEINSTALMENTCOUNT_MONTH_20"));
                ficoResultEntity.setOverdueInstalmentCount_Month_21(rs.getString("OVERDUEINSTALMENTCOUNT_MONTH_21"));
                ficoResultEntity.setOverdueInstalmentCount_Month_22(rs.getString("OVERDUEINSTALMENTCOUNT_MONTH_22"));
                ficoResultEntity.setOverdueInstalmentCount_Month_23(rs.getString("OVERDUEINSTALMENTCOUNT_MONTH_23"));
                ficoResultEntity.setOverdueAmount_00_Instalment(rs.getString("OVERDUEAMOUNT_00_INSTALMENT"));
                ficoResultEntity.setOverdueAmount_01_Instalment(rs.getString("OVERDUEAMOUNT_01_INSTALMENT"));
                ficoResultEntity.setOverdueAmount_02_Instalment(rs.getString("OVERDUEAMOUNT_02_INSTALMENT"));
                ficoResultEntity.setOverdueAmount_03_Instalment(rs.getString("OVERDUEAMOUNT_03_INSTALMENT"));
                ficoResultEntity.setOverdueAmount_04_Instalment(rs.getString("OVERDUEAMOUNT_04_INSTALMENT"));
                ficoResultEntity.setOverdueAmount_05_Instalment(rs.getString("OVERDUEAMOUNT_05_INSTALMENT"));
                ficoResultEntity.setOverdueAmount_06_Instalment(rs.getString("OVERDUEAMOUNT_06_INSTALMENT"));
                ficoResultEntity.setOverdueAmount_07_Instalment(rs.getString("OVERDUEAMOUNT_07_INSTALMENT"));
                ficoResultEntity.setOverdueAmount_08_Instalment(rs.getString("OVERDUEAMOUNT_08_INSTALMENT"));
                ficoResultEntity.setOverdueAmount_09_Instalment(rs.getString("OVERDUEAMOUNT_09_INSTALMENT"));
                ficoResultEntity.setOverdueAmount_10_Instalment(rs.getString("OVERDUEAMOUNT_10_INSTALMENT"));
                ficoResultEntity.setOverdueAmount_11_Instalment(rs.getString("OVERDUEAMOUNT_11_INSTALMENT"));
                ficoResultEntity.setOverdueAmount_12_Instalment(rs.getString("OVERDUEAMOUNT_12_INSTALMENT"));
                ficoResultEntity.setOverdueAmount_13_Instalment(rs.getString("OVERDUEAMOUNT_13_INSTALMENT"));
                ficoResultEntity.setOverdueAmount_14_Instalment(rs.getString("OVERDUEAMOUNT_14_INSTALMENT"));
                ficoResultEntity.setOverdueAmount_15_Instalment(rs.getString("OVERDUEAMOUNT_15_INSTALMENT"));
                ficoResultEntity.setOverdueAmount_16_Instalment(rs.getString("OVERDUEAMOUNT_16_INSTALMENT"));
                ficoResultEntity.setOverdueAmount_17_Instalment(rs.getString("OVERDUEAMOUNT_17_INSTALMENT"));
                ficoResultEntity.setOverdueAmount_18_Instalment(rs.getString("OVERDUEAMOUNT_18_INSTALMENT"));
                ficoResultEntity.setOverdueAmount_19_Instalment(rs.getString("OVERDUEAMOUNT_19_INSTALMENT"));
                ficoResultEntity.setOverdueAmount_20_Instalment(rs.getString("OVERDUEAMOUNT_20_INSTALMENT"));
                ficoResultEntity.setOverdueAmount_21_Instalment(rs.getString("OVERDUEAMOUNT_21_INSTALMENT"));
                ficoResultEntity.setOverdueAmount_22_Instalment(rs.getString("OVERDUEAMOUNT_22_INSTALMENT"));
                ficoResultEntity.setOverdueAmount_23_Instalment(rs.getString("OVERDUEAMOUNT_23_INSTALMENT"));
                ficoResultEntity.setProlongationStartDate_Instalment(rs.getDate("PROLONGATIONSTARTDATE_INSTALMENT"));
                ficoResultEntity.setProlongationEndDate_Instalment(rs.getDate("PROLONGATIONENDDATE_INSTALMENT"));
                ficoResultEntity.setPaymentMethod_Cc(rs.getString("PAYMENTMETHOD_CC"));
                ficoResultEntity.setCreditLimit_Cc(rs.getString("CREDITLIMIT_CC"));
                ficoResultEntity.setAccountingDate_Cc(rs.getDate("ACCOUNTINGDATE_CC"));
                ficoResultEntity.setMonthlyInstallmentAmount_Cc(rs.getString("MONTHLYINSTALMENTAMOUNT_CC"));
                ficoResultEntity.setResidualAmount_Cc(rs.getString("RESIDUALAMOUNT_CC"));
                ficoResultEntity.setOverdueInstalmentCount_Month_0_Cc(rs.getString("OVERDUEINSTALMENTCOUNT_MONTH_0_CC"));
                ficoResultEntity.setOverdueInstalmentCount_Month_1_Cc(rs.getString("OVERDUEINSTALMENTCOUNT_MONTH_1_CC"));
                ficoResultEntity.setOverdueInstalmentCount_Month_2_Cc(rs.getString("OVERDUEINSTALMENTCOUNT_MONTH_2_CC"));
                ficoResultEntity.setOverdueInstalmentCount_Month_3_Cc(rs.getString("OVERDUEINSTALMENTCOUNT_MONTH_3_CC"));
                ficoResultEntity.setOverdueInstalmentCount_Month_4_Cc(rs.getString("OVERDUEINSTALMENTCOUNT_MONTH_4_CC"));
                ficoResultEntity.setOverdueInstalmentCount_Month_5_Cc(rs.getString("OVERDUEINSTALMENTCOUNT_MONTH_5_CC"));
                ficoResultEntity.setOverdueInstalmentCount_Month_6_Cc(rs.getString("OVERDUEINSTALMENTCOUNT_MONTH_6_CC"));
                ficoResultEntity.setOverdueInstalmentCount_Month_7_Cc(rs.getString("OVERDUEINSTALMENTCOUNT_MONTH_7_CC"));
                ficoResultEntity.setOverdueInstalmentCount_Month_8_Cc(rs.getString("OVERDUEINSTALMENTCOUNT_MONTH_8_CC"));
                ficoResultEntity.setOverdueInstalmentCount_Month_9_Cc(rs.getString("OVERDUEINSTALMENTCOUNT_MONTH_9_CC"));
                ficoResultEntity.setOverdueInstalmentCount_Month_10_Cc(rs.getString("OVERDUEINSTALMENTCOUNT_MONTH_10_CC"));
                ficoResultEntity.setOverdueInstalmentCount_Month_11_Cc(rs.getString("OVERDUEINSTALMENTCOUNT_MONTH_11_CC"));
                ficoResultEntity.setOverdueInstalmentCount_Month_12_Cc(rs.getString("OVERDUEINSTALMENTCOUNT_MONTH_12_CC"));
                ficoResultEntity.setOverdueInstalmentCount_Month_13_Cc(rs.getString("OVERDUEINSTALMENTCOUNT_MONTH_13_CC"));
                ficoResultEntity.setOverdueInstalmentCount_Month_14_Cc(rs.getString("OVERDUEINSTALMENTCOUNT_MONTH_14_CC"));
                ficoResultEntity.setOverdueInstalmentCount_Month_15_Cc(rs.getString("OVERDUEINSTALMENTCOUNT_MONTH_15_CC"));
                ficoResultEntity.setOverdueInstalmentCount_Month_16_Cc(rs.getString("OVERDUEINSTALMENTCOUNT_MONTH_16_CC"));
                ficoResultEntity.setOverdueInstalmentCount_Month_17_Cc(rs.getString("OVERDUEINSTALMENTCOUNT_MONTH_17_CC"));
                ficoResultEntity.setOverdueInstalmentCount_Month_18_Cc(rs.getString("OVERDUEINSTALMENTCOUNT_MONTH_18_CC"));
                ficoResultEntity.setOverdueInstalmentCount_Month_19_Cc(rs.getString("OVERDUEINSTALMENTCOUNT_MONTH_19_CC"));
                ficoResultEntity.setOverdueInstalmentCount_Month_20_Cc(rs.getString("OVERDUEINSTALMENTCOUNT_MONTH_20_CC"));
                ficoResultEntity.setOverdueInstalmentCount_Month_21_Cc(rs.getString("OVERDUEINSTALMENTCOUNT_MONTH_21_CC"));
                ficoResultEntity.setOverdueInstalmentCount_Month_22_Cc(rs.getString("OVERDUEINSTALMENTCOUNT_MONTH_22_CC"));
                ficoResultEntity.setOverdueInstalmentCount_Month_23_Cc(rs.getString("OVERDUEINSTALMENTCOUNT_MONTH_23_CC"));
                ficoResultEntity.setOverdueAmount_00_Cc(rs.getString("OVERDUEAMOUNT_00_CC"));
                ficoResultEntity.setOverdueAmount_01_Cc(rs.getString("OVERDUEAMOUNT_01_CC"));
                ficoResultEntity.setOverdueAmount_02_Cc(rs.getString("OVERDUEAMOUNT_02_CC"));
                ficoResultEntity.setOverdueAmount_03_Cc(rs.getString("OVERDUEAMOUNT_03_CC"));
                ficoResultEntity.setOverdueAmount_04_Cc(rs.getString("OVERDUEAMOUNT_04_CC"));
                ficoResultEntity.setOverdueAmount_05_Cc(rs.getString("OVERDUEAMOUNT_05_CC"));
                ficoResultEntity.setOverdueAmount_06_Cc(rs.getString("OVERDUEAMOUNT_06_CC"));
                ficoResultEntity.setOverdueAmount_07_Cc(rs.getString("OVERDUEAMOUNT_07_CC"));
                ficoResultEntity.setOverdueAmount_08_Cc(rs.getString("OVERDUEAMOUNT_08_CC"));
                ficoResultEntity.setOverdueAmount_09_Cc(rs.getString("OVERDUEAMOUNT_09_CC"));
                ficoResultEntity.setOverdueAmount_10_Cc(rs.getString("OVERDUEAMOUNT_10_CC"));
                ficoResultEntity.setOverdueAmount_11_Cc(rs.getString("OVERDUEAMOUNT_11_CC"));
                ficoResultEntity.setOverdueAmount_12_Cc(rs.getString("OVERDUEAMOUNT_12_CC"));
                ficoResultEntity.setOverdueAmount_13_Cc(rs.getString("OVERDUEAMOUNT_13_CC"));
                ficoResultEntity.setOverdueAmount_14_Cc(rs.getString("OVERDUEAMOUNT_14_CC"));
                ficoResultEntity.setOverdueAmount_15_Cc(rs.getString("OVERDUEAMOUNT_15_CC"));
                ficoResultEntity.setOverdueAmount_16_Cc(rs.getString("OVERDUEAMOUNT_16_CC"));
                ficoResultEntity.setOverdueAmount_17_Cc(rs.getString("OVERDUEAMOUNT_17_CC"));
                ficoResultEntity.setOverdueAmount_18_Cc(rs.getString("OVERDUEAMOUNT_18_CC"));
                ficoResultEntity.setOverdueAmount_19_Cc(rs.getString("OVERDUEAMOUNT_19_CC"));
                ficoResultEntity.setOverdueAmount_20_Cc(rs.getString("OVERDUEAMOUNT_20_CC"));
                ficoResultEntity.setOverdueAmount_21_Cc(rs.getString("OVERDUEAMOUNT_21_CC"));
                ficoResultEntity.setOverdueAmount_22_Cc(rs.getString("OVERDUEAMOUNT_22_CC"));
                ficoResultEntity.setOverdueAmount_23_Cc(rs.getString("OVERDUEAMOUNT_23_CC"));
                ficoResultEntity.setProlongationStartDate_Cc(rs.getDate("PROLONGATIONSTARTDATE_CC"));
                ficoResultEntity.setProlongationEndDate_Cc(rs.getDate("PROLONGATIONENDDATE_CC"));
                ficoResultEntity.setCollateralType(rs.getString("COLLATERALTYPE"));
                ficoResultEntity.setSubjectRole(rs.getString("SUBJECTROLE"));
                ficoResultEntity.setEntity(rs.getString("ENTITY"));
                resultList.add(ficoResultEntity);
            } while (rs.next());
        });
        return resultList;
    }
}
