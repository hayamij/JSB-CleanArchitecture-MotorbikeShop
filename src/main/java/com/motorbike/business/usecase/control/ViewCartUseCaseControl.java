package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.viewcart.ViewCartInputData;
import com.motorbike.business.dto.viewcart.ViewCartOutputData;
import com.motorbike.business.ports.repository.CartRepository;
import com.motorbike.business.usecase.output.ViewCartOutputBoundary;
import com.motorbike.domain.entities.GioHang;
import com.motorbike.domain.exceptions.EmptyCartException;
import java.util.List;
import java.util.ArrayList;

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
        GioHang gioHang = cartRepository.findByUserId(inputData.getUserId())
            .orElseThrow(() -> new EmptyCartException());
        
        // Simple if-check with throw
        if (gioHang.getDanhSachSanPham().isEmpty()) {
            throw new EmptyCartException();
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
        checkInputNotNull(inputData);
        if (inputData.getUserId() == null) {
            throw new com.motorbike.domain.exceptions.InvalidUserIdException();
        }
    }
    
    @Override
    protected void handleValidationError(IllegalArgumentException e) {
        String errorCode = "INVALID_INPUT";
        if (e instanceof com.motorbike.domain.exceptions.InvalidInputException) {
            errorCode = ((com.motorbike.domain.exceptions.InvalidInputException) e).getErrorCode();
        }
        ViewCartOutputData outputData = ViewCartOutputData.forError(errorCode, e.getMessage());
        outputBoundary.present(outputData);
    }
    
    @Override
    protected void handleSystemError(Exception e) {
        if (e instanceof EmptyCartException) {
            ViewCartOutputData outputData = ViewCartOutputData.forEmptyCart();
            outputBoundary.present(outputData);
            return;
        }
        
        String errorCode;
        String message;
        if (e instanceof com.motorbike.domain.exceptions.SystemException) {
            com.motorbike.domain.exceptions.SystemException ex = (com.motorbike.domain.exceptions.SystemException) e;
            errorCode = ex.getErrorCode();
            message = ex.getMessage();
        } else {
            throw new com.motorbike.domain.exceptions.SystemException(e);
        }
        
        ViewCartOutputData outputData = ViewCartOutputData.forError(errorCode, message);
        outputBoundary.present(outputData);
    }
}
