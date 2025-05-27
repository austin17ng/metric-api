package com.metric.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportDto {
    private Long id;
    private String appVersion;
    private LocalDateTime reportedAt;
}
