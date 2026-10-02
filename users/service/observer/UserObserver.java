package com.example.users_service.service.observer;


import com.example.users_service.domain.User;

public interface UserObserver {
    void onUserChanged(User user, String action);
    String getObserverType();
}