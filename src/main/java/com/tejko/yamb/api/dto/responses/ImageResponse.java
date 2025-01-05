package com.tejko.yamb.api.dto.responses;

import java.util.UUID;

public class ImageResponse {

    private UUID id;
    private String name;
    private String url;

    public ImageResponse() {}

    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String title) {
        this.name = title;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

    
    
}
