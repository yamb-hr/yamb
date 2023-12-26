package com.tejko.yamb.models.game;

import java.io.Serializable;
import java.util.concurrent.ThreadLocalRandom;

public class Dice implements Serializable {
    
    private int index;
    private int value;

    private Dice() {}

    private Dice(int index, int value) {
        this.index = index;
        this.value = value;
    }

    public static Dice getInstance(int index) {
        return new Dice(index, 6);
    }

    public int getValue() {
        return value;
    }

    public int getIndex() {
        return index;
    }

    public void roll() {
        this.value = ThreadLocalRandom.current().nextInt(1, 7);
    }

}
