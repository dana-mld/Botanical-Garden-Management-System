package com.example.notification_export_service.domain;

public class UserInfo {
    private UserID id;
    private String username;
    private String email;
    private String phoneNumber;
    private String role;
    private boolean active;

    public UserInfo() {}

    public UserID getId() { return id; }
    public void setId(UserID id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public static class UserID {
        private int id;

        public UserID() {}

        public UserID(int id) { this.id = id; }

        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
    }
}