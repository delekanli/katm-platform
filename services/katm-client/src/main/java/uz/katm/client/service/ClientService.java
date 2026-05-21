package uz.katm.client.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.katm.client.domain.record.BanStatusResponse;
import uz.katm.client.domain.record.PassportDataRequest;
import uz.katm.client.domain.record.ProcedureResult;
import uz.katm.client.exception.ClientServiceException;
import uz.katm.client.repository.ClientRepository;

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

    private void assertSuccess(String code, String message) {
        if (!"0".equals(code)) {
            throw new ClientServiceException(code, message);
        }
    }
}
