package com.tejko.yamb.domain.models;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Index;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.tejko.yamb.domain.enums.RelationshipType;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "player_relationship", indexes = {
    @Index(name = "idx_player_relationship_external_id", columnList = "external_id")
})
public class PlayerRelationship {

    @EmbeddedId
    private PlayerRelationshipId id;

    @Column(name = "external_id", nullable = false, updatable = false, unique = true)
    private UUID externalId;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private RelationshipType type;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "active")
    private boolean active;

    protected PlayerRelationship() {}

    protected PlayerRelationship(PlayerRelationshipId id, RelationshipType type, boolean active) {
        this.id = id;
        this.type = type;
        this.active = active;
    }

    public static PlayerRelationship getInstance(Player player, Player relatedPlayer, RelationshipType type) {
        return new PlayerRelationship(new PlayerRelationshipId(player, relatedPlayer), type, false);
    }

    public PlayerRelationshipId getId() {
        return id;
    }

    public UUID getExternalId() {
        return externalId;
    }

    public RelationshipType getType() {
        return type;
    }

    public void setType(RelationshipType type) {
        this.type = type;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @PrePersist
    private void ensureExternalId() {
        if (this.externalId == null) {
            this.externalId = UUID.randomUUID();
        }
    }

}
