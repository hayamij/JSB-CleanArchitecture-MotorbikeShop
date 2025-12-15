package com.motorbike.business.dto.accessory;

import java.math.BigDecimal;

public class UpdateAccessoryInputData {

    public final Long id;
    public final String name;
    public final String description;
    public final BigDecimal price;
    public final String imageUrl;
    public final int stock;
    public final String type;
    public final String brand;
    public final String material;
    public final String size;

    public UpdateAccessoryInputData(
            Long id,
            String name,
            String description,
            BigDecimal price,
            String imageUrl,
            int stock,
            String type,
            String brand,
            String material,
            String size
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.stock = stock;
        this.type = type;
        this.brand = brand;
        this.material = material;
        this.size = size;
    }
}
