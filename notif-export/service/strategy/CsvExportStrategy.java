package com.example.notification_export_service.service.strategy;


import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Component
public class CsvExportStrategy implements ExportStrategy {

    @Override
    public byte[] export(List<Map<String, Object>> plants, int userId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("\uFEFF");
        sb.append("ID,Denumire,Specie,Tip,Carnivora\n");

        for (Map<String, Object> plant : plants) {
            sb.append(escapeCsv(getValueAsString(plant.get("id")))).append(",")
                    .append(escapeCsv(getValueAsString(plant.get("denumire")))).append(",")
                    .append(escapeCsv(getValueAsString(plant.get("specie")))).append(",")
                    .append(escapeCsv(getValueAsString(plant.get("tip")))).append(",")
                    .append(getValueAsString(plant.get("carnivora"))).append("\n");
        }

        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public String getFormat() {
        return "CSV";
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
            if (map.containsKey("id")) {
                return map.get("id").toString();
            }
            return value.toString();
        }
        return value.toString();
    }
}