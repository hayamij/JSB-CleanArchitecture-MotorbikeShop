package com.motorbike.interfaceadapters.controller;

import com.motorbike.business.exception.CartItemNotFoundException;
import com.motorbike.business.exception.ProductNotFoundException;
import com.motorbike.business.exception.ProductOutOfStockException;
import com.motorbike.business.usecase.AddToCartUseCase;
import com.motorbike.business.usecase.UpdateCartQuantityUseCase;
import com.motorbike.business.usecase.ViewCartUseCase;
import com.motorbike.interfaceadapters.dto.AddToCartRequestDTO;
import com.motorbike.interfaceadapters.dto.AddToCartResponseDTO;
import com.motorbike.interfaceadapters.dto.UpdateCartQuantityRequestDTO;
import com.motorbike.interfaceadapters.dto.UpdateCartQuantityResponseDTO;
import com.motorbike.interfaceadapters.dto.ViewCartResponseDTO;
import com.motorbike.interfaceadapters.mapper.CartDTOMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Cart Controller - Interface Adapters Layer
 * REST API controller for cart operations
 * Part of Clean Architecture - Interface Adapters Layer
 */
@RestController
@RequestMapping("/api/cart")
public class CartController {
    
    private final AddToCartUseCase addToCartUseCase;
    private final ViewCartUseCase viewCartUseCase;
    private final UpdateCartQuantityUseCase updateCartQuantityUseCase;
    
    public CartController(AddToCartUseCase addToCartUseCase, 
                         ViewCartUseCase viewCartUseCase,
                         UpdateCartQuantityUseCase updateCartQuantityUseCase) {
        this.addToCartUseCase = addToCartUseCase;
        this.viewCartUseCase = viewCartUseCase;
        this.updateCartQuantityUseCase = updateCartQuantityUseCase;
    }
    
    /**
     * Add product to cart
     * POST /api/cart/add
     * @param requestDTO request containing userId, productId, and quantity
     * @return response with updated cart
     */
    @PostMapping("/add")
    public ResponseEntity<?> addToCart(@RequestBody AddToCartRequestDTO requestDTO) {
        try {
            // Validate request
            if (requestDTO.getUserId() == null) {
                return ResponseEntity.badRequest()
                        .body(createErrorResponse("User ID is required"));
            }
            if (requestDTO.getProductId() == null) {
                return ResponseEntity.badRequest()
                        .body(createErrorResponse("Product ID is required"));
            }
            if (requestDTO.getQuantity() == null || requestDTO.getQuantity() <= 0) {
                return ResponseEntity.badRequest()
                        .body(createErrorResponse("Quantity must be greater than 0"));
            }
            
            // Execute use case
            AddToCartUseCase.AddToCartRequest request = new AddToCartUseCase.AddToCartRequest(
                    requestDTO.getUserId(),
                    requestDTO.getProductId(),
                    requestDTO.getQuantity()
            );
            
            AddToCartUseCase.AddToCartResponse response = addToCartUseCase.execute(request);
            
            // Map to DTO
            AddToCartResponseDTO responseDTO = new AddToCartResponseDTO(
                    CartDTOMapper.toDTO(response.getCart()),
                    response.getMessage(),
                    response.isSuccess()
            );
            
            return ResponseEntity.ok(responseDTO);
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse(e.getMessage()));
                    
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(createErrorResponse(e.getMessage()));
                    
        } catch (ProductOutOfStockException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(createErrorResponse(e.getMessage()));
                    
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("An error occurred: " + e.getMessage()));
        }
    }
    
    /**
     * View cart
     * GET /api/cart/{userId}
     * @param userId user ID
     * @return response with cart contents
     */
    @GetMapping("/{userId}")
    public ResponseEntity<?> viewCart(@PathVariable Long userId) {
        try {
            // Validate user ID
            if (userId == null) {
                return ResponseEntity.badRequest()
                        .body(createErrorResponse("User ID is required"));
            }
            
            // Execute use case
            ViewCartUseCase.ViewCartRequest request = new ViewCartUseCase.ViewCartRequest(userId);
            ViewCartUseCase.ViewCartResponse response = viewCartUseCase.execute(request);
            
            // Map to DTO
            ViewCartResponseDTO responseDTO = new ViewCartResponseDTO(
                    CartDTOMapper.toDTO(response.getCart()),
                    response.isEmpty(),
                    response.getMessage()
            );
            
            return ResponseEntity.ok(responseDTO);
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse(e.getMessage()));
                    
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("An error occurred: " + e.getMessage()));
        }
    }
    
    /**
     * Update cart item quantity
     * PUT /api/cart/update
     * @param requestDTO request containing userId, productId, and new quantity
     * @return response with updated cart
     */
    @PutMapping("/update")
    public ResponseEntity<?> updateCartQuantity(@RequestBody UpdateCartQuantityRequestDTO requestDTO) {
        try {
            // Validate request
            if (requestDTO.getUserId() == null) {
                return ResponseEntity.badRequest()
                        .body(createErrorResponse("User ID is required"));
            }
            if (requestDTO.getProductId() == null) {
                return ResponseEntity.badRequest()
                        .body(createErrorResponse("Product ID is required"));
            }
            if (requestDTO.getNewQuantity() == null) {
                return ResponseEntity.badRequest()
                        .body(createErrorResponse("New quantity is required"));
            }
            if (requestDTO.getNewQuantity() < 0) {
                return ResponseEntity.badRequest()
                        .body(createErrorResponse("Quantity cannot be negative"));
            }
            
            // Execute use case
            UpdateCartQuantityUseCase.UpdateCartQuantityRequest request = 
                    new UpdateCartQuantityUseCase.UpdateCartQuantityRequest(
                            requestDTO.getUserId(),
                            requestDTO.getProductId(),
                            requestDTO.getNewQuantity()
                    );
            
            UpdateCartQuantityUseCase.UpdateCartQuantityResponse response = 
                    updateCartQuantityUseCase.execute(request);
            
            // Map to DTO
            UpdateCartQuantityResponseDTO responseDTO = new UpdateCartQuantityResponseDTO(
                    CartDTOMapper.toDTO(response.getCart()),
                    response.getMessage(),
                    response.isItemRemoved(),
                    response.isSuccess()
            );
            
            return ResponseEntity.ok(responseDTO);
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse(e.getMessage()));
                    
        } catch (CartItemNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(createErrorResponse(e.getMessage()));
                    
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(createErrorResponse(e.getMessage()));
                    
        } catch (ProductOutOfStockException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(createErrorResponse(e.getMessage()));
                    
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("An error occurred: " + e.getMessage()));
        }
    }
    
    /**
     * Create error response map
     * @param message error message
     * @return error response map
     */
    private Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> error = new HashMap<>();
        error.put("success", false);
        error.put("message", message);
        return error;
    }
}
