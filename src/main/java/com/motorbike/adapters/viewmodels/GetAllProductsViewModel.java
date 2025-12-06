package com.motorbike.adapters.viewmodels;

import java.util.List;

public class GetAllProductsViewModel {
    public boolean success;
    public List<ProductItemViewModel> products;
    public String errorCode;
    public String errorMessage;

    public static class ProductItemViewModel {
        public Long id;
        public String name;
        public String description;
        public String formattedPrice;
        public int stock;
        public String imageUrl;
        public String formattedCreatedDate;
        public boolean available;
        public String category; // "XE_MAY", "PHU_KIEN", "KHAC"
        
        // Motorbike fields
        public String brand;
        public String model;
        public String color;
        public Integer year;
        public Integer engineCapacity;
        
        // Accessory fields
        public String type;
        public String material;
        public String size;
    }
}
