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
            GioHang gioHang = cartRepository.findByUserId(inputData.getUserId())
                .orElse(new GioHang(inputData.getUserId()));
            
            SanPham sanPham = productRepository.findById(inputData.getProductId())
                .orElseThrow(() -> DomainException.productNotFound(String.valueOf(inputData.getProductId())));
            
            if (sanPham.getSoLuongTonKho() < inputData.getQuantity()) {
                throw DomainException.insufficientStock(sanPham.getTenSanPham(), sanPham.getSoLuongTonKho());
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
            
        } catch (ValidationException | DomainException e) {
            throw e;
        }
    }
    
    @Override
    protected void validateInput(AddToCartInputData inputData) {
        checkInputNotNull(inputData);
        TaiKhoan.checkInput(inputData.getUserId());
        SanPham.checkInput(inputData.getProductId(), inputData.getQuantity());
    }
    
    @Override
    protected void handleValidationError(IllegalArgumentException e) {
        String errorCode = "INVALID_INPUT";
        if (e instanceof ValidationException) {
            errorCode = ((ValidationException) e).getErrorCode();
        }
        AddToCartOutputData outputData = AddToCartOutputData.forError(errorCode, e.getMessage());
        outputBoundary.present(outputData);
    }
    
    @Override
    protected void handleSystemError(Exception e) {
        String errorCode;
        String message;
        
        if (e instanceof ValidationException) {
            ValidationException ex = (ValidationException) e;
            errorCode = ex.getErrorCode();
            message = ex.getMessage();
        } else if (e instanceof DomainException) {
            DomainException ex = (DomainException) e;
            errorCode = ex.getErrorCode();
            message = ex.getMessage();
        } else if (e instanceof com.motorbike.domain.exceptions.SystemException) {
            com.motorbike.domain.exceptions.SystemException ex = (com.motorbike.domain.exceptions.SystemException) e;
            errorCode = ex.getErrorCode();
            message = ex.getMessage();
        } else {
            throw new com.motorbike.domain.exceptions.SystemException(e);
        }
        
        AddToCartOutputData outputData = AddToCartOutputData.forError(errorCode, message);
        outputBoundary.present(outputData);
    }
}
