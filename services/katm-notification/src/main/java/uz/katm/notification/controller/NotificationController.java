package uz.katm.notification.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uz.katm.common.dto.ApiResponse;
import uz.katm.notification.domain.dto.SendEmailRequest;
import uz.katm.notification.domain.dto.SendSmsRequest;
import uz.katm.notification.domain.record.NotificationHistoryItem;
import uz.katm.notification.domain.record.NotificationType;
import uz.katm.notification.domain.record.OtpResponse;
import uz.katm.notification.domain.record.SubscriptionStatus;
import uz.katm.notification.service.NotificationService;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Notifications", description = "OTP, управление подписками, SMS и email рассылка")
@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
@Validated
public class NotificationController {

    private final NotificationService notificationService;

    @Operation(summary = "Отправить OTP на номер телефона")
    @PostMapping("/otp")
    @PreAuthorize("hasAnyRole('CLIENT', 'BANK', 'ADMIN')")
    public ResponseEntity<ApiResponse<OtpResponse>> sendOtp(
            @Pattern(regexp = "\\d{12}", message = "msisdn должен содержать 12 цифр")
            @NotBlank @RequestParam String msisdn) {
        return ResponseEntity.ok(ApiResponse.ok(notificationService.sendOtp(msisdn)));
    }

    @Operation(summary = "Активировать подписку клиента")
    @PostMapping("/subscriptions/{clientId}/activate")
    @PreAuthorize("hasAnyRole('CLIENT', 'ADMIN')")
    public ResponseEntity<ApiResponse<SubscriptionStatus>> activateSubscription(
            @NotBlank @PathVariable String clientId,
            @Pattern(regexp = "\\d{12}", message = "msisdn должен содержать 12 цифр")
            @NotBlank @RequestParam String msisdn,
            @Min(1) @RequestParam(defaultValue = "1") int lang) {
        return ResponseEntity.ok(ApiResponse.ok(notificationService.activateSubscription(clientId, msisdn, lang)));
    }

    @Operation(summary = "Приостановить подписку клиента")
    @PostMapping("/subscriptions/{clientId}/suspend")
    @PreAuthorize("hasAnyRole('CLIENT', 'ADMIN')")
    public ResponseEntity<ApiResponse<SubscriptionStatus>> suspendSubscription(@NotBlank @PathVariable String clientId) {
        return ResponseEntity.ok(ApiResponse.ok(notificationService.suspendSubscription(clientId)));
    }

    @Operation(summary = "Статус подписки клиента")
    @GetMapping("/subscriptions/{clientId}/status")
    @PreAuthorize("hasAnyRole('CLIENT', 'BANK', 'ADMIN')")
    public ResponseEntity<ApiResponse<SubscriptionStatus>> checkSubscriptionStatus(@NotBlank @PathVariable String clientId) {
        return ResponseEntity.ok(ApiResponse.ok(notificationService.checkSubscriptionStatus(clientId)));
    }

    @Operation(summary = "Переактивировать замороженную подписку")
    @PostMapping("/subscriptions/{clientId}/reactivate")
    @PreAuthorize("hasAnyRole('CLIENT', 'ADMIN')")
    public ResponseEntity<ApiResponse<SubscriptionStatus>> reactivateFreezed(@NotBlank @PathVariable String clientId) {
        return ResponseEntity.ok(ApiResponse.ok(notificationService.reactivateFreezed(clientId)));
    }

    @Operation(summary = "Снять заморозку подписки")
    @PostMapping("/subscriptions/{clientId}/cancel-freeze")
    @PreAuthorize("hasAnyRole('CLIENT', 'ADMIN')")
    public ResponseEntity<ApiResponse<SubscriptionStatus>> cancelFreezed(@NotBlank @PathVariable String clientId) {
        return ResponseEntity.ok(ApiResponse.ok(notificationService.cancelFreezed(clientId)));
    }

    @Operation(summary = "История уведомлений клиента за период")
    @GetMapping("/subscriptions/{clientId}/history")
    @PreAuthorize("hasAnyRole('CLIENT', 'ADMIN')")
    public ResponseEntity<ApiResponse<List<NotificationHistoryItem>>> subscriptionHistory(
            @NotBlank @PathVariable String clientId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo) {
        return ResponseEntity.ok(ApiResponse.ok(notificationService.subscriptionHistory(clientId, dateFrom, dateTo)));
    }

    @Operation(summary = "Справочник типов уведомлений")
    @GetMapping("/types")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<NotificationType>>> notificationTypes() {
        return ResponseEntity.ok(ApiResponse.ok(notificationService.notificationTypes()));
    }

    @Operation(summary = "Отправить SMS")
    @PostMapping("/sms/send")
    @PreAuthorize("hasAnyRole('BANK', 'ADMIN')")
    public ResponseEntity<ApiResponse<String>> sendSms(@Valid @RequestBody SendSmsRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(notificationService.sendSms(request)));
    }

    @Operation(summary = "Массовая отправка SMS")
    @PostMapping("/sms/send-bulk")
    @PreAuthorize("hasAnyRole('BANK', 'ADMIN')")
    public ResponseEntity<ApiResponse<String>> sendBulkSms(
            @NotEmpty @RequestParam List<
                    @Pattern(regexp = "\\d{12}", message = "Каждый recipient должен содержать 12 цифр")
                    String> recipients,
            @NotBlank @RequestParam String text) {
        return ResponseEntity.ok(ApiResponse.ok(notificationService.sendBulkSms(recipients, text)));
    }

    @Operation(summary = "Отправить email")
    @PostMapping("/email/send")
    @PreAuthorize("hasAnyRole('BANK', 'ADMIN')")
    public ResponseEntity<ApiResponse<Void>> sendEmail(@Valid @RequestBody SendEmailRequest request) {
        notificationService.sendEmail(request);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }
}
