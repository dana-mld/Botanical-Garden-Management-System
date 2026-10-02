package com.example.exemplars_service.infrastructure;


import com.example.exemplars_service.domain.Exemplar;
import com.example.exemplars_service.domain.ExemplarID;
import com.example.exemplars_service.infrastructure.tableEntities.ExemplarEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.example.exemplars_service.domain.IExemplarDAO;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ExemplarDAO implements IExemplarDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public ExemplarDAO() {}

    @Override
    public List<Exemplar> exemplars() {
        List<ExemplarEntity> entities = entityManager
                .createQuery("SELECT e FROM ExemplarEntity e", ExemplarEntity.class)
                .getResultList();

        return entities.stream()
                .map(ExemplarEntity::toExemplar)
                .collect(Collectors.toList());
    }

    @Override
    public Exemplar exemplarById(int id) {
        ExemplarEntity entity = entityManager.find(ExemplarEntity.class, id);
        return entity != null ? entity.toExemplar() : null;
    }
    @Override
    public List<Exemplar> findByZone(String zona) {


        String jpql = "SELECT e FROM ExemplarEntity e WHERE e.zonaGradina = :zona";
        List<ExemplarEntity> entities = entityManager.createQuery(jpql, ExemplarEntity.class)
                .setParameter("zona", zona)
                .getResultList();


        return entities.stream()
                .map(entity -> {
                    Exemplar ex = entity.toExemplar();
                    return ex;
                })
                .collect(Collectors.toList());
    }
    @Override
    @Transactional
    public boolean insert(Exemplar exemplar) {
        try {
            ExemplarEntity entity = toEntity(exemplar);
            entityManager.persist(entity);
            entityManager.flush();


            exemplar.setId(new ExemplarID(entity.getId()));

            return true;
        } catch (Exception e) {
            System.err.println("Insert error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    @Transactional
    public boolean update(Exemplar exemplar) {
        try {
            ExemplarEntity entity = toEntity(exemplar);
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
            ExemplarEntity entity = entityManager.find(ExemplarEntity.class, id);

            if (entity != null) {
                entityManager.remove(entity);
                return true;
            }

            return false;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<Exemplar> exemplarsByPlantId(Integer plantId) {

        List<ExemplarEntity> entities = entityManager
                .createQuery(
                        "SELECT e FROM ExemplarEntity e WHERE e.plantId = :plantId",
                        ExemplarEntity.class
                )
                .setParameter("plantId", plantId)
                .getResultList();

        return entities.stream()
                .map(ExemplarEntity::toExemplar)
                .collect(Collectors.toList());
    }

    private ExemplarEntity toEntity(Exemplar exemplar) {
        ExemplarEntity entity = new ExemplarEntity();

        if (exemplar.getId() != null && exemplar.getId().getId() != 0) {
            entity.setId(exemplar.getId().getId());
        }

        entity.setPlantId(exemplar.getPlantId());
        entity.setZonaGradina(exemplar.getZonaGradina());

        return entity;
    }

}
