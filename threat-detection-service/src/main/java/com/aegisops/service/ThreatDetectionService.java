package com.aegisops.service;

import com.aegisops.dto.ThreatDetectedMessage;
import com.aegisops.entity.Threat;
import com.aegisops.repository.ThreatRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ThreatDetectionService {

    private final ThreatRepository threatRepository;
    private final ThreatKafkaProducer threatKafkaProducer;

    public ThreatDetectionService(
            ThreatRepository threatRepository,
            ThreatKafkaProducer threatKafkaProducer) {
        this.threatRepository = threatRepository;
        this.threatKafkaProducer = threatKafkaProducer;
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

        Threat savedThreat = threatRepository.save(threat);
        publishThreat(savedThreat);

        return savedThreat;
    }

    public Threat savePrivilegeEscalationThreat(
            String username,
            String ipAddress) {

        Threat threat = new Threat();

        threat.setThreatType("PRIVILEGE_ESCALATION");
        threat.setSeverity("HIGH");
        threat.setUsername(username);
        threat.setIpAddress(ipAddress);
        threat.setThreatScore(90);
        threat.setDetectedAt(LocalDateTime.now());
        threat.setDescription(
                "Detected a role change that elevated user "
                        + username
                        + " to SECURITY_ADMIN."
        );
        threat.setStatus("OPEN");

        Threat savedThreat = threatRepository.save(threat);
        publishThreat(savedThreat);

        return savedThreat;
    }

    public Threat saveApiAbuseThreat(
            String username,
            String ipAddress,
            int requestCount) {

        Threat threat = new Threat();

        threat.setThreatType("API_ABUSE");
        threat.setSeverity("MEDIUM");
        threat.setUsername(username);
        threat.setIpAddress(ipAddress);
        threat.setThreatScore(70);
        threat.setDetectedAt(LocalDateTime.now());
        threat.setDescription(
                "Detected "
                        + requestCount
                        + " API requests from user "
                        + username
                        + " within the configured detection window."
        );
        threat.setStatus("OPEN");

        Threat savedThreat = threatRepository.save(threat);
        publishThreat(savedThreat);

        return savedThreat;
    }

    public Threat saveSuspiciousActivityThreat(
            String username,
            String ipAddress,
            int unauthorizedAccessCount) {

        Threat threat = new Threat();

        threat.setThreatType("SUSPICIOUS_ACTIVITY");
        threat.setSeverity("MEDIUM");
        threat.setUsername(username);
        threat.setIpAddress(ipAddress);
        threat.setThreatScore(60);
        threat.setDetectedAt(LocalDateTime.now());
        threat.setDescription(
                "Detected "
                        + unauthorizedAccessCount
                        + " unauthorized access attempts for user "
                        + username
                        + " within the configured detection window."
        );
        threat.setStatus("OPEN");

        Threat savedThreat = threatRepository.save(threat);
        publishThreat(savedThreat);

        return savedThreat;
    }

    private void publishThreat(Threat threat) {

        ThreatDetectedMessage message =
                new ThreatDetectedMessage();

        message.setThreatId(threat.getId());
        message.setThreatType(threat.getThreatType());
        message.setSeverity(threat.getSeverity());
        message.setUsername(threat.getUsername());
        message.setIpAddress(threat.getIpAddress());
        message.setThreatScore(threat.getThreatScore());
        message.setDetectedAt(threat.getDetectedAt());
        message.setDescription(threat.getDescription());
        message.setStatus(threat.getStatus());

        threatKafkaProducer.publish(message);

        System.out.println(
                "Published THREAT_DETECTED event for threat "
                        + threat.getId()
                        + " of type "
                        + threat.getThreatType()
        );
    }
}