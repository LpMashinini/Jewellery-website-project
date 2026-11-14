package com.Backend.Jewellery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class JewelleryApplication {

	public static void main(String[] args) {
		SpringApplication.run(JewelleryApplication.class, args);
	}

}
