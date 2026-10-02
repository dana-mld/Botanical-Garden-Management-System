package com.example.exemplars_service.controller;

import com.example.exemplars_service.domain.Imagine;
import com.example.exemplars_service.service.ImaginiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/imagini")
public class ImagineController {

    private final ImaginiService imagineService;

    private final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads/";

    public ImagineController(ImaginiService imagineService) {
        this.imagineService = imagineService;
        new File(UPLOAD_DIR).mkdirs();
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam("exemplarId") int exemplarId) {

        try {
            String originalFileName = file.getOriginalFilename();
            String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
            String fileName = System.currentTimeMillis() + "_" + UUID.randomUUID().toString().substring(0, 8) + extension;

            Path filePath = Paths.get(UPLOAD_DIR + fileName);
            Files.copy(file.getInputStream(), filePath);

            Imagine imagine = new Imagine();
            imagine.setExemplarId(exemplarId);
            imagine.setCaleFisier(fileName);

            boolean success = imagineService.insertImagine(imagine);

            if (success) {
                return ResponseEntity.ok(fileName);
            } else {
                return ResponseEntity.internalServerError().build();
            }

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Eroare la upload: " + e.getMessage());
        }
    }

    @GetMapping("/view/{fileName}")
    public ResponseEntity<byte[]> viewImage(@PathVariable String fileName) {
        try {
            Path filePath = Paths.get(UPLOAD_DIR + fileName);
            byte[] imageBytes = Files.readAllBytes(filePath);

            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                contentType = "image/jpeg";
            }

            return ResponseEntity.ok()
                    .header("Content-Type", contentType)
                    .body(imageBytes);
        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Imagine>> getImagini(
            @RequestParam(required = false) Integer exemplarId) {
        if (exemplarId != null) {
            return ResponseEntity.ok(imagineService.getImaginiByExemplarId(exemplarId));
        }
        return ResponseEntity.ok(imagineService.getImagini());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Imagine> getImagineById(@PathVariable int id) {
        Imagine imagine = imagineService.getImagine(id);
        if (imagine == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(imagine);
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody Imagine imagine) {
        boolean success = imagineService.insertImagine(imagine);
        return success ? ResponseEntity.ok().build() : ResponseEntity.internalServerError().build();
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody Imagine imagine) {
        boolean success = imagineService.updateImagine(imagine);
        return success ? ResponseEntity.ok().build() : ResponseEntity.internalServerError().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        Imagine imagine = imagineService.getImagine(id);
        if (imagine != null) {
            try {
                Path filePath = Paths.get(UPLOAD_DIR + imagine.getCaleFisier());
                Files.deleteIfExists(filePath);
            } catch (IOException e) {
                System.err.println("Eroare la ștergere fișier: " + e.getMessage());
            }
        }

        boolean success = imagineService.deleteImagine(id);
        return success ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/exemplar/{exemplarId}")
    public ResponseEntity<Void> deleteByExemplar(@PathVariable int exemplarId) {
        List<Imagine> imagini = imagineService.getImaginiByExemplarId(exemplarId);
        for (Imagine img : imagini) {
            try {
                Path filePath = Paths.get(UPLOAD_DIR + img.getCaleFisier());
                Files.deleteIfExists(filePath);
            } catch (IOException e) {
                System.err.println("Eroare la ștergere fișier: " + e.getMessage());
            }
        }

        boolean success = imagineService.deleteImaginiByExemplarId(exemplarId);
        return success ? ResponseEntity.ok().build() : ResponseEntity.internalServerError().build();
    }
}