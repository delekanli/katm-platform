package uz.katm.common.kafka;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public abstract class KafkaEvent {
    private String eventId = UUID.randomUUID().toString();
    private String eventType;
    private Instant occurredAt = Instant.now();
    private String correlationId;
    private String sourceService;
    private String traceId;
}
