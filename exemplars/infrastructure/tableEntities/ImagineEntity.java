package com.example.exemplars_service.infrastructure.tableEntities;


import com.example.exemplars_service.domain.Imagine;
import com.example.exemplars_service.domain.ImagineID;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "imagini")
public class ImagineEntity {

    @Id
    private int id;

    private int exemplarId;

    private String caleFisier;

    private String descriere;

    public ImagineEntity() {
    }

    public Imagine toImagine() {
        return new Imagine(
                new ImagineID(id),
                exemplarId,
                caleFisier,
                descriere
        );
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getExemplarId() {
        return exemplarId;
    }

    public void setExemplarId(int exemplarId) {
        this.exemplarId = exemplarId;
    }

    public String getCaleFisier() {
        return caleFisier;
    }

    public void setCaleFisier(String caleFisier) {
        this.caleFisier = caleFisier;
    }

    public String getDescriere() {
        return descriere;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }
}