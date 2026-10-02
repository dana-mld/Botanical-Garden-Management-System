package com.example.notification_export_service.service.strategy;


import org.apache.poi.xwpf.usermodel.*;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Component
public class DocExportStrategy implements ExportStrategy {

    @Override
    public byte[] export(List<Map<String, Object>> plants, int userId) throws Exception {
        try (XWPFDocument document = new XWPFDocument()) {

            XWPFParagraph title = document.createParagraph();
            title.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun titleRun = title.createRun();
            titleRun.setText("Export Plante - Raport Complet");
            titleRun.setBold(true);
            titleRun.setFontSize(20);
            titleRun.addBreak();

            XWPFParagraph metadata = document.createParagraph();
            XWPFRun metadataRun = metadata.createRun();
            metadataRun.setText("Data export: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")));
            metadataRun.addBreak();
            metadataRun.setText("Utilizator ID: " + userId);
            metadataRun.addBreak();
            metadataRun.setText("Total plante: " + plants.size());
            metadataRun.addBreak();
            metadataRun.addBreak();

            XWPFTable table = document.createTable(plants.size() + 1, 5);
            table.setWidth("100%");

            String[] headers = {"ID", "Denumire", "Specie", "Tip", "Carnivora"};
            XWPFTableRow headerRow = table.getRow(0);
            for (int i = 0; i < headers.length; i++) {
                XWPFParagraph cellPara = headerRow.getCell(i).addParagraph();
                XWPFRun run = cellPara.createRun();
                run.setText(headers[i]);
                run.setBold(true);
                run.setFontSize(12);
                headerRow.getCell(i).setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
            }

            for (int i = 0; i < plants.size(); i++) {
                Map<String, Object> plant = plants.get(i);
                XWPFTableRow row = table.getRow(i + 1);

                row.getCell(0).setText(getValueAsString(plant.get("id")));
                row.getCell(1).setText(getValueAsString(plant.get("denumire")));
                row.getCell(2).setText(getValueAsString(plant.get("specie")));
                row.getCell(3).setText(getValueAsString(plant.get("tip")));
                row.getCell(4).setText(getValueAsString(plant.get("carnivora")));

                if (i % 2 == 0) {
                    for (int j = 0; j < 5; j++) {
                        row.getCell(j).setColor("F2F2F2");
                    }
                }
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            document.write(out);
            return out.toByteArray();
        }
    }

    @Override
    public String getFormat() {
        return "DOC";
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
