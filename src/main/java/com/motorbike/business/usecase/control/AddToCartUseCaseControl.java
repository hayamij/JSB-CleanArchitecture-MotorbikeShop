package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.addtocart.AddToCartInputData;
import com.motorbike.business.dto.addtocart.AddToCartOutputData;
import com.motorbike.business.ports.repository.CartRepository;
import com.motorbike.business.ports.repository.ProductRepository;
import com.motorbike.business.usecase.output.AddToCartOutputBoundary;
import com.motorbike.domain.entities.GioHang;
import com.motorbike.domain.entities.SanPham;
import com.motorbike.domain.entities.TaiKhoan;
import com.motorbike.domain.entities.ChiTietGioHang;
import com.motorbike.domain.exceptions.DomainException;
import com.motorbike.domain.exceptions.ValidationException;
import com.motorbike.business.usecase.input.AddToCartInputBoundary;


public class AddToCartUseCaseControl implements AddToCartInputBoundary {
    
    private final AddToCartOutputBoundary outputBoundary;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    
    public AddToCartUseCaseControl(
            AddToCartOutputBoundary outputBoundary,
            CartRepository cartRepository,
            ProductRepository productRepository) {
        this.outputBoundary = outputBoundary;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }
    
    public void execute(AddToCartInputData inputData) {
        AddToCartOutputData outputData = null;
        Exception errorException = null;
        
        try {
            if (inputData == null) {
                throw ValidationException.invalidInput();
            }
            TaiKhoan.checkInput(inputData.getUserId());
            SanPham.checkInput(inputData.getProductId(), inputData.getQuantity());
        } catch (Exception e) {
            errorException = e;
        }
        
        SanPham sanPham = null;
        if (errorException == null) {
            try {
                sanPham = productRepository.findById(inputData.getProductId())
                    .orElseThrow(() -> DomainException.productNotFound(String.valueOf(inputData.getProductId())));
                
                if (sanPham.getSoLuongTonKho() < inputData.getQuantity()) {
                    throw DomainException.insufficientStock(sanPham.getTenSanPham(), sanPham.getSoLuongTonKho());
                }
            } catch (Exception e) {
                errorException = e;
            }
        }
        
        if (errorException == null && sanPham != null) {
            try {
                GioHang gioHang = cartRepository.findByUserId(inputData.getUserId())
                    .orElse(new GioHang(inputData.getUserId()));
                
                ChiTietGioHang chiTiet = new ChiTietGioHang(
                    sanPham.getMaSanPham(),
                    sanPham.getTenSanPham(),
                    sanPham.tinhGiaSauKhuyenMai(),
                    inputData.getQuantity()
                );
                
                gioHang.themSanPham(chiTiet);
                GioHang savedCart = cartRepository.save(gioHang);
                
                outputData = AddToCartOutputData.forSuccess(
                    savedCart.getMaGioHang(),
                    savedCart.getDanhSachSanPham().size(),
                    savedCart.getTongTien()
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
            
            outputData = AddToCartOutputData.forError(errorCode, message);
        }
        
        outputBoundary.present(outputData);
    }
}
