package com.duy.aicommerce.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class AiCommerceBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiCommerceBackendApplication.class, args);
    }

}
