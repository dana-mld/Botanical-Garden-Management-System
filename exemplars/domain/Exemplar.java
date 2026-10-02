package com.example.exemplars_service.domain;

public class Exemplar {
    private ExemplarID id;
    private Integer plantId;
    private String zonaGradina;

    public Exemplar() {}

    public Exemplar(ExemplarID id, Integer plantId, String zonaGradina) {
        this.id = id;
        this.plantId = plantId;
        this.zonaGradina = zonaGradina;
    }

    public ExemplarID getId() {
        return id;
    }

    public void setId(ExemplarID id) {
        this.id = id;
    }

    public Integer getPlantId() {
        return plantId;
    }

    public void setPlantId(Integer plantId) {  // Schimbă din int în Integer
        this.plantId = plantId;
    }

    public String getZonaGradina() {
        return zonaGradina;
    }

    public void setZonaGradina(String zonaGradina) {
        this.zonaGradina = zonaGradina;
    }
}