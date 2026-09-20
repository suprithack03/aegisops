package com.aegisops.identity.controller;

import com.aegisops.identity.dto.ChangeRoleRequest;
import com.aegisops.identity.dto.LoginRequest;
import com.aegisops.identity.dto.LoginResponse;
import com.aegisops.identity.dto.RegisterRequest;
import com.aegisops.identity.dto.RegisterResponse;
import com.aegisops.identity.entity.User;
import com.aegisops.identity.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(
            @Valid @RequestBody RegisterRequest request) {

        User user = authService.register(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new RegisterResponse(user));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request) {

        LoginResponse response = authService.login(request);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/users/{userId}/role")
    public ResponseEntity<RegisterResponse> changeRole(
            @PathVariable Long userId,
            @Valid @RequestBody ChangeRoleRequest request) {

        User user = authService.changeRole(userId, request);

        return ResponseEntity.ok(new RegisterResponse(user));
    }
}