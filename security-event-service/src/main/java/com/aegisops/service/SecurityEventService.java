package com.aegisops.service;

import com.aegisops.dto.SecurityEventRequest;
import com.aegisops.entity.SecurityEvent;
import com.aegisops.repository.SecurityEventRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SecurityEventService {

    private final SecurityEventRepository securityEventRepository;

    public SecurityEventService(SecurityEventRepository securityEventRepository) {
        this.securityEventRepository = securityEventRepository;
    }

    public SecurityEvent createEvent(SecurityEventRequest request) {

        SecurityEvent event = new SecurityEvent();

        event.setEventType(request.getEventType());
        event.setUsername(request.getUsername());
        event.setIpAddress(request.getIpAddress());
        event.setTimestamp(LocalDateTime.now());
        event.setDetails(request.getDetails());

        return securityEventRepository.save(event);
    }
}