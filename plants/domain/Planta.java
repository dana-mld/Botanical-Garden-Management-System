package com.example.demo.domain;

public class Planta {

    private PlantID id;
    private String denumire;
    private String specie;
    private String tip;
    private boolean carnivora;

    public Planta() {
        this.id = new PlantID();
    }

    public Planta(PlantID id, String denumire, String specie, String tip, boolean esteCarnivora) {
        this.id = id;
        this.denumire = denumire;
        this.specie = specie;
        this.tip = tip;
        this.carnivora = esteCarnivora;
    }

    public PlantID getId() {
        return id;
    }

    public void setId(PlantID id) {
        this.id = id;
    }

    public String getDenumire() {
        return denumire;
    }

    public void setDenumire(String denumire) {
        this.denumire = denumire;
    }

    public String getSpecie() {
        return specie;
    }

    public void setSpecie(String specie) {
        this.specie = specie;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public boolean isEsteCarnivora() {
        return carnivora;
    }

    public void setEsteCarnivora(boolean esteCarnivora) {
        this.carnivora = esteCarnivora;
    }


}