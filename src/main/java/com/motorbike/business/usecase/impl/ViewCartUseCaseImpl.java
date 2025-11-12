package com.motorbike.business.usecase.impl;

import com.motorbike.business.dto.viewcart.ViewCartInputData;
import com.motorbike.business.dto.viewcart.ViewCartOutputData;
import com.motorbike.business.dto.viewcart.ViewCartOutputData.CartItemData;
import com.motorbike.business.ports.repository.CartRepository;
import com.motorbike.business.ports.repository.ProductRepository;
import com.motorbike.business.usecase.ViewCartInputBoundary;
import com.motorbike.business.usecase.ViewCartOutputBoundary;
import com.motorbike.domain.entities.Cart;
import com.motorbike.domain.entities.CartItem;
import com.motorbike.domain.entities.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Use Case Implementation: ViewCartUseCaseImpl
 * Handles viewing cart with stock validation
 * 
 * Business Rules:
 * - Guest: display cart from session/cookie (by guestCartId)
 * - Customer: display cart from database (by userId)
 * - Display item info: name, price, quantity, subtotal
 * - Calculate total amount for entire cart
 * - Check and warn if product in cart doesn't have enough stock
 */
public class ViewCartUseCaseImpl implements ViewCartInputBoundary {
    private final ViewCartOutputBoundary outputBoundary;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public ViewCartUseCaseImpl(ViewCartOutputBoundary outputBoundary,
                              CartRepository cartRepository,
                              ProductRepository productRepository) {
        this.outputBoundary = outputBoundary;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    @Override
    public void execute(ViewCartInputData inputData) {
        Cart cart = null;

        // Get cart based on user type
        if (inputData.isLoggedIn()) {
            // Logged-in user: get cart by userId
            Optional<Cart> cartOptional = cartRepository.findByUserId(inputData.getUserId());
            if (cartOptional.isEmpty()) {
                // User has no cart yet - return empty cart view
                presentEmptyCart(inputData.getUserId());
                return;
            }
            cart = cartOptional.get();
        } else if (inputData.isGuest()) {
            // Guest user: get cart by guestCartId
            Optional<Cart> cartOptional = cartRepository.findById(inputData.getGuestCartId());
            if (cartOptional.isEmpty()) {
                // Guest cart not found - return empty cart view
                presentEmptyCart(null);
                return;
            }
            cart = cartOptional.get();
        } else {
            // No user ID or guest cart ID provided
            ViewCartOutputData outputData = new ViewCartOutputData(
                "INVALID_INPUT",
                "Không tìm thấy thông tin giỏ hàng"
            );
            outputBoundary.present(outputData);
            return;
        }

        // Check if cart is empty
        if (cart.getItems().isEmpty()) {
            presentEmptyCart(cart.getUserId());
            return;
        }

        // Process cart items and check stock
        List<CartItemData> cartItemDataList = new ArrayList<>();
        boolean hasStockWarnings = false;

        for (CartItem cartItem : cart.getItems()) {
            Optional<Product> productOptional = productRepository.findById(cartItem.getProductId());
            
            if (productOptional.isEmpty()) {
                // Product no longer exists
                cartItemDataList.add(new CartItemData(
                    cartItem.getProductId(),
                    cartItem.getProductName(),
                    null,
                    cartItem.getProductPrice(),
                    cartItem.getQuantity(),
                    cartItem.getSubtotal(),
                    0,
                    true,
                    "Sản phẩm không còn tồn tại"
                ));
                hasStockWarnings = true;
                continue;
            }

            Product product = productOptional.get();
            boolean itemHasWarning = false;
            String warningMessage = null;

            // Check stock availability
            if (!product.isAvailable()) {
                itemHasWarning = true;
                warningMessage = "Sản phẩm hiện không còn bán";
                hasStockWarnings = true;
            } else if (product.getStockQuantity() == 0) {
                itemHasWarning = true;
                warningMessage = "Sản phẩm đã hết hàng";
                hasStockWarnings = true;
            } else if (product.getStockQuantity() < cartItem.getQuantity()) {
                itemHasWarning = true;
                warningMessage = String.format("Chỉ còn %d sản phẩm trong kho", product.getStockQuantity());
                hasStockWarnings = true;
            }

            cartItemDataList.add(new CartItemData(
                product.getId(),
                product.getName(),
                product.getImageUrl(),
                product.getPrice(),
                cartItem.getQuantity(),
                cartItem.getSubtotal(),
                product.getStockQuantity(),
                itemHasWarning,
                warningMessage
            ));
        }

        // Prepare success output (totals are already calculated in Cart entity)
        ViewCartOutputData outputData = new ViewCartOutputData(
            cart.getId(),
            cart.getUserId(),
            cart.getItemCount(),        // Number of different products
            cart.getTotalItemCount(),   // Total quantity of all items
            cart.getTotalAmount(),
            cartItemDataList,
            false,
            hasStockWarnings
        );

        outputBoundary.present(outputData);
    }

    private void presentEmptyCart(Long userId) {
        ViewCartOutputData outputData = new ViewCartOutputData(
            null,
            userId,
            0,
            0,
            BigDecimal.ZERO,
            new ArrayList<>(),
            true,
            false
        );
        outputBoundary.present(outputData);
    }
}
