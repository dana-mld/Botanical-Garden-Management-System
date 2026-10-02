package com.example.users_service.service.observer;


import com.example.users_service.domain.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AuditObserver implements UserObserver {

    @Override
    public void onUserChanged(User user, String action) {
        System.out.println("========================================");
        System.out.println(" AUDIT LOG");
        System.out.println("   Timestamp: " + LocalDateTime.now());
        System.out.println("   ph: " + user.getPhoneNumber());
        System.out.println("   Action: " + action);
        System.out.println("   User ID: " + user.getId().getId());
        System.out.println("   Username: " + user.getUsername());
        System.out.println("   Email: " + user.getEmail());
        System.out.println("   Role: " + user.getRole());
        System.out.println("========================================");
    }

    @Override
    public String getObserverType() {
        return "AUDIT";
    }
}