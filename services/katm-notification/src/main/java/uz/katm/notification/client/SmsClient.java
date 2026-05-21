package uz.katm.notification.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import uz.katm.notification.domain.record.PlayMobileContent;
import uz.katm.notification.domain.record.PlayMobileMessage;
import uz.katm.notification.domain.record.PlayMobileRequest;
import uz.katm.notification.domain.record.PlayMobileSms;
import uz.katm.notification.exception.SmsException;

import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
public class SmsClient {

    private final RestClient restClient;
    private final String sender;

    public SmsClient(
            @Value("${katm.sms.provider-url}") String providerUrl,
            @Value("${katm.sms.username}")     String username,
            @Value("${katm.sms.api-key}")      String apiKey,
            @Value("${katm.sms.sender}")       String sender
    ) {
        String credentials = Base64.getEncoder()
                .encodeToString((username + ":" + apiKey).getBytes());

        this.sender = sender;
        this.restClient = RestClient.builder()
                .baseUrl(providerUrl)
                .defaultHeader("Authorization", "Basic " + credentials)
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public String sendSms(String recipient, String text) {
        String messageId = UUID.randomUUID().toString().replace("-", "").substring(0, 20);
        return sendSms(recipient, text, messageId);
    }

    public String sendSms(String recipient, String text, String messageId) {
        PlayMobileRequest request = buildRequest(List.of(new Message(recipient, messageId, 8, text)));
        log.debug("Sending SMS to recipient={}, messageId={}", recipient, messageId);
        return doPost("/broker-api/send", request);
    }

    public String sendBulkSms(List<String> recipients, String text) {
        List<Message> messages = recipients.stream()
                .map(r -> new Message(r, UUID.randomUUID().toString().replace("-", "").substring(0, 20), null, text))
                .toList();
        PlayMobileRequest request = buildRequest(messages);
        log.debug("Sending bulk SMS to {} recipients", recipients.size());
        return doPost("/broker-api/sendMas", request);
    }

    private PlayMobileRequest buildRequest(List<Message> messages) {
        return new PlayMobileRequest(
                messages.stream()
                        .map(m -> new PlayMobileMessage(
                                m.recipient(),
                                m.messageId(),
                                m.priority(),
                                new PlayMobileSms(sender, new PlayMobileContent(m.text()))
                        ))
                        .toList()
        );
    }

    private String doPost(String path, PlayMobileRequest request) {
        try {
            return restClient.post()
                    .uri(path)
                    .body(request)
                    .retrieve()
                    .body(String.class);
        } catch (Exception e) {
            log.error("PlayMobile SMS error [{}]: {}", path, e.getMessage(), e);
            throw new SmsException("Ошибка отправки SMS: " + e.getMessage(), e);
        }
    }

    private record Message(String recipient, String messageId, Integer priority, String text) {}
}
