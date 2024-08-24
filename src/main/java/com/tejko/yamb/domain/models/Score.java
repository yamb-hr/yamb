package com.tejko.yamb.domain.models;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.CreationTimestamp;

@Entity
public class Score {

	@Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

	@ManyToOne
	@JoinColumn(name = "player_id", nullable = false)
	private Player player;

	@Column(name = "value", nullable = false)
	private int value;

	protected Score() {}

	private Score(Player player, int value) {
		this.player = player;
		this.value = value;
	}

	public static Score getInstance(Player player, int value) {
		return new Score(player, value);
	}

	public Long getId() {
		return id;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public Player getPlayer() {
		return player;
	}

	public int getValue() {
		return value;
	}

}