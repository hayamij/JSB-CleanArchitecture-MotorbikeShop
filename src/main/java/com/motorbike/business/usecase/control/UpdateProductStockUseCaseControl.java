package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.updateproductstock.UpdateProductStockInputData;
import com.motorbike.business.dto.updateproductstock.UpdateProductStockOutputData;
import com.motorbike.business.usecase.input.UpdateProductStockInputBoundary;
import com.motorbike.business.usecase.output.UpdateProductStockOutputBoundary;
import com.motorbike.business.ports.repository.ProductRepository;
import com.motorbike.domain.entities.SanPham;
import com.motorbike.domain.exceptions.DomainException;
import java.util.Optional;

public class UpdateProductStockUseCaseControl implements UpdateProductStockInputBoundary {
    
    private final UpdateProductStockOutputBoundary outputBoundary;
    private final ProductRepository productRepository;
    
    public UpdateProductStockUseCaseControl(UpdateProductStockOutputBoundary outputBoundary, ProductRepository productRepository) {
        this.outputBoundary = outputBoundary;
        this.productRepository = productRepository;
    }
    
    // Constructor for tests with swapped parameters (repository first)
    public UpdateProductStockUseCaseControl(
            com.motorbike.business.ports.SanPhamRepository sanPhamRepository,
            UpdateProductStockOutputBoundary outputBoundary) {
        this.outputBoundary = outputBoundary;
        this.productRepository = (ProductRepository) sanPhamRepository;
    }
    
    @Override
    public void execute(UpdateProductStockInputData inputData) {
        UpdateProductStockOutputData outputData = updateInternal(inputData);
        outputBoundary.present(outputData);
    }
    
    public UpdateProductStockOutputData updateInternal(UpdateProductStockInputData inputData) {
        UpdateProductStockOutputData outputData = null;
        Exception errorException = null;
        
        try {
            Long productId = inputData.getProductId();
            int stockChange = inputData.getQuantityChange();
            
            Optional<SanPham> productOpt = productRepository.findById(productId);
            if (productOpt.isEmpty()) {
                throw DomainException.productNotFound(productId);
            }
            
            SanPham product = productOpt.get();
            
            if (stockChange > 0) {
                product.tangTonKho(stockChange);
            } else if (stockChange < 0) {
                product.giamTonKho(Math.abs(stockChange));
            }
            
            productRepository.save(product);
            
            outputData = new UpdateProductStockOutputData(true, "Product stock updated successfully", productId, product.getSoLuongTonKho());
            
        } catch (Exception e) {
            errorException = e;
            outputData = new UpdateProductStockOutputData(false, e.getMessage(), inputData != null ? inputData.getProductId() : null, 0);
        }
        
        return outputData;
    }
}
