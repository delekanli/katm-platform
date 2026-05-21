package uz.katm.auth.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Auth Service API")
                        .description("""
                                API сервиса аутентификации KATM Platform.

                                Сервис обеспечивает управление пользователями, ролями и сессиями.
                                """)
                        .version("1.0.0")
                )
                .addServersItem(new Server().url("/").description("Current server"));
    }
}
