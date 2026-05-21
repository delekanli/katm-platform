package uz.katm.notification.domain.record;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public record PlayMobileMessage(
        String recipient,
        @JsonProperty("message-id") String messageId,
        @JsonInclude(JsonInclude.Include.NON_NULL) Integer priority,
        PlayMobileSms sms
) {}
