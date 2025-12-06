package com.motorbike;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MotorbikeShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(MotorbikeShopApplication.class, args);
        System.out.println("Server: http://localhost:8080");
        System.out.println("default admin account:");
        System.out.println("username: admin2 (admin@gmail.com)");
        System.out.println("password: 123");
        System.out.println("default user account:");
        System.out.println("username: hayami (nqtuanp2005@gmail.com)");
        System.out.println("password: 123456");
    }
}
