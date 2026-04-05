package com.motorbike.adapters.controllers;

import com.motorbike.business.dto.productdetail.GetProductDetailInputData;
import com.motorbike.business.usecase.input.GetAllProductsInputBoundary;
import com.motorbike.business.usecase.input.GetProductDetailInputBoundary;
import com.motorbike.adapters.viewmodels.ProductDetailViewModel;
import com.motorbike.adapters.viewmodels.GetAllProductsViewModel;
import com.motorbike.adapters.dto.response.ProductDetailResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {

    private final GetProductDetailInputBoundary getProductDetailUseCase;
    private final ProductDetailViewModel productDetailViewModel;
    private final GetAllProductsInputBoundary getAllProductsUseCase;
    private final GetAllProductsViewModel getAllProductsViewModel;

    @Autowired
    public ProductController(GetProductDetailInputBoundary getProductDetailUseCase,
                            ProductDetailViewModel productDetailViewModel,
                            GetAllProductsInputBoundary getAllProductsUseCase,
                            GetAllProductsViewModel getAllProductsViewModel) {
        this.getProductDetailUseCase = getProductDetailUseCase;
        this.productDetailViewModel = productDetailViewModel;
        this.getAllProductsUseCase = getAllProductsUseCase;
        this.getAllProductsViewModel = getAllProductsViewModel;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDetailResponse> getProductDetail(@PathVariable Long id) {
        GetProductDetailInputData inputData = new GetProductDetailInputData(id);
        
        getProductDetailUseCase.execute(inputData);

        if (!productDetailViewModel.hasError) {
            ProductDetailResponse response = new ProductDetailResponse(
                true,
                productDetailViewModel.productId,
                productDetailViewModel.name,
                productDetailViewModel.description,
                productDetailViewModel.price,
                productDetailViewModel.imageUrl,
                productDetailViewModel.specifications,
                productDetailViewModel.categoryDisplay,
                productDetailViewModel.stockQuantity,
                productDetailViewModel.availabilityStatus,
                productDetailViewModel.errorCode,
                productDetailViewModel.errorMessage
            );
            return ResponseEntity.ok(response);
        } else {
            ProductDetailResponse response = new ProductDetailResponse(
                false,
                null, null, null, null, null, null, null, null, null,
                productDetailViewModel.errorCode,
                productDetailViewModel.errorMessage
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllProducts() {
        getAllProductsUseCase.execute();
        
        if (getAllProductsViewModel.success) {
            List<Map<String, Object>> productList = getAllProductsViewModel.products.stream()
                .map(product -> {
                    Map<String, Object> productMap = new HashMap<>();
                    productMap.put("id", product.id);
                    productMap.put("name", product.name);
                    productMap.put("description", product.description);
                    productMap.put("price", product.price);
                    productMap.put("stock", product.stock);
                    productMap.put("imageUrl", product.imageUrl);
                    productMap.put("createdDate", product.formattedCreatedDate);
                    productMap.put("available", product.available);
                    productMap.put("category", product.category);
                    
                    if ("XE_MAY".equals(product.category)) {
                        productMap.put("brand", product.brand);
                        productMap.put("model", product.model);
                        productMap.put("color", product.color);
                        productMap.put("engineCapacity", product.engineCapacity);
                        productMap.put("year", product.year);
                    } else if ("PHU_KIEN".equals(product.category)) {
                        productMap.put("type", product.type);
                        productMap.put("brand", product.brand);
                        productMap.put("material", product.material);
                        productMap.put("size", product.size);
                    }
                    
                    return productMap;
                })
                .collect(Collectors.toList());
            
            return ResponseEntity.ok(productList);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(null);
        }
    }
}
