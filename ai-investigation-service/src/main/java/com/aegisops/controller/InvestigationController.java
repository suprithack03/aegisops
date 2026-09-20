package com.aegisops.controller;

import com.aegisops.entity.Investigation;
import com.aegisops.service.InvestigationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/investigations")
public class InvestigationController {

    private final InvestigationService investigationService;

    public InvestigationController(
            InvestigationService investigationService) {
        this.investigationService = investigationService;
    }

    @PostMapping
    public Investigation createInvestigation(
            @RequestParam Long incidentId,
            @RequestParam String summary,
            @RequestParam String findings,
            @RequestParam String recommendedAction) {

        return investigationService.createInvestigation(
                incidentId,
                summary,
                findings,
                recommendedAction
        );
    }

    @GetMapping
    public List<Investigation> getAllInvestigations() {
        return investigationService.getAll();
    }

    @GetMapping("/incident/{incidentId}")
    public List<Investigation> getInvestigationsByIncident(
            @PathVariable Long incidentId) {

        return investigationService.getByIncidentId(incidentId);
    }
}