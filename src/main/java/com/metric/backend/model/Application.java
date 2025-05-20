package com.metric.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "applications")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Application {
    @Id
    @Column(name = "package_name", nullable = false, unique = true)
    private String packageName;

    @Column(name = "app_name")
    private String appName;
}
