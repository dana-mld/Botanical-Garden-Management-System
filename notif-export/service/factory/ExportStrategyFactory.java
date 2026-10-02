package com.example.notification_export_service.service.factory;


import com.example.notification_export_service.service.strategy.*;
import org.springframework.stereotype.Component;

@Component
public class ExportStrategyFactory {

    private final CsvExportStrategy csvStrategy;
    private final JsonExportStrategy jsonStrategy;
    private final XmlExportStrategy xmlStrategy;
    private final DocExportStrategy docStrategy;

    public ExportStrategyFactory(CsvExportStrategy csvStrategy,
                                 JsonExportStrategy jsonStrategy,
                                 XmlExportStrategy xmlStrategy,
                                 DocExportStrategy docStrategy) {
        this.csvStrategy = csvStrategy;
        this.jsonStrategy = jsonStrategy;
        this.xmlStrategy = xmlStrategy;
        this.docStrategy = docStrategy;
    }

    public ExportStrategy getStrategy(String format) {
        return switch (format.toUpperCase()) {
            case "CSV" -> csvStrategy;
            case "JSON" -> jsonStrategy;
            case "XML" -> xmlStrategy;
            case "DOC" -> docStrategy;
            default -> throw new IllegalArgumentException("Format necunoscut: " + format);
        };
    }

    public boolean supportsFormat(String format) {
        return switch (format.toUpperCase()) {
            case "CSV", "JSON", "XML", "DOC" -> true;
            default -> false;
        };
    }
}