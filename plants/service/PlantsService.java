package com.example.demo.service;

import com.example.demo.domain.IPlantDAO;
import com.example.demo.domain.Planta;
import com.example.demo.PlantValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PlantsService {

    private final IPlantDAO plantDAO;
    private final RestTemplate restTemplate;

    private final PlantValidator plantValidator;

    private final String EXEMPLAR_SERVICE_URL = "http://localhost:8082/api/exemplars";

    public PlantsService(IPlantDAO plantDAO,
                         RestTemplate restTemplate,
                         PlantValidator plantValidator) {
        this.plantDAO = plantDAO;
        this.restTemplate = restTemplate;
        this.plantValidator = plantValidator;

    }
    public List<Planta> searchAndFilter(String search, String tip, String specie,
                                        Boolean carnivora, String zona) {


        List<Planta> allPlants = plantDAO.plants();

        List<Planta> plantsAfterZone = allPlants;
        if (zona != null && !zona.isEmpty()) {
            Set<Integer> plantIdsInZone = getPlantIdsByZone(zona);

            plantsAfterZone = allPlants.stream()
                    .filter(p -> plantIdsInZone.contains(Integer.valueOf(p.getId().getId())))
                    .collect(Collectors.toList());
            plantsAfterZone.forEach(p -> System.out.println("   - Plant ID: " + p.getId().getId() + ", " + p.getDenumire()));
        }

        List<Planta> plantsAfterSearch = plantsAfterZone.stream()
                .filter(p -> {
                    if (search == null || search.isEmpty()) return true;
                    String searchLower = search.toLowerCase();
                    return (p.getDenumire() != null && p.getDenumire().toLowerCase().contains(searchLower)) ||
                            (p.getSpecie() != null && p.getSpecie().toLowerCase().contains(searchLower));
                })
                .collect(Collectors.toList());

        List<Planta> plantsAfterTip = plantsAfterSearch.stream()
                .filter(p -> {
                    if (tip == null || tip.isEmpty()) return true;
                    return p.getTip() != null && p.getTip().equalsIgnoreCase(tip);
                })
                .collect(Collectors.toList());

        List<Planta> plantsAfterSpecie = plantsAfterTip.stream()
                .filter(p -> {
                    if (specie == null || specie.isEmpty()) return true;
                    return p.getSpecie() != null && p.getSpecie().toLowerCase().contains(specie.toLowerCase());
                })
                .collect(Collectors.toList());

        List<Planta> plantsAfterCarnivora = plantsAfterSpecie.stream()
                .filter(p -> {
                    if (carnivora == null) return true;
                    return p.isEsteCarnivora() == carnivora;
                })
                .collect(Collectors.toList());

        List<Planta> result = plantsAfterCarnivora.stream()
                .sorted(Comparator
                        .comparing(Planta::getTip, Comparator.nullsLast(String::compareToIgnoreCase))
                        .thenComparing(Planta::getSpecie, Comparator.nullsLast(String::compareToIgnoreCase)))
                .collect(Collectors.toList());

        return result;
    }

    private Set<Integer> getPlantIdsByZone(String zona) {
        try {
            String url = EXEMPLAR_SERVICE_URL + "?zona=" + java.net.URLEncoder.encode(zona, "UTF-8");

            ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Map<String, Object>>>() {}
            );

            List<Map<String, Object>> exemplars = response.getBody();

            if (exemplars == null || exemplars.isEmpty()) {
                return new HashSet<>();
            }

            Set<Integer> plantIds = new HashSet<>();
            for (Map<String, Object> ex : exemplars) {
                Object plantIdObj = ex.get("plantId");

                Integer plantId = null;
                if (plantIdObj instanceof Integer) {
                    plantId = (Integer) plantIdObj;
                } else if (plantIdObj instanceof String) {
                    try {
                        plantId = Integer.parseInt((String) plantIdObj);
                    } catch (NumberFormatException e) {}
                } else if (plantIdObj instanceof Map) {
                    Map<?, ?> map = (Map<?, ?>) plantIdObj;
                    Object id = map.get("id");
                    if (id instanceof Integer) {
                        plantId = (Integer) id;
                    }
                }

                if (plantId != null) {
                    plantIds.add(plantId);
                }
            }

            return plantIds;

        } catch (Exception e) {
            e.printStackTrace();
            return new HashSet<>();
        }
    }
    private boolean hasExemplars(int plantId) {
        try {
            ResponseEntity<List> response = restTemplate.getForEntity(
                    EXEMPLAR_SERVICE_URL + "?plantId=" + plantId, List.class
            );
            List body = response.getBody();
            return body != null && !body.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    public List<Planta> getFilteredPlants(String search, String tip, Boolean carnivora) {
        return plantDAO.findFiltered(search, tip, carnivora);
    }

    public boolean deletePlant(int id) {
        if (hasExemplars(id)) {
            return false;
        }
        return plantDAO.delete(id);
    }

    public List<Planta> getPlants() {
        return plantDAO.plants();
    }

    public Planta getPlant(int id) {
        return plantDAO.plantById(id);
    }

    public boolean insertPlant(Planta plant) {
        PlantValidator.ValidationResult result = plantValidator.validate(plant);
        if (!result.isValid()) {
            return false;
        }
        return plantDAO.insert(plant);
    }

    public boolean updatePlant(Planta plant) {
        PlantValidator.ValidationResult result = plantValidator.validate(plant);
        if (!result.isValid()) {
            return false;
        }
        return plantDAO.update(plant);
    }
}