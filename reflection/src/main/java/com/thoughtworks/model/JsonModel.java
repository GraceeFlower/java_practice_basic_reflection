package com.thoughtworks.model;

import java.util.ArrayList;
import java.util.List;

public class JsonModel {
    private String name;
    private int age;
    private Animal animal;

    private List<String> stringList = new ArrayList<>();
    private List<Parrot> parrotList = new ArrayList<>();
    private List<Desk> deskList = new ArrayList<>();

    public JsonModel(String name, int age, Animal animal, List<String> stringList, List<Parrot> parrotList, List<Desk> deskList) {
        this.name = name;
        this.age = age;
        this.animal = animal;
        this.stringList = stringList;
        this.parrotList = parrotList;
        this.deskList = deskList;
    }
}
