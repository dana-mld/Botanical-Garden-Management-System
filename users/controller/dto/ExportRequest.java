package com.example.users_service.controller.dto;

public class ExportRequest {
    private int userId;
    private String format; // JSON, CSV, XML, DOC

    public ExportRequest() {}

    public ExportRequest(int userId, String format) {
        this.userId = userId;
        this.format = format;
    }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getFormat() { return format; }
    public void setFormat(String format) { this.format = format; }
}