package com.example.notification_export_service.controller;

import com.example.notification_export_service.domain.Notification;
import com.example.notification_export_service.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService service;

    public NotificationController(NotificationService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Notification>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Notification>> getUserNotifications(@PathVariable int userId) {
        return ResponseEntity.ok(service.getUserNotifications(userId));
    }

    @GetMapping("/pending")
    public ResponseEntity<List<Notification>> getPendingNotifications() {
        return ResponseEntity.ok(service.getPendingNotifications());
    }

    @PostMapping
    public ResponseEntity<Void> send(@RequestBody Notification notification) {
        return service.send(notification)
                ? ResponseEntity.ok().build()
                : ResponseEntity.internalServerError().build();
    }

    @PostMapping("/retry")
    public ResponseEntity<Void> retryFailed() {
        service.retryFailedNotifications();
        return ResponseEntity.ok().build();
    }
}