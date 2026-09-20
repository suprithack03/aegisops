package com.aegisops.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "incidents")
public class Incident {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "threat_id", nullable = false)
    private Long threatId;

    @Column(name = "incident_type", nullable = false, length = 100)
    private String incidentType;

    @Column(nullable = false, length = 20)
    private String severity;

    @Column(nullable = false, length = 30)
    private String status;

    @Column(length = 100)
    private String username;

    @Column(name = "ip_address", length = 100)
    private String ipAddress;

    @Column(name = "detected_at", nullable = false)
    private LocalDateTime detectedAt;

    @Column(name = "threat_score", nullable = false)
    private Integer threatScore;

    @Column(name = "related_events", columnDefinition = "TEXT")
    private String relatedEvents;

    @Column(columnDefinition = "TEXT")
    private String evidence;

    @Column(name = "ai_investigation", columnDefinition = "TEXT")
    private String aiInvestigation;

    @Column(name = "recommended_action", columnDefinition = "TEXT")
    private String recommendedAction;

    @Column(name = "approval_status", length = 30)
    private String approvalStatus;

    @Column(columnDefinition = "TEXT")
    private String resolution;

    public Long getId() {
        return id;
    }

    public Long getThreatId() {
        return threatId;
    }

    public void setThreatId(Long threatId) {
        this.threatId = threatId;
    }

    public String getIncidentType() {
        return incidentType;
    }

    public void setIncidentType(String incidentType) {
        this.incidentType = incidentType;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public LocalDateTime getDetectedAt() {
        return detectedAt;
    }

    public void setDetectedAt(LocalDateTime detectedAt) {
        this.detectedAt = detectedAt;
    }

    public Integer getThreatScore() {
        return threatScore;
    }

    public void setThreatScore(Integer threatScore) {
        this.threatScore = threatScore;
    }

    public String getRelatedEvents() {
        return relatedEvents;
    }

    public void setRelatedEvents(String relatedEvents) {
        this.relatedEvents = relatedEvents;
    }

    public String getEvidence() {
        return evidence;
    }

    public void setEvidence(String evidence) {
        this.evidence = evidence;
    }

    public String getAiInvestigation() {
        return aiInvestigation;
    }

    public void setAiInvestigation(String aiInvestigation) {
        this.aiInvestigation = aiInvestigation;
    }

    public String getRecommendedAction() {
        return recommendedAction;
    }

    public void setRecommendedAction(String recommendedAction) {
        this.recommendedAction = recommendedAction;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }
}