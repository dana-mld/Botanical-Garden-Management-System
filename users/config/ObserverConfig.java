package com.example.users_service.config;


import com.example.users_service.service.observer.AuditObserver;
import com.example.users_service.service.observer.EmailObserver;
import com.example.users_service.service.observer.MsgObserver;
import com.example.users_service.service.observer.UserSubject;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class ObserverConfig {

    private final UserSubject userSubject;
    private final EmailObserver emailObserver;
    private final MsgObserver smsObserver;
    private final AuditObserver auditObserver;

    public ObserverConfig(UserSubject userSubject,
                          EmailObserver emailObserver,
                          MsgObserver smsObserver,
                          AuditObserver auditObserver) {
        this.userSubject = userSubject;
        this.emailObserver = emailObserver;
        this.smsObserver = smsObserver;
        this.auditObserver = auditObserver;
    }

    @PostConstruct
    public void init() {
        userSubject.attach(emailObserver);
        userSubject.attach(smsObserver);
        userSubject.attach(auditObserver);
        System.out.println(" User observers registered: EMAIL, MSG");
    }
}
