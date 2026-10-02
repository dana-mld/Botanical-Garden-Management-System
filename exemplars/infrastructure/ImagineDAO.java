package com.example.exemplars_service.infrastructure;


import com.example.exemplars_service.domain.IImagineDAO;
import com.example.exemplars_service.domain.Imagine;
import com.example.exemplars_service.infrastructure.tableEntities.ImagineEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ImagineDAO implements IImagineDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public ImagineDAO() {}

    @Override
    public List<Imagine> imagini() {
        List<ImagineEntity> entities = entityManager
                .createQuery("SELECT i FROM ImagineEntity i", ImagineEntity.class)
                .getResultList();

        return entities.stream()
                .map(ImagineEntity::toImagine)
                .collect(Collectors.toList());
    }

    @Override
    public Imagine imagineById(int id) {
        ImagineEntity entity = entityManager.find(ImagineEntity.class, id);
        return entity != null ? entity.toImagine() : null;
    }

    @Override
    @Transactional
    public boolean insert(Imagine imagine) {
        try {
            ImagineEntity entity = toEntity(imagine);
            entityManager.persist(entity);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    @Transactional
    public boolean update(Imagine imagine) {
        try {
            ImagineEntity entity = toEntity(imagine);
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
            ImagineEntity entity = entityManager.find(ImagineEntity.class, id);

            if (entity != null) {
                entityManager.remove(entity);
                return true;
            }

            return false;
        } catch (Exception e) {
            return false;
        }
    }

    private ImagineEntity toEntity(Imagine imagine) {
        ImagineEntity entity = new ImagineEntity();

        entity.setId(imagine.getId().getId());
        entity.setExemplarId(imagine.getExemplarId());
        entity.setCaleFisier(imagine.getCaleFisier());
        entity.setDescriere(imagine.getDescriere());

        return entity;
    }
    @Override
    public List<Imagine> findByExemplarId(int exemplarId) {
        List<ImagineEntity> entities = entityManager
                .createQuery("SELECT i FROM ImagineEntity i WHERE i.exemplarId = :exId", ImagineEntity.class)
                .setParameter("exId", exemplarId)
                .getResultList();
        return entities.stream().map(ImagineEntity::toImagine).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public boolean deleteByExemplarId(int exemplarId) {
        try {
            entityManager.createQuery("DELETE FROM ImagineEntity i WHERE i.exemplarId = :exId")
                    .setParameter("exId", exemplarId)
                    .executeUpdate();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}