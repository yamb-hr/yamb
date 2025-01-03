package com.tejko.yamb.api.events.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.tejko.yamb.api.assemblers.ClashDetailModelAssembler;
import com.tejko.yamb.api.dto.responses.ClashDetailResponse;
import com.tejko.yamb.api.events.ClashUpdatedEvent;
import com.tejko.yamb.domain.enums.MessageType;
import com.tejko.yamb.util.WebSocketManager;

@Component
public class ClashUpdatedEventHandler {

    private final WebSocketManager webSocketManager;
    private final ClashDetailModelAssembler clashDetailModelAssembler;

    @Autowired
    public ClashUpdatedEventHandler(WebSocketManager webSocketManager, ClashDetailModelAssembler clashDetailModelAssembler) {
        this.webSocketManager = webSocketManager;
        this.clashDetailModelAssembler = clashDetailModelAssembler;
    }

    @EventListener
    public void handleClashUpdated(ClashUpdatedEvent event) {
        ClashDetailResponse clashDetailResponse = clashDetailModelAssembler.toModel(event.getClash());
        webSocketManager.convertAndSend(
            "/topic/clashes/" + clashDetailResponse.getId(),
            clashDetailResponse, 
            MessageType.CLASH
        );
    }
    
}
