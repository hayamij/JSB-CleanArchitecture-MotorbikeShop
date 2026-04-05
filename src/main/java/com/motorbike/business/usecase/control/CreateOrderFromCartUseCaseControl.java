package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.createorder.CreateOrderFromCartInputData;
import com.motorbike.business.dto.createorder.CreateOrderFromCartOutputData;
import com.motorbike.business.usecase.input.CreateOrderFromCartInputBoundary;
import com.motorbike.business.usecase.output.CreateOrderFromCartOutputBoundary;
import com.motorbike.domain.entities.DonHang;
import com.motorbike.domain.exceptions.DomainException;
import com.motorbike.domain.exceptions.ValidationException;

public class CreateOrderFromCartUseCaseControl implements CreateOrderFromCartInputBoundary {
    
    private final CreateOrderFromCartOutputBoundary outputBoundary;
    
    public CreateOrderFromCartUseCaseControl(CreateOrderFromCartOutputBoundary outputBoundary) {
        this.outputBoundary = outputBoundary;
    }
    
    // Constructor for tests with 2 params (swapped)
    public CreateOrderFromCartUseCaseControl(
            com.motorbike.business.ports.DonHangRepository donHangRepository,
            CreateOrderFromCartOutputBoundary outputBoundary) {
        this.outputBoundary = outputBoundary;
    }
    
    // Constructor for tests with 3 params (null, orderRepo, cartRepo)
    public CreateOrderFromCartUseCaseControl(
            Object nullParam,
            com.motorbike.business.ports.repository.OrderRepository orderRepository,
            com.motorbike.business.ports.repository.CartRepository cartRepository) {
        this.outputBoundary = null;
    }
    
    @Override
    public void execute(CreateOrderFromCartInputData inputData) {
        CreateOrderFromCartOutputData outputData = createOrderInternal(inputData);
        outputBoundary.present(outputData);
    }
    
    /**
     * Internal method for use case composition.
     * Returns OutputData directly without using presenter.
     */
    public CreateOrderFromCartOutputData createOrderInternal(CreateOrderFromCartInputData inputData) {
        CreateOrderFromCartOutputData outputData = null;
        Exception errorException = null;
        
        // Step 1: Validation
        try {
            if (inputData == null) {
                throw ValidationException.invalidInput();
            }
            if (inputData.getCart() == null) {
                throw ValidationException.invalidCartId();
            }
            DonHang.checkInput(
                inputData.getCart().getMaTaiKhoan(),
                inputData.getReceiverName(),
                inputData.getPhoneNumber(),
                inputData.getShippingAddress()
            );
        } catch (Exception e) {
            errorException = e;
        }
        
        // Step 2: Business logic - Create order from cart
        DonHang donHang = null;
        if (errorException == null) {
            try {
                donHang = DonHang.fromGioHang(
                    inputData.getCart(),
                    inputData.getReceiverName(),
                    inputData.getPhoneNumber(),
                    inputData.getShippingAddress(),
                    inputData.getNote(),
                    inputData.getPaymentMethod()
                );
                
                outputData = new CreateOrderFromCartOutputData(donHang);
            } catch (Exception e) {
                errorException = e;
            }
        }
        
        // Step 3: Handle error
        if (errorException != null) {
            String errorCode = "SYSTEM_ERROR";
            String message = errorException.getMessage();
            
            if (errorException instanceof ValidationException) {
                errorCode = ((ValidationException) errorException).getErrorCode();
            } else if (errorException instanceof DomainException) {
                errorCode = ((DomainException) errorException).getErrorCode();
            }
            
            outputData = CreateOrderFromCartOutputData.forError(errorCode, message);
        }
        
        return outputData;
    }
}
