package com.motorbike.business.dto.motorbike;

import java.math.BigDecimal;

public class UpdateMotorbikeInputData {

    public Long id;

    public String name;
    public String description;
    public BigDecimal price;
    public String imageUrl;

    public Integer stock;
    public String brand;
    public String model;
    public String color;
    public Integer year;
    public Integer displacement;

    public UpdateMotorbikeInputData(Long id, String name, String description,
                                    BigDecimal price, String imageUrl,
                                    int stock, String brand, String model,
                                    String color, int year, int displacement) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.stock = stock;
        this.brand = brand;
        this.model = model;
        this.color = color;
        this.year = year;
        this.displacement = displacement;
    }
}
