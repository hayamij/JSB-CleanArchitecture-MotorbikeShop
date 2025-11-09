package com.motorbike.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.motorbike")
@EnableJpaRepositories(basePackages = "com.motorbike.infrastructure.persistence.repository")
@EntityScan(basePackages = "com.motorbike.infrastructure.persistence.entity")
public class MainApplication {

	public static void main(String[] args) {
		SpringApplication.run(MainApplication.class, args);
		System.out.println("Motorbike Shop Application is running...");
		System.out.println("API available at: http://localhost:8080/api/products");
		System.out.println("H2 Console at: http://localhost:8080/h2-console");
	}

}
