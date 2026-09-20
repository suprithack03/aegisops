package com.aegisops.identity.dto;

import com.aegisops.identity.entity.User;

import java.time.LocalDateTime;

public class RegisterResponse {

    private Long id;
    private String username;
    private String email;
    private User.Role role;
    private LocalDateTime createdAt;

    public RegisterResponse(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.role = user.getRole();
        this.createdAt = user.getCreatedAt();
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public User.Role getRole() {
        return role;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}