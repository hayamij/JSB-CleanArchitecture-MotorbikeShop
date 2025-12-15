package com.motorbike.business.dto.accessory;

import java.math.BigDecimal;

public class UpdateAccessoryOutputData {

    public final boolean success;
    public final String errorCode;
    public final String errorMessage;
    public final AccessoryItem accessory;

    public UpdateAccessoryOutputData(AccessoryItem accessory) {
        this.success = true;
        this.errorCode = null;
        this.errorMessage = null;
        this.accessory = accessory;
    }

    public UpdateAccessoryOutputData(String errorCode, String errorMessage) {
        this.success = false;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.accessory = null;
    }

    public static class AccessoryItem {
        public Long id;
        public String name;
        public String description;
        public BigDecimal price;
        public int stock;
        public String imageUrl;
        public String type;
        public String brand;
        public String material;
        public String size;

        public AccessoryItem(Long id, String name, String description,
                             BigDecimal price, int stock, String imageUrl,
                             String type, String brand, String material, String size) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.price = price;
            this.stock = stock;
            this.imageUrl = imageUrl;
            this.type = type;
            this.brand = brand;
            this.material = material;
            this.size = size;
        }
    }
}
