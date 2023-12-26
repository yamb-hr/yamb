package com.tejko.yamb.models.payload.dto;

import com.tejko.yamb.models.enums.BoxType;

public class BoxDTO {

    public BoxType type;
    public Integer value;

    public BoxDTO(BoxType type, Integer value) {
        this.type = type;
        this.value = value;
    }

    public BoxType getType() {
        return type;
    }

    public Integer getValue() {
        return value;
    }
    
}
