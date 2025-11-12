package com.motorbike.business.usecase.impl;

import com.motorbike.business.dto.viewcart.ViewCartInputData;
import com.motorbike.business.dto.viewcart.ViewCartOutputData;
import com.motorbike.business.ports.repository.CartRepository;
import com.motorbike.business.usecase.ViewCartInputBoundary;
import com.motorbike.business.usecase.ViewCartOutputBoundary;
import com.motorbike.domain.entities.GioHang;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

/**
 * View Cart Use Case Implementation
 */
public class ViewCartUseCaseImpl implements ViewCartInputBoundary {
    
    private final ViewCartOutputBoundary outputBoundary;
    private final CartRepository cartRepository;
    
    public ViewCartUseCaseImpl(
            ViewCartOutputBoundary outputBoundary,
            CartRepository cartRepository) {
        this.outputBoundary = outputBoundary;
        this.cartRepository = cartRepository;
    }
    
    @Override
    public void execute(ViewCartInputData inputData) {
        try {
            // 1. Validate input
            if (inputData == null || inputData.getUserId() == null) {
                ViewCartOutputData outputData = ViewCartOutputData.forError(
                    "INVALID_INPUT",
                    "User ID không hợp lệ"
                );
                outputBoundary.present(outputData);
                return;
            }
            
            // 2. Find cart
            Optional<GioHang> cartOpt = cartRepository.findByUserId(inputData.getUserId());
            
            if (!cartOpt.isPresent()) {
                // Return empty cart
                ViewCartOutputData outputData = ViewCartOutputData.forEmptyCart();
                outputBoundary.present(outputData);
                return;
            }
            
            GioHang gioHang = cartOpt.get();
            
            // 3. Check if cart is empty
            if (gioHang.getDanhSachSanPham().isEmpty()) {
                ViewCartOutputData outputData = ViewCartOutputData.forEmptyCart();
                outputBoundary.present(outputData);
                return;
            }
            // 4. Create success output with cart data
            List<ViewCartOutputData.CartItemData> itemList = new ArrayList<>();
            for (Object obj : gioHang.getDanhSachSanPham()) {
                if (obj instanceof ViewCartOutputData.CartItemData) {
                    itemList.add((ViewCartOutputData.CartItemData) obj);
                }
            }
            ViewCartOutputData successOutput = ViewCartOutputData.forSuccess(
                gioHang.getMaGioHang(),
                itemList,
                gioHang.getTongTien()
            );
            
            outputBoundary.present(successOutput);
            
        } catch (Exception e) {
            ViewCartOutputData outputData = ViewCartOutputData.forError(
                "SYSTEM_ERROR",
                "Đã xảy ra lỗi: " + e.getMessage()
            );
            outputBoundary.present(outputData);
        }
    }
}
