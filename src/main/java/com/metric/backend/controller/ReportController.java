package com.metric.backend.controller;


import com.metric.backend.common.ApiRes;
import com.metric.backend.dto.MetricPointDto;
import com.metric.backend.dto.ReportDto;
import com.metric.backend.dto.ReportRequestDto;
import com.metric.backend.model.Report;
import com.metric.backend.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/reports")
@Tag(name = "Reports API", description = "Endpoints for managing reports and metrics")
public class ReportController {
    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @Operation(summary = "Create a new report", description = "Saves a new report based on the provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Report created successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<ApiRes<Long>> createReport(@RequestBody ReportRequestDto dto) {
        try {
            Long reportId = reportService.saveReport(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    new ApiRes<>("success", "Report saved successfully", reportId)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ApiRes<>("fail", "Failed to save report: " + e.getMessage(), null)
            );
        }
    }

    @Operation(summary = "Get all reports", description = "Fetches all reports stored in the system")
    @GetMapping
    public ResponseEntity<List<Report>> getAllReports() {
        return ResponseEntity.ok(reportService.getAllReports());
    }

    @Operation(summary = "Get report by ID", description = "Fetches a report based on its unique ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Report found"),
            @ApiResponse(responseCode = "404", description = "Report not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Report> getReportById(@PathVariable Long id) {
        return reportService.getReportById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete report by ID", description = "Deletes a report using its unique ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Report deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Report not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiRes<String>> deleteReport(@PathVariable Long id) {
        boolean deleted = reportService.deleteReportById(id);
        if (deleted) {
            return ResponseEntity.ok(new ApiRes<>("success", "Report deleted successfully", "Deleted ID: " + id));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiRes<>("fail", "Report not found with ID: " + id, null));
    }

    @Operation(summary = "Delete device by deviceId", description = "Deletes all reports associated with a device ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Device deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Device not found")
    })
    @DeleteMapping("/device/{deviceId}")
    public ResponseEntity<ApiRes<String>> deleteDevice(@PathVariable String deviceId) {
        boolean deleted = reportService.deleteByDeviceId(deviceId);
        if (deleted) {
            return ResponseEntity.ok(new ApiRes<>("success", "Device deleted successfully", "Deleted deviceId: " + deviceId));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiRes<>("fail", "Device not found with deviceId: " + deviceId, null));
    }

    @Operation(summary = "Get time series metrics", description = "Fetches a list of metric data points for a specific report and metric type")
    @GetMapping("/metrics/timeseries")
    public List<MetricPointDto> getMetricTimeSeries(
            @RequestParam Long reportId,
            @RequestParam String metricType) {
        return reportService.getMetricPoints(reportId, metricType);
    }

    @Operation(summary = "Get latest metric point", description = "Fetches the latest metric data point for a specific report and metric type")
    @GetMapping("/metrics/latest")
    public MetricPointDto getLatestMetric(
            @RequestParam Long reportId,
            @RequestParam String metricType) {
        return reportService.getLatestMetricPoint(reportId, metricType);
    }

    @Operation(summary = "Get reports by package name", description = "Fetches all reports associated with a specific package name")
    @GetMapping("/package")
    public List<ReportDto> getReports(@RequestParam String packageName) {
        return reportService.getReportsByPackageName(packageName);
    }
}
