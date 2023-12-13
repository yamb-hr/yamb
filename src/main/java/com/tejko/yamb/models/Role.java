package com.tejko.yamb.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.springframework.data.rest.core.annotation.RestResource;

@Entity
@RestResource(rel = "roles", path = "roles")
public class Role {
    
    @Id
	private Long id;

	@Column
	private String label;

	@Column
    private String description;

    private Role() {}

    private Role(Long id, String label) {
        this.id = id;
        this.label = label;
    }

    public static Role getInstance(Long id, String label) {
        return new Role(id, label);
    }

    public Long getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public String getdescription() {
        return description;
    }
    
}