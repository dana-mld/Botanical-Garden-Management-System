package com.example.notification_export_service.service.state;


import com.example.notification_export_service.domain.ExportJob;
import org.springframework.stereotype.Component;

@Component
public class CompletedState implements ExportState {

    @Override
    public void handle(ExportJob job) {
        System.out.println(" Export job " + job.getId() + " is COMPLETED - file ready at: " + job.getFilePath());
        job.setStatus(getStatus());
    }

    @Override
    public String getStatus() {
        return "COMPLETED";
    }

    @Override
    public String getMessage() {
        return "Export finalizat cu succes!";
    }
}