package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.removeitemfromcart.RemoveItemFromCartInputData;
import com.motorbike.business.dto.removeitemfromcart.RemoveItemFromCartOutputData;
import com.motorbike.business.ports.repository.CartRepository;
import com.motorbike.business.usecase.input.RemoveItemFromCartInputBoundary;
import com.motorbike.business.usecase.output.RemoveItemFromCartOutputBoundary;
import com.motorbike.domain.entities.ChiTietGioHang;
import com.motorbike.domain.entities.GioHang;
import com.motorbike.domain.exceptions.DomainException;
import com.motorbike.domain.exceptions.ValidationException;

public class RemoveItemFromCartUseCaseControl implements RemoveItemFromCartInputBoundary {
    
    private final RemoveItemFromCartOutputBoundary outputBoundary;
    private final CartRepository cartRepository;
    
    public RemoveItemFromCartUseCaseControl(
            RemoveItemFromCartOutputBoundary outputBoundary,
            CartRepository cartRepository) {
        this.outputBoundary = outputBoundary;
        this.cartRepository = cartRepository;
    }
    
    @Override
    public void execute(RemoveItemFromCartInputData inputData) {
        RemoveItemFromCartOutputData outputData = null;
        Exception errorException = null;
        
        // Step 1: Validation
        try {
            if (inputData == null) {
                throw ValidationException.invalidInput();
            }
            if (inputData.getCartId() == null) {
                throw ValidationException.invalidCartId();
            }
            if (inputData.getProductId() == null) {
                throw ValidationException.invalidProductId();
            }
        } catch (Exception e) {
            errorException = e;
        }
        
        // Step 2: Business logic - Remove item from cart
        GioHang gioHang = null;
        String productName = null;
        if (errorException == null) {
            try {
                gioHang = cartRepository.findById(inputData.getCartId())
                    .orElseThrow(DomainException::cartNotFound);
                
                // Get product name before removal
                ChiTietGioHang itemToRemove = gioHang.getDanhSachSanPham().stream()
                    .filter(item -> item.getMaSanPham().equals(inputData.getProductId()))
                    .findFirst()
                    .orElse(null);
                
                if (itemToRemove != null) {
                    productName = itemToRemove.getTenSanPham();
                }
                
                // Remove product from cart
                gioHang.xoaSanPham(inputData.getProductId());
                
                // Save updated cart
                GioHang savedCart = cartRepository.save(gioHang);
                
                outputData = new RemoveItemFromCartOutputData(
                    savedCart.getMaGioHang(),
                    inputData.getProductId(),
                    productName,
                    savedCart.getDanhSachSanPham().size(),
                    savedCart.getTongTien()
                );
            } catch (Exception e) {
                errorException = e;
            }
        }
        
        // Step 3: Handle error
        if (errorException != null) {
            String errorCode = "SYSTEM_ERROR";
            String message = errorException.getMessage();
            
            if (errorException instanceof ValidationException) {
                errorCode = ((ValidationException) errorException).getErrorCode();
            } else if (errorException instanceof DomainException) {
                errorCode = ((DomainException) errorException).getErrorCode();
            }
            
            outputData = RemoveItemFromCartOutputData.forError(errorCode, message);
        }
        
        // Step 4: Present result
        outputBoundary.present(outputData);
    }
}
