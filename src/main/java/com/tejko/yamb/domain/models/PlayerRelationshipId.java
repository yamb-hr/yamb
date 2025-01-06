package com.tejko.yamb.domain.models;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class PlayerRelationshipId implements Serializable {

    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;

    @ManyToOne
    @JoinColumn(name = "related_player_id")
    private Player relatedPlayer;

    public PlayerRelationshipId() {}

    public PlayerRelationshipId(Player player, Player relatedPlayer) {
        this.player = player;
        this.relatedPlayer = relatedPlayer;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getRelatedPlayer() {
        return relatedPlayer;
    }

    public void setRelatedPlayer(Player relatedPlayer) {
        this.relatedPlayer = relatedPlayer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerRelationshipId that = (PlayerRelationshipId) o;
        return Objects.equals(player, that.player) &&
               Objects.equals(relatedPlayer, that.relatedPlayer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(player, relatedPlayer);
    }
    
}
