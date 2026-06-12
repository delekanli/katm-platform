package uz.katm.system.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.katm.system.domain.dto.GatewayRequestLogRequest;
import uz.katm.system.domain.dto.OnlineRequestErrorRequest;
import uz.katm.system.domain.dto.ReportRequestLogRequest;
import uz.katm.system.domain.record.EgovResendItem;
import uz.katm.system.domain.record.ProcedureResult;
import uz.katm.system.repository.SystemRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/** Системные инструменты: логи, ошибки онлайн-запросов, статусы веб-заявок E-GOV. */
@Slf4j
@Service
@RequiredArgsConstructor
public class SystemService {

    private final SystemRepository repository;

    public ProcedureResult addGatewayRequestsLog(GatewayRequestLogRequest req) {
        return repository.addGatewayRequestsLog(req);
    }

    public ProcedureResult addReportRequestLog(ReportRequestLogRequest req) {
        return repository.addReportRequestLog(req);
    }

    public ProcedureResult addOnlineRequestError(OnlineRequestErrorRequest req) {
        return repository.addOnlineRequestError(req);
    }

    public List<Map<String, Object>> getOnlineRequestErrors(LocalDate requestDate, String tin, String pin,
                                                            String docSeries, String docNumber) {
        return repository.getOnlineRequestErrors(requestDate, tin, pin, docSeries, docNumber);
    }

    public int updateWebClaimStatusEgov(String claimId) {
        log.info("Обновление статуса веб-заявки E-GOV: claimId={}", claimId);
        return repository.updateWebClaimStatusEgov(claimId);
    }

    public List<EgovResendItem> getReportsForEgovResend() {
        return repository.getReportsForEgovResend();
    }
}
