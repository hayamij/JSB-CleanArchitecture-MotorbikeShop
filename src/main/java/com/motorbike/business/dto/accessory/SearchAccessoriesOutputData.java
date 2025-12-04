package com.motorbike.business.dto.accessory;

import java.util.List;

public class SearchAccessoriesOutputData {

    public boolean hasError;
    public String errorCode;
    public String errorMessage;
    public List<AccessoryItem> accessories;

    public SearchAccessoriesOutputData(List<AccessoryItem> accessories) {
        this.hasError = false;
        this.errorCode = null;
        this.errorMessage = null;
        this.accessories = accessories;
    }

    public SearchAccessoriesOutputData(String errorCode, String errorMessage) {
        this.hasError = true;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.accessories = null;
    }

    public static class AccessoryItem {
        public Long id;
        public String name;
        public String description;
        public java.math.BigDecimal price;
        public int stock;
        public String imageUrl;
        public String type;
        public String brand;
        public String material;
        public String size;

        public AccessoryItem(Long id,
                             String name,
                             String description,
                             java.math.BigDecimal price,
                             int stock,
                             String imageUrl,
                             String type,
                             String brand,
                             String material,
                             String size) {
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
