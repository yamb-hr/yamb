package com.tejko.yamb.domain.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.persistence.Id;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
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
    @Indexed
    private UUID externalId = UUID.randomUUID();

    @CreatedDate
    @Field("created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Field("updated_at")
    private LocalDateTime updatedAt;
    
    @Field("players")
    private List<ClashPlayer> players;

    @Indexed
    @Field("player_hash")
    private String playerHash;
    
    @Field("name")
    private String name;

    @Field("owner_id")
    private UUID ownerId;

    @Field("turn")
    private int turn;

    @Field("winner_id")
    private UUID winnerId;

    @Field("type")
    private ClashType type; 

    @Field("status")
    private ClashStatus status;

    protected Clash() {}

    public Clash(String name, UUID ownerId, int turn, List<ClashPlayer> players, String playerHash, ClashType type, ClashStatus status) {
        this.name = name;
        this.ownerId = ownerId;
        this.turn = turn;
        this.players = players;
        this.playerHash = playerHash;
        this.type = type;
        this.status = status;
    }

    public static Clash getInstance(String name, UUID ownerId, Set<UUID> playerIds, ClashType type) {
        List<ClashPlayer> players = generatePlayers(ownerId, playerIds);
        String playerHash = generatePlayerHash(playerIds);
        return new Clash(name, ownerId, 0, players, playerHash, type, ClashStatus.PENDING);
    }

    private static List<ClashPlayer> generatePlayers(UUID ownerId, Set<UUID> playerIds) {
        List<ClashPlayer> players = new ArrayList<>();
        for (UUID playerId : playerIds) {
            players.add(ClashPlayer.getInstance(playerId, (ownerId.equals(playerId) ? InvitationStatus.ACCEPTED : InvitationStatus.PENDING)));
        }
        return players;
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

    public List<ClashPlayer> getPlayers() {
        return players;
    }

    public String getPlayerHash() {
        return playerHash;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public UUID getWinnerId() {
        return winnerId;
    }

    public void setWinnerId(UUID winnerId) {
        this.winnerId = winnerId;
    }

    public ClashType getType() {
        return type;
    }

    public ClashStatus getStatus() {
        return status;
    }

    public void setStatus(ClashStatus status) {
        this.status = status;
    }

    public int getTurn() {
        return turn;
    }

    public boolean allInvitationsAnswered() {
        return players.stream().allMatch(player -> InvitationStatus.ACCEPTED.equals(player.getStatus()) || InvitationStatus.DECLINED.equals(player.getStatus()));
    }

    public List<ClashPlayer> getAcceptedPlayers() {
        return players.stream().filter(player -> InvitationStatus.ACCEPTED.equals(player.getStatus())).collect(Collectors.toList());
    }

    public void addPlayers(Set<UUID> playerIds) {
        for (UUID playerId : playerIds) {
            addPlayer(playerId);
        }
    }

    public void removePlayers(Set<UUID> playerIds) {
        for (UUID playerId : playerIds) {
            removePlayer(playerId);
        }
    }

    public void replacePlayer(UUID oldPlayerId, UUID newPlayerId) {
        if (ownerId.equals(oldPlayerId)) {
            ownerId = newPlayerId;
        }
        if (winnerId.equals(oldPlayerId)) {
            ownerId = newPlayerId;
        }
        for (ClashPlayer player : players) {
            if (player.getId().equals(oldPlayerId)) {
                player.setId(newPlayerId);
            }
        }
        updatePlayerHash();
    }

    private void addPlayer(UUID playerId) {
        validateAddPlayer(playerId);
        players.add(new ClashPlayer(playerId, InvitationStatus.PENDING));
        updatePlayerHash();
    }

    private void validateAddPlayer(UUID playerId) {
        if (status != ClashStatus.PENDING) {
            throw new IllegalStateException("Cannot add players after the clash has started.");
        }
        if (players.stream().anyMatch(player -> player.getId().equals(playerId))) {
            throw new IllegalArgumentException("Player already part of the clash.");
        }
    }

    private void removePlayer(UUID playerId) {
        validateRemovePlayer();
        players.removeIf(player -> player.getId().equals(playerId));
        updatePlayerHash();
    }

    private void validateRemovePlayer() {
        if (status != ClashStatus.PENDING) {
            throw new IllegalStateException("Cannot remove players after the clash has started.");
        } else if (players.size() == 2) {
            throw new IllegalStateException("Cannot have a clash with just one player");
        }
    }

    public void acceptInvitation(UUID playerId, UUID gameId) {
        ClashPlayer player = getPlayer(playerId);
        if (player.getStatus() != InvitationStatus.PENDING) {
            throw new IllegalStateException("Player invitation is not pending.");
        }
        player.setStatus(InvitationStatus.ACCEPTED);
        player.setGameId(gameId);
    }

    public boolean checkStartConditions() {
        return allInvitationsAnswered() && getAcceptedPlayers().size() >= 2;
    }

    public void startClash() {
        status = ClashStatus.IN_PROGRESS;
        players = getAcceptedPlayers(); 
        updatePlayerHash();
    }

    public void declineInvitation(UUID playerId) {
        ClashPlayer player = getPlayer(playerId);
        if (player.getStatus() != InvitationStatus.PENDING) {
            throw new IllegalStateException("Player invitation is not pending.");
        }
        player.setStatus(InvitationStatus.DECLINED);
    }

    public ClashPlayer getPlayer(UUID playerId) {
        return players.stream()
            .filter(player -> player.getId().equals(playerId))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Player not part of this clash."));
    }

    public void advanceTurn() {
        turn = ++turn % players.size();
    }
    
    public static String generatePlayerHash(Set<UUID> playerIds) {
        List<UUID> sortedPlayers = new ArrayList<>(playerIds);
        Collections.sort(sortedPlayers);
        return String.join("-", sortedPlayers.stream().map(UUID::toString).toArray(String[]::new));
    }

    private void updatePlayerHash() {
        playerHash = generatePlayerHash(players.stream()
            .map(ClashPlayer::getId)
            .collect(Collectors.toSet()));
    }

    public static class ClashPlayer {

        private UUID id;
        private UUID gameId;
        private InvitationStatus status;
        private Integer score;

        protected ClashPlayer() {}

        protected ClashPlayer(UUID id, InvitationStatus status) {
            this.id = id;
            this.status = status;
        }

        public static ClashPlayer getInstance(UUID id, InvitationStatus status) {
            return new ClashPlayer(id, status);
        }

        public UUID getId() {
            return id;
        }

        public void setId(UUID id) {
            this.id = id;
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

        public Integer getScore() {
            return score;
        }

        public void setScore(Integer score) {
            this.score = score;
        }

    }
    
}
