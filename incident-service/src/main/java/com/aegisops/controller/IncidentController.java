package com.aegisops.controller;

import com.aegisops.dto.IncidentStatusUpdateRequest;
import com.aegisops.dto.SecurityActionRequest;
import com.aegisops.entity.Incident;
import com.aegisops.entity.SecurityAction;
import com.aegisops.repository.IncidentRepository;
import com.aegisops.repository.SecurityActionRepository;
import com.aegisops.service.IncidentService;
import com.aegisops.service.SecurityActionService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/incidents")
public class IncidentController {

    private final IncidentRepository incidentRepository;
    private final SecurityActionRepository securityActionRepository;
    private final IncidentService incidentService;
    private final SecurityActionService securityActionService;

    public IncidentController(
            IncidentRepository incidentRepository,
            SecurityActionRepository securityActionRepository,
            IncidentService incidentService,
            SecurityActionService securityActionService) {

        this.incidentRepository = incidentRepository;
        this.securityActionRepository = securityActionRepository;
        this.incidentService = incidentService;
        this.securityActionService = securityActionService;
    }

    @GetMapping
    public List<Incident> getAllIncidents() {
        return incidentRepository.findAll();
    }

    @GetMapping("/active")
    public List<Incident> getActiveIncidents() {
        return incidentRepository.findByStatus("OPEN");
    }

    @GetMapping("/{id}")
    public Incident getIncidentById(
            @PathVariable Long id) {

        return incidentRepository.findById(id)
                .orElseThrow(
                        () -> new RuntimeException(
                                "Incident not found: " + id
                        )
                );
    }

    @GetMapping("/{id}/actions")
    public List<SecurityAction> getIncidentActions(
            @PathVariable Long id) {

        incidentRepository.findById(id)
                .orElseThrow(
                        () -> new RuntimeException(
                                "Incident not found: " + id
                        )
                );

        return securityActionRepository.findByIncidentId(id);
    }

    @PutMapping("/{id}/status")
    public Incident updateIncidentStatus(
            @PathVariable Long id,
            @RequestBody IncidentStatusUpdateRequest request) {

        return incidentService.updateStatus(
                id,
                request.getStatus()
        );
    }

    @PostMapping("/{id}/approve")
    @PreAuthorize("hasAuthority('ROLE_SECURITY_ADMIN')")
    public Incident approveIncident(
            @PathVariable Long id) {

        return incidentService.approveIncident(id);
    }

    @PostMapping("/{id}/reject")
    @PreAuthorize("hasAuthority('ROLE_SECURITY_ADMIN')")
    public Incident rejectIncident(
            @PathVariable Long id) {

        return incidentService.rejectIncident(id);
    }

    @PostMapping("/{id}/execute-action")
    @PreAuthorize("hasAuthority('ROLE_SECURITY_ADMIN')")
    public SecurityAction executeSecurityAction(
            @PathVariable Long id,
            @Valid @RequestBody SecurityActionRequest request,
            Authentication authentication) {

        return securityActionService.executeAction(
                id,
                request.getActionType(),
                authentication.getName()
        );
    }
}