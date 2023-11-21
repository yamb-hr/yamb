package com.tejko.models;

import java.io.Serializable;

import com.tejko.constants.YambConstants;
import com.tejko.exceptions.IllegalMoveException;
import com.tejko.models.enums.BoxType;

public class Box implements Serializable {

    private BoxType type;
    private int value;
    private boolean filled;
    private boolean available;

    private Box() {}

    private Box(BoxType type, int value, boolean filled, boolean available) {
        this.type = type;
        this.value = value;
        this.filled = filled;
        this.available = available;
    }  

    public static Box getInstance(BoxType type, boolean available) {
        return new Box(type, 0, false, available);
    }    

    public BoxType getType() {
        return type;
    }

    public int getValue() {
        return value;
    }

    public boolean isFilled() {
        return filled;
    }

    public boolean isAvailable() {
        return available;
    }

    public void fill(int value) {
        validateFillAction();
        this.value = value;
        filled = true;
        available = false;
    }

    private void validateFillAction() {
        if (filled) {
            throw new IllegalMoveException(YambConstants.ERROR_MESSAGE_BOX_ALREADY_FILLED);
        } else if (!available) {
			throw new IllegalMoveException(YambConstants.ERROR_MESSAGE_BOX_NOT_AVAILABLE);
        }
    }

    public void makeAvailable() {
        available = true;
    }

}
