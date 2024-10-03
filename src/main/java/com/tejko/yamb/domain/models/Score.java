package com.tejko.yamb.domain.models;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

@Entity(name="score")
@Table(name = "score", indexes = {
    @Index(name = "idx_score_external_id", columnList = "external_id")
})
public class Score {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "external_id", nullable = false, updatable = false, unique = true)
    private UUID externalId;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false, updatable = false)
    private Player player;

    @Column(name = "value", nullable = false, updatable = false)
    private int value;

	protected Score() {}

	protected Score(Player player, int value) {
		this.player = player;
		this.value = value;
	}

	public static Score getInstance(Player player, int value) {
		return new Score(player, value);
	}

	public Long getId() {
		return id;
	}

	public UUID getExternalId() {
		return externalId;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public int getValue() {
		return value;
	}

	@PrePersist
    private void ensureExternalId() {
        if (this.externalId == null) {
            this.externalId = UUID.randomUUID();
        }
    }

}