package com.example.notification_export_service.infrastracture.tableEntities;

import com.example.notification_export_service.domain.ExportJob;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "export_jobs")
public class ExportEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "format", nullable = false, length = 10)
    private String format;

    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @Column(name = "file_path", length = 500)
    private String filePath;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "completed_time")
    private LocalDateTime completedTime;

    public ExportEntity() {}

    public ExportJob toExportJob() {
        ExportJob job = new ExportJob();
        job.setId(this.id.toString());
        job.setUserId(this.userId);
        job.setFormat(this.format);
        job.setStatus(this.status);
        job.setFilePath(this.filePath);
        job.setErrorMessage(this.errorMessage);
        job.setStartTime(this.startTime);
        job.setCompletedTime(this.completedTime);
        return job;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public String getFormat() { return format; }
    public void setFormat(String format) { this.format = format; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public LocalDateTime getCompletedTime() { return completedTime; }
    public void setCompletedTime(LocalDateTime completedTime) { this.completedTime = completedTime; }
}