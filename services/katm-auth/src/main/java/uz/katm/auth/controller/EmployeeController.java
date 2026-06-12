package uz.katm.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uz.katm.auth.domain.dto.EmployeeLoginRequest;
import uz.katm.auth.domain.dto.EmployeeLogoutRequest;
import uz.katm.auth.domain.record.EmployeeLoginResult;
import uz.katm.auth.domain.record.OperationResult;
import uz.katm.auth.domain.record.ReportAccess;
import uz.katm.auth.service.EmployeeService;

import java.util.List;

/** Авторизация сотрудников и доступ к отчётам (перенос employee-домена монолита). */
@Tag(name = "Employees", description = "Авторизация сотрудников (PKG_USERS) и доступ к отчётам")
@RestController
@RequestMapping("/api/v1/auth/employees")
@RequiredArgsConstructor
@Validated
public class EmployeeController {

    private final EmployeeService employeeService;

    @Operation(summary = "Авторизация сотрудника (USER_AUTHORIZATION)")
    @PostMapping("/login")
    public ResponseEntity<EmployeeLoginResult> login(@Valid @RequestBody EmployeeLoginRequest request) {
        return ResponseEntity.ok(employeeService.login(request.login(), request.password()));
    }

    @Operation(summary = "Завершение сессии сотрудника (USER_SESSION_LOGOUT)")
    @PostMapping("/logout")
    public ResponseEntity<OperationResult> logout(@Valid @RequestBody EmployeeLogoutRequest request) {
        return ResponseEntity.ok(employeeService.logout(request.userId(), request.sessionId()));
    }

    @Operation(summary = "Доступ сотрудника к отчётам (GET_REPORTS_LIST)")
    @GetMapping("/{userId}/report-access")
    public ResponseEntity<List<ReportAccess>> getReportAccess(@NotNull @PathVariable Integer userId) {
        return ResponseEntity.ok(employeeService.getReportAccess(userId));
    }
}
