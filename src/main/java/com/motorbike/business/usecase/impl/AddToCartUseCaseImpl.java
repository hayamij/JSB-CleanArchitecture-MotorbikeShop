package com.motorbike.business.usecase.impl;

import com.motorbike.business.dto.addtocart.AddToCartInputData;
import com.motorbike.business.dto.addtocart.AddToCartOutputData;
import com.motorbike.business.ports.repository.CartRepository;
import com.motorbike.business.ports.repository.ProductRepository;
import com.motorbike.business.usecase.AddToCartInputBoundary;
import com.motorbike.business.usecase.AddToCartOutputBoundary;
import com.motorbike.domain.entities.GioHang;
import com.motorbike.domain.entities.SanPham;
import com.motorbike.domain.entities.ChiTietGioHang;
import com.motorbike.domain.exceptions.InvalidCartException;

import java.util.Optional;

/**
 * Add To Cart Use Case Implementation
 */
public class AddToCartUseCaseImpl implements AddToCartInputBoundary {
    
    private final AddToCartOutputBoundary outputBoundary;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    
    public AddToCartUseCaseImpl(
            AddToCartOutputBoundary outputBoundary,
            CartRepository cartRepository,
            ProductRepository productRepository) {
        this.outputBoundary = outputBoundary;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }
    
    @Override
    public void execute(AddToCartInputData inputData) {
        try {
            // 1. Validate input
            validateInput(inputData);
            
            // 2. Find or create cart
            GioHang gioHang;
            Optional<GioHang> cartOpt = cartRepository.findByUserId(inputData.getUserId());
            
            if (cartOpt.isPresent()) {
                gioHang = cartOpt.get();
            } else {
                // Create new cart for user
                gioHang = new GioHang(inputData.getUserId());
            }
            
            // 3. Find product
            Optional<SanPham> productOpt = productRepository.findById(inputData.getProductId());
            if (!productOpt.isPresent()) {
                AddToCartOutputData outputData = AddToCartOutputData.forError(
                    "PRODUCT_NOT_FOUND",
                    "Không tìm thấy sản phẩm"
                );
                outputBoundary.present(outputData);
                return;
            }
            
            SanPham sanPham = productOpt.get();
            
            // 4. Check stock availability
            if (sanPham.getSoLuongTonKho() < inputData.getQuantity()) {
                AddToCartOutputData outputData = AddToCartOutputData.forError(
                    "INSUFFICIENT_STOCK",
                    "Không đủ hàng trong kho. Còn lại: " + sanPham.getSoLuongTonKho()
                );
                outputBoundary.present(outputData);
                return;
            }
            
            // 5. Create cart item and add to cart - entity handles business logic
            ChiTietGioHang chiTiet = new ChiTietGioHang(
                sanPham.getMaSanPham(),
                sanPham.getTenSanPham(),
                sanPham.tinhGiaSauKhuyenMai(),
                inputData.getQuantity()
            );
            
            gioHang.themSanPham(chiTiet);
            
            // 6. Save cart
            GioHang savedCart = cartRepository.save(gioHang);
            
            // 7. Create success output
            AddToCartOutputData outputData = AddToCartOutputData.forSuccess(
                savedCart.getMaGioHang(),
                savedCart.getDanhSachSanPham().size(),
                savedCart.getTongTien()
            );
            
            outputBoundary.present(outputData);
            
        } catch (InvalidCartException e) {
            AddToCartOutputData outputData = AddToCartOutputData.forError(
                e.getErrorCode(),
                e.getMessage()
            );
            outputBoundary.present(outputData);
            
        } catch (IllegalArgumentException e) {
            AddToCartOutputData outputData = AddToCartOutputData.forError(
                "INVALID_INPUT",
                e.getMessage()
            );
            outputBoundary.present(outputData);
            
        } catch (Exception e) {
            AddToCartOutputData outputData = AddToCartOutputData.forError(
                "SYSTEM_ERROR",
                "Đã xảy ra lỗi: " + e.getMessage()
            );
            outputBoundary.present(outputData);
        }
    }
    
    private void validateInput(AddToCartInputData inputData) {
        if (inputData == null) {
            throw new IllegalArgumentException("Input data không được null");
        }
        
        if (inputData.getUserId() == null) {
            throw new IllegalArgumentException("User ID không được null");
        }
        
        if (inputData.getProductId() == null) {
            throw new IllegalArgumentException("Product ID không được null");
        }
        
        if (inputData.getQuantity() <= 0) {
            throw new IllegalArgumentException("Số lượng phải > 0");
        }
    }
}
