package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.updatecart.UpdateCartQuantityInputData;
import com.motorbike.business.dto.updatecart.UpdateCartQuantityOutputData;
import com.motorbike.business.ports.repository.CartRepository;
import com.motorbike.business.usecase.output.UpdateCartQuantityOutputBoundary;
import com.motorbike.domain.entities.GioHang;
import com.motorbike.domain.entities.ChiTietGioHang;
import com.motorbike.domain.exceptions.DomainException;
import com.motorbike.domain.exceptions.ValidationException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
                .orElseThrow(DomainException::cartNotFound);
            
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
            
        } catch (ValidationException | DomainException e) {
            throw e;
        }
    }
    
    @Override
    protected void validateInput(UpdateCartQuantityInputData inputData) {
        checkInputNotNull(inputData);
        GioHang.checkInput(
            inputData.getCartId(),
            inputData.getProductId(),
            inputData.getNewQuantity()
        );
    }
    
    @Override
    protected void handleValidationError(IllegalArgumentException e) {
        String errorCode = "INVALID_INPUT";
        if (e instanceof ValidationException) {
            errorCode = ((ValidationException) e).getErrorCode();
        }
        UpdateCartQuantityOutputData outputData = UpdateCartQuantityOutputData.forError(errorCode, e.getMessage());
        outputBoundary.present(outputData);
    }
    
    @Override
    protected void handleSystemError(Exception e) {
        String errorCode = null;
        String message = e.getMessage();
        
        if (e instanceof ValidationException) {
            ValidationException ex = (ValidationException) e;
            errorCode = ex.getErrorCode();
            message = ex.getMessage();
        } else if (e instanceof DomainException) {
            DomainException ex = (DomainException) e;
            errorCode = ex.getErrorCode();
            message = ex.getMessage();
        } else if (e instanceof com.motorbike.domain.exceptions.SystemException) {
            com.motorbike.domain.exceptions.SystemException ex = (com.motorbike.domain.exceptions.SystemException) e;
            errorCode = ex.getErrorCode();
            message = ex.getMessage();
        } else {
            errorCode = "SYSTEM_ERROR";
            message = e.getMessage() != null ? e.getMessage() : "Đã xảy ra lỗi hệ thống";
        }
        
        UpdateCartQuantityOutputData outputData = UpdateCartQuantityOutputData.forError(errorCode, message);
        outputBoundary.present(outputData);
    }
}
