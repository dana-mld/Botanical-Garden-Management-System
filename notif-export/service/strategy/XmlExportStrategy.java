package com.example.notification_export_service.service.strategy;


import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Component
public class XmlExportStrategy implements ExportStrategy {

    private final XmlMapper xmlMapper = new XmlMapper();

    @Override
    public byte[] export(List<Map<String, Object>> plants, int userId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        sb.append("<plante>\n");
        sb.append("  <metadata>\n");
        sb.append("    <exportDate>").append(LocalDateTime.now()).append("</exportDate>\n");
        sb.append("    <totalPlants>").append(plants.size()).append("</totalPlants>\n");
        sb.append("    <userId>").append(userId).append("</userId>\n");
        sb.append("  </metadata>\n");
        sb.append("  <listaPlante>\n");

        for (Map<String, Object> plant : plants) {
            sb.append("    <planta>\n");
            sb.append("      <id>").append(escapeXml(getValueAsString(plant.get("id")))).append("</id>\n");
            sb.append("      <denumire>").append(escapeXml(getValueAsString(plant.get("denumire")))).append("</denumire>\n");
            sb.append("      <specie>").append(escapeXml(getValueAsString(plant.get("specie")))).append("</specie>\n");
            sb.append("      <tip>").append(escapeXml(getValueAsString(plant.get("tip")))).append("</tip>\n");
            sb.append("      <carnivora>").append(getValueAsString(plant.get("carnivora"))).append("</carnivora>\n");
            sb.append("    </planta>\n");
        }

        sb.append("  </listaPlante>\n");
        sb.append("</plante>");

        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public String getFormat() {
        return "XML";
    }

    private String escapeXml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
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