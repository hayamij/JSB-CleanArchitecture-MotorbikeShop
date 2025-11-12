package com.motorbike.domain.entities;

import com.motorbike.domain.exceptions.InvalidProductException;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * entity sản phẩm
 * chứa logic nghiệp vụ về product
 */
public class Product {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String imageUrl;
    private String specifications; // chuỗi json
    private ProductCategory category;
    private int stockQuantity;
    private boolean available;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // constructor cho sp mới
    public Product(String name, String description, BigDecimal price, 
                   String imageUrl, String specifications, ProductCategory category, 
                   int stockQuantity) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.specifications = specifications;
        this.category = category;
        this.stockQuantity = stockQuantity;
        this.available = true;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // constructor đầy đủ
    public Product(Long id, String name, String description, BigDecimal price,
                   String imageUrl, String specifications, ProductCategory category,
                   int stockQuantity, boolean available, 
                   LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.specifications = specifications;
        this.category = category;
        this.stockQuantity = stockQuantity;
        this.available = available;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // validate tên sp
    public static void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidProductException("EMPTY_NAME", "tên sp ko được rỗng");
        }
        if (name.length() > 255) {
            throw new InvalidProductException("NAME_TOO_LONG", "tên sp phải <= 255 ký tự");
        }
    }

    // validate giá
    public static void validatePrice(BigDecimal price) {
        if (price == null) {
            throw new InvalidProductException("NULL_PRICE", "giá ko được null");
        }
        if (price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidProductException("INVALID_PRICE", "giá phải > 0");
        }
    }

    // validate số lượng tồn kho
    public static void validateStockQuantity(int quantity) {
        if (quantity < 0) {
            throw new InvalidProductException("NEGATIVE_STOCK", "số lượng tồn kho ko được âm");
        }
    }

    // kiểm tra sp còn hàng ko
    public boolean isInStock() {
        return this.available && this.stockQuantity > 0;
    }

    // kiểm tra còn đủ số lượng yêu cầu ko
    public boolean hasStock(int requestedQuantity) {
        return this.available && this.stockQuantity >= requestedQuantity;
    }

    // giảm số lượng tồn kho
    public void reduceStock(int quantity) {
        if (quantity <= 0) {
            throw new InvalidProductException("INVALID_QUANTITY", "số lượng phải > 0");
        }
        if (this.stockQuantity < quantity) {
            throw new InvalidProductException("INSUFFICIENT_STOCK", 
                "ko đủ hàng. còn: " + this.stockQuantity + ", yêu cầu: " + quantity);
        }
        this.stockQuantity -= quantity;
        this.updatedAt = LocalDateTime.now();
        
        // tự động đánh dấu hết hàng nếu stock = 0
        if (this.stockQuantity == 0) {
            this.available = false;
        }
    }

    // tăng số lượng tồn kho
    public void increaseStock(int quantity) {
        if (quantity <= 0) {
            throw new InvalidProductException("INVALID_QUANTITY", "số lượng phải > 0");
        }
        this.stockQuantity += quantity;
        this.updatedAt = LocalDateTime.now();
        
        // tự động đánh dấu có hàng khi thêm stock
        if (this.stockQuantity > 0) {
            this.available = true;
        }
    }

    // tính tổng tiền cho số lượng
    public BigDecimal calculateSubtotal(int quantity) {
        if (quantity <= 0) {
            throw new InvalidProductException("INVALID_QUANTITY", "số lượng phải > 0");
        }
        return this.price.multiply(BigDecimal.valueOf(quantity));
    }

    // đánh dấu ko khả dụng
    public void markAsUnavailable() {
        this.available = false;
        this.updatedAt = LocalDateTime.now();
    }

    // đánh dấu khả dụng
    public void markAsAvailable() {
        if (this.stockQuantity > 0) {
            this.available = true;
            this.updatedAt = LocalDateTime.now();
        } else {
            throw new InvalidProductException("NO_STOCK", "ko thể đánh dấu khả dụng - ko có hàng");
        }
    }

    // getters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getSpecifications() {
        return specifications;
    }

    public ProductCategory getCategory() {
        return category;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public boolean isAvailable() {
        return available;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    // setters (hạn chế)
    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        validateName(name);
        this.name = name;
        this.updatedAt = LocalDateTime.now();
    }

    public void setDescription(String description) {
        this.description = description;
        this.updatedAt = LocalDateTime.now();
    }

    public void setPrice(BigDecimal price) {
        validatePrice(price);
        this.price = price;
        this.updatedAt = LocalDateTime.now();
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        this.updatedAt = LocalDateTime.now();
    }

    public void setSpecifications(String specifications) {
        this.specifications = specifications;
        this.updatedAt = LocalDateTime.now();
    }

    public void setCategory(ProductCategory category) {
        this.category = category;
        this.updatedAt = LocalDateTime.now();
    }

    public void setStockQuantity(int stockQuantity) {
        if (stockQuantity < 0) {
            throw new InvalidProductException("NEGATIVE_STOCK", "số lượng tồn kho ko được âm");
        }
        this.stockQuantity = stockQuantity;
        this.updatedAt = LocalDateTime.now();
    }

    public void setAvailable(boolean available) {
        this.available = available;
        this.updatedAt = LocalDateTime.now();
    }
}
