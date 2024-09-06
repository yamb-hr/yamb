package com.tejko.yamb.api.dto.requests;

import com.tejko.yamb.domain.enums.BoxType;
import com.tejko.yamb.domain.enums.ColumnType;

public class ActionRequest {

    private int[] diceToRoll;
    private ColumnType columnType;
    private BoxType boxType;

    public ActionRequest() {}

    public int[] getDiceToRoll() {
        return diceToRoll;
    }

    public void setDiceToRoll(int[] diceToRoll) {
        this.diceToRoll = diceToRoll;
    }

    public ColumnType getColumnType() {
        return columnType;
    }

    public void setColumnType(ColumnType columnType) {
        this.columnType = columnType;
    }

    public BoxType getBoxType() {
        return boxType;
    }

    public void setBoxType(BoxType boxType) {
        this.boxType = boxType;
    }
    
}
