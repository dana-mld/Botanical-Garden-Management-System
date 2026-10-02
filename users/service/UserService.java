package com.example.users_service.service;

import com.example.users_service.domain.IUserDAO;
import com.example.users_service.domain.User;
import com.example.users_service.service.observer.UserSubject;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserService {
    private final PasswordEncoder passwordEncoder;

    private final IUserDAO userDAO;
    private final CrossService crossService;
    private final UserSubject userSubject;

    public UserService(PasswordEncoder passwordEncoder, IUserDAO userDAO, CrossService crossService, UserSubject userSubject) {
        this.passwordEncoder = passwordEncoder;
        this.userDAO = userDAO;
        this.crossService = crossService;
        this.userSubject = userSubject;
    }

    public List<User> getUsers() {
        return userDAO.users();
    }

    public User getUser(int id) {
        return userDAO.userById(id);
    }

    public boolean insertUser(User user) {
        System.out.println("Original password: " + user.getPasswordHash());

        if (user.getPasswordHash() != null && !user.getPasswordHash().isEmpty()) {
            String hashed = passwordEncoder.encode(user.getPasswordHash());
            System.out.println("Hashed password: " + hashed);
            user.setPasswordHash(hashed);
        }

        System.out.println("Final hash to save: " + user.getPasswordHash());
        return userDAO.insert(user);
    }


    public boolean updateUser(User user) {
        String passwordHash = user.getPasswordHash();
        if (passwordHash != null && !passwordHash.isEmpty() && !passwordHash.startsWith("$2a$")) {
            user.setPasswordHash(passwordEncoder.encode(passwordHash));
        }

        boolean success = userDAO.update(user);
        if (success) {
            User fullUser = userDAO.userById(user.getId().getId());
            userSubject.notifyObservers(fullUser, "UPDATE"); 
        }
        return success;
    }

    public boolean deleteUser(int id) {
        User user = userDAO.userById(id);
        boolean success = userDAO.delete(id);
        if (success && user != null) {
            userSubject.notifyObservers(user, "DELETE");
        }
        return success;
    }

    public User findByUsername(String username) {
        return userDAO.findByUsername(username);
    }



    public List<Map<String, Object>> getAllPlants() {
        return crossService.getAllPlants();
    }

    public Map<String, Object> getPlantById(String id) {
        return crossService.getPlantById(id);
    }

    public void createPlant(Map<String, Object> plant) {
        crossService.createPlant(plant);
    }

    public void updatePlant(String id, Map<String, Object> plant) {
        crossService.updatePlant(id, plant);
    }

    public void deletePlant(String id) {
        crossService.deletePlant(id);
    }

    public List<Map<String, Object>> getAllExemplars() {
        return crossService.getAllExemplars();
    }

    public Map<String, Object> getExemplarById(String id) {
        return crossService.getExemplarById(id);
    }

    public void createExemplar(Map<String, Object> exemplar) {
        crossService.createExemplar(exemplar);
    }

    public void updateExemplar(String id, Map<String, Object> exemplar) {
        crossService.updateExemplar(id, exemplar);
    }

    public void deleteExemplar(String id) {
        crossService.deleteExemplar(id);
    }

    public List<Map<String, Object>> getExemplarsByPlantId(String plantId) {
        return crossService.getExemplarsByPlantId(plantId);
    }

    public Map<String, Object> getAllStatistics() {
        return crossService.getAllStatistics();
    }

    public Map<String, Object> getDistributionByType() {
        return crossService.getDistributionByType();
    }

    public Map<String, Object> getCarnivorousStats() {
        return crossService.getCarnivorousStats();
    }

    public Map<String, Object> getTopSpecies(int limit) {
        return crossService.getTopSpecies(limit);
    }

    public void exportPlants(int userId, String format) {
        crossService.exportPlants(userId, format);
    }
}