package com.example.exemplars_service.controller;


import com.example.exemplars_service.domain.Exemplar;
import com.example.exemplars_service.service.ExemplarService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exemplars")
public class ExemplarController {

    private final ExemplarService exemplarService;

    public ExemplarController(ExemplarService exemplarService) {
        this.exemplarService = exemplarService;
    }


    @GetMapping
    public ResponseEntity<List<Exemplar>> getExemplars(
            @RequestParam(required = false) Integer plantId,
            @RequestParam(required = false) String zona) {

        if (plantId != null) {
            return ResponseEntity.ok(exemplarService.getExemplarsByPlantId(plantId));
        }
        if (zona != null && !zona.isEmpty()) {
            return ResponseEntity.ok(exemplarService.getExemplarsByZone(zona));
        }
        return ResponseEntity.ok(exemplarService.getExemplars());
    }
    @GetMapping("/{id}")
    public ResponseEntity<Exemplar> getExemplarById(@PathVariable Integer id) {
        Exemplar exemplar = exemplarService.getExemplar(id);

        if (exemplar == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(exemplar);
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody Exemplar exemplar) {
        System.out.println("=== Received exemplar ===");
        System.out.println("PlantId: " + exemplar.getPlantId());
        System.out.println("ZonaGradina: " + exemplar.getZonaGradina());
        System.out.println("ID: " + (exemplar.getId() != null ? exemplar.getId().getId() : "null"));

        boolean success = exemplarService.insertExemplar(exemplar);
        return success ? ResponseEntity.ok().build() : ResponseEntity.internalServerError().build();
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody Exemplar exemplar) {
        boolean success = exemplarService.updateExemplar(exemplar);

        return success
                ? ResponseEntity.ok().build()
                : ResponseEntity.internalServerError().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        boolean success = exemplarService.deleteExemplar(id);

        return success
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }

}