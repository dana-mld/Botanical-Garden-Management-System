package com.example.exemplars_service.domain;




import java.util.List;
import java.util.UUID;

public interface IExemplarDAO {

    List<Exemplar> exemplars();

    Exemplar exemplarById(int  id);

    boolean insert(Exemplar plant);

    boolean update(Exemplar plant);

    boolean delete(int id);

    List<Exemplar> exemplarsByPlantId(Integer plantId);

    List<Exemplar> findByZone(String zona);
}