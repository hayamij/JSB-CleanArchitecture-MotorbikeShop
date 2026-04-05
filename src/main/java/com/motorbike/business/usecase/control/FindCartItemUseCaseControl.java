package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.findcartitem.FindCartItemInputData;
import com.motorbike.business.dto.findcartitem.FindCartItemOutputData;
import com.motorbike.business.usecase.input.FindCartItemInputBoundary;
import com.motorbike.business.usecase.output.FindCartItemOutputBoundary;
import com.motorbike.business.ports.repository.CartRepository;
import com.motorbike.domain.entities.GioHang;
import com.motorbike.domain.entities.ChiTietGioHang;
import com.motorbike.domain.exceptions.DomainException;
import com.motorbike.domain.exceptions.ValidationException;
import java.util.Optional;

public class FindCartItemUseCaseControl implements FindCartItemInputBoundary {
    
    private final FindCartItemOutputBoundary outputBoundary;
    private final CartRepository cartRepository;
    
    public FindCartItemUseCaseControl(FindCartItemOutputBoundary outputBoundary, CartRepository cartRepository) {
        this.outputBoundary = outputBoundary;
        this.cartRepository = cartRepository;
    }
    
    // Constructor for tests with swapped parameters
    public FindCartItemUseCaseControl(
            com.motorbike.business.ports.GioHangRepository gioHangRepository,
            FindCartItemOutputBoundary outputBoundary) {
        this.outputBoundary = outputBoundary;
        this.cartRepository = (CartRepository) gioHangRepository;
    }
    
    @Override
    public void execute(FindCartItemInputData inputData) {
        FindCartItemOutputData outputData = findInternal(inputData);
        outputBoundary.present(outputData);
    }
    
    public FindCartItemOutputData findInternal(FindCartItemInputData inputData) {
        FindCartItemOutputData outputData = null;
        Exception errorException = null;
        
        try {
            if (inputData == null) {
                throw ValidationException.invalidInput();
            }
            Long cartId = inputData.getCartId();
            Long productId = inputData.getProductId();
            
            Optional<GioHang> cartOpt = cartRepository.findById(cartId);
            if (cartOpt.isEmpty()) {
                throw DomainException.cartNotFound();
            }
            
            GioHang cart = cartOpt.get();
            Optional<ChiTietGioHang> itemOpt = cart.getDanhSachSanPham().stream()
                .filter(item -> item.getMaSanPham().equals(productId))
                .findFirst();
            
            if (itemOpt.isPresent()) {
                outputData = new FindCartItemOutputData(true, null, "Item found", itemOpt.get());
            } else {
                outputData = new FindCartItemOutputData(false, "Item not found in cart", null, null);
            }
            
        } catch (Exception e) {
            errorException = e;
            outputData = new FindCartItemOutputData(false, e.getMessage(), null, null);
        }
        
        return outputData;
    }
}
