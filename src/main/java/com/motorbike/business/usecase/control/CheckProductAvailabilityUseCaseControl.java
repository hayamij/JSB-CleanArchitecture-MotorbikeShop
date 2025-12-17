package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.checkproductavailability.CheckProductAvailabilityInputData;
import com.motorbike.business.dto.checkproductavailability.CheckProductAvailabilityOutputData;
import com.motorbike.business.usecase.input.CheckProductAvailabilityInputBoundary;
import com.motorbike.business.usecase.output.CheckProductAvailabilityOutputBoundary;
import com.motorbike.business.ports.repository.ProductRepository;
import com.motorbike.domain.entities.SanPham;
import com.motorbike.domain.exceptions.DomainException;
import java.util.Optional;

public class CheckProductAvailabilityUseCaseControl implements CheckProductAvailabilityInputBoundary {
    
    private final CheckProductAvailabilityOutputBoundary outputBoundary;
    private final ProductRepository productRepository;
    
    public CheckProductAvailabilityUseCaseControl(CheckProductAvailabilityOutputBoundary outputBoundary, ProductRepository productRepository) {
        this.outputBoundary = outputBoundary;
        this.productRepository = productRepository;
    }
    
    // Constructor for tests without repository
    public CheckProductAvailabilityUseCaseControl(CheckProductAvailabilityOutputBoundary outputBoundary) {
        this.outputBoundary = outputBoundary;
        this.productRepository = null;
    }
    
    @Override
    public void execute(CheckProductAvailabilityInputData inputData) {
        CheckProductAvailabilityOutputData outputData = checkInternal(inputData);
        outputBoundary.present(outputData);
    }
    
    public CheckProductAvailabilityOutputData checkInternal(CheckProductAvailabilityInputData inputData) {
        CheckProductAvailabilityOutputData outputData = null;
        Exception errorException = null;
        
        try {
            Long productId = inputData.getProductId();
            int requiredQuantity = inputData.getRequestedQuantity();
            
            Optional<SanPham> productOpt = productRepository.findById(productId);
            if (productOpt.isEmpty()) {
                throw DomainException.productNotFound(productId);
            }
            
            SanPham product = productOpt.get();
            boolean isAvailable = product.coConHang() && product.duSoLuong(requiredQuantity);
            
            outputData = new CheckProductAvailabilityOutputData(isAvailable, 
                isAvailable ? "Product is available" : "Product is not available", 
                product.getMaSanPham(), product.getSoLuongTonKho());
            
        } catch (Exception e) {
            errorException = e;
            outputData = new CheckProductAvailabilityOutputData(false, e.getMessage(), inputData.getProductId(), 0);
        }
        
        return outputData;
    }
}
