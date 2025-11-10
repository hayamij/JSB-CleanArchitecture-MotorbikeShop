package com.motorbike.interfaceadapters.controller;

import com.motorbike.business.exception.EmptyCartException;
import com.motorbike.business.usecase.CheckoutUseCase;
import com.motorbike.interfaceadapters.dto.CheckoutRequestDTO;
import com.motorbike.interfaceadapters.dto.CheckoutResponseDTO;
import com.motorbike.interfaceadapters.mapper.OrderDTOMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Order Controller - Interface Adapters Layer
 * REST API controller for order/checkout operations
 * Part of Clean Architecture - Interface Adapters Layer
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {
    
    private final CheckoutUseCase checkoutUseCase;
    
    public OrderController(CheckoutUseCase checkoutUseCase) {
        this.checkoutUseCase = checkoutUseCase;
    }
    
    /**
     * Checkout and create order
     * POST /api/orders/checkout
     * @param requestDTO request containing checkout information
     * @return response with created order
     */
    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(@RequestBody CheckoutRequestDTO requestDTO) {
        try {
            // Validate request
            if (requestDTO.getUserId() == null) {
                return ResponseEntity.badRequest()
                        .body(createErrorResponse("User ID is required"));
            }
            if (requestDTO.getShippingAddress() == null || requestDTO.getShippingAddress().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(createErrorResponse("Shipping address is required"));
            }
            if (requestDTO.getShippingCity() == null || requestDTO.getShippingCity().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(createErrorResponse("Shipping city is required"));
            }
            if (requestDTO.getShippingPhone() == null || requestDTO.getShippingPhone().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(createErrorResponse("Shipping phone is required"));
            }
            if (requestDTO.getPaymentMethod() == null || requestDTO.getPaymentMethod().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(createErrorResponse("Payment method is required"));
            }
            
            // Execute use case
            CheckoutUseCase.CheckoutRequest request = new CheckoutUseCase.CheckoutRequest(
                    requestDTO.getUserId(),
                    requestDTO.getShippingAddress(),
                    requestDTO.getShippingCity(),
                    requestDTO.getShippingPhone(),
                    requestDTO.getPaymentMethod()
            );
            
            CheckoutUseCase.CheckoutResponse response = checkoutUseCase.execute(request);
            
            // Map to DTO
            CheckoutResponseDTO responseDTO = new CheckoutResponseDTO(
                    OrderDTOMapper.toDTO(response.getOrder()),
                    response.getMessage(),
                    response.isSuccess()
            );
            
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse(e.getMessage()));
                    
        } catch (EmptyCartException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
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
