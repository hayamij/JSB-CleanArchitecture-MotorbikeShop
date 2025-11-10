package com.motorbike.interfaceadapters.controller;

import com.motorbike.business.exception.ProductNotFoundException;
import com.motorbike.business.exception.ProductOutOfStockException;
import com.motorbike.business.usecase.AddToCartUseCase;
import com.motorbike.interfaceadapters.dto.AddToCartRequestDTO;
import com.motorbike.interfaceadapters.dto.AddToCartResponseDTO;
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
    
    public CartController(AddToCartUseCase addToCartUseCase) {
        this.addToCartUseCase = addToCartUseCase;
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
