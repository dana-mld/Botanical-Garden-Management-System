package com.example.notification_export_service.service.strategy;


import java.util.List;
import java.util.Map;

public interface ExportStrategy {
    byte[] export(List<Map<String, Object>> plants, int userId) throws Exception;
    String getFormat();
}