package com.example.notification_export_service.domain;

import java.util.List;

public interface INotificationDAO {
    List<Notification> notifications();
    Notification notificationById(int id);
    List<Notification> findByUserId(int userId);
    List<Notification> findByStatus(String status);
    boolean insert(Notification notification);
    boolean update(Notification notification);
    boolean delete(int id);
}