package com.cloud_based.vpp.virtual_power_plant.entity;

import jakarta.persistence.*;
import lombok.Data;

@MappedSuperclass
@Data
public abstract class EnergyStorageUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "postCode")
    private int postCode;
    @Column(name = "wattCapacity")
    private double wattCapacity;
}