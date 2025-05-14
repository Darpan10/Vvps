package com.cloud_based.vpp.virtual_power_plant.dto;

import lombok.Data;

@Data
public class EnergyStorageUnitDto {
    private Long id;
    private String name;
    private int postCode;
    private double wattCapacity;
}
