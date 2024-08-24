package com.tejko.yamb.api.payload.responses;

import java.time.LocalDateTime;
import java.util.List;

public class PlayerResponse {

    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String name;
    private List<RoleResponse> roles;
    private boolean tempUser;

   
    public PlayerResponse() {
    }

    public PlayerResponse(Long id, LocalDateTime createdAt, LocalDateTime updatedAt, String name, List<RoleResponse> roles, boolean tempUser) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.name = name;
        this.roles = roles;
        this.tempUser = tempUser;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<RoleResponse> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleResponse> roles) {
        this.roles = roles;
    }

    public boolean isTempUser() {
        return tempUser;
    }

    public void setTempUser(boolean tempUser) {
        this.tempUser = tempUser;
    }

}