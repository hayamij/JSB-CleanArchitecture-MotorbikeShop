package com.motorbike.business.usecase.impl;

import com.motorbike.business.dto.productdetail.GetProductDetailInputData;
import com.motorbike.business.dto.productdetail.GetProductDetailOutputData;
import com.motorbike.business.ports.repository.ProductRepository;
import com.motorbike.business.usecase.GetProductDetailInputBoundary;
import com.motorbike.business.usecase.GetProductDetailOutputBoundary;
import com.motorbike.domain.entities.Product;

import java.util.Optional;

/**
 * Use Case Implementation: Get Product Detail
 * 
 * Business Rules:
 * - Sản phẩm phải tồn tại trong hệ thống
 * - Hiển thị đầy đủ thông tin: tên, giá, mô tả, hình ảnh, số lượng tồn kho
 * - Không yêu cầu đăng nhập (guest có thể xem)
 * - Phải hiển thị thông tin loại sản phẩm (category)
 * 
 * KHÔNG phụ thuộc vào: Framework, UI, Database specifics
 * Chỉ phụ thuộc vào: Domain entities và Port interfaces
 */
public class GetProductDetailUseCaseImpl implements GetProductDetailInputBoundary {
    
    private final GetProductDetailOutputBoundary outputBoundary;
    private final ProductRepository productRepository;
    
    /**
     * Constructor with dependency injection
     * @param outputBoundary presenter to handle output
     * @param productRepository repository to fetch product data
     */
    public GetProductDetailUseCaseImpl(
            GetProductDetailOutputBoundary outputBoundary,
            ProductRepository productRepository) {
        this.outputBoundary = outputBoundary;
        this.productRepository = productRepository;
    }
    
    @Override
    public void execute(GetProductDetailInputData inputData) {
        GetProductDetailOutputData outputData = new GetProductDetailOutputData();
        
        try {
            // 1. Validate input
            if (inputData == null || inputData.productId == null) {
                outputData.success = false;
                outputData.errorCode = "INVALID_INPUT";
                outputData.errorMessage = "Product ID is required";
                outputBoundary.present(outputData);
                return;
            }
            
            // 2. Fetch product from repository
            Optional<Product> productOptional = productRepository.findById(inputData.productId);
            
            // 3. Business Rule: Sản phẩm phải tồn tại
            if (!productOptional.isPresent()) {
                outputData.success = false;
                outputData.errorCode = "PRODUCT_NOT_FOUND";
                outputData.errorMessage = "Product with ID " + inputData.productId + " not found";
                outputBoundary.present(outputData);
                return;
            }
            
            Product product = productOptional.get();
            
            // 4. Business Logic: Check if product is in stock (from Entity)
            boolean inStock = product.isInStock();
            
            // 5. Prepare success output data
            outputData.success = true;
            outputData.productId = product.getId();
            outputData.name = product.getName();
            outputData.description = product.getDescription();
            outputData.price = product.getPrice();
            outputData.imageUrl = product.getImageUrl();
            outputData.specifications = product.getSpecifications();
            outputData.category = product.getCategory() != null ? product.getCategory().getCategoryCode() : null;
            outputData.stockQuantity = product.getStockQuantity();
            outputData.available = product.isAvailable();
            outputData.inStock = inStock;
            
            // 6. Present output to presenter
            outputBoundary.present(outputData);
            
        } catch (Exception e) {
            // Handle unexpected errors
            outputData.success = false;
            outputData.errorCode = "INTERNAL_ERROR";
            outputData.errorMessage = "An unexpected error occurred: " + e.getMessage();
            outputBoundary.present(outputData);
        }
    }
}
