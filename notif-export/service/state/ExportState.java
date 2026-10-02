package com.example.notification_export_service.service.state;


import com.example.notification_export_service.domain.ExportJob;

public interface ExportState {
    void handle(ExportJob job);
    String getStatus();
    String getMessage();
}
