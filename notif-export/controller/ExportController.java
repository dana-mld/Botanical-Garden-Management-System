package com.example.notification_export_service.controller;

import com.example.notification_export_service.domain.ExportJob;
import com.example.notification_export_service.service.ExportService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.ByteArrayResource;

import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/exports")
public class ExportController {

    private final ExportService service;

    public ExportController(ExportService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ExportJob>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ExportJob>> getUserExports(@PathVariable int userId) {
        return ResponseEntity.ok(service.getUserExports(userId));
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody ExportJob job) {
        service.createExport(job);

        return ResponseEntity.ok().build();
    }
    @PostMapping("/statistics")
    public ResponseEntity<ByteArrayResource> exportStatistics(
            @RequestParam(defaultValue = "DOCX") String format) {

        System.out.println("Export statistics, format: " + format);

        try {
            byte[] fileContent = service.exportStatisticsToWord(0, format);
            String fileName = "statistics_report." + format.toLowerCase();

            MediaType mediaType = switch (format.toUpperCase()) {
                case "DOC", "DOCX" -> new MediaType("application", "vnd.openxmlformats-officedocument.wordprocessingml.document");
                default -> MediaType.APPLICATION_OCTET_STREAM;
            };

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .contentType(mediaType)
                    .contentLength(fileContent.length)
                    .body(new ByteArrayResource(fileContent));

        } catch (Exception e) {
            System.err.println("Error exporting statistics: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
    @PostMapping("/users-csv")
    public ResponseEntity<byte[]> exportUsersToCSV(
            @RequestBody(required = false) Map<String, String> filter,
            @RequestHeader("Authorization") String token) {

        try {
            String roleFilter = filter != null ? filter.get("roleFilter") : null;
            byte[] csvBytes = service.exportUsersToCSV(roleFilter, token);

            String fileName = "users_export_" + System.currentTimeMillis() + ".csv";

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .contentType(MediaType.parseMediaType("text/csv"))
                    .body(csvBytes);

        } catch (Exception e) {
            System.err.println("Export error: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
    @GetMapping("/download/{jobId}")
    public ResponseEntity<ByteArrayResource> downloadExport(@PathVariable String jobId) {
        try {
            Integer id = Integer.parseInt(jobId);
            List<ExportJob> jobs = service.getAll();
            ExportJob job = jobs.stream()
                    .filter(j -> j.getId().equals(jobId))
                    .findFirst()
                    .orElse(null);

            if (job == null || job.getFilePath() == null) {
                return ResponseEntity.notFound().build();
            }

            byte[] fileContent = service.downloadExportFile(job.getFilePath());
            String fileName = Paths.get(job.getFilePath()).getFileName().toString();

            MediaType mediaType = switch (job.getFormat().toUpperCase()) {
                case "JSON" -> MediaType.APPLICATION_JSON;
                case "CSV" -> new MediaType("text", "csv");
                case "XML" -> MediaType.APPLICATION_XML;
                case "DOC" -> new MediaType("application", "vnd.openxmlformats-officedocument.wordprocessingml.document");
                default -> MediaType.APPLICATION_OCTET_STREAM;
            };

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .contentType(mediaType)
                    .contentLength(fileContent.length)
                    .body(new ByteArrayResource(fileContent));

        } catch (NumberFormatException e) {
            System.err.println("ID invalid: " + jobId);
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            System.err.println("Eroare la descărcare: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}