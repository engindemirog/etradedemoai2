package com.turkcell.etradedemoai.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Basic OpenAPI configuration for Swagger UI.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("eTradeDemoAI API")
                .version("0.0.1")
                .description("OpenAPI documentation for the eTradeDemoAI project"));
    }
}
