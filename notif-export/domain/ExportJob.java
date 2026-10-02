package com.example.notification_export_service.domain;

import java.time.LocalDateTime;

public class ExportJob {
    private String id;
    private int userId;
    private String format; // JSON, CSV, XML, DOC
    private String status; // PENDING, COMPLETED, FAILED
    private String filePath;
    private String errorMessage;
    private LocalDateTime startTime;
    private LocalDateTime completedTime;

    public ExportJob() {
        this.status = "PENDING";
        this.startTime = LocalDateTime.now();
    }

    public ExportJob(int userId, String format) {
        this();
        this.userId = userId;
        this.format = format;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getFormat() { return format; }
    public void setFormat(String format) { this.format = format; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public LocalDateTime getCompletedTime() { return completedTime; }
    public void setCompletedTime(LocalDateTime completedTime) { this.completedTime = completedTime; }
}