package com.example.notification_export_service.service.state;


import com.example.notification_export_service.domain.ExportJob;
import org.springframework.stereotype.Component;

@Component
public class PendingState implements ExportState {

    @Override
    public void handle(ExportJob job) {
        System.out.println("⏳ Export job " + job.getId() + " is PENDING - waiting to start");
        job.setStatus(getStatus());
    }

    @Override
    public String getStatus() {
        return "PENDING";
    }

    @Override
    public String getMessage() {
        return "Exportul este în așteptare...";
    }
}