package com.motorbike.adapters.controllers;

import com.motorbike.business.dto.productdetail.GetProductDetailInputData;
import com.motorbike.business.usecase.control.GetProductDetailUseCaseControl;
import com.motorbike.business.ports.repository.ProductRepository;
import com.motorbike.domain.entities.SanPham;
import com.motorbike.domain.entities.XeMay;
import com.motorbike.domain.entities.PhuKienXeMay;
import com.motorbike.adapters.viewmodels.ProductDetailViewModel;
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

    private final GetProductDetailUseCaseControl getProductDetailUseCase;
    private final ProductDetailViewModel productDetailViewModel;
    private final ProductRepository productRepository;

    @Autowired
    public ProductController(GetProductDetailUseCaseControl getProductDetailUseCase,
                            ProductDetailViewModel productDetailViewModel,
                            ProductRepository productRepository) {
        this.getProductDetailUseCase = getProductDetailUseCase;
        this.productDetailViewModel = productDetailViewModel;
        this.productRepository = productRepository;
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
                productDetailViewModel.formattedPrice,
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
        List<SanPham> products = productRepository.findAll();
        
        List<Map<String, Object>> productList = products.stream()
            .map(product -> {
                Map<String, Object> productMap = new HashMap<>();
                productMap.put("id", product.getMaSanPham());
                productMap.put("name", product.getTenSanPham());
                productMap.put("description", product.getMoTa());
                productMap.put("price", product.getGia());
                productMap.put("stock", product.getSoLuongTonKho());
                productMap.put("imageUrl", product.getHinhAnh());
                
                if (product instanceof XeMay) {
                    productMap.put("category", "XE_MAY");
                } else if (product instanceof PhuKienXeMay) {
                    productMap.put("category", "PHU_KIEN");
                } else {
                    productMap.put("category", "KHAC");
                }
                
                return productMap;
            })
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(productList);
    }
}
