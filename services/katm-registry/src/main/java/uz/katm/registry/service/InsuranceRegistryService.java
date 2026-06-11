package uz.katm.registry.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.katm.registry.domain.OrgDto;
import uz.katm.registry.domain.dto.RegisterInsuranceRequest;
import uz.katm.registry.domain.record.OperationResult;
import uz.katm.registry.domain.record.PasswordResetResult;
import uz.katm.registry.domain.record.RegistrationResult;
import uz.katm.registry.repository.InsuranceRegistryRepository;
import uz.katm.registry.repository.PasswordResetRepository;

import java.util.List;

/** Управление страховыми организациями: список, регистрация, статус, сброс пароля. */
@Slf4j
@Service
@RequiredArgsConstructor
public class InsuranceRegistryService {

    private final InsuranceRegistryRepository repository;
    private final PasswordResetRepository passwordResetRepository;

    public List<OrgDto> list() {
        return repository.findAll();
    }

    public RegistrationResult register(RegisterInsuranceRequest req) {
        log.info("Регистрация страховой: name={}, region={}", req.name(), req.region());
        return repository.register(req);
    }

    public OperationResult changeStatus(String code, int status, int mode) {
        log.info("Смена статуса страховой: code={}, status={}, mode={}", code, status, mode);
        return repository.changeStatus(code, status, mode);
    }

    public PasswordResetResult resetPassword(String login) {
        log.info("Сброс пароля страховой: login={}", login);
        return passwordResetRepository.resetPassword(login);
    }
}
