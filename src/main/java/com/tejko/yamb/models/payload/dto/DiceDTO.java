package com.tejko.yamb.models.payload.dto;

public class DiceDTO {

    private int index;
    private int value;

    public DiceDTO(int index, int value) {
        this.index = index;
        this.value = value;
    }

    public int getIndex() {
        return index;
    }

    public int getValue() {
        return value;
    }
    
}
