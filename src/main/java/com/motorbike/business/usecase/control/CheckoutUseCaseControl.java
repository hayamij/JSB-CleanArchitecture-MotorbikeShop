package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.checkout.CheckoutInputData;
import com.motorbike.business.dto.checkout.CheckoutOutputData;
import com.motorbike.business.ports.repository.CartRepository;
import com.motorbike.business.ports.repository.ProductRepository;
import com.motorbike.business.usecase.input.CheckoutInputBoundary;
import com.motorbike.business.usecase.output.CheckoutOutputBoundary;
import com.motorbike.domain.entities.GioHang;
import com.motorbike.domain.entities.ChiTietGioHang;
import com.motorbike.domain.entities.SanPham;
import com.motorbike.domain.exceptions.InvalidCartException;
import java.util.Optional;

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
            Optional<GioHang> cartOpt = cartRepository.findByUserId(inputData.getUserId());
            
            if (!cartOpt.isPresent() || cartOpt.get().getDanhSachSanPham().isEmpty()) {
                CheckoutOutputData outputData = CheckoutOutputData.forError(
                    "EMPTY_CART",
                    "Giỏ hàng trống"
                );
                outputBoundary.present(outputData);
                return;
            }
            
            GioHang gioHang = cartOpt.get();
            
            for (ChiTietGioHang item : gioHang.getDanhSachSanPham()) {
                Optional<SanPham> productOpt = productRepository.findById(item.getMaSanPham());
                
                if (!productOpt.isPresent()) {
                    CheckoutOutputData outputData = CheckoutOutputData.forError(
                        "PRODUCT_NOT_FOUND",
                        "Sản phẩm không tồn tại: " + item.getMaSanPham()
                    );
                    outputBoundary.present(outputData);
                    return;
                }
                
                SanPham sanPham = productOpt.get();
                
                if (sanPham.getSoLuongTonKho() < item.getSoLuong()) {
                    CheckoutOutputData outputData = CheckoutOutputData.forError(
                        "INSUFFICIENT_STOCK",
                        "Không đủ hàng cho sản phẩm: " + sanPham.getTenSanPham() + 
                        ". Còn lại: " + sanPham.getSoLuongTonKho()
                    );
                    outputBoundary.present(outputData);
                    return;
                }
            }
            
            for (ChiTietGioHang item : gioHang.getDanhSachSanPham()) {
                Optional<SanPham> productOpt = productRepository.findById(item.getMaSanPham());
                SanPham sanPham = productOpt.get();
                
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
            
        } catch (InvalidCartException e) {
            CheckoutOutputData outputData = CheckoutOutputData.forError(
                e.getErrorCode(),
                e.getMessage()
            );
            outputBoundary.present(outputData);
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
        CheckoutOutputData outputData = CheckoutOutputData.forError(
            "SYSTEM_ERROR",
            "Đã xảy ra lỗi: " + e.getMessage()
        );
        outputBoundary.present(outputData);
    }
}
