package com.tejko.yamb.domain.models;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.UpdateTimestamp;

import com.tejko.yamb.domain.enums.TicketStatus;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;

@Entity(name = "ticket")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@Table(name = "ticket", indexes = {
    @Index(name = "idx_ticket_external_id", columnList = "external_id")
})
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "external_id", nullable = false, updatable = false, unique = true)
    private UUID externalId;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", updatable = true)
    private LocalDateTime updatedAt;

    @GeneratedValue(generator = "string-sequence-generator")
    @GenericGenerator(
        name = "string-sequence-generator",
        strategy = "com.tejko.yamb.domain.util.StringSequenceIdentifier"
    )
    @Column(nullable = false, unique = true, length = 10)
    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = true)
    private Player player;

    @Type(type = "jsonb")
    @Column(name = "email_addresses", nullable = false, updatable = true, columnDefinition = "jsonb")
    private Set<String> emailAddresses;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false, columnDefinition = "text")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TicketStatus status;

    protected Ticket() {}

    protected Ticket(Player player, Set<String> emailAddresses, String title, String description, TicketStatus status) {
        this.player = player;
        this.emailAddresses = emailAddresses;
        this.title = title;
        this.description = description;
        this.status = status;
    }
    
    public static Ticket getInstance(Player player, Set<String> emailAddress, String title, String description) {
        return new Ticket(player, emailAddress, title, description, TicketStatus.NEW);
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

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Player getPlayer() {
        return player;
    }

    public Set<String> getEmailAddress() {
        return emailAddresses;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public TicketStatus getStatus() {
        return status;
    }

    @PrePersist
    private void ensureExternalId() {
        if (this.externalId == null) {
            this.externalId = UUID.randomUUID();
        }
    }

}
