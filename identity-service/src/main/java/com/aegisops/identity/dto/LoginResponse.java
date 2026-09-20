package com.aegisops.identity.dto;

import com.aegisops.identity.entity.User;

public class LoginResponse {

    private String token;
    private Long userId;
    private String username;
    private String role;

    public LoginResponse(String token, User user) {
        this.token = token;
        this.userId = user.getId();
        this.username = user.getUsername();
        this.role = user.getRole().name();
    }

    public String getToken() {
        return token;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }
}