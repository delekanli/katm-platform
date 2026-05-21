package uz.katm.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import uz.katm.notification.client.SmsClient;
import uz.katm.notification.domain.record.NotifyToSend;
import uz.katm.notification.repository.SmsOutboxRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SmsOutboxService {

    private final SmsOutboxRepository repository;
    private final SmsClient smsClient;
    private final RetryTemplate retryTemplate;

    public void processOutbox() {
        List<NotifyToSend> pending = repository.getPending();
        if (pending.isEmpty()) {
            log.debug("SMS outbox: nothing to send");
            return;
        }
        log.info("SMS outbox: processing {} messages", pending.size());
        for (NotifyToSend item : pending) {
            send(item);
        }
    }

    private void send(NotifyToSend item) {
        repository.setInProgress(item.id());
        String messageId = buildMessageId(item);
        try {
            retryTemplate.execute(ctx -> {
                log.info("SMS attempt {}: id={}, recipient={}", ctx.getRetryCount() + 1, item.id(), item.recipient());
                smsClient.sendSms(item.recipient(), item.text(), messageId);
                return null;
            });
            repository.setSent(item.id());
            log.info("SMS sent: id={}", item.id());
        } catch (Exception e) {
            repository.setFailed(item.id());
            log.error("SMS failed after retries: id={}, error={}", item.id(), e.getMessage());
        }
    }

    private String buildMessageId(NotifyToSend item) {
        String prefix = item.methodSender() != null ? item.methodSender() : "";
        int padLen = Math.max(1, 20 - prefix.length());
        return (prefix + String.format("%0" + padLen + "d", item.id())).substring(0, 20);
    }
}
