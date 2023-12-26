package com.tejko.yamb.models.payload.dto;

import java.util.List;

public class SheetDTO {

    private List<ColumnDTO> columns;

    public SheetDTO(List<ColumnDTO> columns) {
        this.columns = columns;
    }

    public List<ColumnDTO> getColumns() {
        return columns;
    }
    
}
