package com.metric.backend.controller;

import com.metric.backend.common.ApiResponse;
import com.metric.backend.dto.FullReportRequest;
import com.metric.backend.model.Report;
import com.metric.backend.service.ReportService;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<ApiResponse<Report>> createFullReport(@RequestBody FullReportRequest dto) {
        try {
            Report savedReport = reportService.createOrUpdateFullReport(dto);
            ApiResponse<Report> response = new ApiResponse<>("success", "Report saved successfully", savedReport);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<Report> response = new ApiResponse<>("fail", "Failed to save report", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
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
}
