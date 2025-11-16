package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.addtocart.AddToCartInputData;
import com.motorbike.business.dto.addtocart.AddToCartOutputData;
import com.motorbike.business.ports.repository.CartRepository;
import com.motorbike.business.ports.repository.ProductRepository;
import com.motorbike.business.usecase.output.AddToCartOutputBoundary;
import com.motorbike.domain.entities.GioHang;
import com.motorbike.domain.entities.SanPham;
import com.motorbike.domain.entities.ChiTietGioHang;
import com.motorbike.domain.exceptions.InvalidCartException;
import com.motorbike.domain.exceptions.ProductNotFoundException;
import com.motorbike.domain.exceptions.InsufficientStockException;

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
            GioHang gioHang = cartRepository.findByUserId(inputData.getUserId())
                .orElse(new GioHang(inputData.getUserId()));
            
            SanPham sanPham = productRepository.findById(inputData.getProductId())
                .orElseThrow(() -> new ProductNotFoundException(String.valueOf(inputData.getProductId())));
            
            // Simple if-check with throw
            if (sanPham.getSoLuongTonKho() < inputData.getQuantity()) {
                throw new InsufficientStockException(sanPham.getSoLuongTonKho());
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
            
        } catch (InvalidCartException | ProductNotFoundException | InsufficientStockException e) {
            throw e;
        }
    }
    
    @Override
    protected void validateInput(AddToCartInputData inputData) {
        checkInputNotNull(inputData);
        
        if (inputData.getUserId() == null) {
            throw new com.motorbike.domain.exceptions.InvalidUserIdException();
        }
        
        if (inputData.getProductId() == null) {
            throw new com.motorbike.domain.exceptions.InvalidProductIdException();
        }
        
        if (inputData.getQuantity() <= 0) {
            throw new com.motorbike.domain.exceptions.InvalidQuantityException();
        }
    }
    
    @Override
    protected void handleValidationError(IllegalArgumentException e) {
        String errorCode = "INVALID_INPUT";
        if (e instanceof com.motorbike.domain.exceptions.InvalidInputException) {
            errorCode = ((com.motorbike.domain.exceptions.InvalidInputException) e).getErrorCode();
        }
        AddToCartOutputData outputData = AddToCartOutputData.forError(errorCode, e.getMessage());
        outputBoundary.present(outputData);
    }
    
    @Override
    protected void handleSystemError(Exception e) {
        String errorCode;
        String message;
        
        if (e instanceof InvalidCartException) {
            InvalidCartException ex = (InvalidCartException) e;
            errorCode = ex.getErrorCode();
            message = ex.getMessage();
        } else if (e instanceof ProductNotFoundException) {
            ProductNotFoundException ex = (ProductNotFoundException) e;
            errorCode = ex.getErrorCode();
            message = ex.getMessage();
        } else if (e instanceof InsufficientStockException) {
            InsufficientStockException ex = (InsufficientStockException) e;
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
