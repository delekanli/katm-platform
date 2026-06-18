package uz.katm.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.katm.notification.client.SmsClient;
import uz.katm.notification.domain.dto.SendEmailRequest;
import uz.katm.notification.domain.dto.SendSmsRequest;
import uz.katm.notification.domain.record.FreezeStatus;
import uz.katm.notification.domain.record.NotificationHistoryItem;
import uz.katm.notification.domain.record.NotificationType;
import uz.katm.notification.domain.record.OtpResponse;
import uz.katm.notification.domain.record.ProcedureResult;
import uz.katm.notification.domain.record.SubscriptionStatus;
import uz.katm.notification.repository.NotificationRepository;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final SmsClient smsClient;
    private final EmailSenderService emailSenderService;

    public OtpResponse sendOtp(String msisdn) {
        log.debug("Sending OTP to msisdn={}", msisdn);
        return notificationRepository.sendOtp(msisdn);
    }

    /** OTP для подтверждения привязки карты (тип шаблона 10), перенос sendOtpForCard монолита. */
    public OtpResponse sendOtpForCard(String msisdn, int lang) {
        log.debug("Sending add-card OTP to msisdn={}, lang={}", msisdn, lang);
        return notificationRepository.sendSmsOtp(msisdn, 10, lang);
    }

    public FreezeStatus checkSubscriptionStatusDate(String clientId) {
        log.debug("Freeze status with date for clientId={}", clientId);
        return notificationRepository.checkSubscriptionStatusDate(clientId);
    }

    public SubscriptionStatus activateSubscription(String clientId, String msisdn, int lang) {
        log.debug("Activating subscription for clientId={}", clientId);
        return SubscriptionStatus.fromCode(notificationRepository.activateSubscription(clientId, msisdn, lang));
    }

    public SubscriptionStatus suspendSubscription(String clientId) {
        log.debug("Suspending subscription for clientId={}", clientId);
        return SubscriptionStatus.fromCode(notificationRepository.suspendSubscription(clientId));
    }

    public SubscriptionStatus checkSubscriptionStatus(String clientId) {
        return SubscriptionStatus.fromCode(notificationRepository.checkSubscriptionStatus(clientId));
    }

    public SubscriptionStatus reactivateFreezed(String clientId) {
        log.debug("Reactivating freezed subscription for clientId={}", clientId);
        return SubscriptionStatus.fromCode(notificationRepository.reactivateFreezed(clientId));
    }

    public SubscriptionStatus cancelFreezed(String clientId) {
        log.debug("Cancelling freezed subscription for clientId={}", clientId);
        return SubscriptionStatus.fromCode(notificationRepository.cancelFreezed(clientId));
    }

    public ProcedureResult subscriptionChangeLang(String clientId, int lang) {
        log.debug("Changing subscription locale for clientId={}, lang={}", clientId, lang);
        int result = notificationRepository.subscriptionChangeLang(clientId, lang);
        return switch (result) {
            case 0 -> new ProcedureResult("0", "Локаль успешно обновлена");
            case -1 -> new ProcedureResult("00001", "Неверная локаль. Поддерживаемые значения 1(UZ), 2(RU)");
            case -2 -> new ProcedureResult("00002", "Произошла системная ошибка при смене локали");
            case -20 -> new ProcedureResult("00004", "Не передан ID клиента(KATM-SIR)");
            default -> new ProcedureResult(String.valueOf(result), "Неизвестный результат смены локали");
        };
    }

    public ProcedureResult subscriptionChangeMsisdn(String clientId, String msisdn) {
        log.debug("Changing subscription msisdn for clientId={}", clientId);
        int result = notificationRepository.subscriptionChangeMsisdn(clientId, msisdn);
        return switch (result) {
            case 0 -> new ProcedureResult("0", "Номер успешно обновлен");
            case -1 -> new ProcedureResult("00001", "Неверный формат телефона. Требуемый формат 9989XXXXXXXX");
            case -2 -> new ProcedureResult("00002", "Произошла системная ошибка при смене номера");
            case -20 -> new ProcedureResult("00004", "Не передан ID клиента(KATM-SIR)");
            default -> new ProcedureResult(String.valueOf(result), "Неизвестный результат смены номера");
        };
    }

    public ProcedureResult massSend(List<String> clientIds, int notificationType) {
        log.info("Mass notification: {} clients, type={}", clientIds.size(), notificationType);
        int result = notificationRepository.massSend(clientIds, notificationType);
        return switch (result) {
            case 0 -> new ProcedureResult("0", "СМС отправлено успешно");
            case -10 -> new ProcedureResult("00001", "Список клиентов для отправки СМС пустой");
            case -20 -> new ProcedureResult("00002", "Тип СМС шаблона не передан");
            case -30 -> new ProcedureResult("00003", "Произошла ошибка при отправке СМС");
            default -> new ProcedureResult(String.valueOf(result), "Неизвестный результат массовой отправки");
        };
    }

    /** Подписка на уведомления об оформлении кредита — подключение (SMS_NOTIFY_CONTRACT.activate). */
    public ProcedureResult creditSubscriptionActivate(String clientId) {
        log.debug("Activating credit-notification subscription for clientId={}", clientId);
        int result = notificationRepository.creditNotificationActivate(clientId);
        return switch (result) {
            case 0 -> new ProcedureResult("0", "Подписка успешно активирована");
            // -1: код остаётся успешным (как в монолите) — услуга СМС-информирования не подключена.
            case -1 -> new ProcedureResult("0", "Не подключена услуга СМС-информирования");
            case -2 -> new ProcedureResult("00002", "Неверный формат языка");
            case -20 -> new ProcedureResult("00003", "Клиент не найден");
            default -> new ProcedureResult("00004", "Произошла ошибка при подключении подписки");
        };
    }

    /** Подписка на уведомления об оформлении кредита — приостановка (SMS_NOTIFY_CONTRACT.stop). */
    public ProcedureResult creditSubscriptionSuspend(String clientId) {
        log.debug("Suspending credit-notification subscription for clientId={}", clientId);
        int result = notificationRepository.creditNotificationSuspend(clientId);
        return switch (result) {
            case 0 -> new ProcedureResult("0", "Подписка успешно приостановлена");
            case -1 -> new ProcedureResult("0", "Не подключена услуга СМС-информирования");
            default -> new ProcedureResult("00004", "Произошла ошибка при отключении подписки");
        };
    }

    /** Статус кредитной подписки (SMS_NOTIFY_CONTRACT.status): ACTIVE/INACTIVE. */
    public SubscriptionStatus creditSubscriptionStatus(String clientId) {
        return SubscriptionStatus.fromCode(notificationRepository.creditNotificationStatus(clientId));
    }

    public List<NotificationHistoryItem> subscriptionHistory(String clientId, LocalDate dateFrom, LocalDate dateTo) {
        log.debug("Subscription history for clientId={}", clientId);
        return notificationRepository.subscriptionHistory(clientId, dateFrom, dateTo);
    }

    public List<NotificationType> notificationTypes() {
        return notificationRepository.notificationTypes();
    }

    public void onCreditUpdate(String payload) {
        log.info("[CREDIT UPDATE] payload={}", payload);
    }

    public void onReportReady(String payload) {
        log.info("[REPORT READY] payload={}", payload);
    }

    public String sendSms(SendSmsRequest request) {
        log.info("Sending SMS: recipient={}", request.getRecipient());
        return smsClient.sendSms(request.getRecipient(), request.getText());
    }

    public String sendBulkSms(List<String> recipients, String text) {
        log.info("Bulk SMS: {} recipients", recipients.size());
        return smsClient.sendBulkSms(recipients, text);
    }

    public void sendEmail(SendEmailRequest request) {
        log.info("Sending email: to={}", request.getTo());
        if (request.isHtml()) {
            emailSenderService.sendEmail(request.getTo(), request.getSubject(), request.getBody());
        } else {
            emailSenderService.sendPlainEmail(request.getTo(), request.getSubject(), request.getBody());
        }
    }
}
