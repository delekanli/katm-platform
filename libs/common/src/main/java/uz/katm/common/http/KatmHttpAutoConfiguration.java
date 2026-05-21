package uz.katm.common.http;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class KatmHttpAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(IHttpService.class)
    IHttpService httpService(@Value("${katm.http.ssl-verify:true}") boolean sslVerify) {
        return new HttpServiceImpl(sslVerify);
    }
}
