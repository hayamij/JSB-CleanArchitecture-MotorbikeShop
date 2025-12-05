package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.user.CreateUserInputData;
import com.motorbike.business.dto.user.CreateUserOutputData;
import com.motorbike.business.ports.repository.UserRepository;
import com.motorbike.business.usecase.output.CreateUserOutputBoundary;
import com.motorbike.domain.entities.TaiKhoan;
// import com.motorbike.domain.entities.VaiTro;
import com.motorbike.domain.exceptions.*;

public class CreateUserUseCaseControl {
    
    private final CreateUserOutputBoundary outputBoundary;
    private final UserRepository userRepository;
    
    public CreateUserUseCaseControl(
            CreateUserOutputBoundary outputBoundary,
            UserRepository userRepository) {
        this.outputBoundary = outputBoundary;
        this.userRepository = userRepository;
    }
    
    public void execute(CreateUserInputData inputData) {
        CreateUserOutputData outputData = null;
        Exception errorException = null;
        
        // Step 1: Validation
        try {
            if (inputData == null) {
                throw ValidationException.invalidInput();
            }
            
            TaiKhoan.checkInputForRegister(
                inputData.getEmail(),
                inputData.getTenDangNhap(),
                inputData.getMatKhau(),
                inputData.getSoDienThoai()
            );
            
            if (userRepository.existsByEmail(inputData.getEmail())) {
                throw DomainException.emailAlreadyExists(inputData.getEmail());
            }
        } catch (Exception e) {
            errorException = e;
        }
        
        // Step 2: Business logic
        if (errorException == null) {
            try {
                TaiKhoan taiKhoan = new TaiKhoan(
                    inputData.getEmail(),
                    inputData.getTenDangNhap(),
                    inputData.getMatKhau(),
                    inputData.getSoDienThoai(),
                    inputData.getDiaChi()
                );
                
                // Set role if specified
                if (inputData.getVaiTro() != null && inputData.getVaiTro().equalsIgnoreCase("ADMIN")) {
                    taiKhoan.thangCapAdmin();
                }
                
                TaiKhoan savedTaiKhoan = userRepository.save(taiKhoan);
                
                outputData = CreateUserOutputData.forSuccess(
                    savedTaiKhoan.getMaTaiKhoan(),
                    savedTaiKhoan.getEmail(),
                    savedTaiKhoan.getTenDangNhap()
                );
            } catch (Exception e) {
                errorException = e;
            }
        }
        
        // Step 3: Handle error
        if (errorException != null) {
            String errorCode = extractErrorCode(errorException);
            outputData = CreateUserOutputData.forError(errorCode, errorException.getMessage());
        }
        
        // Step 4: Present result
        outputBoundary.present(outputData);
    }
    
    private String extractErrorCode(Exception e) {
        if (e instanceof ValidationException) {
            return ((ValidationException) e).getErrorCode();
        } else if (e instanceof DomainException) {
            return ((DomainException) e).getErrorCode();
        } else if (e instanceof SystemException) {
            return ((SystemException) e).getErrorCode();
        }
        return "SYSTEM_ERROR";
    }
}
