package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.cart.MergeGuestCartInputData;
import com.motorbike.business.dto.cart.MergeGuestCartOutputData;
import com.motorbike.business.ports.repository.CartRepository;
import com.motorbike.business.usecase.input.MergeGuestCartInputBoundary;
import com.motorbike.business.usecase.output.MergeGuestCartOutputBoundary;
import com.motorbike.domain.entities.GioHang;
import com.motorbike.domain.exceptions.ValidationException;
import com.motorbike.domain.exceptions.DomainException;

import java.util.Optional;

/**
 * Secondary UseCase: Merge giỏ hàng guest vào giỏ hàng user
 * Được sử dụng bởi: LoginUseCaseControl
 */
public class MergeGuestCartUseCaseControl implements MergeGuestCartInputBoundary {
    
    private final MergeGuestCartOutputBoundary outputBoundary;
    private final CartRepository cartRepository;
    
    public MergeGuestCartUseCaseControl(
            MergeGuestCartOutputBoundary outputBoundary,
            CartRepository cartRepository) {
        this.outputBoundary = outputBoundary;
        this.cartRepository = cartRepository;
    }
    
    @Override
    public void execute(MergeGuestCartInputData inputData) {
        MergeGuestCartOutputData outputData = mergeInternal(inputData);
        if (outputBoundary != null) {
            outputBoundary.present(outputData);
        }
    }
    
    /**
     * Internal method for use case composition
     * Returns OutputData directly without using presenter
     */
    public MergeGuestCartOutputData mergeInternal(MergeGuestCartInputData inputData) {
        try {
            // Validate input
            if (inputData == null || inputData.getGuestCartId() == null || inputData.getUserCartId() == null) {
                throw ValidationException.invalidInput();
            }
            
            // Find guest cart
            Optional<GioHang> guestCartOpt = cartRepository.findById(inputData.getGuestCartId());
            if (!guestCartOpt.isPresent()) {
                // Guest cart not found - not an error, just skip merge
                return MergeGuestCartOutputData.forSuccess(inputData.getUserCartId(), false, 0);
            }
            
            // Find user cart
            Optional<GioHang> userCartOpt = cartRepository.findById(inputData.getUserCartId());
            if (!userCartOpt.isPresent()) {
                throw DomainException.cartNotFound();
            }
            
            GioHang guestCart = guestCartOpt.get();
            GioHang userCart = userCartOpt.get();
            
            // Count items before merge
            int mergedItemsCount = guestCart.getDanhSachSanPham().size();
            
            // Merge all items from guest cart to user cart
            guestCart.getDanhSachSanPham().forEach(item -> {
                userCart.themSanPham(item);
            });
            
            // Save merged user cart
            cartRepository.save(userCart);
            
            // Delete guest cart
            cartRepository.delete(guestCart.getMaGioHang());
            
            return MergeGuestCartOutputData.forSuccess(
                userCart.getMaGioHang(),
                true,  // cartMerged
                mergedItemsCount
            );
            
        } catch (Exception e) {
            String errorCode = "SYSTEM_ERROR";
            String message = e.getMessage();
            
            if (e instanceof ValidationException) {
                errorCode = ((ValidationException) e).getErrorCode();
            } else if (e instanceof DomainException) {
                errorCode = ((DomainException) e).getErrorCode();
            }
            
            return MergeGuestCartOutputData.forError(errorCode, message);
        }
    }
}
