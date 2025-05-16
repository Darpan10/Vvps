package com.cloud_based.vpp.virtual_power_plant.service;

import com.cloud_based.vpp.virtual_power_plant.Repository.BatteryRepository;
import com.cloud_based.vpp.virtual_power_plant.dto.BatteryDto;
import com.cloud_based.vpp.virtual_power_plant.dto.BatteryResponse;
import com.cloud_based.vpp.virtual_power_plant.entity.Battery;
import com.cloud_based.vpp.virtual_power_plant.mapper.BatteryMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.testcontainers.utility.TestcontainersConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
public class BatteryServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(BatteryServiceTest.class);

    @Autowired
    private BatteryService batteryService;

    @Autowired
    private BatteryRepository batteryRepository;

    @Autowired
    private BatteryMapper batteryMapper;

    @BeforeEach
    public void setup() {
        batteryRepository.deleteAll();
    }

    @Test
    public void testSaveBattery() {
        logger.info(">>> testSaveBattery started <<<");

        BatteryDto dto = new BatteryDto();
        dto.setName("ServiceBattery");
        dto.setPostCode(9999);
        dto.setWattCapacity(50000);

        try {
            CompletableFuture<Battery> savedBattery = batteryService.saveBattery(dto);
            Battery battery = savedBattery.get();

            assertThat(battery).as("Saved battery should not be null").isNotNull();
            assertThat(battery.getId()).as("Battery ID should be generated").isNotNull();
            assertThat(battery.getName()).isEqualTo("ServiceBattery");
            assertThat(battery.getPostCode()).isEqualTo(9999);
            assertThat(battery.getWattCapacity()).isEqualTo(50000);

        } catch (InterruptedException | ExecutionException e) {
            logger.error("Error saving battery", e);
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testGetBatteryInRange() {
        logger.info(">>> testGetBatteryInRange started <<<");

        try {
            // Create and save batteries (order of saving doesn't matter)
            BatteryDto dto1 = new BatteryDto();
            dto1.setName("Cannington");
            dto1.setPostCode(6107);
            dto1.setWattCapacity(13500);
            batteryService.saveBattery(dto1).get();

            BatteryDto dto2 = new BatteryDto();
            dto2.setName("Midland");
            dto2.setPostCode(6057);
            dto2.setWattCapacity(50500);
            batteryService.saveBattery(dto2).get();

            BatteryDto dto3 = new BatteryDto();
            dto3.setName("Hay Street");
            dto3.setPostCode(6000);
            dto3.setWattCapacity(23500);
            batteryService.saveBattery(dto3).get();

            // Call service method
            BatteryResponse response = batteryService.getBatteryInRange(6000, 6200);
            List<String> batteryNames = response.getBatteryNameLst();

            logger.info("Battery names returned: {}", batteryNames);

            // Assert list is not null and has 3 elements
            assertThat(batteryNames)
                    .as("Battery name list should not be empty")
                    .isNotNull()
                    .hasSize(3);

            // Assert battery names in alphabetical order as service sorts by name
            assertThat(batteryNames)
                    .as("Battery names should be sorted alphabetically")
                    .containsExactly("Cannington", "Hay Street", "Midland");

            // Check total and average watt capacity
            int totalWatt = 13500 + 50500 + 23500;
            assertThat(response.getTotalWatt()).isEqualTo(totalWatt);


        } catch (InterruptedException | ExecutionException e) {
            logger.error("Error getting battery in range", e);
            fail("Exception occurred: " + e.getMessage());
        }
    }


    @Test
    public void testGetBatteryInRangeEmpty() {
        BatteryResponse response = batteryService.getBatteryInRange(9999, 10000);
        assertThat(response.getBatteryNameLst())
                .as("No batteries should be found in this range")
                .isNotNull()
                .isEmpty();
        assertThat(response.getTotalWatt()).isEqualTo(0);
        assertThat(response.getAverageWatt()).isEqualTo(0);
    }

    @Test
    public void testAsyncSaveBattery() {
        BatteryDto dto = new BatteryDto();
        dto.setName("AsyncBattery");
        dto.setPostCode(1111);
        dto.setWattCapacity(60000);

        try {
            CompletableFuture<Battery> futureBattery = batteryService.saveBattery(dto);
            Battery battery = futureBattery.get();

            assertThat(battery).isNotNull();
            assertThat(battery.getId()).isNotNull();
            assertThat(battery.getName()).isEqualTo("AsyncBattery");

        } catch (InterruptedException | ExecutionException e) {
            logger.error("Async save failed", e);
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testGetBatteryInRangeLargeDataset() {
        try {
            for (int i = 0; i < 100; i++) {
                BatteryDto dto = new BatteryDto();
                dto.setName("Battery" + i);
                dto.setPostCode(100 + i);
                dto.setWattCapacity(500 + (i * 10));
                batteryService.saveBattery(dto).get();
            }

            BatteryResponse response = batteryService.getBatteryInRange(100, 200);
            assertThat(response.getBatteryNameLst()).hasSize(100);
            assertThat(response.getTotalWatt()).isGreaterThan(0);
            assertThat(response.getAverageWatt()).isGreaterThan(0);

        } catch (InterruptedException | ExecutionException e) {
            logger.error("Large dataset test failed", e);
            fail("Exception occurred: " + e.getMessage());
        }
    }
}
