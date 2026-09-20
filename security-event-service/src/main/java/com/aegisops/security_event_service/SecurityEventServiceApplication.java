package com.aegisops.security_event_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.aegisops")
@EnableJpaRepositories(basePackages = "com.aegisops.repository")
@EntityScan(basePackages = "com.aegisops.entity")
public class SecurityEventServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecurityEventServiceApplication.class, args);
    }
}