package uz.katm.report.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import uz.katm.report.domain.record.FicoResult;
import uz.katm.report.exception.CreditServiceException;
import uz.katm.report.repository.CreditRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScoringService {

    private final CreditRepository creditRepository;

    public List<FicoResult> getFicoScore(String clientId) {
        if (!StringUtils.hasText(clientId)) {
            throw new CreditServiceException("-1", "Идентификатор клиента не может быть пустым");
        }
        log.info("Запрос FICO-скоринга: clientId={}", clientId);
        List<FicoResult> results = creditRepository.getFicoScore(clientId);
        log.debug("Получено {} записей FICO-скоринга для клиента {}", results.size(), clientId);
        return results;
    }
}
