package com.aegisops.service;

import com.aegisops.dto.GeminiInvestigationResponse;
import com.aegisops.dto.IncidentDetails;
import com.aegisops.entity.Investigation;
import com.aegisops.repository.InvestigationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class InvestigationService {

    private final InvestigationRepository investigationRepository;
    private final IncidentClient incidentClient;
    private final GeminiService geminiService;

    public InvestigationService(
            InvestigationRepository investigationRepository,
            IncidentClient incidentClient,
            GeminiService geminiService) {

        this.investigationRepository = investigationRepository;
        this.incidentClient = incidentClient;
        this.geminiService = geminiService;
    }

    public Investigation createInvestigation(
            Long incidentId,
            String summary,
            String findings,
            String recommendedAction) {

        Investigation investigation = new Investigation();

        investigation.setIncidentId(incidentId);
        investigation.setSummary(summary);
        investigation.setFindings(findings);
        investigation.setRecommendedAction(recommendedAction);
        investigation.setCreatedAt(LocalDateTime.now());
        investigation.setUpdatedAt(LocalDateTime.now());

        return investigationRepository.save(investigation);
    }

    public Investigation investigateIncident(Long incidentId) {

        IncidentDetails incident = incidentClient.getIncident(incidentId);

        String prompt = buildInvestigationPrompt(incident);

        String geminiResponse = geminiService.generateInvestigation(prompt);

        try {
            ObjectMapper objectMapper = new ObjectMapper();

            GeminiInvestigationResponse aiResponse =
                    objectMapper.readValue(
                            geminiResponse,
                            GeminiInvestigationResponse.class
                    );

            return createInvestigation(
                    incidentId,
                    aiResponse.getSummary(),
                    aiResponse.getFindings(),
                    aiResponse.getRecommendedAction()
            );

        } catch (Exception exception) {
            throw new IllegalStateException(
                    "Failed to parse Gemini investigation response.",
                    exception
            );
        }
    }

    private String buildInvestigationPrompt(IncidentDetails incident) {

        return """
                You are investigating a security incident in AegisOps.

                Use only the incident information provided below.
                Do not invent facts.
                Provide evidence-based findings.
                The recommended action must be a security recommendation only.
                Do not claim that any action was executed.

                Incident ID: %d
                Threat ID: %d
                Incident Type: %s
                Severity: %s
                Status: %s
                Username: %s
                IP Address: %s
                Detected At: %s
                Threat Score: %d
                Related Events: %s
                Evidence: %s
                Existing AI Investigation: %s
                Existing Recommended Action: %s
                Approval Status: %s
                Resolution: %s
                """.formatted(
                incident.getId(),
                incident.getThreatId(),
                incident.getIncidentType(),
                incident.getSeverity(),
                incident.getStatus(),
                incident.getUsername(),
                incident.getIpAddress(),
                incident.getDetectedAt(),
                incident.getThreatScore(),
                incident.getRelatedEvents(),
                incident.getEvidence(),
                incident.getAiInvestigation(),
                incident.getRecommendedAction(),
                incident.getApprovalStatus(),
                incident.getResolution()
        );
    }

    public List<Investigation> getByIncidentId(Long incidentId) {
        return investigationRepository.findByIncidentId(incidentId);
    }

    public List<Investigation> getAll() {
        return investigationRepository.findAll();
    }
}