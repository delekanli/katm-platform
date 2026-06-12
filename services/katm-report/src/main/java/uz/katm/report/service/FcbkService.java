package uz.katm.report.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.katm.report.client.FcbkClient;
import uz.katm.report.domain.fcbk.AccountsBlock;
import uz.katm.report.domain.fcbk.FicoResultEntity;
import uz.katm.report.domain.fcbk.FicoScoreParams;
import uz.katm.report.domain.fcbk.HeaderBlock;
import uz.katm.report.domain.fcbk.IndividualBlock;
import uz.katm.report.domain.fcbk.ResponseBlock;
import uz.katm.report.repository.FcbkRepository;

import java.util.List;

/**
 * FCBK FICO-скоринг (перенос FcbkServiceImpl.getFicoScore):
 * собирает данные из DWH_FICO, формирует header/individual/accounts и шлёт во внешний FCBK.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FcbkService {

    private final FcbkRepository fcbkRepository;
    private final FcbkClient fcbkClient;

    public ResponseBlock getFicoScore(String clientId) {
        log.info("FCBK FICO score: clientId={}", clientId);
        List<FicoResultEntity> entities = fcbkRepository.getFicoData(clientId);
        if (entities.isEmpty()) {
            return null;
        }
        FicoResultEntity first = entities.get(0);
        HeaderBlock header = new HeaderBlock(first);
        IndividualBlock individual = new IndividualBlock(first);
        List<AccountsBlock> accounts = entities.stream().map(AccountsBlock::new).toList();
        return fcbkClient.requestFicoScore(new FicoScoreParams(header, individual, accounts));
    }
}
