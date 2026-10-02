package com.example.users_service.service;

import com.example.users_service.controller.dto.NotificationRequest;
import com.example.users_service.controller.dto.ExportRequest;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class CrossService {

    private final RestTemplate restTemplate;

    private final String PLANT_SERVICE_URL = "http://localhost:8081/api/plants";
    private final String STATISTICS_SERVICE_URL = "http://localhost:8081/api/statistics";
    private final String EXEMPLAR_SERVICE_URL = "http://localhost:8082/api/exemplars";
    private final String EXPORT_SERVICE_URL = "http://localhost:8084/api/exports";
    private final String NOTIFICATION_SERVICE_URL = "http://localhost:8084/api/notifications";

    public CrossService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getAllPlants() {
        try {
            return restTemplate.getForEntity(PLANT_SERVICE_URL, List.class).getBody();
        } catch (Exception e) {
            System.err.println("Eroare la preluarea plantelor: " + e.getMessage());
            return List.of();
        }
    }

    public Map<String, Object> getPlantById(String id) {
        try {
            return restTemplate.getForEntity(PLANT_SERVICE_URL + "/" + id, Map.class).getBody();
        } catch (Exception e) {
            System.err.println("Eroare la preluarea plantei: " + e.getMessage());
            return null;
        }
    }

    public void createPlant(Map<String, Object> plant) {
        try {
            restTemplate.postForEntity(PLANT_SERVICE_URL, plant, Void.class);
        } catch (Exception e) {
            System.err.println("Eroare la crearea plantei: " + e.getMessage());
        }
    }

    public void updatePlant(String id, Map<String, Object> plant) {
        try {
            restTemplate.put(PLANT_SERVICE_URL + "/" + id, plant);
        } catch (Exception e) {
            System.err.println("Eroare la actualizarea plantei: " + e.getMessage());
        }
    }

    public void deletePlant(String id) {
        try {
            restTemplate.delete(PLANT_SERVICE_URL + "/" + id);
        } catch (Exception e) {
            System.err.println("Eroare la ștergerea plantei: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getAllExemplars() {
        try {
            return restTemplate.getForEntity(EXEMPLAR_SERVICE_URL, List.class).getBody();
        } catch (Exception e) {
            System.err.println("Eroare la preluarea exemplarelor: " + e.getMessage());
            return List.of();
        }
    }

    public Map<String, Object> getExemplarById(String id) {
        try {
            return restTemplate.getForEntity(EXEMPLAR_SERVICE_URL + "/" + id, Map.class).getBody();
        } catch (Exception e) {
            System.err.println("Eroare la preluarea exemplarului: " + e.getMessage());
            return null;
        }
    }

    public void createExemplar(Map<String, Object> exemplar) {
        try {
            restTemplate.postForEntity(EXEMPLAR_SERVICE_URL, exemplar, Void.class);
        } catch (Exception e) {
            System.err.println("Eroare la crearea exemplarului: " + e.getMessage());
        }
    }

    public void updateExemplar(String id, Map<String, Object> exemplar) {
        try {
            restTemplate.put(EXEMPLAR_SERVICE_URL + "/" + id, exemplar);
        } catch (Exception e) {
            System.err.println("Eroare la actualizarea exemplarului: " + e.getMessage());
        }
    }

    public void deleteExemplar(String id) {
        try {
            restTemplate.delete(EXEMPLAR_SERVICE_URL + "/" + id);
        } catch (Exception e) {
            System.err.println("Eroare la ștergerea exemplarului: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getExemplarsByPlantId(String plantId) {
        try {
            return restTemplate.getForEntity(EXEMPLAR_SERVICE_URL + "?plantId=" + plantId, List.class).getBody();
        } catch (Exception e) {
            System.err.println("Eroare la preluarea exemplarelor pentru planta: " + e.getMessage());
            return List.of();
        }
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getAllStatistics() {
        try {
            return restTemplate.getForEntity(STATISTICS_SERVICE_URL + "/all", Map.class).getBody();
        } catch (Exception e) {
            System.err.println("Eroare la preluarea statisticilor: " + e.getMessage());
            return Map.of();
        }
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getDistributionByType() {
        try {
            return restTemplate.getForEntity(STATISTICS_SERVICE_URL + "/distribution-by-type", Map.class).getBody();
        } catch (Exception e) {
            System.err.println("Eroare la preluarea distribuției pe tipuri: " + e.getMessage());
            return Map.of();
        }
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getCarnivorousStats() {
        try {
            return restTemplate.getForEntity(STATISTICS_SERVICE_URL + "/carnivorous-stats", Map.class).getBody();
        } catch (Exception e) {
            System.err.println("Eroare la preluarea statisticilor carnivore: " + e.getMessage());
            return Map.of();
        }
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getTopSpecies(int limit) {
        try {
            return restTemplate.getForEntity(STATISTICS_SERVICE_URL + "/top-species?limit=" + limit, Map.class).getBody();
        } catch (Exception e) {
            System.err.println("Eroare la preluarea top specii: " + e.getMessage());
            return Map.of();
        }
    }

    public void exportPlants(int userId, String format) {
        try {
            ExportRequest request = new ExportRequest(userId, format);
            restTemplate.postForEntity(EXPORT_SERVICE_URL, request, Void.class);
            System.out.println("Export cerut pentru user " + userId + " în format " + format);
        } catch (Exception e) {
            System.err.println("Eroare la cererea de export: " + e.getMessage());
        }
    }

    public void sendNotification(int userId, String channel, String message) {
        try {
            NotificationRequest request = new NotificationRequest(userId, channel, message);
            restTemplate.postForEntity(NOTIFICATION_SERVICE_URL, request, Void.class);
            System.out.println("Notificare trimisă pentru user " + userId);
        } catch (Exception e) {
            System.err.println("Eroare la trimiterea notificării: " + e.getMessage());
        }
    }
}