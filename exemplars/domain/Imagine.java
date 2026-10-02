package com.example.exemplars_service.domain;


public class Imagine {

    private ImagineID id;
    private int exemplarId;
    private String caleFisier;
    private String descriere;

    public Imagine() {
        this.id = new ImagineID();
    }

    public Imagine(ImagineID id, int exemplarId, String caleFisier, String descriere) {
        this.id = id;
        this.exemplarId = exemplarId;
        this.caleFisier = caleFisier;
        this.descriere = descriere;
    }

    public ImagineID getId() {
        return id;
    }

    public void setId(ImagineID id) {
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