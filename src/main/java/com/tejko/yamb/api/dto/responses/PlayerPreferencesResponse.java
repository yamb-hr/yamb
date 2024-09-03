package com.tejko.yamb.api.dto.responses;

public class PlayerPreferencesResponse {

    private String language;
    private String theme;

    public PlayerPreferencesResponse() {}

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }   

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }
    
}
