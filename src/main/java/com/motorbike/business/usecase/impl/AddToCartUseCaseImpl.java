package com.motorbike.business.usecase.impl;

import com.motorbike.business.dto.addtocart.AddToCartInputData;
import com.motorbike.business.dto.addtocart.AddToCartOutputData;
import com.motorbike.business.ports.repository.CartRepository;
import com.motorbike.business.ports.repository.ProductRepository;
import com.motorbike.business.usecase.AddToCartInputBoundary;
import com.motorbike.business.usecase.AddToCartOutputBoundary;
import com.motorbike.domain.entities.Cart;
import com.motorbike.domain.entities.CartItem;
import com.motorbike.domain.entities.Product;
import com.motorbike.domain.exceptions.InvalidCartException;
import com.motorbike.domain.exceptions.InvalidProductException;

import java.util.Optional;

/**
 * AddToCart Use Case Implementation
 * Contains application-specific business rules for adding products to cart
 * NO dependencies on frameworks or UI
 * 
 * Business Rules:
 * 1. Sản phẩm phải tồn tại và còn hàng
 * 2. Số lượng thêm vào phải > 0
 * 3. Số lượng thêm vào không được vượt quá số lượng tồn kho
 * 4. Nếu sản phẩm đã có trong giỏ, cộng dồn số lượng (không tạo item mới)
 * 5. Guest có thể thêm vào giỏ (lưu trong session/cookie)
 * 6. Customer đã login lưu giỏ hàng vào database
 */
public class AddToCartUseCaseImpl implements AddToCartInputBoundary {
    
    private final AddToCartOutputBoundary outputBoundary;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;

    /**
     * Constructor with dependency injection
     * @param outputBoundary Presenter interface
     * @param productRepository Product repository interface
     * @param cartRepository Cart repository interface
     */
    public AddToCartUseCaseImpl(AddToCartOutputBoundary outputBoundary,
                               ProductRepository productRepository,
                               CartRepository cartRepository) {
        this.outputBoundary = outputBoundary;
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
    }

    @Override
    public void execute(AddToCartInputData inputData) {
        try {
            // Step 1: Validate input
            validateInput(inputData);
            
            // Step 2: Find product
            // Business Rule: Sản phẩm phải tồn tại
            Optional<Product> productOptional = productRepository.findById(inputData.getProductId());
            if (productOptional.isEmpty()) {
                outputBoundary.present(new AddToCartOutputData(
                    "PRODUCT_NOT_FOUND",
                    "Sản phẩm không tồn tại"
                ));
                return;
            }
            
            Product product = productOptional.get();
            
            // Step 3: Check if product is in stock
            // Business Rule: Sản phẩm phải còn hàng
            if (!product.isInStock()) {
                outputBoundary.present(new AddToCartOutputData(
                    "PRODUCT_OUT_OF_STOCK",
                    "Sản phẩm đã hết hàng"
                ));
                return;
            }
            
            // Step 4: Check if quantity is valid
            // Business Rule: Số lượng không được vượt quá tồn kho
            if (inputData.getQuantity() > product.getStockQuantity()) {
                outputBoundary.present(new AddToCartOutputData(
                    "INSUFFICIENT_STOCK",
                    String.format("Chỉ còn %d sản phẩm trong kho", product.getStockQuantity())
                ));
                return;
            }
            
            // Step 5: Get or create cart
            Cart cart = getOrCreateCart(inputData);
            
            // Step 6: Check if adding this quantity would exceed stock
            boolean itemAlreadyInCart = false;
            int newItemQuantity = inputData.getQuantity();
            
            // Find existing item in cart
            Optional<CartItem> existingItemOpt = cart.getItems().stream()
                .filter(item -> item.getProductId().equals(inputData.getProductId()))
                .findFirst();
            
            if (existingItemOpt.isPresent()) {
                itemAlreadyInCart = true;
                CartItem existingItem = existingItemOpt.get();
                newItemQuantity = existingItem.getQuantity() + inputData.getQuantity();
                
                // Business Rule: Tổng số lượng không được vượt quá tồn kho
                if (newItemQuantity > product.getStockQuantity()) {
                    outputBoundary.present(new AddToCartOutputData(
                        "INSUFFICIENT_STOCK",
                        String.format("Bạn đã có %d sản phẩm trong giỏ. Chỉ còn %d sản phẩm trong kho",
                            existingItem.getQuantity(), product.getStockQuantity())
                    ));
                    return;
                }
            }
            
            // Step 7: Create cart item and add to cart
            CartItem cartItem = new CartItem(
                product.getId(),
                product.getName(),
                product.getPrice(),
                inputData.getQuantity()
            );
            
            // Business Rule: Nếu sản phẩm đã có trong giỏ, cộng dồn số lượng
            cart.addItem(cartItem);
            
            // Step 8: Save cart
            Cart savedCart = cartRepository.save(cart);
            
            // Step 9: Prepare success output
            AddToCartOutputData outputData = new AddToCartOutputData(
                savedCart.getId(),
                savedCart.getItemCount(),
                savedCart.getTotalItemCount(),
                savedCart.getTotalAmount(),
                product.getId(),
                product.getName(),
                inputData.getQuantity(),
                newItemQuantity,
                itemAlreadyInCart,
                product.getPrice(),
                product.getStockQuantity()
            );
            
            // Step 10: Present result to output boundary
            outputBoundary.present(outputData);
            
        } catch (InvalidProductException e) {
            // Handle product validation errors
            outputBoundary.present(new AddToCartOutputData(
                e.getErrorCode(),
                e.getMessage()
            ));
        } catch (InvalidCartException e) {
            // Handle cart validation errors
            outputBoundary.present(new AddToCartOutputData(
                e.getErrorCode(),
                e.getMessage()
            ));
        } catch (Exception e) {
            // Handle unexpected errors
            outputBoundary.present(new AddToCartOutputData(
                "UNEXPECTED_ERROR",
                "Đã xảy ra lỗi không mong muốn: " + e.getMessage()
            ));
        }
    }

    /**
     * Validate input data
     * @param inputData Input data to validate
     */
    private void validateInput(AddToCartInputData inputData) {
        if (inputData == null) {
            throw new InvalidCartException("NULL_INPUT", "Dữ liệu đầu vào không được null");
        }
        
        // Validate product ID
        if (inputData.getProductId() == null) {
            throw new InvalidProductException("NULL_PRODUCT_ID", "ID sản phẩm không được null");
        }
        
        // Business Rule: Số lượng thêm vào phải > 0
        if (inputData.getQuantity() <= 0) {
            throw new InvalidCartException("INVALID_QUANTITY", "Số lượng phải lớn hơn 0");
        }
    }

    /**
     * Get existing cart or create new one
     * @param inputData Input data containing user/cart info
     * @return Cart entity
     */
    private Cart getOrCreateCart(AddToCartInputData inputData) {
        Cart cart;
        
        if (inputData.isGuestUser()) {
            // Business Rule: Guest có thể thêm vào giỏ
            if (inputData.getGuestCartId() != null) {
                // Try to find existing guest cart
                Optional<Cart> cartOptional = cartRepository.findById(inputData.getGuestCartId());
                cart = cartOptional.orElse(new Cart(null)); // null userId for guest
            } else {
                // Create new guest cart
                cart = new Cart(null);
            }
        } else {
            // Business Rule: Customer đã login lưu giỏ hàng vào database
            Optional<Cart> cartOptional = cartRepository.findByUserId(inputData.getUserId());
            cart = cartOptional.orElse(new Cart(inputData.getUserId()));
        }
        
        return cart;
    }
}
