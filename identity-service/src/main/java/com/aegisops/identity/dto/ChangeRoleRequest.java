package com.aegisops.identity.dto;

import com.aegisops.identity.entity.User;
import jakarta.validation.constraints.NotNull;

public class ChangeRoleRequest {

    @NotNull
    private User.Role role;

    public ChangeRoleRequest() {
    }

    public User.Role getRole() {
        return role;
    }

    public void setRole(User.Role role) {
        this.role = role;
    }
}