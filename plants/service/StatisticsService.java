package com.example.demo.service;

import com.example.demo.domain.IPlantDAO;
import com.example.demo.domain.Planta;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StatisticsService {

    private final IPlantDAO plantDAO;
    private final ChartImageService chartImageService;

    public StatisticsService(IPlantDAO plantDAO, ChartImageService chartImageService) {
        this.plantDAO = plantDAO;
        this.chartImageService = chartImageService;
    }

    public Map<String, Object> getDistributionByType() {
        List<Planta> plants = plantDAO.plants();

        if (plants == null || plants.isEmpty()) {
            return getEmptyResult("No plants in database");
        }

        Map<String, Long> typeCount = plants.stream()
                .filter(p -> p.getTip() != null && !p.getTip().isEmpty())
                .collect(Collectors.groupingBy(
                        Planta::getTip,
                        TreeMap::new,
                        Collectors.counting()
                ));

        Map<String, Object> result = new HashMap<>();
        result.put("labels", typeCount.keySet());
        result.put("data", typeCount.values());
        result.put("total", plants.size());
        result.put("title", "Distribution of plants by type");
        result.put("hasData", !typeCount.isEmpty());

        try {
            String chartBase64 = chartImageService.generateTypeChartBase64(result);
            result.put("chartImage", chartBase64);
        } catch (Exception e) {
            result.put("chartImage", null);
        }

        return result;
    }

    public Map<String, Object> getCarnivorousVsNonCarnivorous() {
        List<Planta> plants = plantDAO.plants();

        if (plants == null || plants.isEmpty()) {
            Map<String, Object> emptyResult = getEmptyResult("No plants in database");
            emptyResult.put("carnivorousPercentage", 0);
            return emptyResult;
        }

        long carnivorous = plants.stream().filter(Planta::isEsteCarnivora).count();
        long nonCarnivorous = plants.size() - carnivorous;

        Map<String, Object> result = new HashMap<>();
        result.put("labels", List.of("Carnivore", "Non-carnivore"));
        result.put("data", List.of(carnivorous, nonCarnivorous));
        result.put("total", plants.size());
        result.put("carnivorousPercentage", plants.size() > 0 ? (carnivorous * 100.0 / plants.size()) : 0);
        result.put("title", "Carnivorous vs Non-carnivorous plants");
        result.put("hasData", true);

        try {
            String chartBase64 = chartImageService.generateCarnivoreChartBase64(result);
            result.put("chartImage", chartBase64);
        } catch (Exception e) {
            System.err.println("Error generating chart: " + e.getMessage());
            result.put("chartImage", null);
        }

        return result;
    }

    public Map<String, Object> getTopSpecies(int limit) {
        List<Planta> plants = plantDAO.plants();

        if (plants == null || plants.isEmpty()) {
            return getEmptyResult("No plants in database");
        }

        Map<String, Long> speciesCount = plants.stream()
                .filter(p -> p.getSpecie() != null && !p.getSpecie().isEmpty())
                .collect(Collectors.groupingBy(
                        Planta::getSpecie,
                        Collectors.counting()
                ));

        List<Map.Entry<String, Long>> sortedSpecies = speciesCount.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(limit)
                .toList();

        Map<String, Object> result = new HashMap<>();
        result.put("labels", sortedSpecies.stream().map(Map.Entry::getKey).toList());
        result.put("data", sortedSpecies.stream().map(Map.Entry::getValue).toList());
        result.put("totalSpecies", speciesCount.size());
        result.put("totalPlants", plants.size());
        result.put("title", "Top " + limit + " species by number of plants");
        result.put("hasData", !sortedSpecies.isEmpty());

        List<Double> percentages = sortedSpecies.stream()
                .map(entry -> (entry.getValue() * 100.0 / plants.size()))
                .toList();
        result.put("percentages", percentages);

        try {
            String chartBase64 = chartImageService.generateTopSpeciesChartBase64(result);
            result.put("chartImage", chartBase64);
        } catch (Exception e) {
            System.err.println("Error generating chart: " + e.getMessage());
            result.put("chartImage", null);
        }

        return result;
    }

    public Map<String, Object> getAllStatistics() {
        Map<String, Object> allStats = new HashMap<>();
        allStats.put("distributionByType", getDistributionByType());
        allStats.put("carnivorousVsNonCarnivorous", getCarnivorousVsNonCarnivorous());
        allStats.put("topSpecies", getTopSpecies(5));
        allStats.put("plantsByTypeAndSpecies", getPlantsByTypeAndSpecies());
        allStats.put("timestamp", System.currentTimeMillis());
        allStats.put("totalPlants", plantDAO.plants() != null ? plantDAO.plants().size() : 0);

        return allStats;
    }

    public Map<String, Object> getPlantsByTypeAndSpecies() {
        List<Planta> plants = plantDAO.plants();

        if (plants == null || plants.isEmpty()) {
            return getEmptyResult("No plants in database");
        }

        Map<String, Map<String, Long>> typeSpeciesMap = new HashMap<>();

        for (Planta plant : plants) {
            String tip = plant.getTip();
            String specie = plant.getSpecie();

            if (tip != null && !tip.isEmpty() && specie != null && !specie.isEmpty()) {
                typeSpeciesMap.computeIfAbsent(tip, k -> new HashMap<>());
                typeSpeciesMap.get(tip).merge(specie, 1L, Long::sum);
            }
        }

        Set<String> allSpecies = plants.stream()
                .map(Planta::getSpecie)
                .filter(specie -> specie != null && !specie.isEmpty())
                .collect(Collectors.toSet());

        Map<String, Object> result = new HashMap<>();
        result.put("types", typeSpeciesMap.keySet());
        result.put("species", allSpecies);
        result.put("data", typeSpeciesMap);
        result.put("title", "Distribution of plants by type and species");
        result.put("hasData", !typeSpeciesMap.isEmpty());

        return result;
    }

    public Map<String, Object> getSimpleTypeDistribution() {
        List<Planta> plants = plantDAO.plants();

        if (plants == null || plants.isEmpty()) {
            return getEmptyResult("No data available");
        }

        Map<String, Long> typeCount = plants.stream()
                .collect(Collectors.groupingBy(
                        p -> p.getTip() != null ? p.getTip() : "Unknown",
                        Collectors.counting()
                ));

        Map<String, Object> result = new HashMap<>();
        result.put("labels", typeCount.keySet());
        result.put("data", typeCount.values());
        result.put("total", plants.size());

        List<String> colors = generateColors(typeCount.size());
        result.put("colors", colors);

        return result;
    }

    private Map<String, Object> getEmptyResult(String message) {
        Map<String, Object> emptyResult = new HashMap<>();
        emptyResult.put("labels", List.of());
        emptyResult.put("data", List.of());
        emptyResult.put("total", 0);
        emptyResult.put("title", message);
        emptyResult.put("hasData", false);
        return emptyResult;
    }

    private List<String> generateColors(int count) {
        List<String> colors = Arrays.asList(
                "#FF6384", "#36A2EB", "#FFCE56", "#4BC0C0", "#9966FF",
                "#FF9F40", "#FF6384", "#C9CBCF", "#7CFC00", "#FF1493",
                "#00CED1", "#FF4500", "#9400D3", "#FFD700", "#32CD32"
        );

        List<String> result = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            result.add(colors.get(i % colors.size()));
        }
        return result;
    }
}