package com.aegisops.consumer;

import com.aegisops.dto.SecurityEventMessage;
import com.aegisops.service.ThreatDetectionService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import tools.jackson.databind.json.JsonMapper;

import java.time.Duration;

@Service
public class SecurityEventConsumer {

    private static final int BRUTE_FORCE_THRESHOLD = 5;
    private static final Duration FAILED_LOGIN_WINDOW = Duration.ofMinutes(10);

    private static final int API_ABUSE_THRESHOLD = 20;
    private static final Duration API_REQUEST_WINDOW = Duration.ofMinutes(1);

    private static final int UNAUTHORIZED_ACCESS_THRESHOLD = 5;
    private static final Duration UNAUTHORIZED_ACCESS_WINDOW =
            Duration.ofMinutes(10);

    private final JsonMapper jsonMapper;
    private final RedisTemplate<String, String> redisTemplate;
    private final ThreatDetectionService threatDetectionService;

    public SecurityEventConsumer(
            JsonMapper jsonMapper,
            RedisTemplate<String, String> redisTemplate,
            ThreatDetectionService threatDetectionService) {
        this.jsonMapper = jsonMapper;
        this.redisTemplate = redisTemplate;
        this.threatDetectionService = threatDetectionService;
    }

    @KafkaListener(
            topics = "security-events",
            groupId = "threat-detection-group"
    )
    public void consume(String message) {

        try {
            SecurityEventMessage event =
                    jsonMapper.readValue(message, SecurityEventMessage.class);

            System.out.println(
                    "Received security event: "
                            + event.getEventType()
                            + " for user "
                            + event.getUsername()
            );

            if ("LOGIN_FAILED".equals(event.getEventType())) {
                handleFailedLogin(event);
            }

            if ("ROLE_CHANGED".equals(event.getEventType())) {
                handleRoleChange(event);
            }

            if ("API_REQUEST".equals(event.getEventType())) {
                handleApiRequest(event);
            }

            if ("UNAUTHORIZED_ACCESS".equals(event.getEventType())) {
                handleUnauthorizedAccess(event);
            }

        } catch (Exception e) {
            System.err.println(
                    "Failed to process security event: "
                            + e.getMessage()
            );
        }
    }

    private void handleFailedLogin(SecurityEventMessage event) {

        String username = event.getUsername();

        if (username == null || username.isBlank()) {
            return;
        }

        String key = "failed-login:" + username;

        Long failedLoginCount =
                redisTemplate.opsForValue().increment(key);

        if (failedLoginCount != null && failedLoginCount == 1) {
            redisTemplate.expire(key, FAILED_LOGIN_WINDOW);
        }

        System.out.println(
                "Failed login count for "
                        + username
                        + ": "
                        + failedLoginCount
        );

        if (failedLoginCount != null
                && failedLoginCount == BRUTE_FORCE_THRESHOLD) {

            System.out.println(
                    "BRUTE FORCE THREAT DETECTED for user "
                            + username
                            + " - failed logins: "
                            + failedLoginCount
            );

            threatDetectionService.saveBruteForceThreat(
                    username,
                    event.getIpAddress(),
                    failedLoginCount.intValue()
            );
        }
    }

    private void handleRoleChange(SecurityEventMessage event) {

        String details = event.getDetails();

        if (details == null) {
            return;
        }

        boolean privilegeEscalation =
                details.contains("ANALYST")
                        && details.contains("SECURITY_ADMIN");

        if (privilegeEscalation) {

            System.out.println(
                    "PRIVILEGE ESCALATION THREAT DETECTED for user "
                            + event.getUsername()
                            + " - role changed from ANALYST to SECURITY_ADMIN"
            );

            threatDetectionService.savePrivilegeEscalationThreat(
                    event.getUsername(),
                    event.getIpAddress()
            );
        }
    }

    private void handleApiRequest(SecurityEventMessage event) {

        String username = event.getUsername();
        String ipAddress = event.getIpAddress();

        if ((username == null || username.isBlank())
                && (ipAddress == null || ipAddress.isBlank())) {
            return;
        }

        String key =
                "api-request:"
                        + (username == null ? "unknown" : username)
                        + ":"
                        + (ipAddress == null ? "unknown" : ipAddress);

        Long apiRequestCount =
                redisTemplate.opsForValue().increment(key);

        if (apiRequestCount != null && apiRequestCount == 1) {
            redisTemplate.expire(key, API_REQUEST_WINDOW);
        }

        System.out.println(
                "API request count for "
                        + username
                        + " from "
                        + ipAddress
                        + ": "
                        + apiRequestCount
        );

        if (apiRequestCount != null
                && apiRequestCount == API_ABUSE_THRESHOLD) {

            System.out.println(
                    "API ABUSE THREAT DETECTED for user "
                            + username
                            + " from IP "
                            + ipAddress
                            + " - requests: "
                            + apiRequestCount
            );

            threatDetectionService.saveApiAbuseThreat(
                    username,
                    ipAddress,
                    apiRequestCount.intValue()
            );
        }
    }

    private void handleUnauthorizedAccess(SecurityEventMessage event) {

        String username = event.getUsername();
        String ipAddress = event.getIpAddress();

        if ((username == null || username.isBlank())
                && (ipAddress == null || ipAddress.isBlank())) {
            return;
        }

        String key =
                "unauthorized-access:"
                        + (username == null ? "unknown" : username)
                        + ":"
                        + (ipAddress == null ? "unknown" : ipAddress);

        Long unauthorizedAccessCount =
                redisTemplate.opsForValue().increment(key);

        if (unauthorizedAccessCount != null
                && unauthorizedAccessCount == 1) {
            redisTemplate.expire(
                    key,
                    UNAUTHORIZED_ACCESS_WINDOW
            );
        }

        System.out.println(
                "Unauthorized access count for "
                        + username
                        + " from "
                        + ipAddress
                        + ": "
                        + unauthorizedAccessCount
        );

        if (unauthorizedAccessCount != null
                && unauthorizedAccessCount
                == UNAUTHORIZED_ACCESS_THRESHOLD) {

            System.out.println(
                    "SUSPICIOUS ACTIVITY THREAT DETECTED for user "
                            + username
                            + " from IP "
                            + ipAddress
                            + " - unauthorized access attempts: "
                            + unauthorizedAccessCount
            );

            threatDetectionService.saveSuspiciousActivityThreat(
                    username,
                    ipAddress,
                    unauthorizedAccessCount.intValue()
            );
        }
    }
}