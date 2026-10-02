package com.example.demo.infrastructure;

import com.example.demo.domain.IPlantDAO;
import com.example.demo.domain.PlantID;
import com.example.demo.domain.Planta;
import com.example.demo.infrastructure.tableEntities.PlantEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class PlantDAO implements IPlantDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public PlantDAO() {}



    @Override
    public Planta plantById(Integer id) {
        PlantEntity entity = entityManager.find(PlantEntity.class, id);
        return entity != null ? entity.toPlant() : null;
    }

    @Override
    @Transactional
    public boolean insert(Planta plant) {
        try {
            PlantEntity entity = toEntity(plant);
            entityManager.persist(entity);


            plant.setId(new PlantID(entity.getId()));

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    @Override
    public List<Planta> plants() {
        List<PlantEntity> entities = entityManager
                .createQuery("SELECT p FROM PlantEntity p ORDER BY p.tip ASC, p.specie ASC", PlantEntity.class)
                .getResultList();
        return entities.stream().map(PlantEntity::toPlant).collect(Collectors.toList());
    }

    @Override
    public List<Planta> findFiltered(String search, String tip, Boolean carnivora) {
        StringBuilder queryStr = new StringBuilder("SELECT p FROM PlantEntity p WHERE 1=1");

        if (search != null && !search.isEmpty()) {
            queryStr.append(" AND (lower(p.denumire) LIKE lower(:search) OR lower(p.specie) LIKE lower(:search))");
        }
        if (tip != null && !tip.isEmpty()) {
            queryStr.append(" AND p.tip = :tip");
        }
        if (carnivora != null) {
            queryStr.append(" AND p.carnivora = :carnivora");
        }
        queryStr.append(" ORDER BY p.tip ASC, p.specie ASC");

        var query = entityManager.createQuery(queryStr.toString(), PlantEntity.class);

        if (search != null && !search.isEmpty()) query.setParameter("search", "%" + search + "%");
        if (tip != null && !tip.isEmpty()) query.setParameter("tip", tip);
        if (carnivora != null) query.setParameter("carnivora", carnivora);

        return query.getResultList().stream().map(PlantEntity::toPlant).collect(Collectors.toList());
    }
    @Override
    @Transactional
    public boolean update(Planta plant) {
        try {
            PlantEntity entity = toEntity(plant);
            entityManager.merge(entity);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        try {
            PlantEntity entity = entityManager.find(PlantEntity.class, id);
            if (entity != null) {
                entityManager.remove(entity);
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    private PlantEntity toEntity(Planta plant) {
        PlantEntity entity = new PlantEntity();

        if (plant.getId() != null) {
            entity.setId(Integer.parseInt(plant.getId().getId())); 
        }

        entity.setDenumire(plant.getDenumire());
        entity.setSpecie(plant.getSpecie());
        entity.setTip(plant.getTip());
        entity.setEsteCarnivora(plant.isEsteCarnivora());

        return entity;
    }
}