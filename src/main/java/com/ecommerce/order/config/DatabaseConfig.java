package com.ecommerce.order.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


/**
 * Database configuration for Order Service.
 * Migrates from legacy H2 in-memory to production PostgreSQL.
 */
@Configuration
public class DatabaseConfig {

    @Value("${spring.datasource.url}")
    private String jdbcUrl;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

}
