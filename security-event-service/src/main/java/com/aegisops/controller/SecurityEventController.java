package com.aegisops.controller;

import com.aegisops.dto.SecurityEventRequest;
import com.aegisops.entity.SecurityEvent;
import com.aegisops.service.SecurityEventService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/security-events")
public class SecurityEventController {

    private final SecurityEventService securityEventService;

    public SecurityEventController(SecurityEventService securityEventService) {
        this.securityEventService = securityEventService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SecurityEvent createEvent(
            @Valid @RequestBody SecurityEventRequest request) {

        return securityEventService.createEvent(request);
    }
}