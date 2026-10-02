package com.example.notification_export_service.service.strategy;

import com.example.notification_export_service.service.factory.ExportStrategyFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ExportContext {

    private final ExportStrategyFactory factory;

    public ExportContext(ExportStrategyFactory factory) {
        this.factory = factory;
    }

    public byte[] executeExport(String format, List<Map<String, Object>> plants, int userId) throws Exception {
        ExportStrategy strategy = factory.getStrategy(format);
        System.out.println("c Using strategy: " + strategy.getClass().getSimpleName());
        return strategy.export(plants, userId);
    }

    public boolean hasStrategy(String format) {
        return factory.supportsFormat(format);
    }
}