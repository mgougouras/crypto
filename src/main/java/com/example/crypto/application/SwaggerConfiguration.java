package com.example.crypto.application;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("crypto-api")
                .pathsToMatch("/**", "/**/**")
                .build();
    }

    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Crypto API")
                        .description("Information about crypto API endpoints"))
                .externalDocs(new ExternalDocumentation()
                        .description("Crypto documentation")
                        .url("http://localhost:8080/swagger-ui.html"));
    }
}
