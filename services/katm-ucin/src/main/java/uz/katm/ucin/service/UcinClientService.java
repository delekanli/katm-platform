package uz.katm.ucin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.katm.ucin.domain.dto.InitiateClientRequest;
import uz.katm.ucin.domain.dto.InitiateLegalClientRequest;
import uz.katm.ucin.domain.dto.KatmSirRequest;
import uz.katm.ucin.domain.record.ClientDataResult;
import uz.katm.ucin.domain.record.ClientResult;
import uz.katm.ucin.domain.record.KatmSirResult;
import uz.katm.ucin.repository.UcinClientRepository;

/** UCIN client-домен (фаза 1): инициализация клиентов, данные клиента, тариф, поиск в КАТМ СИР. */
@Slf4j
@Service
@RequiredArgsConstructor
public class UcinClientService {

    private final UcinClientRepository repository;

    public ClientResult initClient(InitiateClientRequest req) {
        log.info("UCIN initClient: pinfl={}", req.pinfl());
        return repository.initClient(req);
    }

    public ClientResult initLegalClient(InitiateLegalClientRequest req) {
        log.info("UCIN initLegalClient: inn={}", req.inn());
        return repository.initLegalClient(req);
    }

    public ClientDataResult getClientById(String clientId, String reportId) {
        log.info("UCIN getClientById: clientId={}, reportId={}", clientId, reportId);
        return repository.getClientById(clientId, reportId);
    }

    public Double getClientTariff(String clientId, Integer reportId) {
        log.info("UCIN getClientTariff: clientId={}, reportId={}", clientId, reportId);
        return repository.getClientTariff(clientId, reportId);
    }

    public KatmSirResult getKatmSir(KatmSirRequest req) {
        log.info("UCIN getKatmSir: pinfl={}", req.pinfl());
        return repository.getKatmSir(req);
    }
}
