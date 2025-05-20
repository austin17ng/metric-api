package com.metric.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "devices")
@Data
@Getter
@Setter
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
}
