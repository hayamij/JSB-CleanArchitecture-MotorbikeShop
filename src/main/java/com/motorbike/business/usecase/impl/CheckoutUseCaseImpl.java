package com.motorbike.business.usecase.impl;

import com.motorbike.business.dto.checkout.CheckoutInputData;
import com.motorbike.business.dto.checkout.CheckoutOutputData;
import com.motorbike.business.dto.checkout.CheckoutOutputData.OrderItemData;
import com.motorbike.business.usecase.CheckoutInputBoundary;
import com.motorbike.business.usecase.CheckoutOutputBoundary;
import com.motorbike.domain.entities.*;
import com.motorbike.business.ports.repository.CartRepository;
import com.motorbike.business.ports.repository.ProductRepository;
import com.motorbike.business.ports.repository.UserRepository;
import com.motorbike.domain.repositories.OrderRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Use Case Implementation: CheckoutUseCaseImpl
 * Handles the checkout/payment process
 * 
 * Business Rules:
 * - User must be logged in (userId required)
 * - Cart must have at least 1 product
 * - Check stock availability for all products in cart
 * - Calculate total amount
 * - Create Order with PENDING status
 * - Create OrderItems from CartItems
 * - Deduct stock quantity for each product
 * - Clear cart after successful checkout
 */
public class CheckoutUseCaseImpl implements CheckoutInputBoundary {
    private final CheckoutOutputBoundary outputBoundary;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public CheckoutUseCaseImpl(CheckoutOutputBoundary outputBoundary,
                              UserRepository userRepository,
                              CartRepository cartRepository,
                              ProductRepository productRepository,
                              OrderRepository orderRepository) {
        this.outputBoundary = outputBoundary;
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public void execute(CheckoutInputData inputData) {
        // Business Rule: User must be logged in
        if (inputData.getUserId() == null) {
            CheckoutOutputData outputData = new CheckoutOutputData(
                "USER_NOT_LOGGED_IN",
                "Bạn phải đăng nhập để thực hiện thanh toán"
            );
            outputBoundary.present(outputData);
            return;
        }

        // Get user information
        Optional<User> userOptional = userRepository.findById(inputData.getUserId());
        if (userOptional.isEmpty()) {
            CheckoutOutputData outputData = new CheckoutOutputData(
                "USER_NOT_FOUND",
                "Không tìm thấy thông tin người dùng"
            );
            outputBoundary.present(outputData);
            return;
        }
        User user = userOptional.get();

        // Business Rule: Cart must have at least 1 product
        Optional<Cart> cartOptional = cartRepository.findByUserId(inputData.getUserId());
        if (cartOptional.isEmpty() || cartOptional.get().getItems().isEmpty()) {
            CheckoutOutputData outputData = new CheckoutOutputData(
                "CART_EMPTY",
                "Giỏ hàng trống. Vui lòng thêm sản phẩm trước khi thanh toán"
            );
            outputBoundary.present(outputData);
            return;
        }
        Cart cart = cartOptional.get();

        // Business Rule: Check stock availability for all products
        for (CartItem cartItem : cart.getItems()) {
            Optional<Product> productOptional = productRepository.findById(cartItem.getProductId());
            if (productOptional.isEmpty()) {
                CheckoutOutputData outputData = new CheckoutOutputData(
                    "PRODUCT_NOT_FOUND",
                    "Sản phẩm '" + cartItem.getProductName() + "' không còn tồn tại"
                );
                outputBoundary.present(outputData);
                return;
            }

            Product product = productOptional.get();
            
            // Check if product is available
            if (!product.isAvailable()) {
                CheckoutOutputData outputData = new CheckoutOutputData(
                    "PRODUCT_NOT_AVAILABLE",
                    "Sản phẩm '" + product.getName() + "' hiện không còn bán"
                );
                outputBoundary.present(outputData);
                return;
            }

            // Check stock quantity
            if (product.getStockQuantity() < cartItem.getQuantity()) {
                CheckoutOutputData outputData = new CheckoutOutputData(
                    "INSUFFICIENT_STOCK",
                    "Sản phẩm '" + product.getName() + "' chỉ còn " + 
                    product.getStockQuantity() + " sản phẩm trong kho"
                );
                outputBoundary.present(outputData);
                return;
            }
        }

        // Create Order
        // Use provided address or require it (user entity doesn't have default address)
        if (inputData.getShippingAddress() == null || inputData.getShippingAddress().trim().isEmpty()) {
            CheckoutOutputData outputData = new CheckoutOutputData(
                "SHIPPING_ADDRESS_REQUIRED",
                "Vui lòng cung cấp địa chỉ giao hàng"
            );
            outputBoundary.present(outputData);
            return;
        }
        
        String shippingAddress = inputData.getShippingAddress();
        String customerPhone = inputData.getCustomerPhone() != null ?
                              inputData.getCustomerPhone() : user.getPhoneNumber();

        Order order = new Order(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            customerPhone,
            shippingAddress
        );

        // Create OrderItems from CartItems and deduct stock
        List<OrderItemData> orderItemDataList = new ArrayList<>();
        for (CartItem cartItem : cart.getItems()) {
            Product product = productRepository.findById(cartItem.getProductId()).get();
            
            // Create OrderItem
            OrderItem orderItem = new OrderItem(
                product.getId(),
                product.getName(),
                product.getPrice(),
                cartItem.getQuantity()
            );
            order.addItem(orderItem);

            // Deduct stock quantity
            int newStock = product.getStockQuantity() - cartItem.getQuantity();
            product.setStockQuantity(newStock);
            productRepository.save(product);

            // Prepare data for output
            orderItemDataList.add(new OrderItemData(
                product.getId(),
                product.getName(),
                product.getPrice(),
                cartItem.getQuantity(),
                orderItem.getSubtotal()
            ));
        }

        // Save order
        Order savedOrder = orderRepository.save(order);

        // Clear cart after successful checkout
        cartRepository.delete(cart.getId());

        // Prepare success output
        CheckoutOutputData outputData = new CheckoutOutputData(
            savedOrder.getId(),
            savedOrder.getCustomerId(),
            savedOrder.getCustomerName(),
            savedOrder.getCustomerEmail(),
            savedOrder.getCustomerPhone(),
            savedOrder.getShippingAddress(),
            savedOrder.getStatus().name(),
            savedOrder.getTotalAmount(),
            savedOrder.getTotalItems(),
            savedOrder.getTotalQuantity(),
            orderItemDataList,
            savedOrder.getCreatedAt()
        );

        outputBoundary.present(outputData);
    }
}
