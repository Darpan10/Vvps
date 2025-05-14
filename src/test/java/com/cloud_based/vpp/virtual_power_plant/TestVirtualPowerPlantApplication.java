package com.cloud_based.vpp.virtual_power_plant;

import org.springframework.boot.SpringApplication;

public class TestVirtualPowerPlantApplication {

	public static void main(String[] args) {
		SpringApplication.from(VirtualPowerPlantApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
