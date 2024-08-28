package com.tejko.yamb.api.dto.responses;

public class PlayerPreferencesResponse {

    private String language;
    private String theme;

    public PlayerPreferencesResponse() {}

    public PlayerPreferencesResponse(String language, String theme) {
        this.language = language;
        this.theme = theme;
    }

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
