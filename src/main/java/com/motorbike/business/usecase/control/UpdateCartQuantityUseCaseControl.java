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

public class UpdateCartQuantityUseCaseControl {
    
    private final UpdateCartQuantityOutputBoundary outputBoundary;
    private final CartRepository cartRepository;
    
    public UpdateCartQuantityUseCaseControl(
            UpdateCartQuantityOutputBoundary outputBoundary,
            CartRepository cartRepository) {
        this.outputBoundary = outputBoundary;
        this.cartRepository = cartRepository;
    }
    
    public void execute(UpdateCartQuantityInputData inputData) {
        UpdateCartQuantityOutputData outputData = null;
        Exception errorException = null;
        
        try {
            if (inputData == null) {
                throw ValidationException.invalidInput();
            }
            GioHang.checkInput(
                inputData.getCartId(),
                inputData.getProductId(),
                inputData.getNewQuantity()
            );
        } catch (Exception e) {
            errorException = e;
        }
        
        GioHang gioHang = null;
        ChiTietGioHang existingItem = null;
        int oldQuantity = 0;
        String productName = null;
        
        if (errorException == null) {
            try {
                gioHang = cartRepository.findById(inputData.getCartId())
                    .orElseThrow(DomainException::cartNotFound);
                
                existingItem = gioHang.getDanhSachSanPham().stream()
                    .filter(item -> item.getMaSanPham().equals(inputData.getProductId()))
                    .findFirst()
                    .orElse(null);
                
                if (existingItem != null) {
                    oldQuantity = existingItem.getSoLuong();
                    productName = existingItem.getTenSanPham();
                }
            } catch (Exception e) {
                errorException = e;
            }
        }
        
        if (errorException == null && gioHang != null) {
            try {
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
                
                outputData = new UpdateCartQuantityOutputData(
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
            } catch (Exception e) {
                errorException = e;
            }
        }
        
        if (errorException != null) {
            String errorCode = "SYSTEM_ERROR";
            String message = errorException.getMessage();
            
            if (errorException instanceof ValidationException) {
                errorCode = ((ValidationException) errorException).getErrorCode();
            } else if (errorException instanceof DomainException) {
                errorCode = ((DomainException) errorException).getErrorCode();
            } else if (errorException instanceof com.motorbike.domain.exceptions.SystemException) {
                errorCode = ((com.motorbike.domain.exceptions.SystemException) errorException).getErrorCode();
            }
            
            outputData = UpdateCartQuantityOutputData.forError(errorCode, message);
        }
        
        outputBoundary.present(outputData);
    }
}
