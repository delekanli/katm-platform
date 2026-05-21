package uz.katm.mip.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import uz.katm.common.kafka.KafkaTopics;
import uz.katm.mip.client.MipHttpClient;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class MipService {

    private final MipHttpClient mipHttpClient;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @KafkaListener(topics = KafkaTopics.MIP_REQUEST, groupId = "katm-mip-group")
    public void handleMipRequest(Map<String, Object> payload) {
        String requestType   = (String) payload.getOrDefault("requestType", "UNKNOWN");
        String correlationId = (String) payload.getOrDefault("correlationId", "");
        log.info("Received MIP request: type={}, correlationId={}", requestType, correlationId);
        try {
            Map<String, Object> response = new HashMap<>(mipHttpClient.sendRequest("/process", payload));
            response.put("correlationId", correlationId);
            kafkaTemplate.send(KafkaTopics.MIP_RESPONSE, correlationId, response);
            log.info("MIP response sent: correlationId={}", correlationId);
        } catch (Exception e) {
            log.error("MIP request failed: correlationId={}, error={}", correlationId, e.getMessage());
            Map<String, Object> errResp = Map.of(
                    "correlationId", correlationId,
                    "error", e.getMessage(),
                    "success", false
            );
            kafkaTemplate.send(KafkaTopics.MIP_RESPONSE, correlationId, errResp);
        }
    }

    public Map<String, Object> sendSyncRequest(String requestType, Map<String, Object> data) {
        log.info("MIP sync request: type={}", requestType);
        return mipHttpClient.sendRequest("/" + requestType.toLowerCase(), data);
    }
}
