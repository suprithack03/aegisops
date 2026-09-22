package com.aegisops.dto;

import jakarta.validation.constraints.NotBlank;

public class SecurityActionRequest {

    @NotBlank
    private String actionType;

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }
}