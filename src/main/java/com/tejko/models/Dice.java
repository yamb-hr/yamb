package com.tejko.models;

import java.io.Serializable;
import java.util.concurrent.ThreadLocalRandom;

public class Dice implements Serializable {

    private int value = 6;
    private int order;

    public Dice() {}

    public Dice(int order) {
        this.order = order;
    }

    public int getValue() {
        return value;
    }

    public int getOrder() {
        return order;
    }

    public void roll() {
        this.value = ThreadLocalRandom.current().nextInt(1, 7);
    }

}
