package com.aegisops.service;

import com.aegisops.entity.SecurityEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class SecurityEventKafkaProducer {

    private static final String TOPIC = "security-events";

    private final KafkaTemplate<Object, Object> kafkaTemplate;

    public SecurityEventKafkaProducer(KafkaTemplate<Object, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(SecurityEvent event) {
        kafkaTemplate.send(TOPIC, event);
    }
}

