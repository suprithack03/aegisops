package com.aegisops.identity.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/api/test/protected")
    public String protectedEndpoint(Authentication authentication) {

        return "Authenticated as: " + authentication.getName();
    }

    @GetMapping("/api/test/admin")
    public String adminEndpoint(Authentication authentication) {

        return "Security admin access granted to: "
                + authentication.getName();
    }
}