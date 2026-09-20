package com.aegisops.service;

import com.aegisops.entity.Investigation;
import com.aegisops.repository.InvestigationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class InvestigationService {

    private final InvestigationRepository investigationRepository;

    public InvestigationService(
            InvestigationRepository investigationRepository) {
        this.investigationRepository = investigationRepository;
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

    public List<Investigation> getByIncidentId(Long incidentId) {
        return investigationRepository.findByIncidentId(incidentId);
    }

    public List<Investigation> getAll() {
        return investigationRepository.findAll();
    }
}