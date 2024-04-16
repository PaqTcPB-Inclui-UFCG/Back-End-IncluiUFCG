package com.ufcg.adptare.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customApi() {
        return new OpenAPI()
                .info(new io.swagger.v3.oas.models.info.Info()
                        .title("Adaptare")
                        .version("1.0.0")
                        .license(new io.swagger.v3.oas.models.info.License().name("Apache License 2.0").url("https://www.apache.org/licenses/LICENSE-2.0")));
    }
}
