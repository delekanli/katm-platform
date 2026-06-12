package uz.katm.mip.ws;

import jakarta.xml.ws.BindingProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Единая настройка JAX-WS портов МИП: переопределение endpoint-адреса (если задан в конфиге)
 * и таймауты соединения/чтения. Заменяет дублировавшийся приватный {@code configurePort}
 * во всех *MipService.
 */
@Component
public class MipPortConfigurer {

    @Value("${katm.mip.connect-timeout:10000}")
    private int connectTimeout;

    @Value("${katm.mip.read-timeout:30000}")
    private int readTimeout;

    public void configure(Object port, String endpointUrl) {
        BindingProvider provider = (BindingProvider) port;
        Map<String, Object> ctx = provider.getRequestContext();

        if (endpointUrl != null && !endpointUrl.isBlank()) {
            ctx.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpointUrl);
        }
        ctx.put("com.sun.xml.ws.connect.timeout", connectTimeout);
        ctx.put("com.sun.xml.ws.request.timeout", readTimeout);
        ctx.put("com.sun.xml.internal.ws.connect.timeout", connectTimeout);
        ctx.put("com.sun.xml.internal.ws.request.timeout", readTimeout);
    }
}
