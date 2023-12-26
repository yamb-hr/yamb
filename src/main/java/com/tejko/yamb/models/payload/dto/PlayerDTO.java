package com.tejko.yamb.models.payload.dto;

import java.util.List;

public class PlayerDTO {

    private Long id;
    private String name;
    private List<String> roles;

    public PlayerDTO(Long id, String name, List<String> roles) {
        this.id = id;
        this.name = name;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<String> getRoles() {
        return roles;
    }
    
}