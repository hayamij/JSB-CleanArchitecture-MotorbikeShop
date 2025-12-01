package com.motorbike.business.dto.motorbike;

import java.util.List;

public class SearchMotorbikesOutputData {

    // dùng field public cho đơn giản, giống các DTO khác của bạn
    public boolean hasError;
    public String errorCode;
    public String errorMessage;
    public List<MotorbikeItem> motorbikes;   // 👈 field mà Presenter đang dùng

    // Trường hợp thành công
    public SearchMotorbikesOutputData(List<MotorbikeItem> motorbikes) {
        this.hasError = false;
        this.errorCode = null;
        this.errorMessage = null;
        this.motorbikes = motorbikes;
    }

    // Trường hợp lỗi
    public SearchMotorbikesOutputData(String errorCode, String errorMessage) {
        this.hasError = true;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.motorbikes = null;
    }

    // DTO con cho từng xe
    public static class MotorbikeItem {
        public Long id;
        public String name;
        public String description;
        public java.math.BigDecimal price;
        public int stock;
        public String imageUrl;
        public String brand;
        public String model;
        public String color;
        public Integer year;
        public Integer displacement; // cc

        public MotorbikeItem(Long id,
                             String name,
                             String description,
                             java.math.BigDecimal price,
                             int stock,
                             String imageUrl,
                             String brand,
                             String model,
                             String color,
                             Integer year,
                             Integer displacement) {
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
}
