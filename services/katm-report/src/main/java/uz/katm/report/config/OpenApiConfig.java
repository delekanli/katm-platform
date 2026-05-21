package uz.katm.report.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                .info(new Info()
                        .title("Report Service API")
                        .description("""
                                API сервиса кредитных отчётов KATM Platform.

                                Сервис обеспечивает получение кредитных отчётов, Infoscore-отчётов
                                и FICO-скоринга через Oracle-пакеты DATAS.PKG_ONLINE и DATAS.PKG_FICO.

                                Все запросы требуют Bearer JWT токена.
                                """)
                        .version("2.0.0")
                )
                .addServersItem(new Server().url("/").description("Current server"))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Введите JWT токен, полученный при авторизации")));
    }
}
