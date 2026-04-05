package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.cart.CreateUserCartInputData;
import com.motorbike.business.dto.cart.CreateUserCartOutputData;
import com.motorbike.business.ports.repository.CartRepository;
import com.motorbike.business.usecase.input.CreateUserCartInputBoundary;
import com.motorbike.business.usecase.output.CreateUserCartOutputBoundary;
import com.motorbike.domain.entities.GioHang;
import com.motorbike.domain.exceptions.ValidationException;

/**
 * Secondary UseCase: Tạo giỏ hàng mới cho user
 * Được sử dụng bởi: LoginUseCaseControl, RegisterUseCaseControl
 */
public class CreateUserCartUseCaseControl implements CreateUserCartInputBoundary {
    
    private final CreateUserCartOutputBoundary outputBoundary;
    private final CartRepository cartRepository;
    
    public CreateUserCartUseCaseControl(
            CreateUserCartOutputBoundary outputBoundary,
            CartRepository cartRepository) {
        this.outputBoundary = outputBoundary;
        this.cartRepository = cartRepository;
    }
    
    @Override
    public void execute(CreateUserCartInputData inputData) {
        CreateUserCartOutputData outputData = createInternal(inputData);
        if (outputBoundary != null) {
            outputBoundary.present(outputData);
        }
    }
    
    /**
     * Internal method for use case composition
     * Returns OutputData directly without using presenter
     */
    public CreateUserCartOutputData createInternal(CreateUserCartInputData inputData) {
        try {
            // Validate input
            if (inputData == null || inputData.getUserId() == null) {
                throw ValidationException.invalidInput();
            }
            
            // Create new cart for user
            GioHang newCart = new GioHang(inputData.getUserId());
            GioHang savedCart = cartRepository.save(newCart);
            
            return CreateUserCartOutputData.forSuccess(
                savedCart.getMaGioHang(),
                savedCart.getMaTaiKhoan()
            );
            
        } catch (Exception e) {
            String errorCode = "SYSTEM_ERROR";
            String message = e.getMessage();
            
            if (e instanceof ValidationException) {
                errorCode = ((ValidationException) e).getErrorCode();
            }
            
            return CreateUserCartOutputData.forError(errorCode, message);
        }
    }
}
