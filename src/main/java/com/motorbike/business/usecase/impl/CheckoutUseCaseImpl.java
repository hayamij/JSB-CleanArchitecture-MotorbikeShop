package com.motorbike.business.usecase.impl;

import com.motorbike.business.dto.checkout.CheckoutInputData;
import com.motorbike.business.dto.checkout.CheckoutOutputData;
import com.motorbike.business.ports.repository.CartRepository;
import com.motorbike.business.ports.repository.ProductRepository;
import com.motorbike.business.usecase.CheckoutInputBoundary;
import com.motorbike.business.usecase.CheckoutOutputBoundary;
import com.motorbike.domain.entities.GioHang;
import com.motorbike.domain.entities.ChiTietGioHang;
import com.motorbike.domain.entities.SanPham;
import com.motorbike.domain.exceptions.InvalidCartException;

import java.util.Optional;

/**
 * Checkout Use Case Implementation
 */
public class CheckoutUseCaseImpl implements CheckoutInputBoundary {
    
    private final CheckoutOutputBoundary outputBoundary;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    
    public CheckoutUseCaseImpl(
            CheckoutOutputBoundary outputBoundary,
            CartRepository cartRepository,
            ProductRepository productRepository) {
        this.outputBoundary = outputBoundary;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }
    
    @Override
    public void execute(CheckoutInputData inputData) {
        try {
            // 1. Validate input
            if (inputData == null || inputData.getUserId() == null) {
                CheckoutOutputData outputData = CheckoutOutputData.forError(
                    "INVALID_INPUT",
                    "User ID không hợp lệ"
                );
                outputBoundary.present(outputData);
                return;
            }
            
            // 2. Find cart
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
            
            // 3. Validate stock for all items
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
            
            // 4. Reduce stock for all items
            for (ChiTietGioHang item : gioHang.getDanhSachSanPham()) {
                Optional<SanPham> productOpt = productRepository.findById(item.getMaSanPham());
                SanPham sanPham = productOpt.get();
                
                // Entity handles stock reduction business logic
                sanPham.giamTonKho(item.getSoLuong());
                productRepository.save(sanPham);
            }
            
            // 5. Clear cart after successful checkout
            gioHang.xoaToanBoGioHang();
            cartRepository.save(gioHang);
            
            // 6. Create success output
            CheckoutOutputData outputData = CheckoutOutputData.forSuccess(
                "ORDER_" + System.currentTimeMillis(), // Simple order ID generation
                gioHang.getTongTien()
            );
            
            outputBoundary.present(outputData);
            
        } catch (InvalidCartException e) {
            CheckoutOutputData outputData = CheckoutOutputData.forError(
                e.getErrorCode(),
                e.getMessage()
            );
            outputBoundary.present(outputData);
            
        } catch (Exception e) {
            CheckoutOutputData outputData = CheckoutOutputData.forError(
                "SYSTEM_ERROR",
                "Đã xảy ra lỗi: " + e.getMessage()
            );
            outputBoundary.present(outputData);
        }
    }
}
