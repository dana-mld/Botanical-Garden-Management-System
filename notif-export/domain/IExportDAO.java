package com.example.notification_export_service.domain;

import java.util.List;

public interface IExportDAO {
    List<ExportJob> exports();
    ExportJob exportById(int id);
    List<ExportJob> findByUserId(int userId);
    boolean insert(ExportJob job);
    boolean update(ExportJob job);
    boolean delete(int id);
}