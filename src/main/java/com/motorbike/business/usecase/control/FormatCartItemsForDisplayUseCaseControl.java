package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.formatcartitems.FormatCartItemsForDisplayInputData;
import com.motorbike.business.dto.formatcartitems.FormatCartItemsForDisplayOutputData;
import com.motorbike.business.dto.formatcartitems.FormatCartItemsForDisplayOutputData.CartItemDisplayData;
import com.motorbike.business.ports.repository.ProductRepository;
import com.motorbike.business.usecase.input.FormatCartItemsForDisplayInputBoundary;
import com.motorbike.business.usecase.output.FormatCartItemsForDisplayOutputBoundary;
import com.motorbike.domain.entities.ChiTietGioHang;
import com.motorbike.domain.entities.SanPham;

import java.util.ArrayList;
import java.util.List;

/**
 * UC-78: Format Cart Items For Display Use Case
 * Responsibility: Format cart items (ChiTietGioHang) into display-ready DTOs with:
 * - Product information (image URL, stock)
 * - Stock warnings (if quantity exceeds available stock)
 */
public class FormatCartItemsForDisplayUseCaseControl implements FormatCartItemsForDisplayInputBoundary {
    
    private final FormatCartItemsForDisplayOutputBoundary outputBoundary;
    private final ProductRepository productRepository;
    
    public FormatCartItemsForDisplayUseCaseControl(
            FormatCartItemsForDisplayOutputBoundary outputBoundary,
            ProductRepository productRepository) {
        this.outputBoundary = outputBoundary;
        this.productRepository = productRepository;
    }
    
    // Constructor without ProductRepository (for secondary delegation)
    public FormatCartItemsForDisplayUseCaseControl(
            FormatCartItemsForDisplayOutputBoundary outputBoundary) {
        this.outputBoundary = outputBoundary;
        this.productRepository = null;
    }
    
    @Override
    public FormatCartItemsForDisplayOutputData execute(FormatCartItemsForDisplayInputData inputData) {
        if (outputBoundary != null) {
            FormatCartItemsForDisplayOutputData outputData = formatInternal(inputData);
            outputBoundary.present(outputData);
            return outputData;
        } else {
            return formatInternal(inputData);
        }
    }
    
    /**
     * Internal method for direct delegation from other usecases
     * Returns output data without going through presenter
     */
    public FormatCartItemsForDisplayOutputData formatInternal(FormatCartItemsForDisplayInputData inputData) {
        try {
            List<CartItemDisplayData> formattedItems = new ArrayList<>();
            
            for (ChiTietGioHang item : inputData.getCartItems()) {
                // Load product information from repository
                SanPham product = productRepository != null 
                    ? productRepository.findById(item.getMaSanPham()).orElse(null)
                    : null;
                
                int availableStock = 0;
                String imageUrl = null;
                boolean hasStockWarning = false;
                String stockWarningMessage = null;
                
                if (product != null) {
                    availableStock = product.getSoLuongTonKho();
                    imageUrl = product.getHinhAnh();
                    
                    // Check for stock warnings
                    if (item.getSoLuong() > availableStock) {
                        hasStockWarning = true;
                        stockWarningMessage = String.format(
                            "Số lượng trong giỏ (%d) vượt quá tồn kho (%d)",
                            item.getSoLuong(), availableStock
                        );
                    }
                }
                
                CartItemDisplayData displayData = new CartItemDisplayData(
                    String.valueOf(item.getMaSanPham()),
                    item.getTenSanPham(),
                    imageUrl,
                    item.getGiaSanPham(),
                    item.getSoLuong(),
                    item.getTamTinh(),
                    availableStock,
                    hasStockWarning,
                    stockWarningMessage
                );
                
                formattedItems.add(displayData);
            }
            
            return FormatCartItemsForDisplayOutputData.forSuccess(formattedItems);
            
        } catch (Exception e) {
            return FormatCartItemsForDisplayOutputData.forError("FORMAT_ERROR", e.getMessage());
        }
    }
}
