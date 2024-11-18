package com.tejko.yamb.api.dto.responses;

import java.util.Set;
import java.util.UUID;

import java.time.LocalDateTime;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import com.tejko.yamb.domain.enums.TicketStatus;

@Relation(collectionRelation = "tickets")
public class TicketResponse extends RepresentationModel<TicketResponse> {

    private UUID id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String code;
    private PlayerResponse player;
    private Set<String> emailAddresses;
    private String title;
    private String description;
    private TicketStatus status;

    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public PlayerResponse getPlayer() {
        return player;
    }

    public void setPlayer(PlayerResponse player) {
        this.player = player;
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

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

}
