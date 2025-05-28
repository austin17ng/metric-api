package com.metric.backend.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportRequestDto {
    // Application info
    private String packageName;
    private String appName;

    // Device info
    private String deviceId;
    private String deviceName;
    private String platform;

    // Report info
    private String appVersion;
    private LocalDateTime reportedAt;

    // Metrics info
    private List<MetricTimeseriesDto> metricsTimeSeries;
    private List<MetricSingleValueDto> metricsSingleValue;
}