package com.tejko.yamb.models;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Role extends DatabaseEntity {

	@Column
	private String label;

	@Column
    private String description;

    private Role() {}

    private Role(String label) {
        this.label = label;
    }

    public static Role getInstance(String label) {
        return new Role(label);
    }

    public String getLabel() {
        return label;
    }

    public String getDescription() {
        return description;
    }
    
}