package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.updatecartitemquantity.UpdateCartItemQuantityInputData;
import com.motorbike.business.dto.updatecartitemquantity.UpdateCartItemQuantityOutputData;
import com.motorbike.business.usecase.input.UpdateCartItemQuantityInputBoundary;
import com.motorbike.business.usecase.output.UpdateCartItemQuantityOutputBoundary;
import com.motorbike.business.ports.repository.CartRepository;
import com.motorbike.domain.entities.GioHang;
import com.motorbike.domain.exceptions.DomainException;
import java.util.Optional;

public class UpdateCartItemQuantityUseCaseControl implements UpdateCartItemQuantityInputBoundary {
    
    private final UpdateCartItemQuantityOutputBoundary outputBoundary;
    private final CartRepository cartRepository;
    
    public UpdateCartItemQuantityUseCaseControl(UpdateCartItemQuantityOutputBoundary outputBoundary, CartRepository cartRepository) {
        this.outputBoundary = outputBoundary;
        this.cartRepository = cartRepository;
    }
    
    // Constructor for tests with swapped parameters (repository first)
    public UpdateCartItemQuantityUseCaseControl(
            com.motorbike.business.ports.GioHangRepository gioHangRepository,
            UpdateCartItemQuantityOutputBoundary outputBoundary) {
        this.outputBoundary = outputBoundary;
        this.cartRepository = (CartRepository) gioHangRepository;
    }
    
    @Override
    public void execute(UpdateCartItemQuantityInputData inputData) {
        UpdateCartItemQuantityOutputData outputData = updateInternal(inputData);
        outputBoundary.present(outputData);
    }
    
    public UpdateCartItemQuantityOutputData updateInternal(UpdateCartItemQuantityInputData inputData) {
        UpdateCartItemQuantityOutputData outputData = null;
        Exception errorException = null;
        
        try {
            Long userId = inputData.getUserId();
            Long productId = inputData.getProductId();
            int newQuantity = inputData.getNewQuantity();
            
            Optional<GioHang> cartOpt = cartRepository.findByUserId(userId);
            if (cartOpt.isEmpty()) {
                throw DomainException.cartNotFound();
            }
            
            GioHang cart = cartOpt.get();
            cart.capNhatSoLuong(productId, newQuantity);
            cartRepository.save(cart);
            
            outputData = new UpdateCartItemQuantityOutputData(true, "Cart item quantity updated successfully", newQuantity);
            
        } catch (Exception e) {
            errorException = e;
            outputData = new UpdateCartItemQuantityOutputData(false, e.getMessage(), 0);
        }
        
        return outputData;
    }
}
