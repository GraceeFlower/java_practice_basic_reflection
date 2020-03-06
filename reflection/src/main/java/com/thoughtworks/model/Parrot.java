package com.thoughtworks.model;

import com.thoughtworks.annotation.Alias;
import com.thoughtworks.annotation.Limit;

@Alias
public class Parrot extends Animal {

    public Parrot() {
    }

    public Parrot(boolean canTalk) {
        this.canTalk = canTalk;
    }

    private Parrot(int flySpeed, boolean canTalk) {
        this.flySpeed = flySpeed;
        this.canTalk = canTalk;
    }

    @Limit(min = 1, max = 140)
    private int flySpeed;

    private boolean canTalk;

    public String petName;

    @Override
    public void walk() {
        System.out.println("Parrot is walking");
    }

    private void privateMethod(){
        System.out.println("private method in parrot");
    }

    public static void staticMethod(){
        System.out.println("static method in parrot");
    }

    public void setFlySpeed(int flySpeed) {
        this.flySpeed = flySpeed;
    }


    public int getFlySpeed() {
        return flySpeed;
    }

    public void setCanTalk(boolean canTalk) {
        this.canTalk = canTalk;
    }

    @Override
    public String toString() {
        return "Parrot{" +
                "flySpeed=" + flySpeed +
                ", canTalk=" + canTalk +
                '}';
    }
}
