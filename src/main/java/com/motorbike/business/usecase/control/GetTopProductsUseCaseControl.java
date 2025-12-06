package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.topproducts.GetTopProductsInputData;
import com.motorbike.business.dto.topproducts.GetTopProductsOutputData;
import com.motorbike.business.dto.topproducts.GetTopProductsOutputData.ProductSalesInfo;
import com.motorbike.business.ports.repository.OrderRepository;
import com.motorbike.business.usecase.output.GetTopProductsOutputBoundary;
import com.motorbike.domain.entities.ProductSalesStats;
import com.motorbike.domain.exceptions.ValidationException;

import java.util.List;
import java.util.stream.Collectors;

public class GetTopProductsUseCaseControl {
    
    private final GetTopProductsOutputBoundary outputBoundary;
    private final OrderRepository orderRepository;
    
    public GetTopProductsUseCaseControl(GetTopProductsOutputBoundary outputBoundary,
                                        OrderRepository orderRepository) {
        this.outputBoundary = outputBoundary;
        this.orderRepository = orderRepository;
    }
    
    public void execute(GetTopProductsInputData inputData) {
        GetTopProductsOutputData outputData = null;
        Exception errorException = null;
        
        // Step 1: Validation
        try {
            if (inputData == null) {
                throw ValidationException.invalidInput();
            }
            
            int limit = inputData.getLimit();
            if (limit <= 0) {
                throw new ValidationException("Limit phải lớn hơn 0", "INVALID_LIMIT");
            }
            
            if (limit > 100) {
                throw new ValidationException("Limit không được vượt quá 100", "INVALID_LIMIT");
            }
            
        } catch (Exception e) {
            errorException = e;
        }
        
        // Step 2: Business logic
        if (errorException == null) {
            try {
                // Lấy thống kê từ repository
                List<ProductSalesStats> salesStats = orderRepository.getTopSellingProducts(inputData.getLimit());
                
                // Filter theo business rule: chỉ lấy sản phẩm đang bán chạy
                List<ProductSalesStats> bestSellers = salesStats.stream()
                        .filter(ProductSalesStats::isDangBanChay)
                        .collect(Collectors.toList());
                
                // Map sang DTO
                List<ProductSalesInfo> productInfoList = bestSellers.stream()
                        .map(stat -> new ProductSalesInfo(
                                stat.getMaSanPham(),
                                stat.getTenSanPham(),
                                stat.getTongSoLuongBan()
                        ))
                        .collect(Collectors.toList());
                
                outputData = GetTopProductsOutputData.forSuccess(productInfoList);
                
            } catch (Exception e) {
                errorException = e;
            }
        }
        
        // Step 3: Handle error
        if (errorException != null) {
            String errorCode = extractErrorCode(errorException);
            outputData = GetTopProductsOutputData.forError(errorCode, errorException.getMessage());
        }
        
        // Step 4: Present result
        outputBoundary.present(outputData);
    }
    
    private String extractErrorCode(Exception e) {
        if (e instanceof ValidationException) {
            return "VALIDATION_ERROR";
        }
        return "INTERNAL_ERROR";
    }
}
