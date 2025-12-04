package com.motorbike.business.dto.accessory;

public class SearchAccessoriesInputData {
    public String keyword;
    public String type;
    public String brand;
    public String material;
    public Double minPrice;
    public Double maxPrice;

    public SearchAccessoriesInputData(String keyword,
                                     String type,
                                     String brand,
                                     String material,
                                     Double minPrice,
                                     Double maxPrice) {
        this.keyword = keyword;
        this.type = type;
        this.brand = brand;
        this.material = material;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }
}
