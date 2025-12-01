package com.motorbike.business.dto.motorbike;

public class SearchMotorbikesInputData {
    public String keyword;
    public String brand;
    public String model;
    public String color;
    public Integer minCC;
    public Integer maxCC;

    public SearchMotorbikesInputData(
            String keyword,
            String brand,
            String model,
            String color,
            Integer minCC,
            Integer maxCC
    ) {
        this.keyword = keyword;
        this.brand = brand;
        this.model = model;
        this.color = color;
        this.minCC = minCC;
        this.maxCC = maxCC;
    }
}
