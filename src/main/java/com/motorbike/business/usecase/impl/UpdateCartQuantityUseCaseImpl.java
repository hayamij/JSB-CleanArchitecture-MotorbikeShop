package com.motorbike.business.usecase.impl;

import com.motorbike.business.dto.updatecart.UpdateCartQuantityInputData;
import com.motorbike.business.dto.updatecart.UpdateCartQuantityOutputData;
import com.motorbike.business.ports.repository.CartRepository;
import com.motorbike.business.usecase.UpdateCartQuantityInputBoundary;
import com.motorbike.business.usecase.UpdateCartQuantityOutputBoundary;
import com.motorbike.domain.entities.GioHang;
import com.motorbike.domain.exceptions.InvalidCartException;

import java.util.Optional;

/**
 * Update Cart Quantity Use Case Implementation
 */
public class UpdateCartQuantityUseCaseImpl implements UpdateCartQuantityInputBoundary {
    
    private final UpdateCartQuantityOutputBoundary outputBoundary;
    private final CartRepository cartRepository;
    
    public UpdateCartQuantityUseCaseImpl(
            UpdateCartQuantityOutputBoundary outputBoundary,
            CartRepository cartRepository) {
        this.outputBoundary = outputBoundary;
        this.cartRepository = cartRepository;
    }
    
    @Override
    public void execute(UpdateCartQuantityInputData inputData) {
        try {
            // 1. Validate input
            validateInput(inputData);
            
            // 2. Find cart
            Optional<GioHang> cartOpt = cartRepository.findByUserId(inputData.getUserId());
            
            if (!cartOpt.isPresent()) {
                UpdateCartQuantityOutputData outputData = UpdateCartQuantityOutputData.forError(
                    "CART_NOT_FOUND",
                    "Không tìm thấy giỏ hàng"
                );
                outputBoundary.present(outputData);
                return;
            }
            
            GioHang gioHang = cartOpt.get();
            
            // 3. Update quantity - entity handles business logic
            if (inputData.getNewQuantity() == 0) {
                // Remove item if quantity is 0
                gioHang.xoaSanPham(inputData.getProductId());
            } else {
                // Update quantity
                gioHang.capNhatSoLuong(inputData.getProductId(), inputData.getNewQuantity());
            }
            
            // 4. Save cart
            GioHang savedCart = cartRepository.save(gioHang);
            
            // 5. Create success output
            UpdateCartQuantityOutputData outputData = UpdateCartQuantityOutputData.forSuccess(
                savedCart.getMaGioHang(),
                savedCart.getDanhSachSanPham().size(),
                savedCart.getTongTien()
            );
            
            outputBoundary.present(outputData);
            
        } catch (InvalidCartException e) {
            UpdateCartQuantityOutputData outputData = UpdateCartQuantityOutputData.forError(
                e.getErrorCode(),
                e.getMessage()
            );
            outputBoundary.present(outputData);
            
        } catch (IllegalArgumentException e) {
            UpdateCartQuantityOutputData outputData = UpdateCartQuantityOutputData.forError(
                "INVALID_INPUT",
                e.getMessage()
            );
            outputBoundary.present(outputData);
            
        } catch (Exception e) {
            UpdateCartQuantityOutputData outputData = UpdateCartQuantityOutputData.forError(
                "SYSTEM_ERROR",
                "Đã xảy ra lỗi: " + e.getMessage()
            );
            outputBoundary.present(outputData);
        }
    }
    
    private void validateInput(UpdateCartQuantityInputData inputData) {
        if (inputData == null) {
            throw new IllegalArgumentException("Input data không được null");
        }
        
        if (inputData.getUserId() == null) {
            throw new IllegalArgumentException("User ID không được null");
        }
        
        if (inputData.getProductId() == null) {
            throw new IllegalArgumentException("Product ID không được null");
        }
        
        if (inputData.getNewQuantity() < 0) {
            throw new IllegalArgumentException("Số lượng không được âm");
        }
    }
}
