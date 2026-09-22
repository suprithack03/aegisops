package com.aegisops.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "security_actions")
public class SecurityAction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "incident_id", nullable = false)
    private Long incidentId;

    @Column(name = "action_type", nullable = false, length = 100)
    private String actionType;

    @Column(name = "target_username", length = 100)
    private String targetUsername;

    @Column(nullable = false, length = 30)
    private String status;

    @Column(name = "executed_at")
    private LocalDateTime executedAt;

    @Column(columnDefinition = "TEXT")
    private String details;

    public Long getId() {
        return id;
    }

    public Long getIncidentId() {
        return incidentId;
    }

    public void setIncidentId(Long incidentId) {
        this.incidentId = incidentId;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getTargetUsername() {
        return targetUsername;
    }

    public void setTargetUsername(String targetUsername) {
        this.targetUsername = targetUsername;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getExecutedAt() {
        return executedAt;
    }

    public void setExecutedAt(LocalDateTime executedAt) {
        this.executedAt = executedAt;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}