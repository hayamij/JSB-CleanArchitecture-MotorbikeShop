package com.motorbike.business.dto.accessory;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetAllAccessoriesOutputData {

    private final boolean success;
    private final String errorCode;
    private final String errorMessage;
    private final List<AccessoryItem> accessories;

    public GetAllAccessoriesOutputData(List<AccessoryItem> accessories) {
        this.success = true;
        this.errorCode = null;
        this.errorMessage = null;
        this.accessories = accessories;
    }

    public GetAllAccessoriesOutputData(String errorCode, String errorMessage) {
        this.success = false;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.accessories = java.util.Collections.emptyList();
    }

    public boolean isSuccess() { return success; }
    public String getErrorCode() { return errorCode; }
    public String getErrorMessage() { return errorMessage; }
    public List<AccessoryItem> getAccessories() { return accessories; }

    public static class AccessoryItem {
        @JsonProperty("id")
        private final Long id;
        @JsonProperty("name")
        private final String name;
        @JsonProperty("description")
        private final String description;
        @JsonProperty("price")
        private final BigDecimal price;
        @JsonProperty("stock")
        private final int stock;
        @JsonProperty("imageUrl")
        private final String imageUrl;
        @JsonProperty("type")
        private final String type;
        @JsonProperty("brand")
        private final String brand;
        @JsonProperty("material")
        private final String material;
        @JsonProperty("size")
        private final String size;

        public AccessoryItem(Long id,
                             String name,
                             String description,
                             BigDecimal price,
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

        public Long getId() { return id; }
        public String getName() { return name; }
        public String getDescription() { return description; }
        public BigDecimal getPrice() { return price; }
        public int getStock() { return stock; }
        public String getImageUrl() { return imageUrl; }
        public String getType() { return type; }
        public String getBrand() { return brand; }
        public String getMaterial() { return material; }
        public String getSize() { return size; }
    }
}
