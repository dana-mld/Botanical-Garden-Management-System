package com.example.demo.controller;

import com.example.demo.domain.Planta;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.service.PlantsService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/plants")
public class PlantController {

    private final PlantsService plantService;

    public PlantController(PlantsService plantService) {
        this.plantService = plantService;
    }

    @GetMapping
    public ResponseEntity<List<Planta>> getPlants(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String tip,
            @RequestParam(required = false) String specie,
            @RequestParam(required = false) Boolean carnivora,
            @RequestParam(required = false) String zona) {
        List<Planta> plants = plantService.searchAndFilter(search, tip, specie, carnivora, zona);
        return ResponseEntity.ok(plants);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Planta> getPlantById(@PathVariable int id) {
        Planta planta = plantService.getPlant(id);
        if (planta == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(planta);
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody Planta plant) {


        boolean success = plantService.insertPlant(plant);
        return success ? ResponseEntity.ok().build() : ResponseEntity.internalServerError().build();
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody Planta plant) {
        boolean success = plantService.updatePlant(plant);
        return success ? ResponseEntity.ok().build() : ResponseEntity.internalServerError().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        boolean success = plantService.deletePlant(id);
        return success ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}