package uz.katm.report.domain.fcbk;

import com.fasterxml.jackson.annotation.JsonProperty;
import uz.katm.report.util.ConverterUtils;

public class AccountsBlock {
    @JsonProperty("funding_type")
    private String fundingType;

    @JsonProperty("credit_purpose")
    private String creditPurpose;

    @JsonProperty("credit_purpose2")
    private String creditPurpose2;

    @JsonProperty("credit_object")
    private String creditObject;

    @JsonProperty("contract_phase")
    private String contractPhase;

    @JsonProperty("contract_status_months")
    private String[] contractStatusMonths;

//    @JsonProperty("contract_Status_month_00")
//    private String contractStatusMonth00;
//
//    @JsonProperty("contract_Status_month_01")
//    private String contractStatusMonth01;
//
//    @JsonProperty("contract_Status_month_02")
//    private String contractStatusMonth02;
//
//    @JsonProperty("contract_Status_month_03")
//    private String contractStatusMonth03;
//
//    @JsonProperty("contract_Status_month_04")
//    private String contractStatusMonth04;
//
//    @JsonProperty("contract_Status_month_05")
//    private String contractStatusMonth05;
//
//    @JsonProperty("contract_Status_month_06")
//    private String contractStatusMonth06;
//
//    @JsonProperty("contract_Status_month_07")
//    private String contractStatusMonth07;
//
//    @JsonProperty("contract_Status_month_08")
//    private String contractStatusMonth08;
//
//    @JsonProperty("contract_Status_month_09")
//    private String contractStatusMonth09;
//
//    @JsonProperty("contract_Status_month_10")
//    private String contractStatusMonth10;
//
//    @JsonProperty("contract_Status_month_11")
//    private String contractStatusMonth11;
//
//    @JsonProperty("contract_Status_month_12")
//    private String contractStatusMonth12;
//
//    @JsonProperty("contract_Status_month_13")
//    private String contractStatusMonth13;
//
//    @JsonProperty("contract_Status_month_14")
//    private String contractStatusMonth14;
//
//    @JsonProperty("contract_Status_month_15")
//    private String contractStatusMonth15;
//
//    @JsonProperty("contract_Status_month_16")
//    private String contractStatusMonth16;
//
//    @JsonProperty("contract_Status_month_17")
//    private String contractStatusMonth17;
//
//    @JsonProperty("contract_Status_month_18")
//    private String contractStatusMonth18;
//
//    @JsonProperty("contract_Status_month_19")
//    private String contractStatusMonth19;
//
//    @JsonProperty("contract_Status_month_20")
//    private String contractStatusMonth20;
//
//    @JsonProperty("contract_Status_month_21")
//    private String contractStatusMonth21;
//
//    @JsonProperty("contract_Status_month_22")
//    private String contractStatusMonth22;
//
//    @JsonProperty("contract_Status_month_23")
//    private String contractStatusMonth23;

    @JsonProperty("application_date")
    private String applicationDate;

    @JsonProperty("start_date")
    private String startDate;

    @JsonProperty("end_date")
    private String endDate;

    @JsonProperty("actual_date")
    private String actualDate;

    @JsonProperty("real_payment_date")
    private String realPaymentDate;

    @JsonProperty("special_relationship")
    private String specialRelationship;

    @JsonProperty("grace_principal")
    private String gracePrincipal;

    @JsonProperty("grace_pay")
    private String gracePay;

    @JsonProperty("type_off_pp")
    private String typeOffPp;

    @JsonProperty("product_classification")
    private String productClassification;

    @JsonProperty("total_amount_instalment")
    private  String totalAmountInstalment;

    @JsonProperty("instalment_amount")
    private  String instalmentAmount;

    @JsonProperty("instalment_count")
    private  String instalmentCount;

    @JsonProperty("payment_period_instalment")
    private String paymentPeriodInstalment;

    @JsonProperty("accounting_date_instalment")
    private String accountingDateInstalment;

    @JsonProperty("outstanding_instalment_count")
    private  String outstandingInstalmentCount;

    @JsonProperty("outstanding_amount_instalment")
    private  String outstandingAmountInstalment;

    @JsonProperty("overdue_instalment_count_months")
    private String[] overdueInstalmentCountMonths;

//    @JsonProperty("overdue_instalment_count_month_0")
//    private  String overdueInstalmentCountMonth0;
//
//    @JsonProperty("overdue_instalment_count_month_1")
//    private  String overdueInstalmentCountMonth1;
//
//    @JsonProperty("overdue_instalment_count_month_2")
//    private  String overdueInstalmentCountMonth2;
//
//    @JsonProperty("overdue_instalment_count_month_3")
//    private  String overdueInstalmentCountMonth3;
//
//    @JsonProperty("overdue_instalment_count_month_4")
//    private  String overdueInstalmentCountMonth4;
//
//    @JsonProperty("overdue_instalment_count_month_5")
//    private  String overdueInstalmentCountMonth5;
//
//    @JsonProperty("overdue_instalment_count_month_6")
//    private  String overdueInstalmentCountMonth6;
//
//    @JsonProperty("overdue_instalment_count_month_7")
//    private  String overdueInstalmentCountMonth7;
//
//    @JsonProperty("overdue_instalment_count_month_8")
//    private  String overdueInstalmentCountMonth8;
//
//    @JsonProperty("overdue_instalment_count_month_9")
//    private  String overdueInstalmentCountMonth9;
//
//    @JsonProperty("overdue_instalment_count_month_10")
//    private  String overdueInstalmentCountMonth10;
//
//    @JsonProperty("overdue_instalment_count_month_11")
//    private  String overdueInstalmentCountMonth11;
//
//    @JsonProperty("overdue_instalment_count_month_12")
//    private  String overdueInstalmentCountMonth12;
//
//    @JsonProperty("overdue_instalment_count_month_13")
//    private  String overdueInstalmentCountMonth13;
//
//    @JsonProperty("overdue_instalment_count_month_14")
//    private  String overdueInstalmentCountMonth14;
//
//    @JsonProperty("overdue_instalment_count_month_15")
//    private  String overdueInstalmentCountMonth15;
//
//    @JsonProperty("overdue_instalment_count_month_16")
//    private  String overdueInstalmentCountMonth16;
//
//    @JsonProperty("overdue_instalment_count_month_17")
//    private  String overdueInstalmentCountMonth17;
//
//    @JsonProperty("overdue_instalment_count_month_18")
//    private  String overdueInstalmentCountMonth18;
//
//    @JsonProperty("overdue_instalment_count_month_19")
//    private  String overdueInstalmentCountMonth19;
//
//    @JsonProperty("overdue_instalment_count_month_20")
//    private  String overdueInstalmentCountMonth20;
//
//    @JsonProperty("overdue_instalment_count_month_21")
//    private  String overdueInstalmentCountMonth21;
//
//    @JsonProperty("overdue_instalment_count_month_22")
//    private  String overdueInstalmentCountMonth22;
//
//    @JsonProperty("overdue_instalment_count_month_23")
//    private  String overdueInstalmentCountMonth23;

    @JsonProperty("overdue_amount_instalments")
    private String[] overdueAmountInstalments;

//    @JsonProperty("overdue_amount_00_instalment")
//    private  String overdueAmount00Instalment;
//
//    @JsonProperty("overdue_amount_01_instalment")
//    private  String overdueAmount01Instalment;
//
//    @JsonProperty("overdue_amount_02_instalment")
//    private  String overdueAmount02Instalment;
//
//    @JsonProperty("overdue_amount_03_instalment")
//    private  String overdueAmount03Instalment;
//
//    @JsonProperty("overdue_amount_04_instalment")
//    private  String overdueAmount04Instalment;
//
//    @JsonProperty("overdue_amount_05_instalment")
//    private  String overdueAmount05Instalment;
//
//    @JsonProperty("overdue_amount_06_instalment")
//    private  String overdueAmount06Instalment;
//
//    @JsonProperty("overdue_amount_07_instalment")
//    private  String overdueAmount07Instalment;
//
//    @JsonProperty("overdue_amount_08_instalment")
//    private  String overdueAmount08Instalment;
//
//    @JsonProperty("overdue_amount_09_instalment")
//    private  String overdueAmount09Instalment;
//
//    @JsonProperty("overdue_amount_10_instalment")
//    private  String overdueAmount10Instalment;
//
//    @JsonProperty("overdue_amount_11_instalment")
//    private  String overdueAmount11Instalment;
//
//    @JsonProperty("overdue_amount_12_instalment")
//    private  String overdueAmount12Instalment;
//
//    @JsonProperty("overdue_amount_13_instalment")
//    private  String overdueAmount13Instalment;
//
//    @JsonProperty("overdue_amount_14_instalment")
//    private  String overdueAmount14Instalment;
//
//    @JsonProperty("overdue_amount_15_instalment")
//    private  String overdueAmount15Instalment;
//
//    @JsonProperty("overdue_amount_16_instalment")
//    private  String overdueAmount16Instalment;
//
//    @JsonProperty("overdue_amount_17_instalment")
//    private  String overdueAmount17Instalment;
//
//    @JsonProperty("overdue_amount_18_instalment")
//    private  String overdueAmount18Instalment;
//
//    @JsonProperty("overdue_amount_19_instalment")
//    private  String overdueAmount19Instalment;
//
//    @JsonProperty("overdue_amount_20_instalment")
//    private  String overdueAmount20Instalment;
//
//    @JsonProperty("overdue_amount_21_instalment")
//    private  String overdueAmount21Instalment;
//
//    @JsonProperty("overdue_amount_22_instalment")
//    private  String overdueAmount22Instalment;
//
//    @JsonProperty("overdue_amount_23_instalment")
//    private  String overdueAmount23Instalment;

    @JsonProperty("prolongation_start_date_instalment")
    private String prolongationStartDateInstalment;

    @JsonProperty("prolongation_end_date_instalment")
    private String prolongationEndDateInstalment;

    @JsonProperty("payment_method_cc")
    private String paymentMethodCc;

    @JsonProperty("credit_limit_cc")
    private  String creditLimitCc;

    @JsonProperty("accounting_date_cc")
    private String accountingDateCc;

    @JsonProperty("monthly_installment_amount_cc")
    private  String monthlyInstallmentAmountCc;

    @JsonProperty("residual_amount_cc")
    private  String residualAmountCc;

    @JsonProperty("overdue_instalment_count_months_cc")
    private String[] overdueInstalmentCountMonthsCc;

//    @JsonProperty("overdue_instalment_count_month_0_cc")
//    private  String overdueInstalmentCountMonth0Cc;
//
//    @JsonProperty("overdue_instalment_count_month_1_cc")
//    private  String overdueInstalmentCountMonth1Cc;
//
//    @JsonProperty("overdue_instalment_count_month_2_cc")
//    private  String overdueInstalmentCountMonth2Cc;
//
//    @JsonProperty("overdue_instalment_count_month_3_cc")
//    private  String overdueInstalmentCountMonth3Cc;
//
//    @JsonProperty("overdue_instalment_count_month_4_cc")
//    private  String overdueInstalmentCountMonth4Cc;
//
//    @JsonProperty("overdue_instalment_count_month_5_cc")
//    private  String overdueInstalmentCountMonth5Cc;
//
//    @JsonProperty("overdue_instalment_count_month_6_cc")
//    private  String overdueInstalmentCountMonth6Cc;
//
//    @JsonProperty("overdue_instalment_count_month_7_cc")
//    private  String overdueInstalmentCountMonth7Cc;
//
//    @JsonProperty("overdue_instalment_count_month_8_cc")
//    private  String overdueInstalmentCountMonth8Cc;
//
//    @JsonProperty("overdue_instalment_count_month_9_cc")
//    private  String overdueInstalmentCountMonth9Cc;
//
//    @JsonProperty("overdue_instalment_count_month_10_cc")
//    private  String overdueInstalmentCountMonth10Cc;
//
//    @JsonProperty("overdue_instalment_count_month_11_cc")
//    private  String overdueInstalmentCountMonth11Cc;
//
//    @JsonProperty("overdue_instalment_count_month_12_cc")
//    private  String overdueInstalmentCountMonth12Cc;
//
//    @JsonProperty("overdue_instalment_count_month_13_cc")
//    private  String overdueInstalmentCountMonth13Cc;
//
//    @JsonProperty("overdue_instalment_count_month_14_cc")
//    private  String overdueInstalmentCountMonth14Cc;
//
//    @JsonProperty("overdue_instalment_count_month_15_cc")
//    private  String overdueInstalmentCountMonth15Cc;
//
//    @JsonProperty("overdue_instalment_count_month_16_cc")
//    private  String overdueInstalmentCountMonth16Cc;
//
//    @JsonProperty("overdue_instalment_count_month_17_cc")
//    private  String overdueInstalmentCountMonth17Cc;
//
//    @JsonProperty("overdue_instalment_count_month_18_cc")
//    private  String overdueInstalmentCountMonth18Cc;
//
//    @JsonProperty("overdue_instalment_count_month_19_cc")
//    private  String overdueInstalmentCountMonth19Cc;
//
//    @JsonProperty("overdue_instalment_count_month_20_cc")
//    private  String overdueInstalmentCountMonth20Cc;
//
//    @JsonProperty("overdue_instalment_count_month_21_cc")
//    private  String overdueInstalmentCountMonth21Cc;
//
//    @JsonProperty("overdue_instalment_count_month_22_cc")
//    private  String overdueInstalmentCountMonth22Cc;
//
//    @JsonProperty("overdue_instalment_count_month_23_cc")
//    private  String overdueInstalmentCountMonth23Cc;

    @JsonProperty("overdue_amount_cc")
    private String[] overdueAmountCc;

//    @JsonProperty("overdue_amount_00_cc")
//    private  String overdueAmount00Cc;
//
//    @JsonProperty("overdue_amount_01_cc")
//    private  String overdueAmount01Cc;
//
//    @JsonProperty("overdue_amount_02_cc")
//    private  String overdueAmount02Cc;
//
//    @JsonProperty("overdue_amount_03_cc")
//    private  String overdueAmount03Cc;
//
//    @JsonProperty("overdue_amount_04_cc")
//    private  String overdueAmount04Cc;
//
//    @JsonProperty("overdue_amount_05_cc")
//    private  String overdueAmount05Cc;
//
//    @JsonProperty("overdue_amount_06_cc")
//    private  String overdueAmount06Cc;
//
//    @JsonProperty("overdue_amount_07_cc")
//    private  String overdueAmount07Cc;
//
//    @JsonProperty("overdue_amount_08_cc")
//    private  String overdueAmount08Cc;
//
//    @JsonProperty("overdue_amount_09_cc")
//    private  String overdueAmount09Cc;
//
//    @JsonProperty("overdue_amount_10_cc")
//    private  String overdueAmount10Cc;
//
//    @JsonProperty("overdue_amount_11_cc")
//    private  String overdueAmount11Cc;
//
//    @JsonProperty("overdue_amount_12_cc")
//    private  String overdueAmount12Cc;
//
//    @JsonProperty("overdue_amount_13_cc")
//    private  String overdueAmount13Cc;
//
//    @JsonProperty("overdue_amount_14_cc")
//    private  String overdueAmount14Cc;
//
//    @JsonProperty("overdue_amount_15_cc")
//    private  String overdueAmount15Cc;
//
//    @JsonProperty("overdue_amount_16_cc")
//    private  String overdueAmount16Cc;
//
//    @JsonProperty("overdue_amount_17_cc")
//    private  String overdueAmount17Cc;
//
//    @JsonProperty("overdue_amount_18_cc")
//    private  String overdueAmount18Cc;
//
//    @JsonProperty("overdue_amount_19_cc")
//    private  String overdueAmount19Cc;
//
//    @JsonProperty("overdue_amount_20_cc")
//    private  String overdueAmount20Cc;
//
//    @JsonProperty("overdue_amount_21_cc")
//    private  String overdueAmount21Cc;
//
//    @JsonProperty("overdue_amount_22_cc")
//    private  String overdueAmount22Cc;
//
//    @JsonProperty("overdue_amount_23_cc")
//    private  String overdueAmount23Cc;

    @JsonProperty("prolongation_start_date_cc")
    private String prolongationStartDateCc;

    @JsonProperty("prolongation_end_date_cc")
    private String prolongationEndDateCc;

    @JsonProperty("collateral_type")
    private String collateralType;

    @JsonProperty("subject_role")
    private String subjectRole;

    @JsonProperty("entity")
    private String entity;

    public AccountsBlock(FicoResultEntity resultEntity) {
        this.fundingType = resultEntity.getFundingType();
        this.creditPurpose = resultEntity.getCreditPurpose();
        this.creditPurpose2 = resultEntity.getCreditPurpose2();
        this.creditObject = resultEntity.getCreditObject();
        this.contractPhase = resultEntity.getContractPhase();
        this.contractStatusMonths = resultEntity.getContractStatusMonths();
        this.applicationDate = ConverterUtils.dateToString(resultEntity.getApplicationDate(), ConverterUtils.FORMAT_YYYY_MM_DD);
        this.startDate = ConverterUtils.dateToString(resultEntity.getStartDate(), ConverterUtils.FORMAT_YYYY_MM_DD);
        this.endDate = ConverterUtils.dateToString(resultEntity.getEndDate(), ConverterUtils.FORMAT_YYYY_MM_DD);
        this.actualDate = ConverterUtils.dateToString(resultEntity.getActualDate(), ConverterUtils.FORMAT_YYYY_MM_DD);
        this.realPaymentDate = ConverterUtils.dateToString(resultEntity.getRealPaymentDate(), ConverterUtils.FORMAT_YYYY_MM_DD);
        this.specialRelationship = resultEntity.getSpecial_Relationship();
        this.gracePrincipal = resultEntity.getGrace_Principal();
        this.gracePay = resultEntity.getGrace_Pay();
        this.typeOffPp = resultEntity.getTypeOfPp();
        this.productClassification = resultEntity.getProductClassification();
        this.totalAmountInstalment = resultEntity.getTotalAmount_Instalment();
        this.instalmentAmount = resultEntity.getInstalmentAmount();
        this.instalmentCount = resultEntity.getInstalmentCount();
        this.paymentPeriodInstalment = resultEntity.getPaymentPeriod_Instalment();
        this.accountingDateInstalment = ConverterUtils.dateToString(resultEntity.getAccountingDate_Instalment(), ConverterUtils.FORMAT_YYYY_MM_DD);
        this.outstandingInstalmentCount = resultEntity.getOutstandingInstalmentCount();
        this.outstandingAmountInstalment = resultEntity.getOutstandingAmount_Instalment();
        this.overdueInstalmentCountMonths = resultEntity.getOverdueInstalmentCountMonths();
        this.overdueAmountInstalments = resultEntity.getOverdueAmountInstalments();
        this.prolongationStartDateInstalment = ConverterUtils.dateToString(resultEntity.getProlongationStartDate_Instalment(), ConverterUtils.FORMAT_YYYY_MM_DD);
        this.prolongationEndDateInstalment = ConverterUtils.dateToString(resultEntity.getProlongationEndDate_Instalment(), ConverterUtils.FORMAT_YYYY_MM_DD);
        this.paymentMethodCc = resultEntity.getPaymentMethod_Cc();
        this.creditLimitCc = resultEntity.getCreditLimit_Cc();
        this.accountingDateCc = ConverterUtils.dateToString(resultEntity.getAccountingDate_Cc(), ConverterUtils.FORMAT_YYYY_MM_DD);
        this.monthlyInstallmentAmountCc = resultEntity.getMonthlyInstallmentAmount_Cc();
        this.residualAmountCc = resultEntity.getResidualAmount_Cc();
        this.overdueInstalmentCountMonthsCc = resultEntity.getOverdueInstalmentCountMonthsCc();
        this.overdueAmountCc = resultEntity.getOverdueAmountCc();
        this.prolongationStartDateCc = ConverterUtils.dateToString(resultEntity.getProlongationStartDate_Cc(), ConverterUtils.FORMAT_YYYY_MM_DD);
        this.prolongationEndDateCc = ConverterUtils.dateToString(resultEntity.getProlongationEndDate_Cc(), ConverterUtils.FORMAT_YYYY_MM_DD);
        this.collateralType = resultEntity.getCollateralType();
        this.subjectRole = resultEntity.getSubjectRole();
        this.entity = resultEntity.getEntity();
    }

    public String getFundingType() {
        return fundingType;
    }

    public void setFundingType(String fundingType) {
        this.fundingType = fundingType;
    }

    public String getCreditPurpose() {
        return creditPurpose;
    }

    public void setCreditPurpose(String creditPurpose) {
        this.creditPurpose = creditPurpose;
    }

    public String getCreditPurpose2() {
        return creditPurpose2;
    }

    public void setCreditPurpose2(String creditPurpose2) {
        this.creditPurpose2 = creditPurpose2;
    }

    public String getCreditObject() {
        return creditObject;
    }

    public void setCreditObject(String creditObject) {
        this.creditObject = creditObject;
    }

    public String getContractPhase() {
        return contractPhase;
    }

    public void setContractPhase(String contractPhase) {
        this.contractPhase = contractPhase;
    }

    public String[] getContractStatusMonths() {
        return contractStatusMonths;
    }

    public void setContractStatusMonths(String[] contractStatusMonths) {
        this.contractStatusMonths = contractStatusMonths;
    }

//    public String getContractStatusMonth00() {
//        return contractStatusMonth00;
//    }
//
//    public void setContractStatusMonth00(String contractStatusMonth00) {
//        this.contractStatusMonth00 = contractStatusMonth00;
//    }
//
//    public String getContractStatusMonth01() {
//        return contractStatusMonth01;
//    }
//
//    public void setContractStatusMonth01(String contractStatusMonth01) {
//        this.contractStatusMonth01 = contractStatusMonth01;
//    }
//
//    public String getContractStatusMonth02() {
//        return contractStatusMonth02;
//    }
//
//    public void setContractStatusMonth02(String contractStatusMonth02) {
//        this.contractStatusMonth02 = contractStatusMonth02;
//    }
//
//    public String getContractStatusMonth03() {
//        return contractStatusMonth03;
//    }
//
//    public void setContractStatusMonth03(String contractStatusMonth03) {
//        this.contractStatusMonth03 = contractStatusMonth03;
//    }
//
//    public String getContractStatusMonth04() {
//        return contractStatusMonth04;
//    }
//
//    public void setContractStatusMonth04(String contractStatusMonth04) {
//        this.contractStatusMonth04 = contractStatusMonth04;
//    }
//
//    public String getContractStatusMonth05() {
//        return contractStatusMonth05;
//    }
//
//    public void setContractStatusMonth05(String contractStatusMonth05) {
//        this.contractStatusMonth05 = contractStatusMonth05;
//    }
//
//    public String getContractStatusMonth06() {
//        return contractStatusMonth06;
//    }
//
//    public void setContractStatusMonth06(String contractStatusMonth06) {
//        this.contractStatusMonth06 = contractStatusMonth06;
//    }
//
//    public String getContractStatusMonth07() {
//        return contractStatusMonth07;
//    }
//
//    public void setContractStatusMonth07(String contractStatusMonth07) {
//        this.contractStatusMonth07 = contractStatusMonth07;
//    }
//
//    public String getContractStatusMonth08() {
//        return contractStatusMonth08;
//    }
//
//    public void setContractStatusMonth08(String contractStatusMonth08) {
//        this.contractStatusMonth08 = contractStatusMonth08;
//    }
//
//    public String getContractStatusMonth09() {
//        return contractStatusMonth09;
//    }
//
//    public void setContractStatusMonth09(String contractStatusMonth09) {
//        this.contractStatusMonth09 = contractStatusMonth09;
//    }
//
//    public String getContractStatusMonth10() {
//        return contractStatusMonth10;
//    }
//
//    public void setContractStatusMonth10(String contractStatusMonth10) {
//        this.contractStatusMonth10 = contractStatusMonth10;
//    }
//
//    public String getContractStatusMonth11() {
//        return contractStatusMonth11;
//    }
//
//    public void setContractStatusMonth11(String contractStatusMonth11) {
//        this.contractStatusMonth11 = contractStatusMonth11;
//    }
//
//    public String getContractStatusMonth12() {
//        return contractStatusMonth12;
//    }
//
//    public void setContractStatusMonth12(String contractStatusMonth12) {
//        this.contractStatusMonth12 = contractStatusMonth12;
//    }
//
//    public String getContractStatusMonth13() {
//        return contractStatusMonth13;
//    }
//
//    public void setContractStatusMonth13(String contractStatusMonth13) {
//        this.contractStatusMonth13 = contractStatusMonth13;
//    }
//
//    public String getContractStatusMonth14() {
//        return contractStatusMonth14;
//    }
//
//    public void setContractStatusMonth14(String contractStatusMonth14) {
//        this.contractStatusMonth14 = contractStatusMonth14;
//    }
//
//    public String getContractStatusMonth15() {
//        return contractStatusMonth15;
//    }
//
//    public void setContractStatusMonth15(String contractStatusMonth15) {
//        this.contractStatusMonth15 = contractStatusMonth15;
//    }
//
//    public String getContractStatusMonth16() {
//        return contractStatusMonth16;
//    }
//
//    public void setContractStatusMonth16(String contractStatusMonth16) {
//        this.contractStatusMonth16 = contractStatusMonth16;
//    }
//
//    public String getContractStatusMonth17() {
//        return contractStatusMonth17;
//    }
//
//    public void setContractStatusMonth17(String contractStatusMonth17) {
//        this.contractStatusMonth17 = contractStatusMonth17;
//    }
//
//    public String getContractStatusMonth18() {
//        return contractStatusMonth18;
//    }
//
//    public void setContractStatusMonth18(String contractStatusMonth18) {
//        this.contractStatusMonth18 = contractStatusMonth18;
//    }
//
//    public String getContractStatusMonth19() {
//        return contractStatusMonth19;
//    }
//
//    public void setContractStatusMonth19(String contractStatusMonth19) {
//        this.contractStatusMonth19 = contractStatusMonth19;
//    }
//
//    public String getContractStatusMonth20() {
//        return contractStatusMonth20;
//    }
//
//    public void setContractStatusMonth20(String contractStatusMonth20) {
//        this.contractStatusMonth20 = contractStatusMonth20;
//    }
//
//    public String getContractStatusMonth21() {
//        return contractStatusMonth21;
//    }
//
//    public void setContractStatusMonth21(String contractStatusMonth21) {
//        this.contractStatusMonth21 = contractStatusMonth21;
//    }
//
//    public String getContractStatusMonth22() {
//        return contractStatusMonth22;
//    }
//
//    public void setContractStatusMonth22(String contractStatusMonth22) {
//        this.contractStatusMonth22 = contractStatusMonth22;
//    }
//
//    public String getContractStatusMonth23() {
//        return contractStatusMonth23;
//    }
//
//    public void setContractStatusMonth23(String contractStatusMonth23) {
//        this.contractStatusMonth23 = contractStatusMonth23;
//    }

    public String getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(String applicationDate) {
        this.applicationDate = applicationDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getActualDate() {
        return actualDate;
    }

    public void setActualDate(String actualDate) {
        this.actualDate = actualDate;
    }

    public String getRealPaymentDate() {
        return realPaymentDate;
    }

    public void setRealPaymentDate(String realPaymentDate) {
        this.realPaymentDate = realPaymentDate;
    }

    public String getSpecialRelationship() {
        return specialRelationship;
    }

    public void setSpecialRelationship(String specialRelationship) {
        this.specialRelationship = specialRelationship;
    }

    public String getGracePrincipal() {
        return gracePrincipal;
    }

    public void setGracePrincipal(String gracePrincipal) {
        this.gracePrincipal = gracePrincipal;
    }

    public String getGracePay() {
        return gracePay;
    }

    public void setGracePay(String gracePay) {
        this.gracePay = gracePay;
    }

    public String getTypeOffPp() {
        return typeOffPp;
    }

    public void setTypeOffPp(String typeOffPp) {
        this.typeOffPp = typeOffPp;
    }

    public String getProductClassification() {
        return productClassification;
    }

    public void setProductClassification(String productClassification) {
        this.productClassification = productClassification;
    }

    public  String getTotalAmountInstalment() {
        return totalAmountInstalment;
    }

    public void setTotalAmountInstalment( String totalAmountInstalment) {
        this.totalAmountInstalment = totalAmountInstalment;
    }

    public  String getInstalmentAmount() {
        return instalmentAmount;
    }

    public void setInstalmentAmount( String instalmentAmount) {
        this.instalmentAmount = instalmentAmount;
    }

    public  String getInstalmentCount() {
        return instalmentCount;
    }

    public void setInstalmentCount( String instalmentCount) {
        this.instalmentCount = instalmentCount;
    }

    public String getPaymentPeriodInstalment() {
        return paymentPeriodInstalment;
    }

    public void setPaymentPeriodInstalment(String paymentPeriodInstalment) {
        this.paymentPeriodInstalment = paymentPeriodInstalment;
    }

    public String getAccountingDateInstalment() {
        return accountingDateInstalment;
    }

    public void setAccountingDateInstalment(String accountingDateInstalment) {
        this.accountingDateInstalment = accountingDateInstalment;
    }

    public  String getOutstandingInstalmentCount() {
        return outstandingInstalmentCount;
    }

    public void setOutstandingInstalmentCount( String outstandingInstalmentCount) {
        this.outstandingInstalmentCount = outstandingInstalmentCount;
    }

    public  String getOutstandingAmountInstalment() {
        return outstandingAmountInstalment;
    }

    public void setOutstandingAmountInstalment( String outstandingAmountInstalment) {
        this.outstandingAmountInstalment = outstandingAmountInstalment;
    }

    public String[] getOverdueInstalmentCountMonths() {
        return overdueInstalmentCountMonths;
    }

    public void setOverdueInstalmentCountMonths(String[] overdueInstalmentCountMonths) {
        this.overdueInstalmentCountMonths = overdueInstalmentCountMonths;
    }

//    public  String getOverdueInstalmentCountMonth0() {
//        return overdueInstalmentCountMonth0;
//    }
//
//    public void setOverdueInstalmentCountMonth0( String overdueInstalmentCountMonth0) {
//        this.overdueInstalmentCountMonth0 = overdueInstalmentCountMonth0;
//    }
//
//    public  String getOverdueInstalmentCountMonth1() {
//        return overdueInstalmentCountMonth1;
//    }
//
//    public void setOverdueInstalmentCountMonth1( String overdueInstalmentCountMonth1) {
//        this.overdueInstalmentCountMonth1 = overdueInstalmentCountMonth1;
//    }
//
//    public  String getOverdueInstalmentCountMonth2() {
//        return overdueInstalmentCountMonth2;
//    }
//
//    public void setOverdueInstalmentCountMonth2( String overdueInstalmentCountMonth2) {
//        this.overdueInstalmentCountMonth2 = overdueInstalmentCountMonth2;
//    }
//
//    public  String getOverdueInstalmentCountMonth3() {
//        return overdueInstalmentCountMonth3;
//    }
//
//    public void setOverdueInstalmentCountMonth3( String overdueInstalmentCountMonth3) {
//        this.overdueInstalmentCountMonth3 = overdueInstalmentCountMonth3;
//    }
//
//    public  String getOverdueInstalmentCountMonth4() {
//        return overdueInstalmentCountMonth4;
//    }
//
//    public void setOverdueInstalmentCountMonth4( String overdueInstalmentCountMonth4) {
//        this.overdueInstalmentCountMonth4 = overdueInstalmentCountMonth4;
//    }
//
//    public  String getOverdueInstalmentCountMonth5() {
//        return overdueInstalmentCountMonth5;
//    }
//
//    public void setOverdueInstalmentCountMonth5( String overdueInstalmentCountMonth5) {
//        this.overdueInstalmentCountMonth5 = overdueInstalmentCountMonth5;
//    }
//
//    public  String getOverdueInstalmentCountMonth6() {
//        return overdueInstalmentCountMonth6;
//    }
//
//    public void setOverdueInstalmentCountMonth6( String overdueInstalmentCountMonth6) {
//        this.overdueInstalmentCountMonth6 = overdueInstalmentCountMonth6;
//    }
//
//    public  String getOverdueInstalmentCountMonth7() {
//        return overdueInstalmentCountMonth7;
//    }
//
//    public void setOverdueInstalmentCountMonth7( String overdueInstalmentCountMonth7) {
//        this.overdueInstalmentCountMonth7 = overdueInstalmentCountMonth7;
//    }
//
//    public  String getOverdueInstalmentCountMonth8() {
//        return overdueInstalmentCountMonth8;
//    }
//
//    public void setOverdueInstalmentCountMonth8( String overdueInstalmentCountMonth8) {
//        this.overdueInstalmentCountMonth8 = overdueInstalmentCountMonth8;
//    }
//
//    public  String getOverdueInstalmentCountMonth9() {
//        return overdueInstalmentCountMonth9;
//    }
//
//    public void setOverdueInstalmentCountMonth9( String overdueInstalmentCountMonth9) {
//        this.overdueInstalmentCountMonth9 = overdueInstalmentCountMonth9;
//    }
//
//    public  String getOverdueInstalmentCountMonth10() {
//        return overdueInstalmentCountMonth10;
//    }
//
//    public void setOverdueInstalmentCountMonth10( String overdueInstalmentCountMonth10) {
//        this.overdueInstalmentCountMonth10 = overdueInstalmentCountMonth10;
//    }
//
//    public  String getOverdueInstalmentCountMonth11() {
//        return overdueInstalmentCountMonth11;
//    }
//
//    public void setOverdueInstalmentCountMonth11( String overdueInstalmentCountMonth11) {
//        this.overdueInstalmentCountMonth11 = overdueInstalmentCountMonth11;
//    }
//
//    public  String getOverdueInstalmentCountMonth12() {
//        return overdueInstalmentCountMonth12;
//    }
//
//    public void setOverdueInstalmentCountMonth12( String overdueInstalmentCountMonth12) {
//        this.overdueInstalmentCountMonth12 = overdueInstalmentCountMonth12;
//    }
//
//    public  String getOverdueInstalmentCountMonth13() {
//        return overdueInstalmentCountMonth13;
//    }
//
//    public void setOverdueInstalmentCountMonth13( String overdueInstalmentCountMonth13) {
//        this.overdueInstalmentCountMonth13 = overdueInstalmentCountMonth13;
//    }
//
//    public  String getOverdueInstalmentCountMonth14() {
//        return overdueInstalmentCountMonth14;
//    }
//
//    public void setOverdueInstalmentCountMonth14( String overdueInstalmentCountMonth14) {
//        this.overdueInstalmentCountMonth14 = overdueInstalmentCountMonth14;
//    }
//
//    public  String getOverdueInstalmentCountMonth15() {
//        return overdueInstalmentCountMonth15;
//    }
//
//    public void setOverdueInstalmentCountMonth15( String overdueInstalmentCountMonth15) {
//        this.overdueInstalmentCountMonth15 = overdueInstalmentCountMonth15;
//    }
//
//    public  String getOverdueInstalmentCountMonth16() {
//        return overdueInstalmentCountMonth16;
//    }
//
//    public void setOverdueInstalmentCountMonth16( String overdueInstalmentCountMonth16) {
//        this.overdueInstalmentCountMonth16 = overdueInstalmentCountMonth16;
//    }
//
//    public  String getOverdueInstalmentCountMonth17() {
//        return overdueInstalmentCountMonth17;
//    }
//
//    public void setOverdueInstalmentCountMonth17( String overdueInstalmentCountMonth17) {
//        this.overdueInstalmentCountMonth17 = overdueInstalmentCountMonth17;
//    }
//
//    public  String getOverdueInstalmentCountMonth18() {
//        return overdueInstalmentCountMonth18;
//    }
//
//    public void setOverdueInstalmentCountMonth18( String overdueInstalmentCountMonth18) {
//        this.overdueInstalmentCountMonth18 = overdueInstalmentCountMonth18;
//    }
//
//    public  String getOverdueInstalmentCountMonth19() {
//        return overdueInstalmentCountMonth19;
//    }
//
//    public void setOverdueInstalmentCountMonth19( String overdueInstalmentCountMonth19) {
//        this.overdueInstalmentCountMonth19 = overdueInstalmentCountMonth19;
//    }
//
//    public  String getOverdueInstalmentCountMonth20() {
//        return overdueInstalmentCountMonth20;
//    }
//
//    public void setOverdueInstalmentCountMonth20( String overdueInstalmentCountMonth20) {
//        this.overdueInstalmentCountMonth20 = overdueInstalmentCountMonth20;
//    }
//
//    public  String getOverdueInstalmentCountMonth21() {
//        return overdueInstalmentCountMonth21;
//    }
//
//    public void setOverdueInstalmentCountMonth21( String overdueInstalmentCountMonth21) {
//        this.overdueInstalmentCountMonth21 = overdueInstalmentCountMonth21;
//    }
//
//    public  String getOverdueInstalmentCountMonth22() {
//        return overdueInstalmentCountMonth22;
//    }
//
//    public void setOverdueInstalmentCountMonth22( String overdueInstalmentCountMonth22) {
//        this.overdueInstalmentCountMonth22 = overdueInstalmentCountMonth22;
//    }
//
//    public  String getOverdueInstalmentCountMonth23() {
//        return overdueInstalmentCountMonth23;
//    }
//
//    public void setOverdueInstalmentCountMonth23( String overdueInstalmentCountMonth23) {
//        this.overdueInstalmentCountMonth23 = overdueInstalmentCountMonth23;
//    }

    public String[] getOverdueAmountInstalments() {
        return overdueAmountInstalments;
    }

    public void setOverdueAmountInstalments(String[] overdueAmountInstalments) {
        this.overdueAmountInstalments = overdueAmountInstalments;
    }

//    public  String getOverdueAmount00Instalment() {
//        return overdueAmount00Instalment;
//    }
//
//    public void setOverdueAmount00Instalment( String overdueAmount00Instalment) {
//        this.overdueAmount00Instalment = overdueAmount00Instalment;
//    }
//
//    public  String getOverdueAmount01Instalment() {
//        return overdueAmount01Instalment;
//    }
//
//    public void setOverdueAmount01Instalment( String overdueAmount01Instalment) {
//        this.overdueAmount01Instalment = overdueAmount01Instalment;
//    }
//
//    public  String getOverdueAmount02Instalment() {
//        return overdueAmount02Instalment;
//    }
//
//    public void setOverdueAmount02Instalment( String overdueAmount02Instalment) {
//        this.overdueAmount02Instalment = overdueAmount02Instalment;
//    }
//
//    public  String getOverdueAmount03Instalment() {
//        return overdueAmount03Instalment;
//    }
//
//    public void setOverdueAmount03Instalment( String overdueAmount03Instalment) {
//        this.overdueAmount03Instalment = overdueAmount03Instalment;
//    }
//
//    public  String getOverdueAmount04Instalment() {
//        return overdueAmount04Instalment;
//    }
//
//    public void setOverdueAmount04Instalment( String overdueAmount04Instalment) {
//        this.overdueAmount04Instalment = overdueAmount04Instalment;
//    }
//
//    public  String getOverdueAmount05Instalment() {
//        return overdueAmount05Instalment;
//    }
//
//    public void setOverdueAmount05Instalment( String overdueAmount05Instalment) {
//        this.overdueAmount05Instalment = overdueAmount05Instalment;
//    }
//
//    public  String getOverdueAmount06Instalment() {
//        return overdueAmount06Instalment;
//    }
//
//    public void setOverdueAmount06Instalment( String overdueAmount06Instalment) {
//        this.overdueAmount06Instalment = overdueAmount06Instalment;
//    }
//
//    public  String getOverdueAmount07Instalment() {
//        return overdueAmount07Instalment;
//    }
//
//    public void setOverdueAmount07Instalment( String overdueAmount07Instalment) {
//        this.overdueAmount07Instalment = overdueAmount07Instalment;
//    }
//
//    public  String getOverdueAmount08Instalment() {
//        return overdueAmount08Instalment;
//    }
//
//    public void setOverdueAmount08Instalment( String overdueAmount08Instalment) {
//        this.overdueAmount08Instalment = overdueAmount08Instalment;
//    }
//
//    public  String getOverdueAmount09Instalment() {
//        return overdueAmount09Instalment;
//    }
//
//    public void setOverdueAmount09Instalment( String overdueAmount09Instalment) {
//        this.overdueAmount09Instalment = overdueAmount09Instalment;
//    }
//
//    public  String getOverdueAmount10Instalment() {
//        return overdueAmount10Instalment;
//    }
//
//    public void setOverdueAmount10Instalment( String overdueAmount10Instalment) {
//        this.overdueAmount10Instalment = overdueAmount10Instalment;
//    }
//
//    public  String getOverdueAmount11Instalment() {
//        return overdueAmount11Instalment;
//    }
//
//    public void setOverdueAmount11Instalment( String overdueAmount11Instalment) {
//        this.overdueAmount11Instalment = overdueAmount11Instalment;
//    }
//
//    public  String getOverdueAmount12Instalment() {
//        return overdueAmount12Instalment;
//    }
//
//    public void setOverdueAmount12Instalment( String overdueAmount12Instalment) {
//        this.overdueAmount12Instalment = overdueAmount12Instalment;
//    }
//
//    public  String getOverdueAmount13Instalment() {
//        return overdueAmount13Instalment;
//    }
//
//    public void setOverdueAmount13Instalment( String overdueAmount13Instalment) {
//        this.overdueAmount13Instalment = overdueAmount13Instalment;
//    }
//
//    public  String getOverdueAmount14Instalment() {
//        return overdueAmount14Instalment;
//    }
//
//    public void setOverdueAmount14Instalment( String overdueAmount14Instalment) {
//        this.overdueAmount14Instalment = overdueAmount14Instalment;
//    }
//
//    public  String getOverdueAmount15Instalment() {
//        return overdueAmount15Instalment;
//    }
//
//    public void setOverdueAmount15Instalment( String overdueAmount15Instalment) {
//        this.overdueAmount15Instalment = overdueAmount15Instalment;
//    }
//
//    public  String getOverdueAmount16Instalment() {
//        return overdueAmount16Instalment;
//    }
//
//    public void setOverdueAmount16Instalment( String overdueAmount16Instalment) {
//        this.overdueAmount16Instalment = overdueAmount16Instalment;
//    }
//
//    public  String getOverdueAmount17Instalment() {
//        return overdueAmount17Instalment;
//    }
//
//    public void setOverdueAmount17Instalment( String overdueAmount17Instalment) {
//        this.overdueAmount17Instalment = overdueAmount17Instalment;
//    }
//
//    public  String getOverdueAmount18Instalment() {
//        return overdueAmount18Instalment;
//    }
//
//    public void setOverdueAmount18Instalment( String overdueAmount18Instalment) {
//        this.overdueAmount18Instalment = overdueAmount18Instalment;
//    }
//
//    public  String getOverdueAmount19Instalment() {
//        return overdueAmount19Instalment;
//    }
//
//    public void setOverdueAmount19Instalment( String overdueAmount19Instalment) {
//        this.overdueAmount19Instalment = overdueAmount19Instalment;
//    }
//
//    public  String getOverdueAmount20Instalment() {
//        return overdueAmount20Instalment;
//    }
//
//    public void setOverdueAmount20Instalment( String overdueAmount20Instalment) {
//        this.overdueAmount20Instalment = overdueAmount20Instalment;
//    }
//
//    public  String getOverdueAmount21Instalment() {
//        return overdueAmount21Instalment;
//    }
//
//    public void setOverdueAmount21Instalment( String overdueAmount21Instalment) {
//        this.overdueAmount21Instalment = overdueAmount21Instalment;
//    }
//
//    public  String getOverdueAmount22Instalment() {
//        return overdueAmount22Instalment;
//    }
//
//    public void setOverdueAmount22Instalment( String overdueAmount22Instalment) {
//        this.overdueAmount22Instalment = overdueAmount22Instalment;
//    }
//
//    public  String getOverdueAmount23Instalment() {
//        return overdueAmount23Instalment;
//    }
//
//    public void setOverdueAmount23Instalment( String overdueAmount23Instalment) {
//        this.overdueAmount23Instalment = overdueAmount23Instalment;
//    }

    public String getProlongationStartDateInstalment() {
        return prolongationStartDateInstalment;
    }

    public void setProlongationStartDateInstalment(String prolongationStartDateInstalment) {
        this.prolongationStartDateInstalment = prolongationStartDateInstalment;
    }

    public String getProlongationEndDateInstalment() {
        return prolongationEndDateInstalment;
    }

    public void setProlongationEndDateInstalment(String prolongationEndDateInstalment) {
        this.prolongationEndDateInstalment = prolongationEndDateInstalment;
    }

    public String getPaymentMethodCc() {
        return paymentMethodCc;
    }

    public void setPaymentMethodCc(String paymentMethodCc) {
        this.paymentMethodCc = paymentMethodCc;
    }

    public  String getCreditLimitCc() {
        return creditLimitCc;
    }

    public void setCreditLimitCc( String creditLimitCc) {
        this.creditLimitCc = creditLimitCc;
    }

    public String getAccountingDateCc() {
        return accountingDateCc;
    }

    public void setAccountingDateCc(String accountingDateCc) {
        this.accountingDateCc = accountingDateCc;
    }

    public  String getMonthlyInstallmentAmountCc() {
        return monthlyInstallmentAmountCc;
    }

    public void setMonthlyInstallmentAmountCc( String monthlyInstallmentAmountCc) {
        this.monthlyInstallmentAmountCc = monthlyInstallmentAmountCc;
    }

    public  String getResidualAmountCc() {
        return residualAmountCc;
    }

    public void setResidualAmountCc( String residualAmountCc) {
        this.residualAmountCc = residualAmountCc;
    }

    public String[] getOverdueInstalmentCountMonthsCc() {
        return overdueInstalmentCountMonthsCc;
    }

    public void setOverdueInstalmentCountMonthsCc(String[] overdueInstalmentCountMonthsCc) {
        this.overdueInstalmentCountMonthsCc = overdueInstalmentCountMonthsCc;
    }

//    public  String getOverdueInstalmentCountMonth0Cc() {
//        return overdueInstalmentCountMonth0Cc;
//    }
//
//    public void setOverdueInstalmentCountMonth0Cc( String overdueInstalmentCountMonth0Cc) {
//        this.overdueInstalmentCountMonth0Cc = overdueInstalmentCountMonth0Cc;
//    }
//
//    public  String getOverdueInstalmentCountMonth1Cc() {
//        return overdueInstalmentCountMonth1Cc;
//    }
//
//    public void setOverdueInstalmentCountMonth1Cc( String overdueInstalmentCountMonth1Cc) {
//        this.overdueInstalmentCountMonth1Cc = overdueInstalmentCountMonth1Cc;
//    }
//
//    public  String getOverdueInstalmentCountMonth2Cc() {
//        return overdueInstalmentCountMonth2Cc;
//    }
//
//    public void setOverdueInstalmentCountMonth2Cc( String overdueInstalmentCountMonth2Cc) {
//        this.overdueInstalmentCountMonth2Cc = overdueInstalmentCountMonth2Cc;
//    }
//
//    public  String getOverdueInstalmentCountMonth3Cc() {
//        return overdueInstalmentCountMonth3Cc;
//    }
//
//    public void setOverdueInstalmentCountMonth3Cc( String overdueInstalmentCountMonth3Cc) {
//        this.overdueInstalmentCountMonth3Cc = overdueInstalmentCountMonth3Cc;
//    }
//
//    public  String getOverdueInstalmentCountMonth4Cc() {
//        return overdueInstalmentCountMonth4Cc;
//    }
//
//    public void setOverdueInstalmentCountMonth4Cc( String overdueInstalmentCountMonth4Cc) {
//        this.overdueInstalmentCountMonth4Cc = overdueInstalmentCountMonth4Cc;
//    }
//
//    public  String getOverdueInstalmentCountMonth5Cc() {
//        return overdueInstalmentCountMonth5Cc;
//    }
//
//    public void setOverdueInstalmentCountMonth5Cc( String overdueInstalmentCountMonth5Cc) {
//        this.overdueInstalmentCountMonth5Cc = overdueInstalmentCountMonth5Cc;
//    }
//
//    public  String getOverdueInstalmentCountMonth6Cc() {
//        return overdueInstalmentCountMonth6Cc;
//    }
//
//    public void setOverdueInstalmentCountMonth6Cc( String overdueInstalmentCountMonth6Cc) {
//        this.overdueInstalmentCountMonth6Cc = overdueInstalmentCountMonth6Cc;
//    }
//
//    public  String getOverdueInstalmentCountMonth7Cc() {
//        return overdueInstalmentCountMonth7Cc;
//    }
//
//    public void setOverdueInstalmentCountMonth7Cc( String overdueInstalmentCountMonth7Cc) {
//        this.overdueInstalmentCountMonth7Cc = overdueInstalmentCountMonth7Cc;
//    }
//
//    public  String getOverdueInstalmentCountMonth8Cc() {
//        return overdueInstalmentCountMonth8Cc;
//    }
//
//    public void setOverdueInstalmentCountMonth8Cc( String overdueInstalmentCountMonth8Cc) {
//        this.overdueInstalmentCountMonth8Cc = overdueInstalmentCountMonth8Cc;
//    }
//
//    public  String getOverdueInstalmentCountMonth9Cc() {
//        return overdueInstalmentCountMonth9Cc;
//    }
//
//    public void setOverdueInstalmentCountMonth9Cc( String overdueInstalmentCountMonth9Cc) {
//        this.overdueInstalmentCountMonth9Cc = overdueInstalmentCountMonth9Cc;
//    }
//
//    public  String getOverdueInstalmentCountMonth10Cc() {
//        return overdueInstalmentCountMonth10Cc;
//    }
//
//    public void setOverdueInstalmentCountMonth10Cc( String overdueInstalmentCountMonth10Cc) {
//        this.overdueInstalmentCountMonth10Cc = overdueInstalmentCountMonth10Cc;
//    }
//
//    public  String getOverdueInstalmentCountMonth11Cc() {
//        return overdueInstalmentCountMonth11Cc;
//    }
//
//    public void setOverdueInstalmentCountMonth11Cc( String overdueInstalmentCountMonth11Cc) {
//        this.overdueInstalmentCountMonth11Cc = overdueInstalmentCountMonth11Cc;
//    }
//
//    public  String getOverdueInstalmentCountMonth12Cc() {
//        return overdueInstalmentCountMonth12Cc;
//    }
//
//    public void setOverdueInstalmentCountMonth12Cc( String overdueInstalmentCountMonth12Cc) {
//        this.overdueInstalmentCountMonth12Cc = overdueInstalmentCountMonth12Cc;
//    }
//
//    public  String getOverdueInstalmentCountMonth13Cc() {
//        return overdueInstalmentCountMonth13Cc;
//    }
//
//    public void setOverdueInstalmentCountMonth13Cc( String overdueInstalmentCountMonth13Cc) {
//        this.overdueInstalmentCountMonth13Cc = overdueInstalmentCountMonth13Cc;
//    }
//
//    public  String getOverdueInstalmentCountMonth14Cc() {
//        return overdueInstalmentCountMonth14Cc;
//    }
//
//    public void setOverdueInstalmentCountMonth14Cc( String overdueInstalmentCountMonth14Cc) {
//        this.overdueInstalmentCountMonth14Cc = overdueInstalmentCountMonth14Cc;
//    }
//
//    public  String getOverdueInstalmentCountMonth15Cc() {
//        return overdueInstalmentCountMonth15Cc;
//    }
//
//    public void setOverdueInstalmentCountMonth15Cc( String overdueInstalmentCountMonth15Cc) {
//        this.overdueInstalmentCountMonth15Cc = overdueInstalmentCountMonth15Cc;
//    }
//
//    public  String getOverdueInstalmentCountMonth16Cc() {
//        return overdueInstalmentCountMonth16Cc;
//    }
//
//    public void setOverdueInstalmentCountMonth16Cc( String overdueInstalmentCountMonth16Cc) {
//        this.overdueInstalmentCountMonth16Cc = overdueInstalmentCountMonth16Cc;
//    }
//
//    public  String getOverdueInstalmentCountMonth17Cc() {
//        return overdueInstalmentCountMonth17Cc;
//    }
//
//    public void setOverdueInstalmentCountMonth17Cc( String overdueInstalmentCountMonth17Cc) {
//        this.overdueInstalmentCountMonth17Cc = overdueInstalmentCountMonth17Cc;
//    }
//
//    public  String getOverdueInstalmentCountMonth18Cc() {
//        return overdueInstalmentCountMonth18Cc;
//    }
//
//    public void setOverdueInstalmentCountMonth18Cc( String overdueInstalmentCountMonth18Cc) {
//        this.overdueInstalmentCountMonth18Cc = overdueInstalmentCountMonth18Cc;
//    }
//
//    public  String getOverdueInstalmentCountMonth19Cc() {
//        return overdueInstalmentCountMonth19Cc;
//    }
//
//    public void setOverdueInstalmentCountMonth19Cc( String overdueInstalmentCountMonth19Cc) {
//        this.overdueInstalmentCountMonth19Cc = overdueInstalmentCountMonth19Cc;
//    }
//
//    public  String getOverdueInstalmentCountMonth20Cc() {
//        return overdueInstalmentCountMonth20Cc;
//    }
//
//    public void setOverdueInstalmentCountMonth20Cc( String overdueInstalmentCountMonth20Cc) {
//        this.overdueInstalmentCountMonth20Cc = overdueInstalmentCountMonth20Cc;
//    }
//
//    public  String getOverdueInstalmentCountMonth21Cc() {
//        return overdueInstalmentCountMonth21Cc;
//    }
//
//    public void setOverdueInstalmentCountMonth21Cc( String overdueInstalmentCountMonth21Cc) {
//        this.overdueInstalmentCountMonth21Cc = overdueInstalmentCountMonth21Cc;
//    }
//
//    public  String getOverdueInstalmentCountMonth22Cc() {
//        return overdueInstalmentCountMonth22Cc;
//    }
//
//    public void setOverdueInstalmentCountMonth22Cc( String overdueInstalmentCountMonth22Cc) {
//        this.overdueInstalmentCountMonth22Cc = overdueInstalmentCountMonth22Cc;
//    }
//
//    public  String getOverdueInstalmentCountMonth23Cc() {
//        return overdueInstalmentCountMonth23Cc;
//    }
//
//    public void setOverdueInstalmentCountMonth23Cc( String overdueInstalmentCountMonth23Cc) {
//        this.overdueInstalmentCountMonth23Cc = overdueInstalmentCountMonth23Cc;
//    }

    public String[] getOverdueAmountCc() {
        return overdueAmountCc;
    }

    public void setOverdueAmountCc(String[] overdueAmountCc) {
        this.overdueAmountCc = overdueAmountCc;
    }

//    public  String getOverdueAmount00Cc() {
//        return overdueAmount00Cc;
//    }
//
//    public void setOverdueAmount00Cc( String overdueAmount00Cc) {
//        this.overdueAmount00Cc = overdueAmount00Cc;
//    }
//
//    public  String getOverdueAmount01Cc() {
//        return overdueAmount01Cc;
//    }
//
//    public void setOverdueAmount01Cc( String overdueAmount01Cc) {
//        this.overdueAmount01Cc = overdueAmount01Cc;
//    }
//
//    public  String getOverdueAmount02Cc() {
//        return overdueAmount02Cc;
//    }
//
//    public void setOverdueAmount02Cc( String overdueAmount02Cc) {
//        this.overdueAmount02Cc = overdueAmount02Cc;
//    }
//
//    public  String getOverdueAmount03Cc() {
//        return overdueAmount03Cc;
//    }
//
//    public void setOverdueAmount03Cc( String overdueAmount03Cc) {
//        this.overdueAmount03Cc = overdueAmount03Cc;
//    }
//
//    public  String getOverdueAmount04Cc() {
//        return overdueAmount04Cc;
//    }
//
//    public void setOverdueAmount04Cc( String overdueAmount04Cc) {
//        this.overdueAmount04Cc = overdueAmount04Cc;
//    }
//
//    public  String getOverdueAmount05Cc() {
//        return overdueAmount05Cc;
//    }
//
//    public void setOverdueAmount05Cc( String overdueAmount05Cc) {
//        this.overdueAmount05Cc = overdueAmount05Cc;
//    }
//
//    public  String getOverdueAmount06Cc() {
//        return overdueAmount06Cc;
//    }
//
//    public void setOverdueAmount06Cc( String overdueAmount06Cc) {
//        this.overdueAmount06Cc = overdueAmount06Cc;
//    }
//
//    public  String getOverdueAmount07Cc() {
//        return overdueAmount07Cc;
//    }
//
//    public void setOverdueAmount07Cc( String overdueAmount07Cc) {
//        this.overdueAmount07Cc = overdueAmount07Cc;
//    }
//
//    public  String getOverdueAmount08Cc() {
//        return overdueAmount08Cc;
//    }
//
//    public void setOverdueAmount08Cc( String overdueAmount08Cc) {
//        this.overdueAmount08Cc = overdueAmount08Cc;
//    }
//
//    public  String getOverdueAmount09Cc() {
//        return overdueAmount09Cc;
//    }
//
//    public void setOverdueAmount09Cc( String overdueAmount09Cc) {
//        this.overdueAmount09Cc = overdueAmount09Cc;
//    }
//
//    public  String getOverdueAmount10Cc() {
//        return overdueAmount10Cc;
//    }
//
//    public void setOverdueAmount10Cc( String overdueAmount10Cc) {
//        this.overdueAmount10Cc = overdueAmount10Cc;
//    }
//
//    public  String getOverdueAmount11Cc() {
//        return overdueAmount11Cc;
//    }
//
//    public void setOverdueAmount11Cc( String overdueAmount11Cc) {
//        this.overdueAmount11Cc = overdueAmount11Cc;
//    }
//
//    public  String getOverdueAmount12Cc() {
//        return overdueAmount12Cc;
//    }
//
//    public void setOverdueAmount12Cc( String overdueAmount12Cc) {
//        this.overdueAmount12Cc = overdueAmount12Cc;
//    }
//
//    public  String getOverdueAmount13Cc() {
//        return overdueAmount13Cc;
//    }
//
//    public void setOverdueAmount13Cc( String overdueAmount13Cc) {
//        this.overdueAmount13Cc = overdueAmount13Cc;
//    }
//
//    public  String getOverdueAmount14Cc() {
//        return overdueAmount14Cc;
//    }
//
//    public void setOverdueAmount14Cc( String overdueAmount14Cc) {
//        this.overdueAmount14Cc = overdueAmount14Cc;
//    }
//
//    public  String getOverdueAmount15Cc() {
//        return overdueAmount15Cc;
//    }
//
//    public void setOverdueAmount15Cc( String overdueAmount15Cc) {
//        this.overdueAmount15Cc = overdueAmount15Cc;
//    }
//
//    public  String getOverdueAmount16Cc() {
//        return overdueAmount16Cc;
//    }
//
//    public void setOverdueAmount16Cc( String overdueAmount16Cc) {
//        this.overdueAmount16Cc = overdueAmount16Cc;
//    }
//
//    public  String getOverdueAmount17Cc() {
//        return overdueAmount17Cc;
//    }
//
//    public void setOverdueAmount17Cc( String overdueAmount17Cc) {
//        this.overdueAmount17Cc = overdueAmount17Cc;
//    }
//
//    public  String getOverdueAmount18Cc() {
//        return overdueAmount18Cc;
//    }
//
//    public void setOverdueAmount18Cc( String overdueAmount18Cc) {
//        this.overdueAmount18Cc = overdueAmount18Cc;
//    }
//
//    public  String getOverdueAmount19Cc() {
//        return overdueAmount19Cc;
//    }
//
//    public void setOverdueAmount19Cc( String overdueAmount19Cc) {
//        this.overdueAmount19Cc = overdueAmount19Cc;
//    }
//
//    public  String getOverdueAmount20Cc() {
//        return overdueAmount20Cc;
//    }
//
//    public void setOverdueAmount20Cc( String overdueAmount20Cc) {
//        this.overdueAmount20Cc = overdueAmount20Cc;
//    }
//
//    public  String getOverdueAmount21Cc() {
//        return overdueAmount21Cc;
//    }
//
//    public void setOverdueAmount21Cc( String overdueAmount21Cc) {
//        this.overdueAmount21Cc = overdueAmount21Cc;
//    }
//
//    public  String getOverdueAmount22Cc() {
//        return overdueAmount22Cc;
//    }
//
//    public void setOverdueAmount22Cc( String overdueAmount22Cc) {
//        this.overdueAmount22Cc = overdueAmount22Cc;
//    }
//
//    public  String getOverdueAmount23Cc() {
//        return overdueAmount23Cc;
//    }
//
//    public void setOverdueAmount23Cc( String overdueAmount23Cc) {
//        this.overdueAmount23Cc = overdueAmount23Cc;
//    }

    public String getProlongationStartDateCc() {
        return prolongationStartDateCc;
    }

    public void setProlongationStartDateCc(String prolongationStartDateCc) {
        this.prolongationStartDateCc = prolongationStartDateCc;
    }

    public String getProlongationEndDateCc() {
        return prolongationEndDateCc;
    }

    public void setProlongationEndDateCc(String prolongationEndDateCc) {
        this.prolongationEndDateCc = prolongationEndDateCc;
    }

    public String getCollateralType() {
        return collateralType;
    }

    public void setCollateralType(String collateralType) {
        this.collateralType = collateralType;
    }

    public String getSubjectRole() {
        return subjectRole;
    }

    public void setSubjectRole(String subjectRole) {
        this.subjectRole = subjectRole;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }
}
