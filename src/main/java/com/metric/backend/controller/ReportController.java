package com.metric.backend.controller;

import com.metric.backend.common.ApiResponse;
import com.metric.backend.dto.ReportRequestDto;
import com.metric.backend.model.Report;
import com.metric.backend.service.ReportService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/reports")
public class ReportController {
    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Long>> createReport(@RequestBody ReportRequestDto dto) {
        try {
            Long reportId = reportService.saveReport(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    new ApiResponse<>("success", "Report saved successfully", reportId)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ApiResponse<>("fail", "Failed to save report: " + e.getMessage(), null)
            );
        }
    }

    @GetMapping
    public ResponseEntity<List<Report>> getAllReports() {
        return ResponseEntity.ok(reportService.getAllReports());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Report> getReportById(@PathVariable Long id) {
        return reportService.getReportById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteReport(@PathVariable Long id) {
        boolean deleted = reportService.deleteReportById(id);
        if (deleted) {
            return ResponseEntity.ok(new ApiResponse<>("success", "Report deleted successfully", "Deleted ID: " + id));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>("fail", "Report not found with ID: " + id, null));
    }

    @DeleteMapping("/device/{deviceId}")
    public ResponseEntity<ApiResponse<String>> deleteDevice(@PathVariable String deviceId) {
        boolean deleted = reportService.deleteByDeviceId(deviceId);
        if (deleted) {
            return ResponseEntity.ok(new ApiResponse<>("success", "Device deleted successfully", "Deleted deviceId: " + deviceId));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>("fail", "Device not found with deviceId: " + deviceId, null));
    }
}
