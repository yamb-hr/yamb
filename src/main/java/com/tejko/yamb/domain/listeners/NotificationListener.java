package com.tejko.yamb.domain.listeners;

import javax.persistence.PostPersist;

import org.springframework.beans.factory.annotation.Autowired;

import com.tejko.yamb.domain.enums.MessageType;
import com.tejko.yamb.domain.models.Notification;
import com.tejko.yamb.util.WebSocketManager;

public class NotificationListener {

	private WebSocketManager webSocketManager;

	@Autowired
	public NotificationListener(WebSocketManager webSocketManager) {
		this.webSocketManager = webSocketManager;
	}

	@PostPersist
	public void onPostPersist(Notification notification) {
		webSocketManager.convertAndSend("/topic/players/" + notification.getPlayer().getExternalId(), notification.getContent(), MessageType.NOTIFICATION);
	}

}
