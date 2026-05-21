package uz.katm.report.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.katm.report.domain.record.CreditReportRequest;
import uz.katm.report.domain.record.CreditReportResponse;
import uz.katm.report.domain.record.FicoResult;
import uz.katm.report.exception.CreditServiceException;
import uz.katm.report.repository.CreditRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreditService {

    private final CreditRepository creditRepository;

    public CreditReportResponse getCreditReport(String head, String code, CreditReportRequest request) {
        log.info("Запрос кредитного отчёта: head={}, code={}, claimId={}, isLegal={}",
                head, code, request.claimId(), request.isLegal());
        CreditReportResponse response = creditRepository.getCreditReport(head, code, request);
        assertOkOrPending(response);
        return response;
    }

    public CreditReportResponse getCreditReportStatus(String head, String code, String claimId, String token) {
        log.info("Запрос статуса кредитного отчёта: head={}, code={}, claimId={}, token={}",
                head, code, claimId, token);
        CreditReportResponse response = creditRepository.getCreditReportStatus(head, code, claimId, token);
        assertOkOrPending(response);
        return response;
    }

    public CreditReportResponse getInfoscoreReport(String pin) {
        log.info("Запрос Infoscore по ПИНФЛ: pin={}", pin);
        CreditReportResponse response = creditRepository.getInfoscoreReport(pin);
        assertOkOrPending(response);
        return response;
    }

    public CreditReportResponse getInfoscoreLegalReport(String tin) {
        log.info("Запрос Infoscore (юрлицо) по ИНН: tin={}", tin);
        CreditReportResponse response = creditRepository.getInfoscoreLegalReport(tin);
        assertOkOrPending(response);
        return response;
    }

    public List<FicoResult> getFicoScore(String clientId) {
        log.info("Запрос FICO-скора: clientId={}", clientId);
        return creditRepository.getFicoScore(clientId);
    }

    private void assertOkOrPending(CreditReportResponse response) {
        if (!response.isSuccess() && !response.isPending()) {
            log.warn("Процедура вернула код ошибки: code={}, message={}", response.code(), response.message());
            throw new CreditServiceException(response.code(), response.message());
        }
    }
}
