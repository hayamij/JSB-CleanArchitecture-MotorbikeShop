package com.motorbike.business.dto.motorbike;

import java.math.BigDecimal;

public class AddMotorbikeOutputData {

    // ==============
    // 1) Data item
    // ==============
    public static class MotorbikeItem {
        public Long id;
        public String name;
        public String description;
        public BigDecimal price;
        public int stock;
        public String imageUrl;
        public String brand;
        public String model;
        public String color;
        public int year;
        public int displacement;

        public MotorbikeItem(
                Long id,
                String name,
                String description,
                BigDecimal price,
                int stock,
                String imageUrl,
                String brand,
                String model,
                String color,
                int year,
                int displacement
        ) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.price = price;
            this.stock = stock;
            this.imageUrl = imageUrl;
            this.brand = brand;
            this.model = model;
            this.color = color;
            this.year = year;
            this.displacement = displacement;
        }
    }

    // ==============
    // 2) Fields
    // ==============
    public MotorbikeItem motorbike;   // sản phẩm vừa thêm
    public boolean hasError;
    public String errorCode;
    public String errorMessage;

    // ==============
    // 3) Constructors
    // ==============

    // Thành công
    public AddMotorbikeOutputData(MotorbikeItem motorbike) {
        this.motorbike = motorbike;
        this.hasError = false;
        this.errorCode = null;
        this.errorMessage = null;
    }

    // Thất bại
    public AddMotorbikeOutputData(String errorCode, String errorMessage) {
        this.motorbike = null;
        this.hasError = true;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
