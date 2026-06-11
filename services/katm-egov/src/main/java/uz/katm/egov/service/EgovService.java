package uz.katm.egov.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.katm.egov.domain.dto.IndividualReportRequest;
import uz.katm.egov.domain.dto.LegalReportRequest;
import uz.katm.egov.domain.record.CreditReportResult;
import uz.katm.egov.domain.record.OperationResult;
import uz.katm.egov.repository.EgovReportRepository;

/** Канал кредитных отчётов E-GOV: отчёт по физлицу/юрлицу, обновление статуса заявки. */
@Slf4j
@Service
@RequiredArgsConstructor
public class EgovService {

    private final EgovReportRepository repository;

    public CreditReportResult getIndividualReport(IndividualReportRequest req) {
        log.info("E-GOV кредитный отчёт (физлицо): claimId={}", req.claimId());
        return repository.getIndividualReport(req);
    }

    public CreditReportResult getLegalReport(LegalReportRequest req) {
        log.info("E-GOV кредитный отчёт (юрлицо): inn={}", req.inn());
        return repository.getLegalReport(req);
    }

    public OperationResult updateClaimStatus(String claimId, String status, String node) {
        log.info("E-GOV обновление статуса заявки: claimId={}, status={}, node={}", claimId, status, node);
        return repository.updateClaimStatus(claimId, status, node);
    }
}
