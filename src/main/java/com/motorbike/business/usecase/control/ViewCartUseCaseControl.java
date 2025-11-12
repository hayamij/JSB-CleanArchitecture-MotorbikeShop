package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.viewcart.ViewCartInputData;
import com.motorbike.business.dto.viewcart.ViewCartOutputData;
import com.motorbike.business.ports.repository.CartRepository;
import com.motorbike.business.usecase.output.ViewCartOutputBoundary;
import com.motorbike.domain.entities.GioHang;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

/**
 * View Cart Use Case Control
 * Extends AbstractUseCaseControl for common validation and error handling
 */
public class ViewCartUseCaseControl 
        extends AbstractUseCaseControl<ViewCartInputData, ViewCartOutputBoundary> {
    
    private final CartRepository cartRepository;
    
    public ViewCartUseCaseControl(
            ViewCartOutputBoundary outputBoundary,
            CartRepository cartRepository) {
        super(outputBoundary);
        this.cartRepository = cartRepository;
    }
    
    @Override
    protected void executeBusinessLogic(ViewCartInputData inputData) throws Exception {
        Optional<GioHang> cartOpt = cartRepository.findByUserId(inputData.getUserId());
        
        if (!cartOpt.isPresent()) {
            ViewCartOutputData outputData = ViewCartOutputData.forEmptyCart();
            outputBoundary.present(outputData);
            return;
        }
        
        GioHang gioHang = cartOpt.get();
        
        if (gioHang.getDanhSachSanPham().isEmpty()) {
            ViewCartOutputData outputData = ViewCartOutputData.forEmptyCart();
            outputBoundary.present(outputData);
            return;
        }
        
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
    }
    
    @Override
    protected void validateInput(ViewCartInputData inputData) {
        if (inputData == null || inputData.getUserId() == null) {
            throw new IllegalArgumentException("User ID không hợp lệ");
        }
    }
    
    @Override
    protected void handleValidationError(IllegalArgumentException e) {
        ViewCartOutputData outputData = ViewCartOutputData.forError(
            "INVALID_INPUT",
            e.getMessage()
        );
        outputBoundary.present(outputData);
    }
    
    @Override
    protected void handleSystemError(Exception e) {
        ViewCartOutputData outputData = ViewCartOutputData.forError(
            "SYSTEM_ERROR",
            "Đã xảy ra lỗi: " + e.getMessage()
        );
        outputBoundary.present(outputData);
    }
}
