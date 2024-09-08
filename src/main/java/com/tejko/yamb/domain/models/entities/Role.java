package com.tejko.yamb.domain.models.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "role")
public class Role {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@Column(name = "label", nullable = false, unique = true)
	private String label;

	@Column(name = "description")
    private String description;

    protected Role() {}

    protected Role(String label) {
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