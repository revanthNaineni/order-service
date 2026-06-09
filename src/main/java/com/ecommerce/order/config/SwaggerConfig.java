package com.ecommerce.order.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger/OpenAPI configuration for Order Service API documentation.
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI orderServiceOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Order Service API")
                .description("Microservice for managing customer orders")
                .version("1.0.0")
                .contact(new Contact()
                    .name("E-commerce Team")
                    .email("support@ecommerce.com")))
            .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
            .components(new io.swagger.v3.oas.models.Components()
                .addSecuritySchemes("Bearer Authentication",
                    new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")));
    }
}
