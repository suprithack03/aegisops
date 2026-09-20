package com.aegisops.consumer;

import com.aegisops.dto.ThreatDetectedMessage;
import com.aegisops.service.IncidentService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import tools.jackson.databind.json.JsonMapper;

@Service
public class ThreatDetectedConsumer {

    private final JsonMapper jsonMapper;
    private final IncidentService incidentService;

    public ThreatDetectedConsumer(
            JsonMapper jsonMapper,
            IncidentService incidentService) {
        this.jsonMapper = jsonMapper;
        this.incidentService = incidentService;
    }

    @KafkaListener(
            topics = "threats-detected",
            groupId = "incident-service-group"
    )
    public void consume(String message) {

        try {
            ThreatDetectedMessage threat =
                    jsonMapper.readValue(
                            message,
                            ThreatDetectedMessage.class
                    );

            System.out.println(
                    "Received THREAT_DETECTED event for threat "
                            + threat.getThreatId()
                            + " of type "
                            + threat.getThreatType()
            );

            incidentService.createIncident(threat);

        } catch (Exception e) {
            System.err.println(
                    "Failed to process THREAT_DETECTED event: "
                            + e.getMessage()
            );
        }
    }
}