package uz.katm.registry.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.katm.registry.domain.OrgDto;
import uz.katm.registry.domain.dto.RegisterLeasingRequest;
import uz.katm.registry.domain.record.PasswordResetResult;
import uz.katm.registry.domain.record.RegistrationResult;
import uz.katm.registry.repository.LeasingRegistryRepository;
import uz.katm.registry.repository.PasswordResetRepository;

import java.util.List;

/** Управление лизинговыми организациями: список, регистрация, сброс пароля (без смены статуса — см. монолит). */
@Slf4j
@Service
@RequiredArgsConstructor
public class LeasingRegistryService {

    private final LeasingRegistryRepository repository;
    private final PasswordResetRepository passwordResetRepository;

    public List<OrgDto> list() {
        return repository.findAll();
    }

    public RegistrationResult register(RegisterLeasingRequest req) {
        log.info("Регистрация лизинга: name={}, tin={}", req.name(), req.tin());
        return repository.register(req);
    }

    public PasswordResetResult resetPassword(String login) {
        log.info("Сброс пароля лизинга: login={}", login);
        return passwordResetRepository.resetPassword(login);
    }
}
