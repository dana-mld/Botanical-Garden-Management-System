package com.example.exemplars_service.service;


import com.example.exemplars_service.domain.Exemplar;
import com.example.exemplars_service.domain.IExemplarDAO;
import org.jspecify.annotations.Nullable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

@Service
public class ExemplarService {

    private final String PLANT_SERVICE_URL = "http://localhost:8081/api/plants";
    private final IExemplarDAO exemplarDAO;
    private final RestTemplate restTemplate;
    public ExemplarService(IExemplarDAO exemplarDAO, RestTemplate restTemplate) {
        this.exemplarDAO = exemplarDAO;
        this.restTemplate = restTemplate;
    }

    private boolean plantExists(Integer plantId) {
        try {
            ResponseEntity<Object> response = restTemplate.getForEntity(
                    PLANT_SERVICE_URL + "/" + plantId, Object.class
            );
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean insertExemplar(Exemplar exemplar) {
        if (!plantExists(exemplar.getPlantId())) {
            System.err.println("Planta cu ID-ul " + exemplar.getPlantId() + " nu există în sistem!");
            return false;
        }
        return exemplarDAO.insert(exemplar);
    }
    public List<Exemplar> getExemplarsByZone(String zona) {
        return exemplarDAO.findByZone(zona);
    }
    public List<Exemplar> getExemplars() {
        return exemplarDAO.exemplars();
    }

    public Exemplar getExemplar(int id) {
        return exemplarDAO.exemplarById(id);
    }


    public boolean updateExemplar(Exemplar exemplar) {
        return exemplarDAO.update(exemplar);
    }

    public boolean deleteExemplar(int id) {
        return exemplarDAO.delete(id);
    }

    public List<Exemplar> getExemplarsByPlantId(Integer plantId) {
        return exemplarDAO.exemplarsByPlantId(plantId);
    }
}