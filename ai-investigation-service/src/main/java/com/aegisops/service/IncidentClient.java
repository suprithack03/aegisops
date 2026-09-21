package com.aegisops.service;

import com.aegisops.dto.IncidentDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class IncidentClient {

    private final RestClient restClient;

    public IncidentClient(
            @Value("${incident.service.url}") String incidentServiceUrl) {

        this.restClient = RestClient.builder()
                .baseUrl(incidentServiceUrl)
                .build();
    }

    public IncidentDetails getIncident(Long incidentId) {

        return restClient.get()
                .uri("/api/incidents/{id}", incidentId)
                .retrieve()
                .body(IncidentDetails.class);
    }
}