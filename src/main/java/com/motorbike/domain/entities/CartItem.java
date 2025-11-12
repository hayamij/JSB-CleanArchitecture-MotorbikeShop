package com.motorbike.domain.entities;

import com.motorbike.domain.exceptions.InvalidCartException;
import java.math.BigDecimal;

/**
 * Domain Entity: CartItem
 * Represents an item in the shopping cart
 */
public class CartItem {
    private Long id;
    private Long cartId;
    private Long productId;
    private String productName;
    private BigDecimal productPrice;
    private int quantity;
    private BigDecimal subtotal;

    // constructor
    public CartItem(Long productId, String productName, BigDecimal productPrice, int quantity) {
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.quantity = quantity;
        this.subtotal = calculateSubtotal();
    }

    // Full constructor
    public CartItem(Long id, Long cartId, Long productId, String productName,
                    BigDecimal productPrice, int quantity, BigDecimal subtotal) {
        this.id = id;
        this.cartId = cartId;
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.quantity = quantity;
        this.subtotal = subtotal;
    }

    // Calculate subtotal
    private BigDecimal calculateSubtotal() {
        return productPrice.multiply(BigDecimal.valueOf(quantity));
    }

    // Validate cart item
    public void validate() {
        if (productId == null) {
            throw new InvalidCartException("NULL_PRODUCT_ID", "Product ID ko được null");
        }
        if (productPrice == null || productPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidCartException("INVALID_PRICE", "Product price phải > 0");
        }
        if (quantity <= 0) {
            throw new InvalidCartException("INVALID_QUANTITY", "Quantity phải > 0");
        }
    }

    // Set quantity and recalculate subtotal
    public void setQuantity(int quantity) {
        if (quantity <= 0) {
            throw new InvalidCartException("INVALID_QUANTITY", "Quantity phải > 0");
        }
        this.quantity = quantity;
        this.subtotal = calculateSubtotal();
    }

    // Increase quantity
    public void increaseQuantity(int amount) {
        if (amount <= 0) {
            throw new InvalidCartException("INVALID_AMOUNT", "Amount phải > 0");
        }
        this.quantity += amount;
        this.subtotal = calculateSubtotal();
    }

    // Decrease quantity
    public void decreaseQuantity(int amount) {
        if (amount <= 0) {
            throw new InvalidCartException("INVALID_AMOUNT", "Amount phải > 0");
        }
        if (this.quantity - amount < 0) {
            throw new InvalidCartException("INSUFFICIENT_QUANTITY", "Cannot decrease below 0");
        }
        this.quantity -= amount;
        this.subtotal = calculateSubtotal();
    }

    // getters
    public Long getId() {
        return id;
    }

    public Long getCartId() {
        return cartId;
    }

    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    // setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    public void setProductPrice(BigDecimal productPrice) {
        if (productPrice == null || productPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidCartException("INVALID_PRICE", "Product price phải > 0");
        }
        this.productPrice = productPrice;
        this.subtotal = calculateSubtotal();
    }
}
