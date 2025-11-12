package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.checkout.CheckoutInputData;
import com.motorbike.business.dto.checkout.CheckoutOutputData;
import com.motorbike.business.ports.repository.CartRepository;
import com.motorbike.business.ports.repository.ProductRepository;
import com.motorbike.business.usecase.output.CheckoutOutputBoundary;
import com.motorbike.domain.entities.GioHang;
import com.motorbike.domain.entities.ChiTietGioHang;
import com.motorbike.domain.entities.SanPham;
import com.motorbike.domain.exceptions.InvalidCartException;
import com.motorbike.domain.exceptions.EmptyCartException;
import com.motorbike.domain.exceptions.ProductNotFoundException;
import com.motorbike.domain.exceptions.InsufficientStockException;

/**
 * Checkout Use Case Control
 * Extends AbstractUseCaseControl for common validation and error handling
 */
public class CheckoutUseCaseControl 
        extends AbstractUseCaseControl<CheckoutInputData, CheckoutOutputBoundary> {
    
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    
    public CheckoutUseCaseControl(
            CheckoutOutputBoundary outputBoundary,
            CartRepository cartRepository,
            ProductRepository productRepository) {
        super(outputBoundary);
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }
    
    @Override
    protected void executeBusinessLogic(CheckoutInputData inputData) throws Exception {
        try {
            GioHang gioHang = cartRepository.findByUserId(inputData.getUserId())
                .orElseThrow(() -> new EmptyCartException());
            
            // Simple if-check with throw
            if (gioHang.getDanhSachSanPham().isEmpty()) {
                throw new EmptyCartException();
            }
            
            for (ChiTietGioHang item : gioHang.getDanhSachSanPham()) {
                SanPham sanPham = productRepository.findById(item.getMaSanPham())
                    .orElseThrow(() -> new ProductNotFoundException(String.valueOf(item.getMaSanPham()), "Sản phẩm không tồn tại: " + item.getMaSanPham()));
                
                // Simple if-check with throw
                if (sanPham.getSoLuongTonKho() < item.getSoLuong()) {
                    throw new InsufficientStockException(sanPham.getTenSanPham(), sanPham.getSoLuongTonKho());
                }
            }
            
            for (ChiTietGioHang item : gioHang.getDanhSachSanPham()) {
                SanPham sanPham = productRepository.findById(item.getMaSanPham()).get();
                
                sanPham.giamTonKho(item.getSoLuong());
                productRepository.save(sanPham);
            }
            
            gioHang.xoaToanBoGioHang();
            cartRepository.save(gioHang);
            
            CheckoutOutputData outputData = CheckoutOutputData.forSuccess(
                "ORDER_" + System.currentTimeMillis(),
                gioHang.getTongTien()
            );
            
            outputBoundary.present(outputData);
            
        } catch (InvalidCartException | EmptyCartException | ProductNotFoundException | InsufficientStockException e) {
            throw e;
        }
    }
    
    @Override
    protected void validateInput(CheckoutInputData inputData) {
        if (inputData == null || inputData.getUserId() == null) {
            throw new IllegalArgumentException("User ID không hợp lệ");
        }
    }
    
    @Override
    protected void handleValidationError(IllegalArgumentException e) {
        CheckoutOutputData outputData = CheckoutOutputData.forError(
            "INVALID_INPUT",
            e.getMessage()
        );
        outputBoundary.present(outputData);
    }
    
    @Override
    protected void handleSystemError(Exception e) {
        String errorCode = "SYSTEM_ERROR";
        String message = "Đã xảy ra lỗi: " + e.getMessage();
        
        try {
            throw e;
        } catch (InvalidCartException ex) {
            errorCode = ex.getErrorCode();
            message = ex.getMessage();
        } catch (EmptyCartException ex) {
            errorCode = ex.getErrorCode();
            message = ex.getMessage();
        } catch (ProductNotFoundException ex) {
            errorCode = ex.getErrorCode();
            message = ex.getMessage();
        } catch (InsufficientStockException ex) {
            errorCode = ex.getErrorCode();
            message = ex.getMessage();
        } catch (Exception ex) {
            // Keep default
        }
        
        CheckoutOutputData outputData = CheckoutOutputData.forError(errorCode, message);
        outputBoundary.present(outputData);
    }
}
