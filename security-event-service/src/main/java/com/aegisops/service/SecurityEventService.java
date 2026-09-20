package com.aegisops.service;

import com.aegisops.dto.SecurityEventRequest;
import com.aegisops.entity.SecurityEvent;
import com.aegisops.repository.SecurityEventRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SecurityEventService {

    private final SecurityEventRepository securityEventRepository;
    private final SecurityEventKafkaProducer securityEventKafkaProducer;

    public SecurityEventService(
            SecurityEventRepository securityEventRepository,
            SecurityEventKafkaProducer securityEventKafkaProducer) {

        this.securityEventRepository = securityEventRepository;
        this.securityEventKafkaProducer = securityEventKafkaProducer;
    }

    public SecurityEvent createEvent(SecurityEventRequest request) {

        SecurityEvent event = new SecurityEvent();

        event.setEventType(request.getEventType());
        event.setUsername(request.getUsername());
        event.setIpAddress(request.getIpAddress());
        event.setTimestamp(LocalDateTime.now());
        event.setDetails(request.getDetails());

        // 1. Save the security event in PostgreSQL
        SecurityEvent savedEvent = securityEventRepository.save(event);

        // 2. Publish the saved event to Kafka
        securityEventKafkaProducer.publish(savedEvent);

        return savedEvent;
    }
}

