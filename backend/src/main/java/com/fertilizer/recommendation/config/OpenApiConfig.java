package com.fertilizer.recommendation.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("🌱 Fertilizer Recommendation System REST API")
                        .version("1.0.0")
                        .description("Comprehensive REST API for crop nutrient analysis, soil diagnostic evaluation, and agronomic fertilizer advisory.")
                        .contact(new Contact().name("Fertilizer Recommendation Team").email("support@fertilizer-system.org")));
    }
}
