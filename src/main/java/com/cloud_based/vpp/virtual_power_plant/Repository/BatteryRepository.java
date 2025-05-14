package com.cloud_based.vpp.virtual_power_plant.Repository;

import com.cloud_based.vpp.virtual_power_plant.entity.Battery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BatteryRepository extends JpaRepository<Battery, Long> {
   List<Battery> findByPostCodeBetween(int minPostCode, int maxPostCode);
}
