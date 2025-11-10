package com.motorbike.frameworks.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.motorbike")
@EnableJpaRepositories(basePackages = "com.motorbike.persistence.repository")
@EntityScan(basePackages = "com.motorbike.persistence.entity")
public class MainApplication {

	public static void main(String[] args) {
		SpringApplication.run(MainApplication.class, args);
		System.out.println("Motorbike Shop Application is running...");

        // GET: http://localhost:8080/api/products
		System.out.println("API available at: http://localhost:8080/api/products/{id}");
        

        // POST: http://localhost:8080/api/auth/login
        // Request Body: { "username": "customer1", "password": "password123" }
        System.out.println("API available at: http://localhost:8080/api/auth/login");


        // POST: http://localhost:8080/api/auth/register
        // Request Body: { "email": "customer@motorbike.com", "username": "customer1", "password": "password123", "phoneNumber": "0901234567" }
        System.out.println("API available at: http://localhost:8080/api/auth/register");
        
        // POST: http://localhost:8080/api/cart/add
        // Request Body: { "userId": 1, "productId": 1, "quantity": 2 }
        System.out.println("API available at: http://localhost:8080/api/cart/add");

        // GET: http://localhost:8080/api/cart/{userId}
        System.out.println("API available at: http://localhost:8080/api/cart/{userId}");

        // PUT: http://localhost:8080/api/cart/update
        // Request Body: { "userId": 1, "productId": 1, "newQuantity": 3 }
        System.out.println("API available at: http://localhost:8080/api/cart/update");
	}

}
