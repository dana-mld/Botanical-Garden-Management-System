package com.example.notification_export_service.infrastracture;

import com.example.notification_export_service.domain.IExportDAO;
import com.example.notification_export_service.domain.ExportJob;
import com.example.notification_export_service.infrastracture.tableEntities.ExportEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ExportDAO implements IExportDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<ExportJob> exports() {
        TypedQuery<ExportEntity> query = entityManager.createQuery(
                "SELECT e FROM ExportEntity e ORDER BY e.startTime DESC", ExportEntity.class);
        List<ExportEntity> entities = query.getResultList();
        return entities.stream()
                .map(ExportEntity::toExportJob)
                .collect(Collectors.toList());
    }

    @Override
    public ExportJob exportById(int id) {
        ExportEntity entity = entityManager.find(ExportEntity.class, id);
        return entity != null ? entity.toExportJob() : null;
    }

    @Override
    public List<ExportJob> findByUserId(int userId) {
        TypedQuery<ExportEntity> query = entityManager.createQuery(
                "SELECT e FROM ExportEntity e WHERE e.userId = :userId ORDER BY e.startTime DESC",
                ExportEntity.class);
        query.setParameter("userId", userId);
        List<ExportEntity> entities = query.getResultList();
        return entities.stream()
                .map(ExportEntity::toExportJob)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public boolean insert(ExportJob job) {
        ExportEntity entity = new ExportEntity();
        entity.setUserId(job.getUserId());
        entity.setFormat(job.getFormat());
        entity.setStatus(job.getStatus());
        entity.setFilePath(job.getFilePath());
        entity.setErrorMessage(job.getErrorMessage());
        entity.setStartTime(job.getStartTime() != null ? job.getStartTime() : LocalDateTime.now());
        entity.setCompletedTime(job.getCompletedTime());

        entityManager.persist(entity);
        return entity.getId() != null && entity.getId() > 0;
    }

    @Override
    @Transactional
    public boolean update(ExportJob job) {
        Integer id = Integer.parseInt(job.getId());
        ExportEntity entity = entityManager.find(ExportEntity.class, id);

        if (entity != null) {
            entity.setUserId(job.getUserId());
            entity.setFormat(job.getFormat());
            entity.setStatus(job.getStatus());
            entity.setFilePath(job.getFilePath());
            entity.setErrorMessage(job.getErrorMessage());
            entity.setStartTime(job.getStartTime());
            entity.setCompletedTime(job.getCompletedTime());
            entityManager.merge(entity);
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        ExportEntity entity = entityManager.find(ExportEntity.class, id);
        if (entity != null) {
            entityManager.remove(entity);
            return true;
        }
        return false;
    }
}