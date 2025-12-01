package com.motorbike.business.usecase.control;

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

public class ViewCartUseCaseControl
        extends AbstractUseCaseControl<ViewCartInputData, ViewCartOutputBoundary> {
    
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    
    public ViewCartUseCaseControl(
            ViewCartOutputBoundary outputBoundary,
            CartRepository cartRepository,
            ProductRepository productRepository) {
        super(outputBoundary);
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }
    
    @Override
    protected void executeBusinessLogic(ViewCartInputData inputData) throws Exception {
        GioHang gioHang = cartRepository.findByUserId(inputData.getUserId())
            .orElseThrow(DomainException::emptyCart);
        
        if (gioHang.getDanhSachSanPham().isEmpty()) {
            throw DomainException.emptyCart();
        }
        
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
        com.motorbike.domain.entities.TaiKhoan.checkInput(inputData.getUserId());
    }
    
    @Override
    protected void handleValidationError(IllegalArgumentException e) {
        String errorCode = "INVALID_INPUT";
        if (e instanceof ValidationException) {
            errorCode = ((ValidationException) e).getErrorCode();
        }
        ViewCartOutputData outputData = ViewCartOutputData.forError(errorCode, e.getMessage());
        outputBoundary.present(outputData);
    }
    
    @Override
    protected void handleSystemError(Exception e) {
        if (e instanceof DomainException && "EMPTY_CART".equals(((DomainException) e).getErrorCode())) {
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
