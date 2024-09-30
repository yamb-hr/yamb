package com.tejko.yamb.domain.models;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import com.tejko.yamb.domain.enums.FriendRequestStatus;
import com.tejko.yamb.domain.enums.RelationshipType;

import java.time.LocalDateTime;

@Entity
@Table(name = "friend_request")
public class FriendRequest {

    @EmbeddedId
    private PlayerRelationshipId id;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private FriendRequestStatus status;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    protected FriendRequest() {}

    protected FriendRequest(Player player, Player friend, RelationshipType type, FriendRequestStatus status) {
        this.id = new PlayerRelationshipId(player, friend);
        this.status = status;
    }

    public static FriendRequest getInstance(Player player, Player friend, RelationshipType type) {
        return new FriendRequest(player, friend, type, FriendRequestStatus.PENDING);
    }

    public PlayerRelationshipId getId() {
        return id;
    }

    public FriendRequestStatus getStatus() {
        return status;
    }

    public void setStatus(FriendRequestStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

}
