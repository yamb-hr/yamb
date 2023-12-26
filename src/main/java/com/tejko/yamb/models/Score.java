package com.tejko.yamb.models;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;

@Entity
public class Score extends DatabaseEntity {

	@ManyToOne
	@JsonIncludeProperties({ "id", "username" })
	@JoinColumn(name = "player_id", nullable = false)
	private Player player;

	@Column(nullable = false)
	private int value;

	@Column(nullable = false)
	private LocalDateTime date;

	protected Score() {}

	private Score(Player player, int value, LocalDateTime date) {
		this.player = player;
		this.value = value;
		this.date = date;
	}

	public static Score getInstance(Player player, int value) {
		return new Score(player, value, LocalDateTime.now());
	}

	public Player getPlayer() {
		return player;
	}

	public int getValue() {
		return value;
	}
	
	public LocalDateTime getDate() {
		return date;
	}

}