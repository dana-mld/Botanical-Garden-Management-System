package com.example.notification_export_service.service.strategy;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class JsonExportStrategy implements ExportStrategy {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public byte[] export(List<Map<String, Object>> plants, int userId) throws Exception {
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(plants);
    }

    @Override
    public String getFormat() {
        return "JSON";
    }
}
