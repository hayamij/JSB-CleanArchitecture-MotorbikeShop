package com.motorbike.business.dto.motorbike;

/**
 * Input Data cho Use Case: Export xe máy ra Excel
 */
public class ExportMotorbikesInputData {
    
    // Có thể thêm filters sau này
    private String keyword;
    private String brand;
    private String model;
    private String color;
    
    public ExportMotorbikesInputData() {
    }
    
    public ExportMotorbikesInputData(String keyword, String brand, String model, String color) {
        this.keyword = keyword;
        this.brand = brand;
        this.model = model;
        this.color = color;
    }
    
    // Getters and Setters
    public String getKeyword() {
        return keyword;
    }
    
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
    
    public String getBrand() {
        return brand;
    }
    
    public void setBrand(String brand) {
        this.brand = brand;
    }
    
    public String getModel() {
        return model;
    }
    
    public void setModel(String model) {
        this.model = model;
    }
    
    public String getColor() {
        return color;
    }
    
    public void setColor(String color) {
        this.color = color;
    }
}
