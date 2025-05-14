package com.cloud_based.vpp.virtual_power_plant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication()
public class VirtualPowerPlantApplication {

	public static void main(String[] args) {
		SpringApplication.run(VirtualPowerPlantApplication.class, args);
	}

}
