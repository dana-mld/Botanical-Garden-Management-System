package com.example.demo.domain;

public class PlantID {

    private int id;

    public PlantID() {

    }

    public PlantID(int id) {
        this.id = id;
    }

    public String getId() {
        return String.valueOf(id);
    }

    public void setId(int id) {
        this.id = id;
    }
}
