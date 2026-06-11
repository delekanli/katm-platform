package uz.katm.registry.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.katm.registry.domain.OrgDto;
import uz.katm.registry.domain.dto.RegisterRetailerRequest;
import uz.katm.registry.domain.record.OperationResult;
import uz.katm.registry.domain.record.PasswordResetResult;
import uz.katm.registry.domain.record.RegistrationResult;
import uz.katm.registry.repository.PasswordResetRepository;
import uz.katm.registry.repository.RetailerRegistryRepository;

import java.util.List;

/** Управление ритейлерами: список, регистрация, статус, сброс пароля. */
@Slf4j
@Service
@RequiredArgsConstructor
public class RetailerRegistryService {

    private final RetailerRegistryRepository repository;
    private final PasswordResetRepository passwordResetRepository;

    public List<OrgDto> list() {
        return repository.findAll();
    }

    public RegistrationResult register(RegisterRetailerRequest req) {
        log.info("Регистрация ритейлера: name={}, tin={}", req.name(), req.tin());
        return repository.register(req);
    }

    public OperationResult changeStatus(String code, int status, int mode) {
        log.info("Смена статуса ритейлера: code={}, status={}, mode={}", code, status, mode);
        return repository.changeStatus(code, status, mode);
    }

    public PasswordResetResult resetPassword(String login) {
        log.info("Сброс пароля ритейлера: login={}", login);
        return passwordResetRepository.resetPassword(login);
    }
}
