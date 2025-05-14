package com.cloud_based.vpp.virtual_power_plant.entity;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class Battery extends EnergyStorageUnit{
}
