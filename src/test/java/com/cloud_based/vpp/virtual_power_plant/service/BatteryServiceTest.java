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

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;

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
    public void beforeEachTest() {
        // Clean up the database before each test
        batteryRepository.deleteAll();
    }

    @Test
    public void testSaveBattery() throws ExecutionException, InterruptedException {
        try {
            logger.info(">>> testSaveBattery started <<<");

            BatteryDto dto = new BatteryDto();
            dto.setName("ServiceBattery");
            dto.setPostCode(9999);
            dto.setWattCapacity(50000);

            logger.info("Battery DTO prepared: {}", dto);

            CompletableFuture<Battery> savedBattery = batteryService.saveBattery(dto);

            logger.info("Battery saved: {}", savedBattery);

            assertThat(savedBattery.get().getId()).isNotNull();
            assertThat(savedBattery.get().getName()).isEqualTo("ServiceBattery");
            assertThat(savedBattery.get().getPostCode()).isEqualTo(9999);
            assertThat(savedBattery.get().getWattCapacity()).isEqualTo(50000);

        } catch (Exception e) {
            logger.error("An error occurred while saving the battery", e);
            throw e;
        }
    }

    @Test
    public void testGetBatteryInRange() {
        try {
            logger.info(">>> testGetBatteryInRange started <<<");

            // Save batteries for testing
            BatteryDto dto1 = new BatteryDto();
            dto1.setName("alpha");
            dto1.setPostCode(2000);
            dto1.setWattCapacity(10000);
            batteryService.saveBattery(dto1);

            BatteryDto dto2 = new BatteryDto();
            dto2.setName("beta");
            dto2.setPostCode(2500);
            dto2.setWattCapacity(15000);
            batteryService.saveBattery(dto2);

            BatteryDto dto3 = new BatteryDto();
            dto3.setName("gamma");
            dto3.setPostCode(3000);
            dto3.setWattCapacity(20000);
            batteryService.saveBattery(dto3);

            logger.info("Saved test batteries.");

            // Perform test
            BatteryResponse response = batteryService.getBatteryInRange(2000, 3000);
            List<String> resultList = response.getBatteryNameLst();

            logger.info("Response: {}", response);

            // Assertions
            assertThat(resultList).isNotEmpty();
            assertThat(resultList.get(0)).isEqualTo("alpha");
            assertThat(resultList.get(1)).isEqualTo("beta");
            assertThat(resultList.get(2)).isEqualTo("gamma");
            assertThat(response.getTotalWatt()).isEqualTo(45000);
            assertThat(response.getAverageWatt()).isEqualTo(15000);

        } catch (Exception e) {
            logger.error("An error occurred while testing getBatteryInRange", e);
            throw e;
        }
    }

    @Test
    public void testGetBatteryInRangeEmpty() {
        BatteryResponse response = batteryService.getBatteryInRange(9999, 10000); // range with no batteries
        assertThat(response.getBatteryNameLst()).isEmpty();
    }


    @Test
    public void testAsyncSaveBattery() throws ExecutionException, InterruptedException {
        BatteryDto dto = new BatteryDto();
        dto.setName("AsyncBattery");
        dto.setPostCode(1111);
        dto.setWattCapacity(60000);

        CompletableFuture<Battery> savedBattery = batteryService.saveBattery(dto);
        assertThat(savedBattery.get().getId()).isNotNull();
    }



    @Test
    public void testGetBatteryInRangeLargeDataset() {
        // Create and save a large number of batteries
        for (int i = 0; i < 100; i++) {
            BatteryDto dto = new BatteryDto();
            dto.setName("Battery" + i);
            dto.setPostCode(100 + i);
            dto.setWattCapacity(500 + (i * 10));
            batteryService.saveBattery(dto);
        }

        // Test for batteries within a range
        BatteryResponse response = batteryService.getBatteryInRange(100, 200);
        assertThat(response.getBatteryNameLst()).hasSize(100);
    }

}
