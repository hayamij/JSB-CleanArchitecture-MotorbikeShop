package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.user.DeleteUserInputData;
import com.motorbike.business.dto.user.DeleteUserOutputData;
import com.motorbike.business.ports.repository.UserRepository;
import com.motorbike.business.usecase.input.DeleteUserInputBoundary;
import com.motorbike.business.usecase.output.DeleteUserOutputBoundary;
import com.motorbike.domain.entities.TaiKhoan;
import com.motorbike.domain.exceptions.DomainException;
import com.motorbike.domain.exceptions.ValidationException;
import com.motorbike.domain.exceptions.SystemException;

public class DeleteUserUseCaseControl implements DeleteUserInputBoundary {
    
    private final DeleteUserOutputBoundary outputBoundary;
    private final UserRepository userRepository;
    
    public DeleteUserUseCaseControl(DeleteUserOutputBoundary outputBoundary,
                                   UserRepository userRepository) {
        this.outputBoundary = outputBoundary;
        this.userRepository = userRepository;
    }
    
    @Override
    public void execute(DeleteUserInputData inputData) {
        DeleteUserOutputData outputData = null;
        Exception errorException = null;
        TaiKhoan taiKhoan = null;
        
        // Step 1: Validation
        try {
            if (inputData == null) {
                throw ValidationException.invalidInput();
            }
            
            TaiKhoan.checkInput(inputData.getMaTaiKhoan());
        } catch (Exception e) {
            errorException = e;
        }
        
        // Step 2: Check if user exists
        if (errorException == null) {
            try {
                taiKhoan = userRepository.findById(inputData.getMaTaiKhoan())
                    .orElseThrow(() -> DomainException.userNotFound(inputData.getMaTaiKhoan()));
            } catch (Exception e) {
                errorException = e;
            }
        }
        
        // Step 3: Delete user
        if (errorException == null && taiKhoan != null) {
            try {
                String tenDangNhap = taiKhoan.getTenDangNhap();
                Long maTaiKhoan = taiKhoan.getMaTaiKhoan();
                
                userRepository.deleteById(maTaiKhoan);
                
                outputData = DeleteUserOutputData.forSuccess(maTaiKhoan, tenDangNhap);
            } catch (Exception e) {
                errorException = e;
            }
        }
        
        // Step 4: Handle error
        if (errorException != null) {
            String errorCode = extractErrorCode(errorException);
            outputData = DeleteUserOutputData.forError(errorCode, errorException.getMessage());
        }
        
        // Step 5: Present result
        outputBoundary.present(outputData);
    }
    
    private String extractErrorCode(Exception exception) {
        if (exception instanceof ValidationException) {
            return ((ValidationException) exception).getErrorCode();
        } else if (exception instanceof DomainException) {
            return ((DomainException) exception).getErrorCode();
        } else if (exception instanceof SystemException) {
            return ((SystemException) exception).getErrorCode();
        }
        return "SYSTEM_ERROR";
    }
}
