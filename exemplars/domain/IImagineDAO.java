package com.example.exemplars_service.domain;


import java.util.List;

public interface IImagineDAO {
    List<Imagine> findByExemplarId(int exemplarId);
    boolean deleteByExemplarId(int exemplarId);
    List<Imagine> imagini();

    Imagine imagineById(int id);

    boolean insert(Imagine imagine);

    boolean update(Imagine imagine);

    boolean delete(int id);
}