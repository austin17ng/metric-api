package com.metric.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "reports")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String appVersion;

    private LocalDateTime reportedAt;

    @ManyToOne
    @JoinColumn(name = "package_name", referencedColumnName = "package_name")
    private Application application;

    @ManyToOne
    @JoinColumn(name = "device_id", referencedColumnName = "id")
    private Device device;

    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MetricSingleValue> metricSingleValues = new ArrayList<>();

    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MetricTimeseries> metricTimeseries = new ArrayList<>();
}
