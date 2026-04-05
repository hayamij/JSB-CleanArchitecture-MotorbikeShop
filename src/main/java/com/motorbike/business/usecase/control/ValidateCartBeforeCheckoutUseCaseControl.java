package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.validatecart.ValidateCartBeforeCheckoutInputData;
import com.motorbike.business.dto.validatecart.ValidateCartBeforeCheckoutOutputData;
import com.motorbike.business.ports.repository.CartRepository;
import com.motorbike.business.ports.repository.ProductRepository;
import com.motorbike.business.usecase.input.ValidateCartBeforeCheckoutInputBoundary;
import com.motorbike.business.usecase.output.ValidateCartBeforeCheckoutOutputBoundary;
import com.motorbike.domain.entities.ChiTietGioHang;
import com.motorbike.domain.entities.GioHang;
import com.motorbike.domain.entities.SanPham;
import com.motorbike.domain.exceptions.DomainException;
import com.motorbike.domain.exceptions.ValidationException;
import java.util.ArrayList;
import java.util.List;

public class ValidateCartBeforeCheckoutUseCaseControl implements ValidateCartBeforeCheckoutInputBoundary {
    
    private final ValidateCartBeforeCheckoutOutputBoundary outputBoundary;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    
    public ValidateCartBeforeCheckoutUseCaseControl(
            ValidateCartBeforeCheckoutOutputBoundary outputBoundary,
            CartRepository cartRepository,
            ProductRepository productRepository) {
        this.outputBoundary = outputBoundary;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }
    
    @Override
    public void execute(ValidateCartBeforeCheckoutInputData inputData) {
        ValidateCartBeforeCheckoutOutputData outputData = validateInternal(inputData);
        outputBoundary.present(outputData);
    }
    
    /**
     * Internal method for use case composition.
     * Returns OutputData directly without using presenter.
     */
    public ValidateCartBeforeCheckoutOutputData validateInternal(ValidateCartBeforeCheckoutInputData inputData) {
        ValidateCartBeforeCheckoutOutputData outputData = null;
        Exception errorException = null;
        
        // Step 1: Validation
        try {
            if (inputData == null) {
                throw ValidationException.invalidInput();
            }
            if (inputData.getCartId() == null) {
                throw ValidationException.invalidCartId();
            }
        } catch (Exception e) {
            errorException = e;
        }
        
        // Step 2: Business logic - Validate cart
        GioHang gioHang = null;
        if (errorException == null) {
            try {
                gioHang = cartRepository.findById(inputData.getCartId())
                    .orElseThrow(DomainException::cartNotFound);
                
                if (gioHang.getDanhSachSanPham().isEmpty()) {
                    throw DomainException.emptyCart();
                }
                
                boolean isValid = true;
                List<ValidateCartBeforeCheckoutOutputData.InvalidItemData> invalidItems = new ArrayList<>();
                List<String> reasons = new ArrayList<>();
                
                // Check each item in cart
                for (ChiTietGioHang item : gioHang.getDanhSachSanPham()) {
                    SanPham sanPham = productRepository.findById(item.getMaSanPham())
                        .orElse(null);
                    
                    if (sanPham == null) {
                        isValid = false;
                        invalidItems.add(new ValidateCartBeforeCheckoutOutputData.InvalidItemData(
                            item.getMaSanPham(),
                            item.getTenSanPham(),
                            item.getSoLuong(),
                            0,
                            "Sản phẩm không tồn tại"
                        ));
                        reasons.add(String.format("Sản phẩm '%s' không tồn tại trong hệ thống", item.getTenSanPham()));
                    } else if (sanPham.getSoLuongTonKho() < item.getSoLuong()) {
                        isValid = false;
                        invalidItems.add(new ValidateCartBeforeCheckoutOutputData.InvalidItemData(
                            item.getMaSanPham(),
                            item.getTenSanPham(),
                            item.getSoLuong(),
                            sanPham.getSoLuongTonKho(),
                            "Không đủ tồn kho"
                        ));
                        reasons.add(String.format("Sản phẩm '%s' chỉ còn %d (yêu cầu %d)", 
                            item.getTenSanPham(), sanPham.getSoLuongTonKho(), item.getSoLuong()));
                    } else if (!sanPham.isConHang()) {
                        isValid = false;
                        invalidItems.add(new ValidateCartBeforeCheckoutOutputData.InvalidItemData(
                            item.getMaSanPham(),
                            item.getTenSanPham(),
                            item.getSoLuong(),
                            0,
                            "Sản phẩm đã hết hàng"
                        ));
                        reasons.add(String.format("Sản phẩm '%s' đã hết hàng", item.getTenSanPham()));
                    }
                }
                
                outputData = new ValidateCartBeforeCheckoutOutputData(isValid, invalidItems, reasons);
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
            
            outputData = ValidateCartBeforeCheckoutOutputData.forError(errorCode, message);
        }
        
        return outputData;
    }
}
