package com.tejko.yamb.api.payload.requests;

import com.tejko.yamb.domain.enums.BoxType;
import com.tejko.yamb.domain.enums.ColumnType;

public class ActionRequest {

    private int[] diceToRoll;
    private ColumnType columnType;
    private BoxType boxType;

    private ActionRequest() {}
    
    private ActionRequest(int[] diceToRoll, ColumnType columnType, BoxType boxType) {
        this.diceToRoll = diceToRoll;
        this.columnType = columnType;
        this.boxType = boxType;
    }

    public int[] getDiceToRoll() {
        return diceToRoll;
    }

    public ColumnType getColumnType() {
        return columnType;
    }

    public BoxType getBoxType() {
        return boxType;
    }
    
}
