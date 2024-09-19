package com.tejko.yamb.domain.models;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity(name = "role")
@Table(name = "role", indexes = {
    @Index(name = "idx_role_external_id", columnList = "external_id")
})
public class Role {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "external_id", nullable = false, updatable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID externalId;

	@Column(name = "label", nullable = false, unique = true)
	private String label;

	@Column(name = "description")
    private String description;

    protected Role() {}

    protected Role(String label) {
        this.label = label;
    }

    public Long getId() {
        return id;
    }

    public UUID getExternalId() {
        return externalId;
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