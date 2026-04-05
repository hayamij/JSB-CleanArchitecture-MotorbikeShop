package com.motorbike.business.dto.accessory;

/**
 * Input Data cho Use Case: Export phụ kiện ra Excel
 */
public class ExportAccessoriesInputData {
    
    private String keyword;
    private String type;
    private String brand;
    private String material;
    
    public ExportAccessoriesInputData() {
    }
    
    public ExportAccessoriesInputData(String keyword, String type, String brand, String material) {
        this.keyword = keyword;
        this.type = type;
        this.brand = brand;
        this.material = material;
    }
    
    // Getters and Setters
    public String getKeyword() {
        return keyword;
    }
    
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getBrand() {
        return brand;
    }
    
    public void setBrand(String brand) {
        this.brand = brand;
    }
    
    public String getMaterial() {
        return material;
    }
    
    public void setMaterial(String material) {
        this.material = material;
    }
}
