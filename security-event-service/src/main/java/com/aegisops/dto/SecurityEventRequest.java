package com.aegisops.dto;

import com.aegisops.entity.SecurityEvent.EventType;
import jakarta.validation.constraints.NotNull;

public class SecurityEventRequest {

    @NotNull
    private EventType eventType;

    private String username;

    private String ipAddress;

    private String details;

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}