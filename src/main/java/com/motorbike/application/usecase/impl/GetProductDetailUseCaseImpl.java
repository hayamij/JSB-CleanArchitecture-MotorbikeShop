package com.motorbike.application.usecase.impl;

import com.motorbike.application.usecase.GetProductDetailUseCase;
import com.motorbike.domain.entity.Product;
import com.motorbike.domain.repository.ProductRepository;
import org.springframework.stereotype.Service;

/**
 * Implementation of Get Product Detail Use Case
 * Contains the business logic for retrieving product details
 */
@Service
public class GetProductDetailUseCaseImpl implements GetProductDetailUseCase {
    
    private final ProductRepository productRepository;
    
    public GetProductDetailUseCaseImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    
    @Override
    public ProductDetailResponse execute(ProductDetailRequest request) {
        // Step 1: Validate input
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }
        
        // Step 2: Retrieve product from repository
        Product product = productRepository.findById(request.getProductId())
            .orElseThrow(() -> new ProductNotFoundException(
                "Product not found with id: " + request.getProductId()
            ));
        
        // Step 3: Apply business rules if needed
        // For example, we could log the view, increment view counter, etc.
        
        // Step 4: Return response
        return new ProductDetailResponse(product);
    }
}
