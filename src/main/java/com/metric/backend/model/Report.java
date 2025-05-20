package com.metric.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reports")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "package_name")
    private String packageName;

    @Column(name = "app_version")
    private String appVersion;

    @Column(name = "reported_at")
    private LocalDateTime reportedAt;

    @ManyToOne
    @JoinColumn(name = "device_id", referencedColumnName = "id")
    private Device device;
}
