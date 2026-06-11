package uz.katm.common.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Import;
import uz.katm.common.exception.GlobalExceptionHandler;
import uz.katm.common.security.SecurityConfig;

/**
 * Регистрирует общие web-бины ({@link SecurityConfig} и {@link GlobalExceptionHandler})
 * для всех сервлет-сервисов.
 *
 * <p>Сервисы используют {@code @SpringBootApplication} в своём пакете {@code uz.katm.<svc>}
 * и компонент-скан не доходит до {@code uz.katm.common.*}, поэтому без авто-конфигурации
 * {@code @EnableMethodSecurity}/{@code @PreAuthorize}, кастомная цепочка фильтров и
 * глобальный обработчик ошибок не применялись.
 *
 * <p>{@code @AutoConfigureBefore} (по имени, без жёсткой зависимости от классов Spring Boot)
 * гарантирует, что наша {@code SecurityFilterChain} регистрируется до дефолтных цепочек Boot,
 * и те корректно отступают по {@code @ConditionalOnMissingBean(SecurityFilterChain)}.
 * Реактивный gateway не затрагивается — {@link SecurityConfig} условен на сервлет-приложение.
 */
@AutoConfiguration(beforeName = {
        "org.springframework.boot.security.autoconfigure.web.servlet.ServletWebSecurityAutoConfiguration",
        "org.springframework.boot.security.oauth2.server.resource.autoconfigure.servlet.OAuth2ResourceServerAutoConfiguration"
})
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@Import({SecurityConfig.class, GlobalExceptionHandler.class})
public class CommonWebAutoConfiguration {
}
