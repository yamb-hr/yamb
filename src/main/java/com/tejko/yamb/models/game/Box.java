package com.tejko.yamb.models.game;

import java.io.Serializable;

import com.tejko.yamb.models.enums.BoxType;

public class Box implements Serializable {

    private BoxType type;
    private Integer value;

    private Box() {}

    private Box(BoxType type, Integer value) {
        this.type = type;
        this.value = value;
    }  

    public static Box getInstance(BoxType type) {
        return new Box(type, null);
    }    

    public BoxType getType() {
        return type;
    }

    public Integer getValue() {
        return value;
    }

    public void fill(int value) {
        this.value = value;
    }

}
