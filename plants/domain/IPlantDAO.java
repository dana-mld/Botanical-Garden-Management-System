package com.example.demo.domain;



import java.util.List;
import java.util.UUID;

public interface IPlantDAO {
    List<Planta> plants();
    List<Planta> findFiltered(String search, String tip, Boolean carnivora);
    Planta plantById(Integer id);
    boolean insert(Planta plant);
    boolean update(Planta plant);
    boolean delete(int id);
}