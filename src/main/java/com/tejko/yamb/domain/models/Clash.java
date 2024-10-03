package com.tejko.yamb.domain.models;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.persistence.Id;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.tejko.yamb.domain.enums.ClashStatus;
import com.tejko.yamb.domain.enums.ClashType;
import com.tejko.yamb.domain.enums.InvitationStatus;

@Document(collection = "clashes")
public class Clash {

    @Id
    private String id;

    @Field(name = "external_id")
    private UUID externalId = UUID.randomUUID();

    @CreatedDate
    @Field("created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Field("updated_at")
    private LocalDateTime updatedAt;

    @Field("player_ids")
    private List<UUID> playerIds;
    
    @Field("invitations")
    private Map<UUID, InvitationStatus> invitations;

    @Field("owner_id")
    private UUID ownerId;

    @Field("current_player_id")
    private UUID currentPlayerId;

    @Field("winner_id")
    private UUID winnerId;

    @Field("type")
    private ClashType type; 

    @Field("status")
    private ClashStatus status;

    protected Clash() {}

    public Clash(UUID ownerId, List<UUID> playerIds, Map<UUID, InvitationStatus> invitations, ClashType type, ClashStatus status) {
        this.ownerId = ownerId;
        this.playerIds = playerIds;
        this.invitations = invitations;
        this.type = type;
        this.status = status;
        this.currentPlayerId = ownerId;
    }

    public static Clash getInstance(UUID ownerId, List<UUID> playerIds, ClashType type) {
        return new Clash(ownerId, playerIds, generateParticipants(ownerId, playerIds), type, ClashStatus.IN_PROGRESS);
    }

    private static Map<UUID, InvitationStatus> generateParticipants(UUID ownerId, List<UUID> playerIds) {
        Map<UUID, InvitationStatus> participants = new HashMap<>();
        for (UUID playerId : playerIds) {
            participants.put(playerId, InvitationStatus.PENDING);
        }
        participants.put(ownerId, InvitationStatus.ACCEPTED);
        return participants;
    }

    public String getId() {
        return id;
    }

    public UUID getExternalId() {
        return externalId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public List<UUID> getPlayerIds() {
        return playerIds;
    }

    public Map<UUID, InvitationStatus> getInvitations() {
        return invitations;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public UUID getCurrentPlayerId() {
        return currentPlayerId;
    }

    public UUID getWinnerId() {
        return winnerId;
    }

    public ClashType getType() {
        return type;
    }

    public ClashStatus getStatus() {
        return status;
    }

    public void acceptInvitation(UUID playerId) {
        validateAccept(playerId);
    }

    public void declineInvitation(UUID playerId) {
        validateDecline(playerId);
    }

    private void validateAccept(UUID playerId) {
        if (!invitations.containsKey(playerId)) {
            throw new IllegalStateException();
        } else if (!InvitationStatus.PENDING.equals(invitations.get(playerId))) {
            throw new IllegalStateException();
        }
    }

    private void validateDecline(UUID playerId) {
        if (!invitations.containsKey(playerId)) {
            throw new IllegalStateException();
        } else if (!InvitationStatus.PENDING.equals(invitations.get(playerId))) {
            throw new IllegalStateException();
        }
    }   

    public void advanceTurn() {
        int currentIndex = playerIds.indexOf(currentPlayerId);
        int nextIndex = (currentIndex + 1) % playerIds.size();
        currentPlayerId = playerIds.get(nextIndex);
    }

    public void replacePlayer(UUID oldPlayerExternalId, UUID newPlayerExternalId) {
        playerIds.set(playerIds.indexOf(oldPlayerExternalId), newPlayerExternalId);
        if (ownerId == oldPlayerExternalId) {
            ownerId = newPlayerExternalId;
        }
        if (currentPlayerId == oldPlayerExternalId) {
            currentPlayerId = newPlayerExternalId;
        }
        if (winnerId == oldPlayerExternalId) {
            winnerId = newPlayerExternalId;
        }
    }

    public void complete() {
        status = ClashStatus.COMPLETED;
    }
    
}
