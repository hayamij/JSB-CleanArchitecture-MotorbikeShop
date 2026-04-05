package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.removecartitem.RemoveCartItemInputData;
import com.motorbike.business.dto.removecartitem.RemoveCartItemOutputData;
import com.motorbike.business.usecase.input.RemoveCartItemInputBoundary;
import com.motorbike.business.usecase.output.RemoveCartItemOutputBoundary;
import com.motorbike.business.ports.repository.CartRepository;
import com.motorbike.domain.entities.GioHang;
import com.motorbike.domain.exceptions.DomainException;
import com.motorbike.domain.exceptions.ValidationException;
import java.util.Optional;

public class RemoveCartItemUseCaseControl implements RemoveCartItemInputBoundary {
    
    private final RemoveCartItemOutputBoundary outputBoundary;
    private final CartRepository cartRepository;
    
    public RemoveCartItemUseCaseControl(RemoveCartItemOutputBoundary outputBoundary, CartRepository cartRepository) {
        this.outputBoundary = outputBoundary;
        this.cartRepository = cartRepository;
    }
    
    // Constructor for tests with swapped parameters (repository first)
    public RemoveCartItemUseCaseControl(
            com.motorbike.business.ports.GioHangRepository gioHangRepository,
            RemoveCartItemOutputBoundary outputBoundary) {
        this.outputBoundary = outputBoundary;
        this.cartRepository = (CartRepository) gioHangRepository;
    }
    
    @Override
    public void execute(RemoveCartItemInputData inputData) {
        RemoveCartItemOutputData outputData = removeInternal(inputData);
        outputBoundary.present(outputData);
    }
    
    public RemoveCartItemOutputData removeInternal(RemoveCartItemInputData inputData) {
        RemoveCartItemOutputData outputData = null;
        Exception errorException = null;
        
        try {
            if (inputData == null) {
                throw ValidationException.invalidInput();
            }
            Long userId = inputData.getUserId();
            Long productId = inputData.getProductId();
            
            Optional<GioHang> cartOpt = cartRepository.findByUserId(userId);
            if (cartOpt.isEmpty()) {
                throw DomainException.cartNotFound();
            }
            
            GioHang cart = cartOpt.get();
            cart.xoaSanPham(productId);
            cartRepository.save(cart);
            
            outputData = new RemoveCartItemOutputData(true, "Item removed from cart successfully");
            
        } catch (Exception e) {
            errorException = e;
            outputData = new RemoveCartItemOutputData(false, e.getMessage());
        }
        
        return outputData;
    }
}
