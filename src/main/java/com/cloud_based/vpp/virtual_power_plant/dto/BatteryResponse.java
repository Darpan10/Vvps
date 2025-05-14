package com.cloud_based.vpp.virtual_power_plant.dto;

import lombok.Data;

import java.util.List;

@Data
public class BatteryResponse {
    List<String> batteryNameLst;
    double averageWatt;
    double totalWatt;

}
