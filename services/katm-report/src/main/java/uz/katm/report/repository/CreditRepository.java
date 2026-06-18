package uz.katm.report.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import uz.katm.common.util.JdbcUtils;
import uz.katm.report.domain.record.ClaimData;
import uz.katm.report.domain.record.CreditReportRequest;
import uz.katm.report.domain.record.CreditReportResponse;
import uz.katm.report.domain.record.FicoResult;
import uz.katm.report.domain.record.PrepareResult;
import uz.katm.report.domain.record.ProcedureResult;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class CreditRepository {

    private final SimpleJdbcCall getCreditReportCall;
    private final SimpleJdbcCall getCreditReportStatusCall;
    private final SimpleJdbcCall getInfoscoreCall;
    private final SimpleJdbcCall getInfoscoreLegalCall;
    private final SimpleJdbcCall getFicoScoreCall;
    private final SimpleJdbcCall getQualityReportCall;
    private final SimpleJdbcCall addNewRequestCall;
    private final SimpleJdbcCall claimLoanSubjectCheckCall;
    private final SimpleJdbcCall getDataByClaimCall;
    private final SimpleJdbcCall prepareCreditReportCall;
    private final SimpleJdbcCall addGatewayRequestsLogCall;
    private final SimpleJdbcCall availabilityOfCreditCall;
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    public CreditRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.namedJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.getCreditReportCall = new SimpleJdbcCall(dataSource)
                .withSchemaName("DATAS")
                .withCatalogName("PKG_ONLINE")
                .withProcedureName("GET_CREDIT_REPORT")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("P_HEAD", Types.VARCHAR),
                        new SqlParameter("P_CODE", Types.VARCHAR),
                        new SqlParameter("P_IS_LEGAL", Types.INTEGER),
                        new SqlParameter("P_CLAIM_ID", Types.VARCHAR),
                        new SqlParameter("P_IP", Types.VARCHAR),
                        new SqlParameter("P_REPORT_ID", Types.INTEGER),
                        new SqlParameter("P_MIP_RESPONSE", Types.VARCHAR),
                        new SqlParameter("P_IS_SHOW", Types.INTEGER),
                        new SqlParameter("P_LANG", Types.VARCHAR),
                        new SqlParameter("P_JURIDICAL_STATUS", Types.VARCHAR),
                        new SqlParameter("P_LOAN_SUBJECT", Types.VARCHAR),
                        new SqlParameter("P_PERSONAL_CODE", Types.VARCHAR),
                        new SqlParameter("P_INN", Types.VARCHAR),
                        new SqlParameter("P_OWNER_ID", Types.VARCHAR),
                        new SqlOutParameter("P_RESULT", Types.VARCHAR),
                        new SqlOutParameter("P_RET_MSG", Types.VARCHAR),
                        new SqlOutParameter("P_REPORT", Types.CLOB),
                        new SqlOutParameter("P_TOKEN", Types.VARCHAR)
                );

        this.getCreditReportStatusCall = new SimpleJdbcCall(dataSource)
                .withSchemaName("DATAS")
                .withCatalogName("PKG_ONLINE")
                .withProcedureName("GET_CREDIT_REPORT_STATUS")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("P_HEAD", Types.VARCHAR),
                        new SqlParameter("P_CODE", Types.VARCHAR),
                        new SqlParameter("P_CLAIM_ID", Types.VARCHAR),
                        new SqlParameter("P_TOKEN", Types.VARCHAR),
                        new SqlOutParameter("P_RESULT", Types.VARCHAR),
                        new SqlOutParameter("P_RET_MSG", Types.VARCHAR),
                        new SqlOutParameter("P_REPORT", Types.CLOB)
                );

        this.getInfoscoreCall = new SimpleJdbcCall(dataSource)
                .withSchemaName("DATAS")
                .withCatalogName("PKG_ONLINE")
                .withProcedureName("GET_INFOSCORE_REPORT")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("P_PIN", Types.VARCHAR),
                        new SqlOutParameter("P_RESULT", Types.VARCHAR),
                        new SqlOutParameter("P_RET_MSG", Types.VARCHAR),
                        new SqlOutParameter("P_REPORT", Types.CLOB)
                );

        this.getInfoscoreLegalCall = new SimpleJdbcCall(dataSource)
                .withSchemaName("DATAS")
                .withCatalogName("PKG_ONLINE")
                .withProcedureName("GET_INFOSCORE_LEGAL_REPORT")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("P_TIN", Types.VARCHAR),
                        new SqlOutParameter("P_RESULT", Types.VARCHAR),
                        new SqlOutParameter("P_RET_MSG", Types.VARCHAR),
                        new SqlOutParameter("P_REPORT", Types.CLOB)
                );

        this.getFicoScoreCall = new SimpleJdbcCall(dataSource)
                .withSchemaName("DATAS")
                .withCatalogName("PKG_FICO")
                .withProcedureName("GET_FICO_SCORE")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("P_CLIENT_ID", Types.VARCHAR),
                        new SqlOutParameter("P_RESULT", Types.VARCHAR),
                        new SqlOutParameter("P_RET_MSG", Types.VARCHAR),
                        new SqlOutParameter("P_SCORE", Types.NUMERIC),
                        new SqlOutParameter("P_SCORE_DATE", Types.VARCHAR)
                );

        // Отчёт по изменению качества КИ за период. Перенос
        // gov.uz.katm.db.orcl.credit.history.GetQualityReport (DATAS.PKG_REPORTS.DISCOVERCO_129_XML_MOD).
        // Сигнатура сверена с ALL_ARGUMENTS: P_LOGIN, P_PASSWORD, PHEAD, PCODE, PDATE_FROM, PDATE_TO, RESULT_M.
        // Биндинг позиционный (withoutProcedureColumnMetaDataAccess) — порядок обязан совпадать с БД.
        this.getQualityReportCall = new SimpleJdbcCall(dataSource)
                .withSchemaName("DATAS")
                .withCatalogName("PKG_REPORTS")
                .withProcedureName("DISCOVERCO_129_XML_MOD")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("P_LOGIN", Types.VARCHAR),
                        new SqlParameter("P_PASSWORD", Types.VARCHAR),
                        new SqlParameter("P_HEAD", Types.VARCHAR),
                        new SqlParameter("P_CODE", Types.VARCHAR),
                        new SqlParameter("PDATE_FROM", Types.DATE),
                        new SqlParameter("PDATE_TO", Types.DATE),
                        new SqlOutParameter("RESULT_M", Types.CLOB)
                );

        // Журналирование запроса отчёта (антидубль). Перенос
        // gov.uz.katm.db.orcl.system.LogCreditReportRequest (DATAS.PKG_ONLINE.ADDNEWREQUEST).
        this.addNewRequestCall = new SimpleJdbcCall(dataSource)
                .withSchemaName("DATAS")
                .withCatalogName("PKG_ONLINE")
                .withProcedureName("ADDNEWREQUEST")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("PBANK", Types.VARCHAR),
                        new SqlParameter("PBRANCH", Types.VARCHAR),
                        new SqlParameter("PCLAIMID", Types.VARCHAR),
                        new SqlParameter("REP_ID", Types.VARCHAR),
                        new SqlParameter("P_OWNER_ID", Types.VARCHAR),
                        new SqlParameter("P_SUBREPORT_ID", Types.INTEGER)
                );

        // Проверка соответствия предмета займа заявке. Перенос
        // gov.uz.katm.db.orcl.credit.history.ClaimLoanSubjectCheck (DATAS.PKG_ONLINE.CLAIM_LOAN_SUBJECT_CHECK).
        // Биндинг позиционный — порядок параметров обязан совпадать с БД.
        this.claimLoanSubjectCheckCall = new SimpleJdbcCall(dataSource)
                .withSchemaName("DATAS")
                .withCatalogName("PKG_ONLINE")
                .withProcedureName("CLAIM_LOAN_SUBJECT_CHECK")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("P_HEAD", Types.VARCHAR),
                        new SqlParameter("P_CODE", Types.VARCHAR),
                        new SqlParameter("P_CLAIM_ID", Types.VARCHAR),
                        new SqlParameter("P_LOAN_SUBJECT", Types.VARCHAR),
                        new SqlParameter("P_OWNER_ID", Types.VARCHAR),
                        new SqlOutParameter("P_RESULT", Types.VARCHAR),
                        new SqlOutParameter("P_RET_MSG", Types.VARCHAR)
                );

        // Данные субъекта по заявке (для UzCard-submit нужны ПИНФЛ и признак юрлица). Перенос
        // gov.uz.katm.db.orcl.client.info.GetDataByClaim (DATAS.PKG_ONLINE.GET_DATA_BY_CLAIM).
        // Дублирует биндинг katm-client (shared-DB). P_LOGIN/P_PASSWORD опущены (head/code из JWT).
        // OUT-параметры объявлены в порядке монолита (позиционный биндинг); читаем только ПИНФЛ и IS_LEGAL.
        this.getDataByClaimCall = new SimpleJdbcCall(dataSource)
                .withSchemaName("DATAS")
                .withCatalogName("PKG_ONLINE")
                .withProcedureName("GET_DATA_BY_CLAIM")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("P_HEAD", Types.VARCHAR),
                        new SqlParameter("P_CODE", Types.VARCHAR),
                        new SqlParameter("P_CLAIM_ID", Types.VARCHAR),
                        new SqlParameter("P_REPORT_ID", Types.VARCHAR),
                        new SqlOutParameter("P_RESULT", Types.VARCHAR),
                        new SqlOutParameter("P_RET_MSG", Types.VARCHAR),
                        new SqlOutParameter("P_PINFL", Types.VARCHAR),
                        new SqlOutParameter("P_INN", Types.VARCHAR),
                        new SqlOutParameter("P_SERIE", Types.VARCHAR),
                        new SqlOutParameter("P_NUMBER", Types.VARCHAR),
                        new SqlOutParameter("P_CREDITOR_TYPE", Types.VARCHAR),
                        new SqlOutParameter("P_CREDITOR_CODE", Types.VARCHAR),
                        new SqlOutParameter("P_CLAIM_DATE", Types.VARCHAR),
                        new SqlOutParameter("P_AGREEMENT_NUMBER", Types.VARCHAR),
                        new SqlOutParameter("P_AGREEMENT_DATE", Types.VARCHAR),
                        new SqlOutParameter("P_USER_NAME", Types.VARCHAR),
                        new SqlOutParameter("P_USER_PASSWORD", Types.VARCHAR),
                        new SqlOutParameter("P_LANGUAGE", Types.VARCHAR),
                        new SqlOutParameter("P_PORTALBRANCHID", Types.VARCHAR),
                        new SqlOutParameter("P_HASH", Types.VARCHAR),
                        new SqlOutParameter("P_IS_LEGAL", Types.INTEGER),
                        new SqlOutParameter("P_DATE_BIRTH", Types.DATE),
                        new SqlOutParameter("P_F_NAME", Types.VARCHAR),
                        new SqlOutParameter("P_NAME", Types.VARCHAR),
                        new SqlOutParameter("P_PHONE", Types.VARCHAR),
                        new SqlOutParameter("P_CLIENT_ID", Types.VARCHAR)
                );

        // Подготовка отчёта (выделяет demandId). Перенос
        // gov.uz.katm.db.orcl.credit.history.PrepareCreditReport (DATAS.PKG_ONLINE.PREPARE_CREDIT_REPORT).
        // P_LOGIN/P_PASSWORD опущены (head/code из JWT).
        this.prepareCreditReportCall = new SimpleJdbcCall(dataSource)
                .withSchemaName("DATAS")
                .withCatalogName("PKG_ONLINE")
                .withProcedureName("PREPARE_CREDIT_REPORT")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("P_HEAD", Types.VARCHAR),
                        new SqlParameter("P_CODE", Types.VARCHAR),
                        new SqlParameter("P_IS_LEGAL", Types.INTEGER),
                        new SqlParameter("P_CLAIM_ID", Types.VARCHAR),
                        new SqlParameter("P_IP", Types.VARCHAR),
                        new SqlParameter("P_REPORT_ID", Types.INTEGER),
                        new SqlParameter("P_MIP_RESPONSE", Types.VARCHAR),
                        new SqlParameter("P_IS_SHOW", Types.INTEGER),
                        new SqlParameter("P_LANG", Types.VARCHAR),
                        new SqlParameter("P_LOAN_SUBJECT", Types.VARCHAR),
                        new SqlParameter("P_OWNER_ID", Types.VARCHAR),
                        new SqlParameter("P_REPORT_REASON", Types.INTEGER),
                        new SqlOutParameter("P_RESULT", Types.VARCHAR),
                        new SqlOutParameter("P_RET_MSG", Types.VARCHAR),
                        new SqlOutParameter("P_REPORT", Types.CLOB),
                        new SqlOutParameter("P_TOKEN", Types.VARCHAR),
                        new SqlOutParameter("P_DEMAND_ID", Types.VARCHAR)
                );

        // Журналирование шлюзовых запросов (UzCard). Перенос
        // gov.uz.katm.db.orcl.gateway.AddGatewayRequestsLog (DATAS.PKG_ONLINE.ADD_GATEWAY_REQUESTS_LOG).
        this.addGatewayRequestsLogCall = new SimpleJdbcCall(dataSource)
                .withSchemaName("DATAS")
                .withCatalogName("PKG_ONLINE")
                .withProcedureName("ADD_GATEWAY_REQUESTS_LOG")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("P_ID", Types.INTEGER),
                        new SqlParameter("P_SOURCE", Types.INTEGER),
                        new SqlParameter("P_HEAD", Types.VARCHAR),
                        new SqlParameter("P_CODE", Types.VARCHAR),
                        new SqlParameter("P_CLAIM_ID", Types.VARCHAR),
                        new SqlParameter("P_COUNT_ID", Types.VARCHAR),
                        new SqlParameter("P_REQUEST_DATE", Types.DATE),
                        new SqlParameter("P_REQUEST", Types.VARCHAR),
                        new SqlParameter("P_RESPONSE", Types.VARCHAR),
                        new SqlOutParameter("P_RESULT", Types.VARCHAR),
                        new SqlOutParameter("P_RET_MSG", Types.VARCHAR)
                );

        // Блок наличия КИ, дописываемый к отчётам salary/einvoice/cashback. Перенос
        // gov.uz.katm.db.orcl.credit.history.AvailabilityOfCreditRepC (DATAS.NEW_REPORTS.AVAILABILITY_OF_CREDIT_REP_C).
        this.availabilityOfCreditCall = new SimpleJdbcCall(dataSource)
                .withSchemaName("DATAS")
                .withCatalogName("NEW_REPORTS")
                .withProcedureName("AVAILABILITY_OF_CREDIT_REP_C")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("P_HEAD", Types.VARCHAR),
                        new SqlParameter("P_CODE", Types.VARCHAR),
                        new SqlParameter("P_CLAIM_ID", Types.VARCHAR),
                        new SqlOutParameter("MAIN_RESULT", Types.CLOB)
                );
    }

    /** XML-документ блока наличия КИ по заявке (или null). */
    public byte[] getAvailabilityDoc(String head, String code, String claimId) {
        Map<String, Object> out = availabilityOfCreditCall.execute(new MapSqlParameterSource()
                .addValue("P_HEAD", head)
                .addValue("P_CODE", code)
                .addValue("P_CLAIM_ID", claimId));
        return JdbcUtils.readClob(out, "MAIN_RESULT");
    }

    // === UzCard submit: данные субъекта, подготовка demandId, журналирование шлюза ===

    /** ПИНФЛ и признак юрлица по заявке (GET_DATA_BY_CLAIM). reportId передаётся строкой. */
    public ClaimData getClaimData(String head, String code, String claimId, String reportId) {
        Map<String, Object> out = getDataByClaimCall.execute(new MapSqlParameterSource()
                .addValue("P_HEAD", head)
                .addValue("P_CODE", code)
                .addValue("P_CLAIM_ID", claimId)
                .addValue("P_REPORT_ID", reportId));
        // Телефон: к 9-значному номеру добавляем код страны 998 (как монолитный ClientService).
        String phone = (String) out.get("P_PHONE");
        if (phone != null && phone.length() == 9) {
            phone = "998" + phone;
        }
        Object birth = out.get("P_DATE_BIRTH");
        String birthDate = (birth instanceof java.util.Date d)
                ? new java.text.SimpleDateFormat("dd.MM.yyyy").format(d) : null;
        return new ClaimData(
                String.valueOf(out.getOrDefault("P_RESULT", "-1")),
                (String) out.get("P_RET_MSG"),
                (String) out.get("P_PINFL"),
                (String) out.get("P_INN"),
                out.get("P_IS_LEGAL") != null ? ((Number) out.get("P_IS_LEGAL")).intValue() : null,
                phone,
                (String) out.get("P_SERIE"),
                (String) out.get("P_NUMBER"),
                (String) out.get("P_NAME"),     // P_NAME → имя
                (String) out.get("P_F_NAME"),   // P_F_NAME → фамилия
                birthDate);
    }

    /** Подготовка отчёта (PREPARE_CREDIT_REPORT) — выделяет demandId. mipResponse="", isShow=0, reportReason=null. */
    public PrepareResult prepareCreditReport(String head, String code, int isLegal, String claimId, String ip,
                                             int reportId, String lang, String loanSubject, String ownerId) {
        Map<String, Object> out = prepareCreditReportCall.execute(new MapSqlParameterSource()
                .addValue("P_HEAD", head)
                .addValue("P_CODE", code)
                .addValue("P_IS_LEGAL", isLegal)
                .addValue("P_CLAIM_ID", claimId)
                .addValue("P_IP", ip)
                .addValue("P_REPORT_ID", reportId)
                .addValue("P_MIP_RESPONSE", "")
                .addValue("P_IS_SHOW", 0)
                .addValue("P_LANG", lang)
                .addValue("P_LOAN_SUBJECT", loanSubject)
                .addValue("P_OWNER_ID", ownerId)
                .addValue("P_REPORT_REASON", null, Types.INTEGER));
        return new PrepareResult(
                String.valueOf(out.getOrDefault("P_RESULT", "-1")),
                (String) out.get("P_RET_MSG"),
                (String) out.get("P_DEMAND_ID"));
    }

    /** Журналирование шлюзового запроса (ADD_GATEWAY_REQUESTS_LOG). source: 1=UzCard, 2=Humo. */
    public void addGatewayRequestsLog(int id, int source, String head, String code, String claimId,
                                      String countId, String request, String response) {
        addGatewayRequestsLogCall.execute(new MapSqlParameterSource()
                .addValue("P_ID", id)
                .addValue("P_SOURCE", source)
                .addValue("P_HEAD", head)
                .addValue("P_CODE", code)
                .addValue("P_CLAIM_ID", claimId)
                .addValue("P_COUNT_ID", countId)
                .addValue("P_REQUEST_DATE", new Date(System.currentTimeMillis()), Types.DATE)
                .addValue("P_REQUEST", request)
                .addValue("P_RESPONSE", response));
    }

    /** Журналирование шлюзового запроса с source=1 (UzCard). */
    public void addGatewayRequestsLog(int id, String head, String code, String claimId,
                                      String countId, String request, String response) {
        addGatewayRequestsLog(id, 1, head, code, claimId, countId, request, response);
    }

    // === Валидация запроса отчёта (перенос CreditReportServiceImpl.validateCreditReportRequest) ===

    /**
     * Приём отчётов разрешён, если активна сессия загрузчика (Loader.x_Open_Session: id=3, state=1).
     * Перенос CreditReportRepositoryImpl.checkActiveDemandWork.
     */
    public int checkActiveDemandWork() {
        Integer cnt = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM Loader.x_Open_Session t WHERE t.State = 1 AND t.Id = 3", Integer.class);
        return cnt != null ? cnt : 0;
    }

    /** Доступ организации (head=ORG) к отчёту: datas.pkg_online.Get_Report_Correctness (<1 — нет доступа). */
    public int getReportCorrectness(String head, String code, String reportId) {
        Integer res = namedJdbcTemplate.queryForObject(
                "SELECT datas.pkg_online.Get_Report_Correctness(:head, :code, :reportId) FROM DUAL",
                new MapSqlParameterSource()
                        .addValue("head", head).addValue("code", code).addValue("reportId", reportId),
                Integer.class);
        return res != null ? res : 0;
    }

    /** Антидубль: >0, если идентичный запрос отправлен <5 мин назад. datas.pkg_online.checkLastRequestTime. */
    public int checkLastDemandTime(String head, String code, String claimId, String reportId,
                                   String ownerId, Integer subReportId) {
        Integer res = namedJdbcTemplate.queryForObject(
                "SELECT datas.pkg_online.checkLastRequestTime(:head, :code, :claimId, :reportId, :ownerId, :subReportId) FROM DUAL",
                new MapSqlParameterSource()
                        .addValue("head", head).addValue("code", code).addValue("claimId", claimId)
                        .addValue("reportId", reportId).addValue("ownerId", ownerId)
                        .addValue("subReportId", subReportId, Types.INTEGER),
                Integer.class);
        return res != null ? res : 0;
    }

    /** Журналирование запроса отчёта: datas.pkg_online.ADDNEWREQUEST. */
    public void logCreditReportRequest(String head, String code, String claimId, String reportId,
                                       String ownerId, Integer subReportId) {
        addNewRequestCall.execute(new MapSqlParameterSource()
                .addValue("PBANK", head).addValue("PBRANCH", code).addValue("PCLAIMID", claimId)
                .addValue("REP_ID", reportId).addValue("P_OWNER_ID", ownerId)
                .addValue("P_SUBREPORT_ID", subReportId, Types.INTEGER));
    }

    /** Проверка предмета займа: datas.pkg_online.CLAIM_LOAN_SUBJECT_CHECK (P_RESULT=05000 — успех). */
    public ProcedureResult checkClaimLoanSubject(String head, String code, String claimId,
                                                 String loanSubject, String ownerId) {
        Map<String, Object> out = claimLoanSubjectCheckCall.execute(new MapSqlParameterSource()
                .addValue("P_HEAD", head).addValue("P_CODE", code).addValue("P_CLAIM_ID", claimId)
                .addValue("P_LOAN_SUBJECT", loanSubject).addValue("P_OWNER_ID", ownerId));
        return new ProcedureResult(String.valueOf(out.get("P_RESULT")), (String) out.get("P_RET_MSG"));
    }

    /**
     * Freeze-блокировка: datas.PKG_FREEZE.Get_Subscription_Status возвращает 1, если субъект запретил
     * выдачу КИ (услуга Freeze). Перенос freeze-проверки из validateCreditReportRequest
     * (монолит вызывал notificationService.checkSubscription — функция PKG_FREEZE.Get_Subscription_Status).
     */
    public int checkFreezeSubscription(String head, String code, String claimId, String reportId) {
        Integer res = namedJdbcTemplate.queryForObject(
                "SELECT datas.PKG_FREEZE.Get_Subscription_Status(:head, :code, :claimId, :reportId) FROM DUAL",
                new MapSqlParameterSource()
                        .addValue("head", head).addValue("code", code)
                        .addValue("claimId", claimId).addValue("reportId", reportId),
                Integer.class);
        return res != null ? res : 0;
    }

    // === Интеграция UzCard (асинхронный опрос card.counts.get по записям gateway_requests_log) ===

    /**
     * Идентификаторы заданий (count_id) по заявке для опроса UzCard. Перенос
     * CreditReportRepositoryImpl.getCountIdsFromGatewayRequestsLog: ещё не завершённые (status null/0)
     * записи демонда, кроме самого токена.
     */
    public List<String> getCountIdsFromGatewayRequestsLog(String head, String code, String claimId,
                                                          String demandId, String exceptCountId) {
        return namedJdbcTemplate.queryForList(
                "select COUNT_ID from datas.gateway_requests_log " +
                        "where head = :head and code = :code and claim_id = :claimId " +
                        "and count_id like :countId and count_id != :exceptCountId " +
                        "and (status is null or status = 0)",
                new MapSqlParameterSource()
                        .addValue("head", head).addValue("code", code).addValue("claimId", claimId)
                        .addValue("countId", "%" + demandId + "%").addValue("exceptCountId", exceptCountId),
                String.class);
    }

    /** Фиксация результата опроса задания UzCard в gateway_requests_log. */
    public void updateGatewayRequestsLog(Integer requestId, String countId, String request,
                                         String response, Integer status) {
        namedJdbcTemplate.update(
                "UPDATE datas.gateway_requests_log SET status = :status, request = :request, " +
                        "response = :response WHERE ID = :requestId AND COUNT_ID = :countId",
                new MapSqlParameterSource()
                        .addValue("status", status).addValue("request", request)
                        .addValue("response", response).addValue("requestId", requestId)
                        .addValue("countId", countId));
    }

    public CreditReportResponse getCreditReport(String head, String code, CreditReportRequest req) {
        var params = new MapSqlParameterSource()
                .addValue("P_HEAD", head)
                .addValue("P_CODE", code)
                .addValue("P_IS_LEGAL", req.isLegal())
                .addValue("P_CLAIM_ID", req.claimId())
                .addValue("P_IP", req.ip())
                .addValue("P_REPORT_ID", req.reportId())
                .addValue("P_MIP_RESPONSE", req.mipResponse())
                .addValue("P_IS_SHOW", req.isShow())
                .addValue("P_LANG", req.lang())
                .addValue("P_JURIDICAL_STATUS", req.juridicalStatus())
                .addValue("P_LOAN_SUBJECT", req.loanSubject())
                .addValue("P_PERSONAL_CODE", req.personalCode())
                .addValue("P_INN", req.inn())
                .addValue("P_OWNER_ID", req.ownerId());
        return toResponse(getCreditReportCall.execute(params), "P_REPORT", "P_TOKEN");
    }

    public CreditReportResponse getCreditReportStatus(String head, String code, String claimId, String token) {
        var params = new MapSqlParameterSource()
                .addValue("P_HEAD", head)
                .addValue("P_CODE", code)
                .addValue("P_CLAIM_ID", claimId)
                .addValue("P_TOKEN", token);
        return toResponse(getCreditReportStatusCall.execute(params), "P_REPORT", null);
    }

    public CreditReportResponse getInfoscoreReport(String pin) {
        var params = new MapSqlParameterSource().addValue("P_PIN", pin);
        return toResponse(getInfoscoreCall.execute(params), "P_REPORT", null);
    }

    public CreditReportResponse getInfoscoreLegalReport(String tin) {
        var params = new MapSqlParameterSource().addValue("P_TIN", tin);
        return toResponse(getInfoscoreLegalCall.execute(params), "P_REPORT", null);
    }

    public List<FicoResult> getFicoScore(String clientId) {
        var params = new MapSqlParameterSource().addValue("P_CLIENT_ID", clientId);
        Map<String, Object> out = getFicoScoreCall.execute(params);
        List<FicoResult> results = new ArrayList<>();
        if (out.get("P_SCORE") != null) {
            results.add(new FicoResult(
                    clientId,
                    (BigDecimal) out.get("P_SCORE"),
                    (String) out.get("P_SCORE_DATE")
            ));
        }
        return results;
    }

    public CreditReportResponse getQualityReport(String login, String password, String head, String code,
                                                 LocalDate dateFrom, LocalDate dateTo) {
        var params = new MapSqlParameterSource()
                .addValue("P_LOGIN", login)
                .addValue("P_PASSWORD", password)
                .addValue("P_HEAD", head)
                .addValue("P_CODE", code)
                .addValue("PDATE_FROM", dateFrom != null ? Date.valueOf(dateFrom) : null, Types.DATE)
                .addValue("PDATE_TO", dateTo != null ? Date.valueOf(dateTo) : null, Types.DATE);
        Map<String, Object> out = getQualityReportCall.execute(params);
        byte[] report = JdbcUtils.readClob(out, "RESULT_M");
        // Процедура не возвращает P_RESULT/P_RET_MSG: успех определяется наличием документа.
        if (report != null && report.length > 0) {
            return new CreditReportResponse("0", "OK", report, null);
        }
        return new CreditReportResponse("-1", "Данные не найдены. Проверьте корректность организации и периода", null, null);
    }

    private CreditReportResponse toResponse(Map<String, Object> out, String reportKey, String tokenKey) {
        String code    = String.valueOf(out.getOrDefault("P_RESULT", "-1"));
        String message = (String) out.get("P_RET_MSG");
        byte[] report  = JdbcUtils.readClob(out, reportKey);
        String token   = tokenKey != null ? (String) out.get(tokenKey) : null;
        return new CreditReportResponse(code, message, report, token);
    }
}
