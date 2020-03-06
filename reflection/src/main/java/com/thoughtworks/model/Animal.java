package com.thoughtworks.model;

public abstract class Animal implements Walkable {

    public int age;

    public Animal() {
    }

    public Animal(String name) {
        this.name = name;
    }

    private String name;

    protected void run() {
        System.out.println("running");
    }
}
