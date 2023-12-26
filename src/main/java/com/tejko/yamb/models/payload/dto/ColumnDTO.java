package com.tejko.yamb.models.payload.dto;

import java.util.List;

import com.tejko.yamb.models.enums.ColumnType;

public class ColumnDTO {
    
    private ColumnType type;
    private List<BoxDTO> boxes;

    public ColumnDTO(ColumnType type, List<BoxDTO> boxes) {
        this.type = type;
        this.boxes = boxes;
    }

    public ColumnType getType() {
        return type;
    }

    public List<BoxDTO> getBoxes() {
        return boxes;
    }
    
}
