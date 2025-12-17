
package com.motorbike.business.usecase.control;
import com.motorbike.business.usecase.input.UpdateCartQuantityInputBoundary;

import com.motorbike.business.dto.updatecart.UpdateCartQuantityInputData;
import com.motorbike.business.dto.updatecart.UpdateCartQuantityOutputData;
import com.motorbike.business.dto.checkinventory.CheckInventoryAvailabilityInputData;
import com.motorbike.business.dto.removeitemfromcart.RemoveItemFromCartInputData;
import com.motorbike.business.dto.calculatecarttotals.CalculateCartTotalsInputData;
import com.motorbike.business.ports.repository.CartRepository;
import com.motorbike.business.usecase.output.UpdateCartQuantityOutputBoundary;
import com.motorbike.business.usecase.input.CheckInventoryAvailabilityInputBoundary;
import com.motorbike.business.usecase.input.RemoveItemFromCartInputBoundary;
import com.motorbike.business.usecase.input.CalculateCartTotalsInputBoundary;
import com.motorbike.domain.entities.GioHang;
import com.motorbike.domain.entities.ChiTietGioHang;
import com.motorbike.domain.exceptions.DomainException;
import com.motorbike.domain.exceptions.ValidationException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class UpdateCartQuantityUseCaseControl implements UpdateCartQuantityInputBoundary {
    
    private final UpdateCartQuantityOutputBoundary outputBoundary;
    private final CartRepository cartRepository;
    private final CheckInventoryAvailabilityInputBoundary checkInventoryUseCase;
    private final RemoveItemFromCartInputBoundary removeItemUseCase;
    private final CalculateCartTotalsInputBoundary calculateTotalsUseCase;
    
    public UpdateCartQuantityUseCaseControl(
            UpdateCartQuantityOutputBoundary outputBoundary,
            CartRepository cartRepository,
            CheckInventoryAvailabilityInputBoundary checkInventoryUseCase,
            RemoveItemFromCartInputBoundary removeItemUseCase,
            CalculateCartTotalsInputBoundary calculateTotalsUseCase) {
        this.outputBoundary = outputBoundary;
        this.cartRepository = cartRepository;
        this.checkInventoryUseCase = checkInventoryUseCase;
        this.removeItemUseCase = removeItemUseCase;
        this.calculateTotalsUseCase = calculateTotalsUseCase;
    }

    // Constructor with 2 parameters (for backward compatibility)
    public UpdateCartQuantityUseCaseControl(
            UpdateCartQuantityOutputBoundary outputBoundary,
            CartRepository cartRepository
    ) {
        this.outputBoundary = outputBoundary;
        this.cartRepository = cartRepository;
        this.checkInventoryUseCase = new CheckInventoryAvailabilityUseCaseControl(null, null);
        this.removeItemUseCase = new RemoveItemFromCartUseCaseControl(null, cartRepository);
        this.calculateTotalsUseCase = new CalculateCartTotalsUseCaseControl(null);
    }
    
    @Override
    public void execute(UpdateCartQuantityInputData inputData) {
        UpdateCartQuantityOutputData outputData = null;
        Exception errorException = null;
        
        // Step 1: Validation
        try {
            if (inputData == null) {
                throw ValidationException.invalidInput();
            }
            GioHang.checkInput(
                inputData.getCartId(),
                inputData.getProductId(),
                inputData.getNewQuantity()
            );
        } catch (Exception e) {
            errorException = e;
        }
        
        GioHang gioHang = null;
        ChiTietGioHang existingItem = null;
        int oldQuantity = 0;
        String productName = null;
        
        // Step 2: Get cart and existing item info
        if (errorException == null) {
            try {
                gioHang = cartRepository.findById(inputData.getCartId())
                    .orElseThrow(DomainException::cartNotFound);
                
                existingItem = gioHang.getDanhSachSanPham().stream()
                    .filter(item -> item.getMaSanPham().equals(inputData.getProductId()))
                    .findFirst()
                    .orElse(null);
                
                if (existingItem != null) {
                    oldQuantity = existingItem.getSoLuong();
                    productName = existingItem.getTenSanPham();
                }
            } catch (Exception e) {
                errorException = e;
            }
        }
        
        // Step 3: Delegate to atomic use cases based on operation
        if (errorException == null && gioHang != null) {
            try {
                if (inputData.getNewQuantity() == 0) {
                    // UC-40: Remove item from cart
                    RemoveItemFromCartInputData removeInput = new RemoveItemFromCartInputData(
                        inputData.getCartId(),
                        inputData.getProductId()
                    );
                    removeItemUseCase.execute(removeInput);
                    
                    // Reload cart after removal
                    gioHang = cartRepository.findById(inputData.getCartId())
                        .orElseThrow(DomainException::cartNotFound);
                } else {
                    // UC-39: Check inventory before updating
                    CheckInventoryAvailabilityInputData checkInput = new CheckInventoryAvailabilityInputData(
                        inputData.getProductId(),
                        inputData.getNewQuantity()
                    );
                    var inventoryResult = ((CheckInventoryAvailabilityUseCaseControl) checkInventoryUseCase)
                        .checkInventoryInternal(checkInput);
                    
                    if (!inventoryResult.isSuccess()) {
                        throw new DomainException(inventoryResult.getErrorMessage(), inventoryResult.getErrorCode());
                    }
                    
                    if (!inventoryResult.isAvailable()) {
                        throw DomainException.insufficientStock(
                            productName, 
                            inventoryResult.getAvailableStock()
                        );
                    }
                    
                    // Update quantity (core responsibility of this use case)
                    gioHang.capNhatSoLuong(inputData.getProductId(), inputData.getNewQuantity());
                    gioHang = cartRepository.save(gioHang);
                }
                
                // UC-42: Calculate cart totals
                CalculateCartTotalsInputData totalsInput = new CalculateCartTotalsInputData(
                    gioHang.getDanhSachSanPham()
                );
                var totalsResult = ((CalculateCartTotalsUseCaseControl) calculateTotalsUseCase)
                    .calculateInternal(totalsInput);
                
                // Build response with all cart items
                List<UpdateCartQuantityOutputData.CartItemData> allItems = new ArrayList<>();
                BigDecimal newSubtotal = BigDecimal.ZERO;
                for (ChiTietGioHang item : gioHang.getDanhSachSanPham()) {
                    allItems.add(new UpdateCartQuantityOutputData.CartItemData(
                        item.getMaSanPham(),
                        item.getTenSanPham(),
                        item.getGiaSanPham(),
                        item.getSoLuong(),
                        item.getTamTinh()
                    ));
                    if (item.getMaSanPham().equals(inputData.getProductId())) {
                        newSubtotal = item.getTamTinh();
                    }
                }
                
                outputData = new UpdateCartQuantityOutputData(
                    gioHang.getMaGioHang(),
                    gioHang.getMaTaiKhoan(),
                    inputData.getProductId(),
                    productName,
                    oldQuantity,
                    inputData.getNewQuantity(),
                    inputData.getNewQuantity() == 0,
                    totalsResult.getTotalItems(),
                    totalsResult.getTotalQuantity(),
                    totalsResult.getTotalAmount(),
                    newSubtotal,
                    allItems
                );
            } catch (Exception e) {
                errorException = e;
            }
        }
        
        // Step 4: Handle error
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
            
            outputData = UpdateCartQuantityOutputData.forError(errorCode, message);
        }
        
        // Step 5: Present result
        outputBoundary.present(outputData);
    }
}
