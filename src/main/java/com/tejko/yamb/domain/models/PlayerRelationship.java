package com.tejko.yamb.domain.models;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.tejko.yamb.domain.enums.RelationshipType;

import java.time.LocalDateTime;

@Entity
@Table(name = "player_relationship")
public class PlayerRelationship {

    @EmbeddedId
    private PlayerRelationshipId id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private RelationshipType type;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    protected PlayerRelationship() {}

    protected PlayerRelationship(Player firstPlayer, Player secondPlayer, RelationshipType type) {
        this.id = new PlayerRelationshipId(firstPlayer, secondPlayer);
        this.type = type;
    }

    public static PlayerRelationship getInstance(Player firstPlayer, Player secondPlayer, RelationshipType type) {
        return new PlayerRelationship(firstPlayer, secondPlayer, type);
    }

    public PlayerRelationshipId getId() {
        return id;
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

}
