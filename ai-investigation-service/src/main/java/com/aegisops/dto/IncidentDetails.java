package com.aegisops.dto;

import java.time.LocalDateTime;

public class IncidentDetails {

    private Long id;
    private Long threatId;
    private String incidentType;
    private String severity;
    private String status;
    private String username;
    private String ipAddress;
    private LocalDateTime detectedAt;
    private Integer threatScore;
    private String relatedEvents;
    private String evidence;
    private String aiInvestigation;
    private String recommendedAction;
    private String approvalStatus;
    private String resolution;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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