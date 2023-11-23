package com.tejko.security;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tejko.models.Player;
import com.tejko.repositories.PlayerRepository;

@Component
public class SessionManager {

	@Autowired
	PlayerRepository playerRepo;

	@Autowired
	HttpSession httpSession;

	public Player createPlayerSession() {
		Player player = playerRepo.save(Player.getInstance("test", "test"));
		httpSession.setAttribute("playerId", player.getId());
		return player;
	}

	public Player getPlayerFromSession() {
		if (httpSession.getAttribute("playerId") != null) {
			return playerRepo.findById((Long) httpSession.getAttribute("playerId")).get();
		}
		return null;
	}

}