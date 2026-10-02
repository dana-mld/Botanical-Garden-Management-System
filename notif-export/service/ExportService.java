package com.example.notification_export_service.service;

import com.example.notification_export_service.domain.*;
import com.example.notification_export_service.service.state.ExportStateContext;
import com.example.notification_export_service.service.strategy.ExportContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class ExportService {

    private final IExportDAO dao;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final XmlMapper xmlMapper;
    private final ExportContext exportContext;
    private final ExportStateContext exportStateContext;

    @Value("${plants.service.url:http://localhost:8081}")
    private String plantsServiceUrl;

    @Value("${users.service.url:http://localhost:8083}")
    private String usersServiceUrl;

    private final String EXPORT_DIR = System.getProperty("user.dir") + "/exports/";

    public ExportService(IExportDAO dao,
                         RestTemplate restTemplate,
                         ExportContext exportContext,
                         ExportStateContext exportStateContext) {
        this.dao = dao;
        this.restTemplate = restTemplate;
        this.exportContext = exportContext;
        this.exportStateContext = exportStateContext;
        this.objectMapper = new ObjectMapper();
        this.xmlMapper = new XmlMapper();
        createExportDirectory();
    }

    private void createExportDirectory() {
        try {
            Files.createDirectories(Paths.get(EXPORT_DIR));
            System.out.println("Export directory created: " + EXPORT_DIR);
        } catch (IOException e) {
            System.err.println("Could not create export directory: " + e.getMessage());
        }
    }

    public List<ExportJob> getAll() {
        return dao.exports();
    }

    public List<ExportJob> getUserExports(int userId) {
        return dao.findByUserId(userId);
    }

    public boolean createExport(ExportJob job) {
        try {
            exportStateContext.transitionTo(job, "PENDING");
            job.setStartTime(LocalDateTime.now());
            dao.insert(job);

            exportStateContext.transitionTo(job, "PROCESSING");

            List<Map<String, Object>> plants = fetchPlantsFromService();

            if (plants == null || plants.isEmpty()) {
                System.err.println("No plants found for export");
                exportStateContext.transitionTo(job, "FAILED");
                dao.update(job);
                return false;
            }

            String filePath = generateExportFile(plants, job.getUserId(), job.getFormat());

            if (filePath != null) {
                job.setFilePath(filePath);
                job.setCompletedTime(LocalDateTime.now());
                exportStateContext.transitionTo(job, "COMPLETED");
                dao.update(job);
                System.out.println(" Export completed: " + filePath);
                return true;
            } else {
                System.err.println("Failed to generate file");
                updateJobStatus(job, "FAILED", "Could not generate file");
                return false;
            }
        } catch (Exception e) {
            System.err.println("Export failed: " + e.getMessage());
            e.printStackTrace();
            updateJobStatus(job, "FAILED", e.getMessage());
            return false;
        }
    }

    private String generateExportFile(List<Map<String, Object>> plants, int userId, String format) {
        try {
            byte[] fileContent = exportContext.executeExport(format, plants, userId);

            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String fileName = "export_user_" + userId + "_" + timestamp + "." + format.toLowerCase();
            Path filePath = Paths.get(EXPORT_DIR + fileName);
            Files.write(filePath, fileContent);

            System.out.println("Export " + format + " generated: " + filePath.toAbsolutePath());
            return filePath.toAbsolutePath().toString();

        } catch (Exception e) {
            System.err.println("Error generating file: " + e.getMessage());
            return null;
        }
    }

    public byte[] exportStatisticsToWord(int userId, String format) throws Exception {
        System.out.println("User ID: " + userId);
        System.out.println("Format: " + format);

        Map<String, Object> statistics = fetchStatisticsFromService();

        if (statistics == null || statistics.isEmpty()) {
            throw new RuntimeException("No statistical data available");
        }

        byte[] wordContent = generateStatisticsWordReportWithImages(statistics, userId);

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String fileName = "statistics_user_" + userId + "_" + timestamp + ".docx";
        Path filePath = Paths.get(EXPORT_DIR + fileName);
        Files.write(filePath, wordContent);

        System.out.println("Statistics report saved: " + filePath.toAbsolutePath());

        return wordContent;
    }

    private Map<String, Object> fetchStatisticsFromService() {
        try {
            String url = plantsServiceUrl + "/api/statistics/all";
            System.out.println("Fetching statistics from: " + url);
            var response = restTemplate.getForEntity(url, Map.class);
            return response.getBody();
        } catch (Exception e) {
            System.err.println("Error fetching statistics: " + e.getMessage());
            throw new RuntimeException("Could not fetch statistics", e);
        }
    }

    private List<Map<String, Object>> fetchPlantsFromService() {
        try {
            String url = plantsServiceUrl + "/api/plants";
            System.out.println("Fetching plants from: " + url);
            var response = restTemplate.getForEntity(url, List.class);
            return response.getBody();
        } catch (Exception e) {
            System.err.println("Error fetching plants: " + e.getMessage());
            throw new RuntimeException("Could not fetch plants", e);
        }
    }

    @SuppressWarnings("unchecked")
    private byte[] generateStatisticsWordReportWithImages(Map<String, Object> statistics, int userId) throws Exception {
        try (XWPFDocument document = new XWPFDocument()) {

            document.getDocument().getBody().addNewSectPr().addNewPgMar();

            XWPFParagraph title = document.createParagraph();
            title.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun titleRun = title.createRun();
            titleRun.setText(" STATISTICS REPORT - BOTANICAL GARDEN");
            titleRun.setBold(true);
            titleRun.setFontSize(24);
            titleRun.addBreak();
            titleRun.addBreak();

            XWPFParagraph metadata = document.createParagraph();
            XWPFRun metadataRun = metadata.createRun();
            metadataRun.setText("Generation date: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")));
            metadataRun.addBreak();
            metadataRun.setText("Total registered plants: " + (statistics.get("totalPlants") != null ? statistics.get("totalPlants") : 0));
            metadataRun.addBreak();
            metadataRun.addBreak();

            Map<String, Object> distribution = (Map<String, Object>) statistics.get("distributionByType");
            if (distribution != null && distribution.get("chartImage") != null) {
                XWPFParagraph section1 = document.createParagraph();
                XWPFRun run1 = section1.createRun();
                run1.setText("1. " + distribution.get("title"));
                run1.setBold(true);
                run1.setFontSize(16);
                run1.addBreak();
                run1.addBreak();

                byte[] imageBytes = Base64.getDecoder().decode((String) distribution.get("chartImage"));
                run1.addPicture(new ByteArrayInputStream(imageBytes), XWPFDocument.PICTURE_TYPE_PNG, "chart1.png",
                        Units.toEMU(500), Units.toEMU(350));
                run1.addBreak();
                run1.addBreak();
                System.out.println(" Chart 1 added");
            }

            Map<String, Object> carnivore = (Map<String, Object>) statistics.get("carnivorousVsNonCarnivorous");
            if (carnivore != null && carnivore.get("chartImage") != null) {
                XWPFParagraph section2 = document.createParagraph();
                XWPFRun run2 = section2.createRun();
                run2.setText("2. " + carnivore.get("title"));
                run2.setBold(true);
                run2.setFontSize(16);
                run2.addBreak();
                run2.addBreak();

                byte[] imageBytes = Base64.getDecoder().decode((String) carnivore.get("chartImage"));
                run2.addPicture(new ByteArrayInputStream(imageBytes), XWPFDocument.PICTURE_TYPE_PNG, "chart2.png",
                        Units.toEMU(450), Units.toEMU(350));
                run2.addBreak();
                run2.addBreak();
                System.out.println(" Chart 2 added");
            }

            Map<String, Object> topSpecies = (Map<String, Object>) statistics.get("topSpecies");
            if (topSpecies != null && topSpecies.get("chartImage") != null) {
                XWPFParagraph section3 = document.createParagraph();
                XWPFRun run3 = section3.createRun();
                run3.setText("3. " + topSpecies.get("title"));
                run3.setBold(true);
                run3.setFontSize(16);
                run3.addBreak();
                run3.addBreak();

                byte[] imageBytes = Base64.getDecoder().decode((String) topSpecies.get("chartImage"));
                run3.addPicture(new ByteArrayInputStream(imageBytes), XWPFDocument.PICTURE_TYPE_PNG, "chart3.png",
                        Units.toEMU(500), Units.toEMU(350));
                run3.addBreak();
                run3.addBreak();
                System.out.println(" Chart 3 added");
            }

            addStatisticsTables(document, statistics);

            XWPFParagraph footer = document.createParagraph();
            footer.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun footerRun = footer.createRun();
            footerRun.setText("Report automatically generated by Plant Management System");
            footerRun.setFontSize(10);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            document.write(out);

            return out.toByteArray();
        }
    }

    @SuppressWarnings("unchecked")
    private void addStatisticsTables(XWPFDocument document, Map<String, Object> statistics) {
        Map<String, Object> distribution = (Map<String, Object>) statistics.get("distributionByType");
        if (distribution != null && distribution.containsKey("labels")) {
            List<String> labels = new ArrayList<>((Collection<String>) distribution.get("labels"));
            List<Integer> data = new ArrayList<>((Collection<Integer>) distribution.get("data"));

            if (!labels.isEmpty()) {
                XWPFParagraph tableTitle = document.createParagraph();
                XWPFRun tableRun = tableTitle.createRun();
                tableRun.setText("Statistical data - " + distribution.get("title"));
                tableRun.setBold(true);
                tableRun.setFontSize(14);
                tableRun.addBreak();

                XWPFTable table = document.createTable(labels.size() + 1, 2);
                table.setWidth("100%");
                table.getRow(0).getCell(0).setText("Plant type");
                table.getRow(0).getCell(1).setText("Number of plants");

                for (int i = 0; i < labels.size(); i++) {
                    table.getRow(i + 1).getCell(0).setText(labels.get(i));
                    table.getRow(i + 1).getCell(1).setText(String.valueOf(data.get(i)));
                }
            }
        }
    }

    private void updateJobStatus(ExportJob job, String status, String errorMessage) {
        job.setStatus(status);
        job.setErrorMessage(errorMessage);
        job.setCompletedTime(LocalDateTime.now());
        dao.update(job);
    }

    public byte[] exportUsersToCSV(String roleFilter, String authToken) {
        try {
            String url = usersServiceUrl + "/api/users" +
                    (roleFilter != null && !roleFilter.isEmpty() ? "?role=" + roleFilter : "");

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", authToken);
            HttpEntity<?> entity = new HttpEntity<>(headers);

            ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity,
                    new ParameterizedTypeReference<List<Map<String, Object>>>() {}
            );

            List<Map<String, Object>> users = response.getBody();

            if (users == null || users.isEmpty()) {
                throw new RuntimeException("No users to export");
            }

            StringBuilder sb = new StringBuilder();
            sb.append("\uFEFF");
            sb.append("ID,Username,Email,Role,Active,Phone Number\n");

            for (Map<String, Object> user : users) {
                sb.append(getValueAsString(user.get("id"))).append(",")
                        .append(escapeCsv(getValueAsString(user.get("username")))).append(",")
                        .append(escapeCsv(getValueAsString(user.get("email")))).append(",")
                        .append(getValueAsString(user.get("role"))).append(",")
                        .append(getActiveStatus(user.get("active"))).append(",")
                        .append(escapeCsv(getValueAsString(user.get("phoneNumber")))).append("\n");
            }

            return sb.toString().getBytes(StandardCharsets.UTF_8);

        } catch (Exception e) {
            System.err.println("Export error: " + e.getMessage());
            throw new RuntimeException("Failed to export users", e);
        }
    }

    private String escapeCsv(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    private String getValueAsString(Object value) {
        if (value == null) return "";
        if (value instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) value;
            return map.containsKey("id") ? String.valueOf(map.get("id")) : "";
        }
        return String.valueOf(value);
    }

    private String getActiveStatus(Object active) {
        if (active == null) return "No";
        if (active instanceof Boolean) return (Boolean) active ? "Yes" : "No";
        return "No";
    }

    public byte[] downloadExportFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            throw new IOException("File does not exist: " + filePath);
        }
        return Files.readAllBytes(path);
    }
}