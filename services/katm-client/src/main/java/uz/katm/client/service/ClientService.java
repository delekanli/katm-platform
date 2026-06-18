package uz.katm.client.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.katm.client.domain.record.AddCreditBanRequest;
import uz.katm.client.domain.record.BanStatusResponse;
import uz.katm.client.domain.record.ClientUserInfo;
import uz.katm.client.domain.record.CreditBanHistoryItem;
import uz.katm.client.domain.record.CreditBanInfo;
import uz.katm.client.domain.record.DeactivateCreditBanRequest;
import uz.katm.client.domain.record.PassportDataRequest;
import uz.katm.client.domain.record.ProcedureResult;
import uz.katm.client.exception.ClientServiceException;
import uz.katm.client.repository.ClientRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    public ProcedureResult savePassportData(PassportDataRequest request) {
        ProcedureResult result = clientRepository.savePassportData(request);
        assertSuccess(result.code(), result.message());
        return result;
    }

    public ProcedureResult updatePassword(String clientId, String newPassword) {
        ProcedureResult result = clientRepository.updatePassword(clientId, newPassword);
        assertSuccess(result.code(), result.message());
        return result;
    }

    public BanStatusResponse checkCreditBanStatus(
            String head, String code,
            String subjectId, String identifier, String clientIp, String additionalInfo) {
        BanStatusResponse response = clientRepository.checkCreditBanStatus(
                head, code, subjectId, identifier, clientIp, additionalInfo);
        assertSuccess(response.code(), response.message());
        return response;
    }

    public ProcedureResult addCreditBan(String head, String code, String clientIp, AddCreditBanRequest request) {
        ProcedureResult result = clientRepository.addCreditBan(head, code, clientIp, request);
        assertSuccess(result.code(), result.message());
        return result;
    }

    public ProcedureResult deactivateCreditBan(String head, String code, String clientIp, DeactivateCreditBanRequest request) {
        ProcedureResult result = clientRepository.deactivateCreditBan(head, code, clientIp, request);
        assertSuccess(result.code(), result.message());
        return result;
    }

    public List<CreditBanHistoryItem> getCreditBanHistory(String identifier, String subjectId) {
        return clientRepository.getCreditBanHistory(identifier, subjectId);
    }

    public CreditBanInfo getCreditBanInfoByHash(String hash) {
        return clientRepository.getCreditBanInfoByHash(hash);
    }

    public ClientUserInfo getClientUserByLogin(String login) {
        // Учётка "p.vladimir" намеренно скрыта из поиска (правило перенесено из монолита).
        if ("p.vladimir".equalsIgnoreCase(login)) {
            return null;
        }
        return clientRepository.getClientUserByLogin(login);
    }

    private void assertSuccess(String code, String message) {
        if (!"0".equals(code)) {
            throw new ClientServiceException(code, message);
        }
    }
}
