package com.aegisops.controller;

import com.aegisops.dto.IncidentStatusUpdateRequest;
import com.aegisops.entity.Incident;
import com.aegisops.repository.IncidentRepository;
import com.aegisops.service.IncidentService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/incidents")
public class IncidentController {

    private final IncidentRepository incidentRepository;
    private final IncidentService incidentService;

    public IncidentController(
            IncidentRepository incidentRepository,
            IncidentService incidentService) {

        this.incidentRepository = incidentRepository;
        this.incidentService = incidentService;
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
}