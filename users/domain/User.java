package com.example.users_service.domain;

public class User {

    private UserID id;
    private String username;
    private String email;
    private String passwordHash;
    private UserRole role;
    private boolean active;
    private String phoneNumber;

    public User() {
        this.id = new UserID();
    }

    public User(UserID id, String username, String email, String passwordHash, UserRole role, boolean active, String phoneNumber) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
        this.active = active;
        this.phoneNumber = phoneNumber;
    }

    public UserID getId() { return id; }
    public void setId(UserID id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
}