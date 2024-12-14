package com.tejko.yamb.domain.models;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;

@Entity(name = "image")
@Table(name = "image", indexes = {
    @Index(name = "idx_image_external_id", columnList = "external_id")
})
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "external_id", nullable = false, updatable = false, unique = true)
    private UUID externalId;

    @Column(name = "name")
    private String name;

    @Column(name = "url")
    private String url;

    @Column(name = "public_id", nullable = false, updatable = true, unique = true)
    private String publicId;

    @OneToOne
    private Player player;
    
    @PrePersist
    private void ensureExternalId() {
        if (this.externalId == null) {
            this.externalId = UUID.randomUUID();
        }
    }

    protected Image() {}

    protected Image(Player player, String name, String url, String publicId) {
        this.player = player;
        this.name = name;
        this.url = url;
        this.publicId = publicId;
    }

    public static Image getInstance(Player player, String name, String url, String publicId) {
        return new Image(player, name, url, publicId);
    }

    public Long getId() {
        return id;
    }

    public UUID getExternalId() {
        return externalId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }

}
