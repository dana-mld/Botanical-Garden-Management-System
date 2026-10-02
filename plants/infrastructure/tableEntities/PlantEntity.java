package com.example.demo.infrastructure.tableEntities;


import com.example.demo.domain.PlantID;
import com.example.demo.domain.Planta;
import jakarta.persistence.*;

@Entity
@Table(name = "Plante")
public class PlantEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private int id;

    private String denumire;

    private String specie;

    private String tip;

    private boolean carnivora;

    public PlantEntity() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public Planta toPlant() {
        PlantID plantID = new PlantID(this.id);
        return new Planta(plantID, this.denumire, this.specie, this.tip, this.carnivora);
    }

}