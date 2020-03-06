package com.thoughtworks.model;

import com.thoughtworks.annotation.Limit;

public class Desk extends Parrot {
    @Limit(min = 1,max = 100)
    private int length;

    public Desk(int length) {
        this.length = length;
    }

    @Override
    public String toString() {
        return "Desk{" +
                "length=" + length +
                '}';
    }
}
