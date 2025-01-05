package com.tejko.yamb.api.events;

import com.tejko.yamb.domain.models.Clash;

public class ClashUpdatedEvent {

    private final Clash clash;

    public ClashUpdatedEvent(Clash clash) {
        this.clash = clash;
    }

    public Clash getClash() {
        return clash;
    }
    
}
