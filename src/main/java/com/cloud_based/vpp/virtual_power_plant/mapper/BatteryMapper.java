package com.cloud_based.vpp.virtual_power_plant.mapper;

import com.cloud_based.vpp.virtual_power_plant.dto.BatteryDto;
import com.cloud_based.vpp.virtual_power_plant.entity.Battery;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface BatteryMapper {

    Battery toEntity(BatteryDto dto);

    BatteryDto toDto(Battery entity);


    default List<Battery> toEntityList(List<BatteryDto> dtoList) {
        if (dtoList == null) {
            return null;
        }
        return dtoList.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    default List<BatteryDto> toDtoList(List<Battery> entityList) {
        if (entityList == null) {
            return null;
        }
        return entityList.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}