package com.motorbike.adapters.dto.response;

public class AddMotorbikeResponse {

    public long id;
    public String name;
    public String description;
    public String imageUrl;
    public int stock;

    public String brand;
    public String model;
    public String color;
    public int year;
    public int displacement;

    public AddMotorbikeResponse(long id,
                                String name,
                                String description,
                                String imageUrl,
                                int stock,
                                String brand,
                                String model,
                                String color,
                                int year,
                                int displacement) {

        this.id = id;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.stock = stock;
        this.brand = brand;
        this.model = model;
        this.color = color;
        this.year = year;
        this.displacement = displacement;
    }
}
