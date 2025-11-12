package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.updatecart.UpdateCartQuantityInputData;
import com.motorbike.business.dto.updatecart.UpdateCartQuantityOutputData;
import com.motorbike.business.ports.repository.CartRepository;
import com.motorbike.business.usecase.output.UpdateCartQuantityOutputBoundary;
import com.motorbike.domain.entities.GioHang;
import com.motorbike.domain.exceptions.InvalidCartException;
import java.util.Optional;

/**
 * Update Cart Quantity Use Case Control
 * Extends AbstractUseCaseControl for common validation and error handling
 */
public class UpdateCartQuantityUseCaseControl 
        extends AbstractUseCaseControl<UpdateCartQuantityInputData, UpdateCartQuantityOutputBoundary> {
    
    private final CartRepository cartRepository;
    
    public UpdateCartQuantityUseCaseControl(
            UpdateCartQuantityOutputBoundary outputBoundary,
            CartRepository cartRepository) {
        super(outputBoundary);
        this.cartRepository = cartRepository;
    }
    
    @Override
    protected void executeBusinessLogic(UpdateCartQuantityInputData inputData) throws Exception {
        try {
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
            
            if (inputData.getNewQuantity() == 0) {
                gioHang.xoaSanPham(inputData.getProductId());
            } else {
                gioHang.capNhatSoLuong(inputData.getProductId(), inputData.getNewQuantity());
            }
            
            GioHang savedCart = cartRepository.save(gioHang);
            
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
        }
    }
    
    @Override
    protected void validateInput(UpdateCartQuantityInputData inputData) {
        checkInputNotNull(inputData);
        
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
    
    @Override
    protected void handleValidationError(IllegalArgumentException e) {
        UpdateCartQuantityOutputData outputData = UpdateCartQuantityOutputData.forError(
            "INVALID_INPUT",
            e.getMessage()
        );
        outputBoundary.present(outputData);
    }
    
    @Override
    protected void handleSystemError(Exception e) {
        UpdateCartQuantityOutputData outputData = UpdateCartQuantityOutputData.forError(
            "SYSTEM_ERROR",
            "Đã xảy ra lỗi: " + e.getMessage()
        );
        outputBoundary.present(outputData);
    }
}
