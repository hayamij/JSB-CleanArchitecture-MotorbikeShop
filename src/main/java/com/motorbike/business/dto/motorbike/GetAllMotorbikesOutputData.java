package com.motorbike.business.dto.motorbike;

import java.math.BigDecimal;
import java.util.List;

public class GetAllMotorbikesOutputData {

    private final boolean success;
    private final String errorCode;
    private final String errorMessage;
    private final List<MotorbikeItem> motorbikes;

    public GetAllMotorbikesOutputData(List<MotorbikeItem> motorbikes) {
        this.success = true;
        this.errorCode = null;
        this.errorMessage = null;
        this.motorbikes = motorbikes;
    }

    public GetAllMotorbikesOutputData(String errorCode, String errorMessage) {
        this.success = false;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.motorbikes = java.util.Collections.emptyList();
    }

    public boolean isSuccess() {
        return success;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public List<MotorbikeItem> getMotorbikes() {
        return motorbikes;
    }

    // ================== INNER CLASS ==================

    public static class MotorbikeItem {
        private final Long id;
        private final String name;
        private final String description;
        private final BigDecimal price;
        private final int stock;
        private final String imageUrl;
        private final String brand;      // hangXe
        private final String model;      // dongXe
        private final String color;      // mauSac
        private final int year;          // namSanXuat
        private final int displacement;  // dungTich

        public MotorbikeItem(Long id,
                             String name,
                             String description,
                             BigDecimal price,
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

        public Long getId() { return id; }
        public String getName() { return name; }
        public String getDescription() { return description; }
        public BigDecimal getPrice() { return price; }
        public int getStock() { return stock; }
        public String getImageUrl() { return imageUrl; }
        public String getBrand() { return brand; }
        public String getModel() { return model; }
        public String getColor() { return color; }
        public int getYear() { return year; }
        public int getDisplacement() { return displacement; }
    }
}
