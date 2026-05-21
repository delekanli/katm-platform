package uz.katm.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.katm.notification.client.SmsClient;
import uz.katm.notification.domain.dto.SendEmailRequest;
import uz.katm.notification.domain.dto.SendSmsRequest;
import uz.katm.notification.domain.record.OtpResponse;
import uz.katm.notification.domain.record.SubscriptionStatus;
import uz.katm.notification.repository.NotificationRepository;

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
