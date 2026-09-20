package com.aegisops.service;

import com.aegisops.dto.ThreatDetectedMessage;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ThreatKafkaProducer {

    private static final String TOPIC = "threats-detected";

    private final KafkaTemplate<Object, Object> kafkaTemplate;

    public ThreatKafkaProducer(
            KafkaTemplate<Object, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(ThreatDetectedMessage message) {
        kafkaTemplate.send(TOPIC, message);
    }
}