package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.addtocart.AddToCartInputData;
import com.motorbike.business.dto.addtocart.AddToCartOutputData;
import com.motorbike.business.dto.checkinventory.CheckInventoryAvailabilityInputData;
import com.motorbike.business.ports.repository.CartRepository;
import com.motorbike.business.ports.repository.ProductRepository;
import com.motorbike.business.usecase.output.AddToCartOutputBoundary;
import com.motorbike.business.usecase.input.CheckInventoryAvailabilityInputBoundary;
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
    private final CheckInventoryAvailabilityInputBoundary checkInventoryUseCase;
    
    public AddToCartUseCaseControl(
            AddToCartOutputBoundary outputBoundary,
            CartRepository cartRepository,
            ProductRepository productRepository,
            CheckInventoryAvailabilityInputBoundary checkInventoryUseCase) {
        this.outputBoundary = outputBoundary;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.checkInventoryUseCase = checkInventoryUseCase;
    }
    
    // Constructor for tests without CheckInventoryUseCase
    public AddToCartUseCaseControl(
            AddToCartOutputBoundary outputBoundary,
            CartRepository cartRepository,
            ProductRepository productRepository) {
        this(outputBoundary, cartRepository, productRepository, 
             new CheckInventoryAvailabilityUseCaseControl(null, productRepository));
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
        GioHang existingCart = null;
        if (errorException == null) {
            try {
                // Get product first to have product name for error messages
                sanPham = productRepository.findById(inputData.getProductId())
                    .orElseThrow(() -> DomainException.productNotFound(String.valueOf(inputData.getProductId())));
                
                // Check if product already exists in cart
                existingCart = cartRepository.findByUserId(inputData.getUserId()).orElse(null);
                int existingQuantity = 0;
                if (existingCart != null) {
                    var existingItem = existingCart.getDanhSachSanPham().stream()
                        .filter(item -> item.getMaSanPham().equals(inputData.getProductId()))
                        .findFirst();
                    if (existingItem.isPresent()) {
                        existingQuantity = existingItem.get().getSoLuong();
                    }
                }
                
                // UC-39: Check inventory availability (total = existing + new)
                int totalQuantity = existingQuantity + inputData.getQuantity();
                CheckInventoryAvailabilityInputData checkInput = new CheckInventoryAvailabilityInputData(
                    inputData.getProductId(),
                    totalQuantity
                );
                var inventoryResult = ((CheckInventoryAvailabilityUseCaseControl) checkInventoryUseCase)
                    .checkInventoryInternal(checkInput);
                
                if (!inventoryResult.isSuccess()) {
                    throw new DomainException(inventoryResult.getErrorMessage(), inventoryResult.getErrorCode());
                }
                
                if (!inventoryResult.isAvailable()) {
                    throw DomainException.insufficientStock(
                        sanPham.getTenSanPham(),
                        inventoryResult.getAvailableStock()
                    );
                }
            } catch (Exception e) {
                errorException = e;
            }
        }
        
        if (errorException == null && sanPham != null) {
            try {
                // Reuse existingCart if already loaded, otherwise create new
                GioHang gioHang = (existingCart != null) ? existingCart : 
                    cartRepository.findByUserId(inputData.getUserId())
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
