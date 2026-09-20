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
                && failedLoginCount >= BRUTE_FORCE_THRESHOLD) {

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
}