package com.tejko.yamb.models.payload;

public class TempPlayerRequest {

    private String username;

    private TempPlayerRequest() {}

    private TempPlayerRequest(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
    
}
