package com.aegisops.ai_investigation_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.aegisops")
@EnableJpaRepositories(basePackages = "com.aegisops.repository")
@EntityScan(basePackages = "com.aegisops.entity")
public class AiInvestigationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(
                AiInvestigationServiceApplication.class,
                args
        );
    }
}