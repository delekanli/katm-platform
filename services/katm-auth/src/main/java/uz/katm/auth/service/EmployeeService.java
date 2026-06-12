package uz.katm.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.katm.auth.domain.record.EmployeeLoginResult;
import uz.katm.auth.domain.record.OperationResult;
import uz.katm.auth.domain.record.ReportAccess;
import uz.katm.auth.repository.AuthRepository;

import java.util.List;

/** Авторизация сотрудников (операторов) против БД PKG_USERS + доступ к отчётам. */
@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final AuthRepository authRepository;

    public EmployeeLoginResult login(String login, String password) {
        log.info("Авторизация сотрудника: login={}", login);
        return authRepository.employeeLogin(login, password);
    }

    public OperationResult logout(Integer userId, String sessionId) {
        log.info("Завершение сессии сотрудника: userId={}", userId);
        return authRepository.employeeLogout(userId, sessionId);
    }

    public List<ReportAccess> getReportAccess(Integer userId) {
        return authRepository.getReportAccess(userId);
    }
}
