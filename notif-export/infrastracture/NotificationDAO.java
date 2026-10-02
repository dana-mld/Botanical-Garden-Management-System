package com.example.notification_export_service.infrastracture;

import com.example.notification_export_service.domain.INotificationDAO;
import com.example.notification_export_service.domain.Notification;
import com.example.notification_export_service.infrastracture.tableEntities.NotificationEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class NotificationDAO implements INotificationDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Notification> notifications() {
        TypedQuery<NotificationEntity> query = entityManager.createQuery(
                "SELECT n FROM NotificationEntity n ORDER BY n.createdAt DESC",
                NotificationEntity.class);
        List<NotificationEntity> entities = query.getResultList();
        return entities.stream()
                .map(NotificationEntity::toNotification)
                .collect(Collectors.toList());
    }

    @Override
    public Notification notificationById(int id) {
        NotificationEntity entity = entityManager.find(NotificationEntity.class, id);
        return entity != null ? entity.toNotification() : null;
    }

    @Override
    public List<Notification> findByUserId(int userId) {
        TypedQuery<NotificationEntity> query = entityManager.createQuery(
                "SELECT n FROM NotificationEntity n WHERE n.userId = :userId ORDER BY n.createdAt DESC",
                NotificationEntity.class);
        query.setParameter("userId", userId);
        List<NotificationEntity> entities = query.getResultList();
        return entities.stream()
                .map(NotificationEntity::toNotification)
                .collect(Collectors.toList());
    }

    @Override
    public List<Notification> findByStatus(String status) {
        TypedQuery<NotificationEntity> query = entityManager.createQuery(
                "SELECT n FROM NotificationEntity n WHERE n.status = :status ORDER BY n.createdAt DESC",
                NotificationEntity.class);
        query.setParameter("status", status);
        List<NotificationEntity> entities = query.getResultList();
        return entities.stream()
                .map(NotificationEntity::toNotification)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public boolean insert(Notification notification) {
        try {
            NotificationEntity entity = new NotificationEntity();
            entity.setUserId(notification.getUserId());
            entity.setChannel(notification.getChannel());
            entity.setMessage(notification.getMessage());
            entity.setStatus(notification.getStatus());
            entity.setCreatedAt(notification.getCreatedAt());
            entity.setSentAt(notification.getSentAt());
            entity.setErrorMessage(notification.getErrorMessage());

            entityManager.persist(entity);
            entityManager.flush();

            if (entity.getId() != null) {
                notification.setId(String.valueOf(entity.getId()));
                System.out.println(" Notification ID set: " + entity.getId());
            } else {
                System.err.println(" Failed to generate notification ID");
            }

            return true;
        } catch (Exception e) {
            System.err.println(" Insert error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    @Transactional
    public boolean update(Notification notification) {
        try {
            if (notification.getId() == null || notification.getId().isEmpty()) {
                System.err.println(" Cannot update notification: ID is null");
                return false;
            }

            Integer id = Integer.parseInt(notification.getId());
            NotificationEntity entity = entityManager.find(NotificationEntity.class, id);

            if (entity != null) {
                entity.setUserId(notification.getUserId());
                entity.setChannel(notification.getChannel());
                entity.setMessage(notification.getMessage());
                entity.setStatus(notification.getStatus());
                entity.setErrorMessage(notification.getErrorMessage());
                entity.setSentAt(notification.getSentAt());
                entityManager.merge(entity);
                return true;
            }
            return false;
        } catch (NumberFormatException e) {
            System.err.println(" Invalid notification ID format: " + notification.getId());
            return false;
        } catch (Exception e) {
            System.err.println(" Update error: " + e.getMessage());
            return false;
        }
    }
    @Override
    @Transactional
    public boolean delete(int id) {
        NotificationEntity entity = entityManager.find(NotificationEntity.class, id);
        if (entity != null) {
            entityManager.remove(entity);
            return true;
        }
        return false;
    }
}