package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.updatecart.UpdateCartQuantityInputData;
import com.motorbike.business.dto.updatecart.UpdateCartQuantityOutputData;
import com.motorbike.business.ports.repository.CartRepository;
import com.motorbike.business.usecase.output.UpdateCartQuantityOutputBoundary;
import com.motorbike.domain.entities.GioHang;
import com.motorbike.domain.entities.ChiTietGioHang;
import com.motorbike.domain.exceptions.InvalidCartException;
import com.motorbike.domain.exceptions.CartNotFoundException;
import com.motorbike.domain.exceptions.ProductNotInCartException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
            GioHang gioHang = cartRepository.findById(inputData.getCartId())
                .orElseThrow(() -> new CartNotFoundException());
            
            // Find product in cart to get old quantity and productName
            ChiTietGioHang existingItem = gioHang.getDanhSachSanPham().stream()
                .filter(item -> item.getMaSanPham().equals(inputData.getProductId()))
                .findFirst()
                .orElse(null);
            
            int oldQuantity = existingItem != null ? existingItem.getSoLuong() : 0;
            String productName = existingItem != null ? existingItem.getTenSanPham() : null;
            
            if (inputData.getNewQuantity() == 0) {
                gioHang.xoaSanPham(inputData.getProductId());
            } else {
                gioHang.capNhatSoLuong(inputData.getProductId(), inputData.getNewQuantity());
            }
            
            GioHang savedCart = cartRepository.save(gioHang);
            
            // Build cart items list
            List<UpdateCartQuantityOutputData.CartItemData> allItems = new ArrayList<>();
            BigDecimal newSubtotal = BigDecimal.ZERO;
            for (ChiTietGioHang item : savedCart.getDanhSachSanPham()) {
                allItems.add(new UpdateCartQuantityOutputData.CartItemData(
                    item.getMaSanPham(),
                    item.getTenSanPham(),
                    item.getGiaSanPham(),
                    item.getSoLuong(),
                    item.getTamTinh()
                ));
                if (item.getMaSanPham().equals(inputData.getProductId())) {
                    newSubtotal = item.getTamTinh();
                }
            }
            
            UpdateCartQuantityOutputData outputData = new UpdateCartQuantityOutputData(
                savedCart.getMaGioHang(),
                savedCart.getMaTaiKhoan(),
                inputData.getProductId(),
                productName,
                oldQuantity,
                inputData.getNewQuantity(),
                inputData.getNewQuantity() == 0,
                savedCart.getDanhSachSanPham().size(),
                savedCart.getDanhSachSanPham().stream().mapToInt(ChiTietGioHang::getSoLuong).sum(),
                savedCart.getTongTien(),
                newSubtotal,
                allItems
            );
            
            outputBoundary.present(outputData);
            
        } catch (InvalidCartException | CartNotFoundException | ProductNotInCartException e) {
            throw e;
        }
    }
    
    @Override
    protected void validateInput(UpdateCartQuantityInputData inputData) {
        checkInputNotNull(inputData);
        
        if (inputData.getCartId() == null) {
            throw new InvalidCartException("INVALID_CART_ID", "Cart ID không được để trống");
        }
        
        if (inputData.getProductId() == null) {
            throw new com.motorbike.domain.exceptions.InvalidProductIdException();
        }
        
        if (inputData.getNewQuantity() < 0) {
            throw new com.motorbike.domain.exceptions.InvalidQuantityException();
        }
    }
    
    @Override
    protected void handleValidationError(IllegalArgumentException e) {
        String errorCode = "INVALID_INPUT";
        if (e instanceof com.motorbike.domain.exceptions.InvalidInputException) {
            errorCode = ((com.motorbike.domain.exceptions.InvalidInputException) e).getErrorCode();
        }
        UpdateCartQuantityOutputData outputData = UpdateCartQuantityOutputData.forError(errorCode, e.getMessage());
        outputBoundary.present(outputData);
    }
    
    @Override
    protected void handleSystemError(Exception e) {
        String errorCode = null;
        String message = e.getMessage();
        
        if (e instanceof InvalidCartException) {
            InvalidCartException ex = (InvalidCartException) e;
            errorCode = ex.getErrorCode();
            message = ex.getMessage();
        } else if (e instanceof CartNotFoundException) {
            CartNotFoundException ex = (CartNotFoundException) e;
            errorCode = ex.getErrorCode();
            message = ex.getMessage();
        } else if (e instanceof ProductNotInCartException) {
            ProductNotInCartException ex = (ProductNotInCartException) e;
            errorCode = ex.getErrorCode();
            message = ex.getMessage();
        } else if (e instanceof com.motorbike.domain.exceptions.SystemException) {
            com.motorbike.domain.exceptions.SystemException ex = (com.motorbike.domain.exceptions.SystemException) e;
            errorCode = ex.getErrorCode();
            message = ex.getMessage();
        } else {
            // Unknown exception - use generic error code
            errorCode = "SYSTEM_ERROR";
            message = e.getMessage() != null ? e.getMessage() : "Đã xảy ra lỗi hệ thống";
        }
        
        UpdateCartQuantityOutputData outputData = UpdateCartQuantityOutputData.forError(errorCode, message);
        outputBoundary.present(outputData);
    }
}
