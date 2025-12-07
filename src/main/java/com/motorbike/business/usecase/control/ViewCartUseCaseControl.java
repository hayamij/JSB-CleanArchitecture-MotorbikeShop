
package com.motorbike.business.usecase.control;
import com.motorbike.business.usecase.input.ViewCartInputBoundary;

import com.motorbike.business.dto.viewcart.ViewCartInputData;
import com.motorbike.business.dto.viewcart.ViewCartOutputData;
import com.motorbike.business.ports.repository.CartRepository;
import com.motorbike.business.ports.repository.ProductRepository;
import com.motorbike.business.usecase.output.ViewCartOutputBoundary;
import com.motorbike.domain.entities.GioHang;
import com.motorbike.domain.entities.SanPham;
import com.motorbike.domain.exceptions.DomainException;
import com.motorbike.domain.exceptions.ValidationException;
import java.util.List;
import java.util.ArrayList;

public class ViewCartUseCaseControl implements ViewCartInputBoundary {
    
    private final ViewCartOutputBoundary outputBoundary;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    
    public ViewCartUseCaseControl(
            ViewCartOutputBoundary outputBoundary,
            CartRepository cartRepository,
            ProductRepository productRepository) {
        this.outputBoundary = outputBoundary;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }
    
    @Override
    public void execute(ViewCartInputData inputData) {
        ViewCartOutputData outputData = null;
        Exception errorException = null;
        
        try {
            if (inputData == null) {
                throw ValidationException.invalidInput();
            }
            com.motorbike.domain.entities.TaiKhoan.checkInput(inputData.getUserId());
        } catch (Exception e) {
            errorException = e;
        }
        
        GioHang gioHang = null;
        if (errorException == null) {
            try {
                gioHang = cartRepository.findByUserId(inputData.getUserId())
                    .orElseThrow(DomainException::emptyCart);
                
                if (gioHang.getDanhSachSanPham().isEmpty()) {
                    throw DomainException.emptyCart();
                }
            } catch (Exception e) {
                errorException = e;
            }
        }
        
        if (errorException == null && gioHang != null) {
            try {
                List<ViewCartOutputData.CartItemData> itemList = new ArrayList<>();
                for (com.motorbike.domain.entities.ChiTietGioHang item : gioHang.getDanhSachSanPham()) {
                    SanPham product = productRepository.findById(item.getMaSanPham())
                        .orElse(null);
                    
                    int availableStock = 0;
                    String imageUrl = null;
                    boolean hasStockWarning = false;
                    String stockWarningMessage = null;
                    
                    if (product != null) {
                        availableStock = product.getSoLuongTonKho();
                        imageUrl = product.getHinhAnh();
                        
                        if (item.getSoLuong() > availableStock) {
                            hasStockWarning = true;
                            stockWarningMessage = String.format(
                                "Số lượng trong giỏ (%d) vượt quá tồn kho (%d)",
                                item.getSoLuong(), availableStock
                            );
                        }
                    }
                    
                    ViewCartOutputData.CartItemData cartItem = new ViewCartOutputData.CartItemData(
                        item.getMaSanPham(),
                        item.getTenSanPham(),
                        imageUrl,
                        item.getGiaSanPham(),
                        item.getSoLuong(),
                        item.getTamTinh(),
                        availableStock,
                        hasStockWarning,
                        stockWarningMessage
                    );
                    itemList.add(cartItem);
                }
                
                outputData = ViewCartOutputData.forSuccess(
                    gioHang.getMaGioHang(),
                    itemList,
                    gioHang.getTongTien()
                );
            } catch (Exception e) {
                errorException = e;
            }
        }
        
        if (errorException != null) {
            if (errorException instanceof DomainException && "EMPTY_CART".equals(((DomainException) errorException).getErrorCode())) {
                outputData = ViewCartOutputData.forEmptyCart();
            } else {
                String errorCode = "SYSTEM_ERROR";
                String message = errorException.getMessage();
                
                if (errorException instanceof ValidationException) {
                    errorCode = ((ValidationException) errorException).getErrorCode();
                } else if (errorException instanceof DomainException) {
                    errorCode = ((DomainException) errorException).getErrorCode();
                } else if (errorException instanceof com.motorbike.domain.exceptions.SystemException) {
                    errorCode = ((com.motorbike.domain.exceptions.SystemException) errorException).getErrorCode();
                }
                
                outputData = ViewCartOutputData.forError(errorCode, message);
            }
        }
        
        outputBoundary.present(outputData);
    }
}
