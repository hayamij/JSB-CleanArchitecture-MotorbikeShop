package com.motorbike.business.usecase.impl;

import com.motorbike.business.dto.updatecart.UpdateCartQuantityInputData;
import com.motorbike.business.dto.updatecart.UpdateCartQuantityOutputData;
import com.motorbike.business.ports.repository.CartRepository;
import com.motorbike.business.ports.repository.ProductRepository;
import com.motorbike.business.usecase.UpdateCartQuantityInputBoundary;
import com.motorbike.business.usecase.UpdateCartQuantityOutputBoundary;
import com.motorbike.domain.entities.Cart;
import com.motorbike.domain.entities.CartItem;
import com.motorbike.domain.entities.Product;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of Update Cart Quantity Use Case.
 * Handles updating the quantity of a product in the cart.
 * 
 * Business Rules:
 * 1. New quantity must be >= 0
 * 2. If new quantity is 0, remove the item from cart
 * 3. Check product stock availability
 * 4. New quantity cannot exceed available stock
 * 5. Update cart item quantity
 * 6. Recalculate cart totals
 * 7. Support both logged-in users and guest users
 */
public class UpdateCartQuantityUseCaseImpl implements UpdateCartQuantityInputBoundary {
    
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UpdateCartQuantityOutputBoundary outputBoundary;

    public UpdateCartQuantityUseCaseImpl(
            CartRepository cartRepository,
            ProductRepository productRepository,
            UpdateCartQuantityOutputBoundary outputBoundary) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void execute(UpdateCartQuantityInputData inputData) {
        // Validate input
        if (inputData == null) {
            presentError("INVALID_INPUT", "Input data cannot be null");
            return;
        }

        // Get cart
        Cart cart;
        if (inputData.isLoggedIn()) {
            cart = cartRepository.findByUserId(inputData.getUserId()).orElse(null);
        } else {
            cart = cartRepository.findById(inputData.getGuestCartId()).orElse(null);
        }

        if (cart == null) {
            presentError("CART_NOT_FOUND", "Cart not found");
            return;
        }

        Long productId = inputData.getProductId();
        int newQuantity = inputData.getNewQuantity();

        // Validate new quantity
        if (newQuantity < 0) {
            presentError("INVALID_QUANTITY", "Quantity cannot be negative");
            return;
        }

        // Find cart item
        CartItem cartItem = findCartItem(cart, productId);
        if (cartItem == null) {
            presentError("PRODUCT_NOT_IN_CART", "Product not found in cart");
            return;
        }

        int oldQuantity = cartItem.getQuantity();

        // If new quantity is 0, remove item
        if (newQuantity == 0) {
            cart.removeItem(productId);
            cartRepository.save(cart);
            presentRemovalSuccess(cart, productId, cartItem.getProductName(), oldQuantity);
            return;
        }

        // Check product stock availability
        Product product = productRepository.findById(productId).orElse(null);
        if (product == null) {
            presentError("PRODUCT_NOT_FOUND", "Product not found");
            return;
        }

        if (!product.isAvailable()) {
            presentError("PRODUCT_NOT_AVAILABLE", "Product is not available");
            return;
        }

        if (newQuantity > product.getStockQuantity()) {
            presentError("INSUFFICIENT_STOCK", 
                String.format("Insufficient stock. Available: %d", product.getStockQuantity()));
            return;
        }

        // Update quantity
        cart.updateItemQuantity(productId, newQuantity);
        cartRepository.save(cart);

        // Present success
        presentUpdateSuccess(cart, product, oldQuantity, newQuantity);
    }

    private CartItem findCartItem(Cart cart, Long productId) {
        return cart.getItems().stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst()
                .orElse(null);
    }

    private void presentUpdateSuccess(Cart cart, Product product, int oldQuantity, int newQuantity) {
        // Build cart items data
        List<UpdateCartQuantityOutputData.CartItemData> itemsData = new ArrayList<>();
        for (CartItem item : cart.getItems()) {
            Product p = productRepository.findById(item.getProductId()).orElse(null);
            if (p != null) {
                itemsData.add(new UpdateCartQuantityOutputData.CartItemData(
                    item.getProductId(),
                    p.getName(),
                    item.getProductPrice(),
                    item.getQuantity(),
                    item.getSubtotal()
                ));
            }
        }

        UpdateCartQuantityOutputData outputData = new UpdateCartQuantityOutputData(
            cart.getId(),
            cart.getUserId(),
            product.getId(),
            product.getName(),
            oldQuantity,
            newQuantity,
            false, // item not removed
            cart.getItemCount(),
            cart.getTotalItemCount(),
            cart.getTotalAmount(),
            cart.getItems().stream()
                .filter(item -> item.getProductId().equals(product.getId()))
                .findFirst()
                .map(CartItem::getSubtotal)
                .orElse(null),
            itemsData
        );

        outputBoundary.present(outputData);
    }

    private void presentRemovalSuccess(Cart cart, Long productId, String productName, int oldQuantity) {
        // Build cart items data (after removal)
        List<UpdateCartQuantityOutputData.CartItemData> itemsData = new ArrayList<>();
        for (CartItem item : cart.getItems()) {
            Product p = productRepository.findById(item.getProductId()).orElse(null);
            if (p != null) {
                itemsData.add(new UpdateCartQuantityOutputData.CartItemData(
                    item.getProductId(),
                    p.getName(),
                    item.getProductPrice(),
                    item.getQuantity(),
                    item.getSubtotal()
                ));
            }
        }

        UpdateCartQuantityOutputData outputData = new UpdateCartQuantityOutputData(
            cart.getId(),
            cart.getUserId(),
            productId,
            productName,
            oldQuantity,
            0,
            true, // item removed
            cart.getItemCount(),
            cart.getTotalItemCount(),
            cart.getTotalAmount(),
            null, // no subtotal since item was removed
            itemsData
        );

        outputBoundary.present(outputData);
    }

    private void presentError(String errorCode, String errorMessage) {
        UpdateCartQuantityOutputData outputData = 
            new UpdateCartQuantityOutputData(errorCode, errorMessage);
        outputBoundary.present(outputData);
    }
}
