package uz.katm.ucin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.katm.ucin.domain.dto.CreditReportRequest;
import uz.katm.ucin.domain.record.UcinReportResult;
import uz.katm.ucin.repository.UcinReportRepository;

/** UCIN credit-report домен (фаза 2): отчёт, статус, по хэшу, готовый отчёт, активная выгрузка. */
@Slf4j
@Service
@RequiredArgsConstructor
public class UcinReportService {

    private final UcinReportRepository repository;

    public UcinReportResult getCreditReport(CreditReportRequest req) {
        log.info("UCIN getCreditReport: clientId={}, claimId={}", req.clientId(), req.claimId());
        return repository.getCreditReport(req);
    }

    public UcinReportResult getCreditReportStatus(String claimId, String token) {
        log.info("UCIN getCreditReportStatus: claimId={}", claimId);
        return repository.getCreditReportStatus(claimId, token);
    }

    public String getCreditReportByHash(String token) {
        log.info("UCIN getCreditReportByHash");
        return repository.getCreditReportByHash(token);
    }

    public UcinReportResult getReport(String clientId) {
        log.info("UCIN getReport: clientId={}", clientId);
        return repository.getReport(clientId);
    }

    public int checkActiveDemandWork() {
        return repository.checkActiveDemandWork();
    }
}
