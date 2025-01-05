package com.tejko.yamb.api.dto.requests;

import java.util.Set;
import java.util.UUID;

import javax.validation.constraints.NotBlank;

public class TicketRequest {

    private UUID playerId;

    private Set<String> emailAddresses;

    @NotBlank(message = "error.title_required")
    private String title;

    @NotBlank(message = "error.description_required")
    private String description;

    public UUID getPlayerId() {
        return playerId;
    }

    public void setPlayerId(UUID playerId) {
        this.playerId = playerId;
    }

    public Set<String> getEmailAddresses() {
        return emailAddresses;
    }

    public void setEmailAddresses(Set<String> emailAddresses) {
        this.emailAddresses = emailAddresses;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
}
