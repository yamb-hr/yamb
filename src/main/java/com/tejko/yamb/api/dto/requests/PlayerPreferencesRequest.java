package com.tejko.yamb.api.dto.requests;

import javax.validation.constraints.NotBlank;

public class PlayerPreferencesRequest {

    @NotBlank
    private String language;
    
    @NotBlank
    private String theme;

    public PlayerPreferencesRequest() {}

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
