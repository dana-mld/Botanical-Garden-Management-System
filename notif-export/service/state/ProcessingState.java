package com.example.notification_export_service.service.state;


import com.example.notification_export_service.domain.ExportJob;
import org.springframework.stereotype.Component;

@Component
public class ProcessingState implements ExportState {

    @Override
    public void handle(ExportJob job) {
        System.out.println(" Export job " + job.getId() + " is PROCESSING - generating file");
        job.setStatus(getStatus());
    }

    @Override
    public String getStatus() {
        return "PROCESSING";
    }

    @Override
    public String getMessage() {
        return "Se procesează exportul...";
    }
}