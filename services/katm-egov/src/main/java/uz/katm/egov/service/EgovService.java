package uz.katm.egov.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.katm.egov.client.UcinClient;
import uz.katm.egov.domain.dto.IndividualReportRequest;
import uz.katm.egov.domain.dto.LegalReportRequest;
import uz.katm.egov.domain.dto.RegisterClaimRequest;
import uz.katm.egov.domain.record.ClientInitResult;
import uz.katm.egov.domain.record.CreditReportResult;
import uz.katm.egov.domain.record.OperationResult;
import uz.katm.egov.repository.EgovReportRepository;

import java.util.HashMap;
import java.util.Map;

/** Канал E-GOV: кредитные отчёты, статус заявки, регистрация заявки (через UCIN). */
@Slf4j
@Service
@RequiredArgsConstructor
public class EgovService {

    private final EgovReportRepository repository;
    private final UcinClient ucinClient;

    /**
     * Регистрация заявки E-GOV = инициализация клиента в UCIN (перенос eGov registerClaim,
     * который делегировал IUcinClientService.initClient). Вызывает katm-ucin /clients/init.
     */
    public ClientInitResult registerClaim(RegisterClaimRequest r) {
        log.info("E-GOV registerClaim: pinfl={}", r.pinfl());
        Map<String, Object> body = new HashMap<>();
        body.put("pinfl", r.pinfl());
        body.put("identityCardSerial", r.docSeries());
        body.put("identityCardNumber", r.docNumber());
        body.put("identityCardDate", r.issueDocDate());
        body.put("inn", r.inn());
        body.put("region", r.region());
        body.put("localRegion", r.localRegion());
        body.put("familyName", r.lastName());
        body.put("name", r.firstName());
        body.put("patronymic", r.middleName());
        body.put("dateBirth", r.birthDate());
        body.put("phone", r.phone());
        body.put("postAddress", r.address());
        body.put("resident", r.resident());
        body.put("sex", r.sex());
        body.put("ip", r.ip());
        return ucinClient.initClient(body);
    }

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
