package com.example.users_service.service.observer;


import com.example.users_service.domain.User;
import com.example.users_service.service.CrossService;
import org.springframework.stereotype.Component;

@Component
public class MsgObserver implements UserObserver {

    private final CrossService crossService;

    public MsgObserver(CrossService crossService) {
        this.crossService = crossService;
    }

    @Override
    public void onUserChanged(User user, String action) {
        String message = "Contul dumneavoastră a fost " +
                (action.equals("UPDATE") ? "actualizat" : "șters");

        crossService.sendNotification(user.getId().getId(), "WhatsApp", message);
        System.out.println(" msg notification sent to: " + user.getPhoneNumber());
    }

    @Override
    public String getObserverType() {
        return "MSG";
    }
}
