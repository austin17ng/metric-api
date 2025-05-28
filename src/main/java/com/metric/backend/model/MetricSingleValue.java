package com.metric.backend.model;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "metrics_single_value")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MetricSingleValue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "report_id")
    private Report report;

    @Column(name = "metric_type")
    private String metricType;

    @Column(name = "metric_value")
    private Double value;
}

