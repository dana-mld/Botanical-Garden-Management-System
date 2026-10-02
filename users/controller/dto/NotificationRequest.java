package com.example.users_service.controller.dto;

import java.time.LocalDateTime;

public class NotificationRequest {
    private int userId;
    private String channel; // EMAIL, SMS
    private String message;
    private String status;
    private LocalDateTime createdAt;

    public NotificationRequest() {}

    public NotificationRequest(int userId, String channel, String message) {
        this.userId = userId;
        this.channel = channel;
        this.message = message;
        this.status = "PENDING";
        this.createdAt = LocalDateTime.now();
    }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getChannel() { return channel; }
    public void setChannel(String channel) { this.channel = channel; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }


}

