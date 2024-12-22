package com.tejko.yamb.api.dto.responses;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import com.tejko.yamb.domain.enums.ClashStatus;
import com.tejko.yamb.domain.enums.ClashType;
import com.tejko.yamb.domain.enums.InvitationStatus;

@Relation(collectionRelation = "clashes")
public class ClashDetailResponse extends RepresentationModel<ClashDetailResponse> {
 
    private UUID id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String name;
    private PlayerResponse owner;
    private int turn;
    private PlayerResponse winner;
    private List<ClashPlayer> players;
    private ClashStatus status;
    private ClashType type;
    
    public ClashDetailResponse() {}

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PlayerResponse getOwner() {
        return owner;
    }

    public void setOwner(PlayerResponse owner) {
        this.owner = owner;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public PlayerResponse getWinner() {
        return winner;
    }

    public void setWinner(PlayerResponse winner) {
        this.winner = winner;
    }

    public List<ClashPlayer> getPlayers() {
        return players;
    }

    public void setPlayers(List<ClashPlayer> players) {
        this.players = players;
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

    public static class ClashPlayer extends RepresentationModel<ClashPlayer> {
 
        private UUID id;
        private String name;
        private UUID gameId;
        private InvitationStatus status;
        
        public ClashPlayer() {}

        public UUID getId() {
            return id;
        }

        public void setId(UUID id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public UUID getGameId() {
            return gameId;
        }

        public void setGameId(UUID gameId) {
            this.gameId = gameId;
        }

        public InvitationStatus getStatus() {
            return status;
        }

        public void setStatus(InvitationStatus status) {
            this.status = status;
        }

    }
    
}


