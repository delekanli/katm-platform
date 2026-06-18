package uz.katm.client.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.katm.client.client.HalkBankInpsClient;
import uz.katm.client.domain.dto.InpsCheckRequest;
import uz.katm.client.domain.dto.InpsData;
import uz.katm.client.domain.dto.InpsGetRequest;
import uz.katm.client.domain.record.ClientClaimData;
import uz.katm.client.domain.record.InpsReportResult;
import uz.katm.client.domain.record.ProcedureResult;
import uz.katm.client.exception.ClientServiceException;
import uz.katm.client.repository.ClientRepository;

import java.util.List;

/**
 * Отчисления ИНПС через Халк банк. Перенос InternalServiceImpl.getInpsDataForScor (скоринг,
 * эндпоинт INPS_SERVICE) и getInpsData (отчёт, эндпоинт INPS_SERVICE_1) вместе с checkInps.
 * Полученные данные сохраняются в БД (ADD_CLIENTS_INCOME); отчётом служит сырой ответ Халк банка.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class InpsService {

    private final ClientRepository clientRepository;
    private final HalkBankInpsClient halkBankClient;
    private final ObjectMapper objectMapper;

    /** Данные ИНПС для скоринга (REPORT_SCORRING). */
    public InpsReportResult getScoringInps(String head, String code, String claimId, String reportId) {
        log.info("Запрос отчислений ИНПС (скоринг): head={}, code={}, claimId={}", head, code, claimId);
        return fetchAndStore(head, code, claimId, reportId, true);
    }

    /** Данные ИНПС для отчёта (REPORT_INPS). */
    public InpsReportResult getInps(String head, String code, String claimId, String reportId) {
        log.info("Запрос отчислений ИНПС: head={}, code={}, claimId={}", head, code, claimId);
        return fetchAndStore(head, code, claimId, reportId, false);
    }

    private InpsReportResult fetchAndStore(String head, String code, String claimId, String reportId, boolean scoring) {
        ClientClaimData client = clientRepository.getClientDataByClaim(head, code, claimId, reportId);
        if (!client.isSuccess()) {
            throw new ClientServiceException(client.code(), client.message());
        }

        InpsGetRequest request = resolveInpsRequest(client);
        String raw = scoring ? halkBankClient.getScorData(request) : halkBankClient.getInpsData(request);

        List<InpsData> list = parse(raw);
        if (list.isEmpty()) {
            throw new ClientServiceException("-1", "Пришёл пустой ответ от сервиса Халк банка");
        }
        InpsData data = list.get(0);
        if (data.getResultCode() == null || data.getResultCode() != 0) {
            throw new ClientServiceException("-1", data.getResultMessage());
        }

        ProcedureResult saved = clientRepository.addClientInpsData(head, code, claimId, data);
        if (!"0".equals(saved.code())) {
            throw new ClientServiceException(saved.code(), saved.message());
        }
        return new InpsReportResult("0", "OK", raw);
    }

    /**
     * Определение параметров запроса в Халк банк (перенос checkInps): проверяем счёт по ПИНФЛ;
     * если найден ровно один валидный счёт — используем его номер ИНПС, иначе паспортные данные субъекта.
     */
    private InpsGetRequest resolveInpsRequest(ClientClaimData client) {
        String checkRaw = halkBankClient.checkInps(new InpsCheckRequest(client.pinfl()));
        List<InpsData> checks = parse(checkRaw);
        if (checks.size() == 1) {
            InpsData only = checks.get(0);
            if (only.getResultCode() != null && only.getResultCode() == 0) {
                return InpsGetRequest.byInps(only.getInps());
            }
            throw new ClientServiceException("-1", only.getResultMessage());
        }
        // Счёт не найден либо дубликаты — паспортные данные берём из заявки.
        return InpsGetRequest.byPassport(client.docSeries(), client.docNumber(), client.pinfl());
    }

    private List<InpsData> parse(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<List<InpsData>>() {
            });
        } catch (Exception e) {
            log.error("Некорректный ответ сервиса Халк банка: {}", e.getMessage());
            throw new ClientServiceException("-1", "Некорректный ответ сервиса Халк банка");
        }
    }
}
