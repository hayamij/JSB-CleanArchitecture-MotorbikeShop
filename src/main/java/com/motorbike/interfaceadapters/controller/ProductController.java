package com.motorbike.interfaceadapters.controller;

import com.motorbike.business.usecase.GetProductDetailUseCase;
import com.motorbike.business.usecase.GetProductDetailUseCase.ProductDetailRequest;
import com.motorbike.business.usecase.GetProductDetailUseCase.ProductDetailResponse;
import com.motorbike.business.usecase.impl.ProductNotFoundException;
import com.motorbike.interfaceadapters.dto.ProductDTO;
import com.motorbike.interfaceadapters.mapper.ProductMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for Product operations
 * Exposes the Product Use Cases through HTTP endpoints
 */
@RestController
@RequestMapping(value = "/api/products", produces = "application/json;charset=UTF-8")
public class ProductController {
    
    private final GetProductDetailUseCase getProductDetailUseCase;
    private final ProductMapper productMapper;
    
    public ProductController(GetProductDetailUseCase getProductDetailUseCase,
                           ProductMapper productMapper) {
        this.getProductDetailUseCase = getProductDetailUseCase;
        this.productMapper = productMapper;
    }
    
    /**
     * Get product details by ID
     * Endpoint: GET /api/products/{id}
     * 
     * Use Case: Xem chi tiết sản phẩm
     * Actor: Guest, Customer, Admin
     * 
     * @param id Product ID
     * @return ProductDTO with product details
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductDetail(@PathVariable Long id) {
        try {
            // Step 1: Create use case request
            ProductDetailRequest request = new ProductDetailRequest(id);
            
            // Step 2: Execute use case
            ProductDetailResponse response = getProductDetailUseCase.execute(request);
            
            // Step 3: Map to presentation DTO
            ProductDTO productDTO = productMapper.toDTO(response);
            
            // Step 4: Return response
            return ResponseEntity.ok(productDTO);
            
        } catch (ProductNotFoundException e) {
            // Product not found - return 404
            return ResponseEntity.notFound().build();
            
        } catch (IllegalArgumentException e) {
            // Invalid input - return 400
            return ResponseEntity.badRequest().build();
            
        } catch (Exception e) {
            // Internal error - return 500
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
