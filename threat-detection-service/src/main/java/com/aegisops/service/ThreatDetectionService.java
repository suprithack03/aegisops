package com.aegisops.service;

import com.aegisops.entity.Threat;
import com.aegisops.repository.ThreatRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ThreatDetectionService {

    private final ThreatRepository threatRepository;

    public ThreatDetectionService(ThreatRepository threatRepository) {
        this.threatRepository = threatRepository;
    }

    public Threat saveBruteForceThreat(
            String username,
            String ipAddress,
            int failedLoginCount) {

        Threat threat = new Threat();

        threat.setThreatType("BRUTE_FORCE");
        threat.setSeverity("HIGH");
        threat.setUsername(username);
        threat.setIpAddress(ipAddress);
        threat.setThreatScore(80);
        threat.setDetectedAt(LocalDateTime.now());
        threat.setDescription(
                "Detected " + failedLoginCount
                        + " failed login attempts for user "
                        + username
                        + " within the configured detection window."
        );
        threat.setStatus("OPEN");

        return threatRepository.save(threat);
    }
}