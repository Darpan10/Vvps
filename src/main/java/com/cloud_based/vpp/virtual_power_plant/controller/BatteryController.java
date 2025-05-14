package com.cloud_based.vpp.virtual_power_plant.controller;

import com.cloud_based.vpp.virtual_power_plant.dto.BatteryDto;
import com.cloud_based.vpp.virtual_power_plant.dto.BatteryResponse;
import com.cloud_based.vpp.virtual_power_plant.entity.Battery;
import com.cloud_based.vpp.virtual_power_plant.mapper.BatteryMapper;
import com.cloud_based.vpp.virtual_power_plant.service.BatteryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.Future;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/vvp")
public class BatteryController {
    private final BatteryService batteryService;
    private final BatteryMapper batteryMapper;

    @PostMapping("/battery")
    public ResponseEntity<?> addBattery(@RequestBody @Validated BatteryDto batteryRequest) {
        Future<Battery> futureBattery = batteryService.saveBattery(batteryRequest);
        return ResponseEntity.accepted().body("Request accepted, processing in background.");
    }

    @GetMapping("/batteries")
    public ResponseEntity<BatteryResponse> getBattery(@RequestParam int minPostCode,
                                                      @RequestParam int maxPostCode) {
        return ResponseEntity.ok(batteryService.getBatteryInRange(minPostCode, maxPostCode));
    }

}
