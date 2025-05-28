package com.metric.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "devices")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "device_id", nullable = false, unique = true)
    private String deviceId;

    @Column(name = "device_name")
    private String deviceName;

    @Column(name = "platform")
    private String platform;

    @OneToMany(mappedBy = "device", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Report> reports = new ArrayList<>();
}
