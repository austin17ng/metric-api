package com.metric.backend.model;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "metrics_timeseries")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MetricTimeseries {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "report_id")
    private Report report;

    @Column(name = "metric_type")
    private String metricType;

    @Column(name = "metric_value")
    private Double value;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;
}
