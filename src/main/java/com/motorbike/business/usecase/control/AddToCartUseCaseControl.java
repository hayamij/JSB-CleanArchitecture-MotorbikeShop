package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.addtocart.AddToCartInputData;
import com.motorbike.business.dto.addtocart.AddToCartOutputData;
import com.motorbike.business.ports.repository.CartRepository;
import com.motorbike.business.ports.repository.ProductRepository;
import com.motorbike.business.usecase.input.AddToCartInputBoundary;
import com.motorbike.business.usecase.output.AddToCartOutputBoundary;
import com.motorbike.domain.entities.GioHang;
import com.motorbike.domain.entities.SanPham;
import com.motorbike.domain.entities.ChiTietGioHang;
import com.motorbike.domain.exceptions.InvalidCartException;
import java.util.Optional;

/**
 * Add To Cart Use Case Control
 * Extends AbstractUseCaseControl for common validation and error handling
 */
public class AddToCartUseCaseControl 
        extends AbstractUseCaseControl<AddToCartInputData, AddToCartOutputBoundary> {
    
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    
    public AddToCartUseCaseControl(
            AddToCartOutputBoundary outputBoundary,
            CartRepository cartRepository,
            ProductRepository productRepository) {
        super(outputBoundary);
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }
    
    @Override
    protected void executeBusinessLogic(AddToCartInputData inputData) throws Exception {
        try {
            GioHang gioHang;
            Optional<GioHang> cartOpt = cartRepository.findByUserId(inputData.getUserId());
            
            if (cartOpt.isPresent()) {
                gioHang = cartOpt.get();
            } else {
                gioHang = new GioHang(inputData.getUserId());
            }
            
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
            
            if (sanPham.getSoLuongTonKho() < inputData.getQuantity()) {
                AddToCartOutputData outputData = AddToCartOutputData.forError(
                    "INSUFFICIENT_STOCK",
                    "Không đủ hàng trong kho. Còn lại: " + sanPham.getSoLuongTonKho()
                );
                outputBoundary.present(outputData);
                return;
            }
            
            ChiTietGioHang chiTiet = new ChiTietGioHang(
                sanPham.getMaSanPham(),
                sanPham.getTenSanPham(),
                sanPham.tinhGiaSauKhuyenMai(),
                inputData.getQuantity()
            );
            
            gioHang.themSanPham(chiTiet);
            GioHang savedCart = cartRepository.save(gioHang);
            
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
        }
    }
    
    @Override
    protected void validateInput(AddToCartInputData inputData) {
        checkInputNotNull(inputData);
        
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
    
    @Override
    protected void handleValidationError(IllegalArgumentException e) {
        AddToCartOutputData outputData = AddToCartOutputData.forError(
            "INVALID_INPUT",
            e.getMessage()
        );
        outputBoundary.present(outputData);
    }
    
    @Override
    protected void handleSystemError(Exception e) {
        AddToCartOutputData outputData = AddToCartOutputData.forError(
            "SYSTEM_ERROR",
            "Đã xảy ra lỗi: " + e.getMessage()
        );
        outputBoundary.present(outputData);
    }
}
