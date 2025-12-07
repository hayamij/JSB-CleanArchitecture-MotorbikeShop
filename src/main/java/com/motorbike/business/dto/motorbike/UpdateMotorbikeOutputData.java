package com.motorbike.business.dto.motorbike;

public class UpdateMotorbikeOutputData {

    public MotorbikeItem motorbike;
    public String errorCode;
    public String errorMessage;

    public UpdateMotorbikeOutputData(MotorbikeItem motorbike) {
        this.motorbike = motorbike;
    }

    public UpdateMotorbikeOutputData(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public static class MotorbikeItem {
        public Long id;
        public String name;
        public String description;
        public String imageUrl;
        public java.math.BigDecimal price;
        public int stock;
        public String brand;
        public String model;
        public String color;
        public int year;
        public int displacement;

        public MotorbikeItem(Long id,
                             String name,
                             String description,
                             java.math.BigDecimal price,
                             int stock,
                             String imageUrl,
                             String brand,
                             String model,
                             String color,
                             int year,
                             int displacement) {
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

    // ============================
    // Getter hỗ trợ cho UseCase & Test
    // ============================
    public MotorbikeItem getMotorbike() {
        return motorbike;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
