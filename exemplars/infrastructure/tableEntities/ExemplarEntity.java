package com.example.exemplars_service.infrastructure.tableEntities;


import com.example.exemplars_service.domain.Exemplar;
import com.example.exemplars_service.domain.ExemplarID;
import jakarta.persistence.*;

@Entity
@Table(name = "exemplare")
public class ExemplarEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private int id;

    private Integer plantId;

    private String zonaGradina;

    public ExemplarEntity() {
    }

    public Exemplar toExemplar() {
        return new Exemplar(
                new ExemplarID(id),
                plantId,
                zonaGradina
        );
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getPlantId() {
        return plantId;
    }

    public void setPlantId(Integer plantId) {
        this.plantId = plantId;
    }

    public String getZonaGradina() {
        return zonaGradina;
    }

    public void setZonaGradina(String zonaGradina) {
        this.zonaGradina = zonaGradina;
    }
}