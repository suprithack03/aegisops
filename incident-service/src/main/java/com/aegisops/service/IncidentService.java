package com.aegisops.service;

import com.aegisops.dto.ThreatDetectedMessage;
import com.aegisops.entity.Incident;
import com.aegisops.repository.IncidentRepository;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class IncidentService {

    private static final Set<String> VALID_STATUSES = Set.of(
            "OPEN",
            "INVESTIGATING",
            "AWAITING_APPROVAL",
            "MITIGATED",
            "RESOLVED",
            "FALSE_POSITIVE"
    );

    private static final String APPROVAL_PENDING = "PENDING";
    private static final String APPROVAL_APPROVED = "APPROVED";
    private static final String APPROVAL_REJECTED = "REJECTED";

    private final IncidentRepository incidentRepository;

    public IncidentService(
            IncidentRepository incidentRepository) {

        this.incidentRepository = incidentRepository;
    }

    public Incident createIncident(
            ThreatDetectedMessage threat) {

        Incident incident = new Incident();

        incident.setThreatId(threat.getThreatId());
        incident.setIncidentType(threat.getThreatType());
        incident.setSeverity(threat.getSeverity());
        incident.setStatus("OPEN");
        incident.setUsername(threat.getUsername());
        incident.setIpAddress(threat.getIpAddress());
        incident.setDetectedAt(threat.getDetectedAt());
        incident.setThreatScore(threat.getThreatScore());

        incident.setRelatedEvents(
                "Threat ID: " + threat.getThreatId()
        );

        incident.setEvidence(
                threat.getDescription()
        );

        incident.setApprovalStatus(
                APPROVAL_PENDING
        );

        Incident savedIncident =
                incidentRepository.save(incident);

        System.out.println(
                "Incident created: "
                        + savedIncident.getId()
                        + " for threat "
                        + threat.getThreatId()
        );

        return savedIncident;
    }

    public Incident updateStatus(
            Long incidentId,
            String newStatus) {

        if (newStatus == null ||
                !VALID_STATUSES.contains(newStatus)) {

            throw new IllegalArgumentException(
                    "Invalid incident status: "
                            + newStatus
            );
        }

        Incident incident =
                incidentRepository.findById(incidentId)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Incident not found: "
                                                + incidentId
                                )
                        );

        incident.setStatus(newStatus);

        Incident updatedIncident =
                incidentRepository.save(incident);

        System.out.println(
                "Incident "
                        + incidentId
                        + " status updated to "
                        + newStatus
        );

        return updatedIncident;
    }

    public Incident approveIncident(
            Long incidentId) {

        Incident incident =
                incidentRepository.findById(incidentId)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Incident not found: "
                                                + incidentId
                                )
                        );

        if (!APPROVAL_PENDING.equals(
                incident.getApprovalStatus())) {

            throw new IllegalStateException(
                    "Incident approval is not pending."
            );
        }

        incident.setApprovalStatus(
                APPROVAL_APPROVED
        );

        Incident approvedIncident =
                incidentRepository.save(incident);

        System.out.println(
                "Incident "
                        + incidentId
                        + " approved."
        );

        return approvedIncident;
    }

    public Incident rejectIncident(
            Long incidentId) {

        Incident incident =
                incidentRepository.findById(incidentId)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Incident not found: "
                                                + incidentId
                                )
                        );

        if (!APPROVAL_PENDING.equals(
                incident.getApprovalStatus())) {

            throw new IllegalStateException(
                    "Incident approval is not pending."
            );
        }

        incident.setApprovalStatus(
                APPROVAL_REJECTED
        );

        Incident rejectedIncident =
                incidentRepository.save(incident);

        System.out.println(
                "Incident "
                        + incidentId
                        + " rejected."
        );

        return rejectedIncident;
    }
}