package com.aegisops.service;

import com.aegisops.dto.GeminiInvestigationResponse;
import com.aegisops.dto.IncidentDetails;
import com.aegisops.entity.Investigation;
import com.aegisops.repository.InvestigationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class InvestigationService {

    private final InvestigationRepository investigationRepository;
    private final IncidentClient incidentClient;
    private final GeminiService geminiService;
    private final KnowledgeSearchService knowledgeSearchService;

    public InvestigationService(
            InvestigationRepository investigationRepository,
            IncidentClient incidentClient,
            GeminiService geminiService,
            KnowledgeSearchService knowledgeSearchService) {

        this.investigationRepository = investigationRepository;
        this.incidentClient = incidentClient;
        this.geminiService = geminiService;
        this.knowledgeSearchService = knowledgeSearchService;
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

        List<Map<String, Object>> knowledgeDocuments =
                knowledgeSearchService.searchSimilar(
                        buildKnowledgeQuery(incident),
                        3
                );

        String knowledgeContext =
                buildKnowledgeContext(knowledgeDocuments);

        String prompt =
                buildInvestigationPrompt(incident, knowledgeContext);

        String geminiResponse =
                geminiService.generateInvestigation(prompt);

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

    private String buildKnowledgeQuery(IncidentDetails incident) {

        return """
                Security investigation for incident type %s with severity %s.
                The incident involves user %s from IP address %s.
                Evidence: %s
                """.formatted(
                incident.getIncidentType(),
                incident.getSeverity(),
                incident.getUsername(),
                incident.getIpAddress(),
                incident.getEvidence()
        );
    }

    private String buildKnowledgeContext(
            List<Map<String, Object>> knowledgeDocuments) {

        if (knowledgeDocuments.isEmpty()) {
            return "No relevant security knowledge was retrieved.";
        }

        return knowledgeDocuments.stream()
                .map(document ->
                        """
                        Knowledge Title: %s
                        Knowledge Type: %s
                        Source: %s
                        Content: %s
                        Similarity: %s
                        """.formatted(
                                document.get("title"),
                                document.get("document_type"),
                                document.get("source"),
                                document.get("content"),
                                document.get("similarity")
                        )
                )
                .collect(Collectors.joining("\n"));
    }

    private String buildInvestigationPrompt(
            IncidentDetails incident,
            String knowledgeContext) {

        return """
                You are investigating a security incident in AegisOps.

                Use only the incident information and retrieved security
                knowledge provided below.

                Do not invent facts.
                Treat retrieved knowledge as guidance, not as evidence that
                an event actually occurred.
                Findings about the incident must be based on the incident data.
                The recommended action must be a security recommendation only.
                Do not claim that any action was executed.

                INCIDENT INFORMATION

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

                RETRIEVED SECURITY KNOWLEDGE

                %s
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
                incident.getResolution(),
                knowledgeContext
        );
    }

    public List<Investigation> getByIncidentId(Long incidentId) {
        return investigationRepository.findByIncidentId(incidentId);
    }

    public List<Investigation> getAll() {
        return investigationRepository.findAll();
    }
}