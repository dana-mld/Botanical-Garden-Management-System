package com.example.notification_export_service.service.state;


import com.example.notification_export_service.domain.ExportJob;
import org.springframework.stereotype.Component;

@Component
public class FailedState implements ExportState {

    @Override
    public void handle(ExportJob job) {
        System.err.println(" Export job " + job.getId() + " FAILED: " + job.getErrorMessage());
        job.setStatus(getStatus());
    }

    @Override
    public String getStatus() {
        return "FAILED";
    }

    @Override
    public String getMessage() {
        return "Exportul a eșuat!";
    }
}