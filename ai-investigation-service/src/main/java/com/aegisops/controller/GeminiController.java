package com.aegisops.controller;

import com.aegisops.dto.IncidentDetails;
import com.aegisops.entity.Investigation;
import com.aegisops.service.GeminiService;
import com.aegisops.service.IncidentClient;
import com.aegisops.service.InvestigationService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
public class GeminiController {

    private final GeminiService geminiService;
    private final IncidentClient incidentClient;
    private final InvestigationService investigationService;

    public GeminiController(
            GeminiService geminiService,
            IncidentClient incidentClient,
            InvestigationService investigationService) {

        this.geminiService = geminiService;
        this.incidentClient = incidentClient;
        this.investigationService = investigationService;
    }

    @GetMapping("/test")
    public String testGemini(@RequestParam String prompt) {
        return geminiService.generateInvestigation(prompt);
    }

    @GetMapping("/incident/{incidentId}")
    public IncidentDetails getIncident(@PathVariable Long incidentId) {
        return incidentClient.getIncident(incidentId);
    }

    @PostMapping("/incident/{incidentId}/investigate")
    public Investigation investigateIncident(
            @PathVariable Long incidentId) {

        return investigationService.investigateIncident(incidentId);
    }
}