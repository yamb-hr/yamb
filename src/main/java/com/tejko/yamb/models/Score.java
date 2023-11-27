package com.tejko.yamb.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;

@Entity
public class Score {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@ManyToOne
	@JsonIncludeProperties({ "id", "username" })
	@JoinColumn(name = "player_id", nullable = false)
	private Player player;

	@Column(nullable = false)
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

	public Player getPlayer() {
		return player;
	}

	public int getValue() {
		return value;
	}

}