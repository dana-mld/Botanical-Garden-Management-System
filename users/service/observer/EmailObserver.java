package com.example.users_service.service.observer;


import com.example.users_service.domain.User;
import com.example.users_service.service.CrossService;
import org.springframework.stereotype.Component;

@Component
public class EmailObserver implements UserObserver {

    private final CrossService crossService;

    public EmailObserver(CrossService crossService) {
        this.crossService = crossService;
    }

    @Override
    public void onUserChanged(User user, String action) {
        String message = "Contul dumneavoastră a fost " +
                (action.equals("UPDATE") ? "actualizat" : "șters") +
                ". Detalii: username=" + user.getUsername();

        crossService.sendNotification(user.getId().getId(), "EMAIL", message);
        System.out.println("Email notification sent to: " + user.getEmail());
    }

    @Override
    public String getObserverType() {
        return "EMAIL";
    }
}