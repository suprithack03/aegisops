package com.aegisops.service;

import com.aegisops.entity.AuditRecord;
import com.aegisops.entity.Incident;
import com.aegisops.entity.SecurityAction;
import com.aegisops.repository.AuditRecordRepository;
import com.aegisops.repository.IncidentRepository;
import com.aegisops.repository.SecurityActionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SecurityActionService {

    private static final String APPROVAL_APPROVED = "APPROVED";
    private static final String ACTION_SUCCESS = "SUCCESS";
    private static final String ACTION_FAILED = "FAILED";

    private final IncidentRepository incidentRepository;
    private final SecurityActionRepository securityActionRepository;
    private final AuditRecordRepository auditRecordRepository;

    public SecurityActionService(
            IncidentRepository incidentRepository,
            SecurityActionRepository securityActionRepository,
            AuditRecordRepository auditRecordRepository) {

        this.incidentRepository = incidentRepository;
        this.securityActionRepository = securityActionRepository;
        this.auditRecordRepository = auditRecordRepository;
    }

    public SecurityAction executeAction(
            Long incidentId,
            String actionType,
            String actorUsername) {

        Incident incident = incidentRepository.findById(incidentId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Incident not found: " + incidentId
                        )
                );

        if (!APPROVAL_APPROVED.equals(
                incident.getApprovalStatus())) {

            throw new IllegalStateException(
                    "Incident must be approved before a security action can be executed."
            );
        }

        SecurityAction action = new SecurityAction();

        action.setIncidentId(incidentId);
        action.setActionType(actionType);
        action.setTargetUsername(incident.getUsername());
        action.setStatus(ACTION_SUCCESS);
        action.setExecutedAt(LocalDateTime.now());
        action.setDetails(
                "Security action executed after approved incident review."
        );

        SecurityAction savedAction =
                securityActionRepository.save(action);

        AuditRecord auditRecord = new AuditRecord();

        auditRecord.setIncidentId(incidentId);
        auditRecord.setActionId(savedAction.getId());
        auditRecord.setActorUsername(actorUsername);
        auditRecord.setAction(actionType);
        auditRecord.setResult(ACTION_SUCCESS);
        auditRecord.setTimestamp(LocalDateTime.now());
        auditRecord.setDetails(
                "Security action approved and executed for incident "
                        + incidentId
        );

        auditRecordRepository.save(auditRecord);

        incident.setStatus("MITIGATED");
        incident.setResolution(
                "Security action " + actionType
                        + " executed by " + actorUsername
        );

        incidentRepository.save(incident);

        return savedAction;
    }
}