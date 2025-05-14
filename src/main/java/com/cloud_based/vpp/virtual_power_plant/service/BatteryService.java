package com.cloud_based.vpp.virtual_power_plant.service;

import com.cloud_based.vpp.virtual_power_plant.Repository.BatteryRepository;
import com.cloud_based.vpp.virtual_power_plant.dto.BatteryDto;
import com.cloud_based.vpp.virtual_power_plant.dto.BatteryResponse;
import com.cloud_based.vpp.virtual_power_plant.entity.Battery;
import com.cloud_based.vpp.virtual_power_plant.mapper.BatteryMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BatteryService {
    private final BatteryRepository batteryRepository;
    private final BatteryMapper batteryMapper;


    private static final Logger logger = LoggerFactory.getLogger(BatteryService.class);

    @Async
    @Transactional
    public CompletableFuture<Battery> saveBattery(BatteryDto batteryDto) {
        try {
            logger.info("Starting to save battery: {}", batteryDto);
            Battery battery = batteryMapper.toEntity(batteryDto);
            Battery savedBattery = batteryRepository.save(battery);
            logger.info("Successfully saved battery: {}", savedBattery);
            return CompletableFuture.completedFuture(savedBattery);
        } catch (Exception e) {
            logger.error("An error occurred while saving the battery: {}", batteryDto, e);
            throw new RuntimeException("Failed to save battery", e);
        }
    }


    @Transactional
    public BatteryResponse getBatteryInRange(int minPostCode, int maxPostCode) {
        logger.info(">>> getBatteryInRange started with minPostCode: {} and maxPostCode: {}", minPostCode, maxPostCode);

        // Validate input range
        if (minPostCode > maxPostCode) {
            logger.error("Invalid input range: minPostCode cannot be greater than maxPostCode");
            throw new IllegalArgumentException("minPostCode cannot be greater than maxPostCode");
        }

        List<BatteryDto> batteriesInRange;
        try {

            batteriesInRange = batteryMapper.toDtoList(batteryRepository.findByPostCodeBetween(minPostCode, maxPostCode))
                    .stream()
                    .sorted(Comparator.comparing(BatteryDto::getName))
                    .collect(Collectors.toList());

            logger.info("Fetched {} batteries in range", batteriesInRange.size());
        } catch (Exception e) {
            logger.error("An error occurred while fetching batteries from the database", e);
            throw new RuntimeException("Failed to fetch batteries in range", e);
        }


        if (batteriesInRange.isEmpty()) {
            logger.warn("No batteries found in the specified postcode range.");
        }

        double totalWatt = batteriesInRange.stream()
                .mapToDouble(BatteryDto::getWattCapacity)
                .sum();
        logger.debug("Calculated total watt capacity: {}", totalWatt);


        double averageWatt = batteriesInRange.stream()
                .mapToDouble(BatteryDto::getWattCapacity)
                .average()
                .orElse(0.0);
        logger.debug("Calculated average watt capacity: {}", averageWatt);


        BatteryResponse batteryResponse = new BatteryResponse();
        batteryResponse.setBatteryNameLst(batteriesInRange.stream()
                .map(BatteryDto::getName)
                .collect(Collectors.toList()));
        batteryResponse.setTotalWatt(totalWatt);
        batteryResponse.setAverageWatt(averageWatt);

        logger.info(">>> getBatteryInRange completed successfully");
        return batteryResponse;
    }

}
