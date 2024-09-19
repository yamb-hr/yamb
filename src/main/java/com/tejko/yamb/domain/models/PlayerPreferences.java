package com.tejko.yamb.domain.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity(name = "preferences")
@Table(name = "preferences")
public class PlayerPreferences {

    @Id
    @Column(name = "id")
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private Player player;

    @Column(name = "theme", nullable = false)
    private String theme = "dark"; // e.g. "dark", "light"

    @Column(name = "language", nullable = false)
    private String language = "en";  // e.g. "en", "hr"

    protected PlayerPreferences() {}

    protected PlayerPreferences(Player player, String theme, String language) {
        this.player = player;
        this.theme = theme;
        this.language = language;
    }

    public static PlayerPreferences getInstance(Player player, String theme, String language) {
        return new PlayerPreferences(player, theme, language);
    }

    public Long getId() {
        return id;
    }

    public Player getPlayer() {
        return player;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

}
