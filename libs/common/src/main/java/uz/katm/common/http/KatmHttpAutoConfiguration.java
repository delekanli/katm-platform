package uz.katm.common.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import uz.katm.common.security.ServiceTokenProvider;

@AutoConfiguration
public class KatmHttpAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(IHttpService.class)
    IHttpService httpService(@Value("${katm.http.ssl-verify:true}") boolean sslVerify) {
        return new HttpServiceImpl(sslVerify);
    }

    @Bean
    @ConditionalOnMissingBean(ServiceTokenProvider.class)
    ServiceTokenProvider serviceTokenProvider(
            IHttpService httpService,
            ObjectMapper objectMapper,
            @Value("${katm.service-auth.token-uri:}") String tokenUri,
            @Value("${katm.service-auth.client-id:}") String clientId,
            @Value("${katm.service-auth.client-secret:}") String clientSecret) {
        return new ServiceTokenProvider(httpService, objectMapper, tokenUri, clientId, clientSecret);
    }
}
