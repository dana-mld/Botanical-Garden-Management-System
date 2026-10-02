package com.example.users_service.controller;

import com.example.users_service.domain.User;
import com.example.users_service.domain.UserRole;
import com.example.users_service.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

   /* @GetMapping
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok(userService.getUsers());
    }*/

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable int id) {
        User user = userService.getUser(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody User user) {
        return userService.insertUser(user)
                ? ResponseEntity.ok().build()
                : ResponseEntity.internalServerError().build();
    }
    @GetMapping
    public ResponseEntity<List<User>> getUsers(
            @RequestParam(required = false) String role) {

        List<User> users = userService.getUsers();

        if (role != null && !role.isEmpty()) {
            try {
                UserRole userRole = UserRole.valueOf(role.toUpperCase());
                users = users.stream()
                        .filter(u -> u.getRole() == userRole)
                        .collect(Collectors.toList());
            } catch (IllegalArgumentException e) {
                System.err.println("Invalid role: " + role);
            }
        }

        return ResponseEntity.ok(users);
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody User user) {
        return userService.updateUser(user)
                ? ResponseEntity.ok().build()
                : ResponseEntity.internalServerError().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        return userService.deleteUser(id)
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }

    @GetMapping("/plants")
    public ResponseEntity<List<Map<String, Object>>> getAllPlants() {
        return ResponseEntity.ok(userService.getAllPlants());
    }

    @GetMapping("/plants/{id}")
    public ResponseEntity<Map<String, Object>> getPlantById(@PathVariable String id) {
        Map<String, Object> plant = userService.getPlantById(id);
        return plant != null ? ResponseEntity.ok(plant) : ResponseEntity.notFound().build();
    }

    @PostMapping("/plants")
    public ResponseEntity<Void> createPlant(@RequestBody Map<String, Object> plant) {
        userService.createPlant(plant);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/plants/{id}")
    public ResponseEntity<Void> updatePlant(@PathVariable String id, @RequestBody Map<String, Object> plant) {
        userService.updatePlant(id, plant);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/plants/{id}")
    public ResponseEntity<Void> deletePlant(@PathVariable String id) {
        userService.deletePlant(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/exemplars")
    public ResponseEntity<List<Map<String, Object>>> getAllExemplars() {
        return ResponseEntity.ok(userService.getAllExemplars());
    }

    @GetMapping("/exemplars/{id}")
    public ResponseEntity<Map<String, Object>> getExemplarById(@PathVariable String id) {
        Map<String, Object> exemplar = userService.getExemplarById(id);
        return exemplar != null ? ResponseEntity.ok(exemplar) : ResponseEntity.notFound().build();
    }

    @GetMapping("/exemplars/plant/{plantId}")
    public ResponseEntity<List<Map<String, Object>>> getExemplarsByPlantId(@PathVariable String plantId) {
        return ResponseEntity.ok(userService.getExemplarsByPlantId(plantId));
    }

    @PostMapping("/exemplars")
    public ResponseEntity<Void> createExemplar(@RequestBody Map<String, Object> exemplar) {
        userService.createExemplar(exemplar);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/exemplars/{id}")
    public ResponseEntity<Void> updateExemplar(@PathVariable String id, @RequestBody Map<String, Object> exemplar) {
        userService.updateExemplar(id, exemplar);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/exemplars/{id}")
    public ResponseEntity<Void> deleteExemplar(@PathVariable String id) {
        userService.deleteExemplar(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/statistics/all")
    public ResponseEntity<Map<String, Object>> getAllStatistics() {
        return ResponseEntity.ok(userService.getAllStatistics());
    }

    @GetMapping("/statistics/distribution")
    public ResponseEntity<Map<String, Object>> getDistributionByType() {
        return ResponseEntity.ok(userService.getDistributionByType());
    }

    @GetMapping("/statistics/carnivorous")
    public ResponseEntity<Map<String, Object>> getCarnivorousStats() {
        return ResponseEntity.ok(userService.getCarnivorousStats());
    }

    @GetMapping("/statistics/top-species")
    public ResponseEntity<Map<String, Object>> getTopSpecies(@RequestParam(defaultValue = "5") int limit) {
        return ResponseEntity.ok(userService.getTopSpecies(limit));
    }

    @PostMapping("/export/{userId}/{format}")
    public ResponseEntity<Void> exportPlants(@PathVariable int userId, @PathVariable String format) {
        userService.exportPlants(userId, format);
        return ResponseEntity.ok().build();
    }
}