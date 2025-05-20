package com.metric.backend.dto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MetricTimeseriesDto {
    private String metricType;
    private Double value;
    private LocalDateTime timestamp;
}
