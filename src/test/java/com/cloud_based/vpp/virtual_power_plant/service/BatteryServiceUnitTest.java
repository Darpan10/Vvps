package com.cloud_based.vpp.virtual_power_plant.service;

import com.cloud_based.vpp.virtual_power_plant.Repository.BatteryRepository;
import com.cloud_based.vpp.virtual_power_plant.dto.BatteryDto;
import com.cloud_based.vpp.virtual_power_plant.entity.Battery;
import com.cloud_based.vpp.virtual_power_plant.mapper.BatteryMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class BatteryServiceUnitTest {

    @InjectMocks
    private BatteryService batteryService;

    @Mock
    private BatteryRepository batteryRepository;

    @Mock
    private BatteryMapper batteryMapper;

    private List<BatteryDto> testData = List.of(
            createDto("Cannington", 6107, 13500),
            createDto("Midland", 6057, 50500),
            createDto("Hay Street", 6000, 23500),
            createDto("Mount Adams", 6525, 12000),
            createDto("Koolan Island", 6733, 10000),
            createDto("Armadale", 6992, 25000),
            createDto("Lesmurdie", 6076, 13500),
            createDto("Kalamunda", 6076, 13500),
            createDto("Carmel", 6076, 36000),
            createDto("Bentley", 6102, 85000),
            createDto("Akunda Bay", 2084, 13500),
            createDto("Werrington County", 2747, 13500),
            createDto("Bagot", 820, 27000),
            createDto("Yirrkala", 880, 13500),
            createDto("University of Melbourne", 3010, 85000),
            createDto("Norfolk Island", 2899, 13500),
            createDto("Ootha", 2875, 13500),
            createDto("Kent Town", 5067, 13500),
            createDto("Northgate Mc", 9464, 13500),
            createDto("Gold Coast Mc", 9729, 50000)
    );

    private static BatteryDto createDto(String name, int postcode, int capacity) {
        BatteryDto dto = new BatteryDto();
        dto.setName(name);
        dto.setPostCode(postcode);
        dto.setWattCapacity(capacity);
        return dto;
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveMultipleBatteriesSuccessfully() throws Exception {
        for (BatteryDto dto : testData) {
            Battery batteryEntity = new Battery();
            batteryEntity.setName(dto.getName());
            batteryEntity.setPostCode(dto.getPostCode());
            batteryEntity.setWattCapacity(dto.getWattCapacity());

            when(batteryMapper.toEntity(dto)).thenReturn(batteryEntity);
            when(batteryRepository.save(any(Battery.class))).thenReturn(batteryEntity);

            CompletableFuture<Battery> result = batteryService.saveBattery(dto);

            assertThat(result).isNotNull();
            assertThat(result.get().getName()).isEqualTo(dto.getName());
            assertThat(result.get().getPostCode()).isEqualTo(dto.getPostCode());
            assertThat(result.get().getWattCapacity()).isEqualTo(dto.getWattCapacity());

            verify(batteryMapper).toEntity(dto);
            verify(batteryRepository).save(batteryEntity);

            reset(batteryMapper, batteryRepository);  // Reset mocks for next iteration
        }
    }
    @Test
    void testSaveBatteryHandlesRepositoryException() throws Exception {
        BatteryDto dto = createDto("Test Battery", 1234, 10000);

        Battery batteryEntity = new Battery();
        batteryEntity.setName(dto.getName());
        batteryEntity.setPostCode(dto.getPostCode());
        batteryEntity.setWattCapacity(dto.getWattCapacity());

        when(batteryMapper.toEntity(dto)).thenReturn(batteryEntity);
        when(batteryRepository.save(any(Battery.class))).thenThrow(new RuntimeException("DB failure"));

        assertThatThrownBy(() -> batteryService.saveBattery(dto).get())
                .isInstanceOf(ExecutionException.class)
                .hasCauseInstanceOf(RuntimeException.class)
                .hasRootCauseMessage("DB failure");

        verify(batteryMapper).toEntity(dto);
        verify(batteryRepository).save(batteryEntity);
    }

}
