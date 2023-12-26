package com.tejko.yamb.models.payload.dto;

public class RoleDTO {

    private Long id;
    private String name;
    private String description;

    public RoleDTO(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
    
}
