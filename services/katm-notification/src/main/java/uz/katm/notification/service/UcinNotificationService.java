package uz.katm.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.katm.notification.client.UcinNotificationClient;
import uz.katm.notification.domain.dto.CreditNotificationRequest;
import uz.katm.notification.domain.dto.FreezeNotificationRequest;
import uz.katm.notification.domain.record.UcinNotificationResult;

/**
 * Ретрансляция уведомлений субъектам через UCIN NOTIFICATION SERVICE.
 * Перенос InternalServiceImpl.sendFreezeNotification / sendCreditNotification.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UcinNotificationService {

    private final UcinNotificationClient ucinNotificationClient;

    public UcinNotificationResult sendFreezeNotification(FreezeNotificationRequest request) {
        log.info("Отправка уведомления FREEZE по ПИНФЛ {}", request.pinfl());
        return ucinNotificationClient.send(request, request.pinfl());
    }

    public UcinNotificationResult sendCreditNotification(CreditNotificationRequest request) {
        log.info("Отправка уведомления об оформлении кредита по ПИНФЛ {}", request.pinfl());
        return ucinNotificationClient.send(request, request.pinfl());
    }
}
