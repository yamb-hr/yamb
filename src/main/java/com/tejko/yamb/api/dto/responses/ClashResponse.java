package com.tejko.yamb.api.dto.responses;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import com.tejko.yamb.domain.enums.ClashStatus;
import com.tejko.yamb.domain.enums.ClashType;
import com.tejko.yamb.domain.enums.InvitationStatus;

@Relation(collectionRelation = "clashes")
public class ClashResponse extends RepresentationModel<ClashResponse> {
 
    private UUID id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private PlayerResponse owner;
    private PlayerResponse currentPlayer;
    private PlayerResponse winner;
    private List<PlayerResponse> players;
    private Map<UUID, InvitationStatus> invitations;
    private ClashStatus status;
    private ClashType type;

    public ClashResponse() {}

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
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

    public PlayerResponse getOwner() {
        return owner;
    }

    public void setOwner(PlayerResponse owner) {
        this.owner = owner;
    }

    public PlayerResponse getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(PlayerResponse currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public PlayerResponse getWinner() {
        return winner;
    }

    public void setWinner(PlayerResponse winner) {
        this.winner = winner;
    }

    public List<PlayerResponse> getPlayers() {
        return players;
    }

    public void setPlayers(List<PlayerResponse> players) {
        this.players = players;
    }

    public Map<UUID, InvitationStatus> getInvitations() {
        return invitations;
    }

    public void setInvitations(Map<UUID, InvitationStatus> invitations) {
        this.invitations = invitations;
    }

    public ClashStatus getStatus() {
        return status;
    }

    public void setStatus(ClashStatus status) {
        this.status = status;
    }

    public ClashType getType() {
        return type;
    }

    public void setType(ClashType type) {
        this.type = type;
    }
    
    
    
}


