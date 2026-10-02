package com.example.users_service.service.observer;


import com.example.users_service.domain.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserSubject {

    private final List<UserObserver> observers = new ArrayList<>();

    public void attach(UserObserver observer) {
        observers.add(observer);
        System.out.println(" Observer attached: " + observer.getObserverType());
    }

    public void detach(UserObserver observer) {
        observers.remove(observer);
        System.out.println("️ Observer detached: " + observer.getObserverType());
    }

    public void notifyObservers(User user, String action) {
        for (UserObserver observer : observers) {
            try {
                observer.onUserChanged(user, action);
            } catch (Exception e) {
                System.err.println("Error notifying observer " + observer.getObserverType() + ": " + e.getMessage());
            }
        }
    }
}