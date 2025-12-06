package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.user.AddUserInputData;
import com.motorbike.business.dto.user.AddUserOutputData;
import com.motorbike.business.ports.repository.UserRepository;
import com.motorbike.business.usecase.input.AddUserInputBoundary;
import com.motorbike.business.usecase.output.AddUserOutputBoundary;
import com.motorbike.domain.entities.TaiKhoan;
import com.motorbike.domain.entities.VaiTro;
import com.motorbike.domain.exceptions.DomainException;
import com.motorbike.domain.exceptions.ValidationException;
import com.motorbike.domain.exceptions.SystemException;

public class AddUserUseCaseControl implements AddUserInputBoundary {
    
    private final AddUserOutputBoundary outputBoundary;
    private final UserRepository userRepository;
    
    public AddUserUseCaseControl(AddUserOutputBoundary outputBoundary,
                                UserRepository userRepository) {
        this.outputBoundary = outputBoundary;
        this.userRepository = userRepository;
    }
    
    @Override
    public void execute(AddUserInputData inputData) {
        AddUserOutputData outputData = null;
        Exception errorException = null;
        TaiKhoan taiKhoan = null;
        
        // Step 1: Validation
        try {
            if (inputData == null) {
                throw ValidationException.invalidInput();
            }
            
            TaiKhoan.checkInputForRegister(
                inputData.getHoTen(),
                inputData.getEmail(),
                inputData.getTenDangNhap(),
                inputData.getMatKhau(),
                inputData.getSoDienThoai()
            );
            
            // Check if email already exists
            if (userRepository.existsByEmail(inputData.getEmail())) {
                throw DomainException.emailAlreadyExists(inputData.getEmail());
            }
            
        } catch (Exception e) {
            errorException = e;
        }
        
        // Step 2: Create user entity
        if (errorException == null) {
            try {
                taiKhoan = new TaiKhoan(
                    inputData.getHoTen(),
                    inputData.getEmail(),
                    inputData.getTenDangNhap(),
                    inputData.getMatKhau(),
                    inputData.getSoDienThoai(),
                    inputData.getDiaChi()
                );
                
                // Set role and status if provided by admin
                if (inputData.getVaiTro() != null) {
                    if (inputData.getVaiTro() == VaiTro.ADMIN) {
                        taiKhoan.thangCapAdmin();
                    }
                }
                
                if (!inputData.isHoatDong()) {
                    taiKhoan.khoaTaiKhoan();
                }
                
            } catch (Exception e) {
                errorException = e;
            }
        }
        
        // Step 3: Save to database
        if (errorException == null && taiKhoan != null) {
            try {
                taiKhoan = userRepository.save(taiKhoan);
                
                outputData = AddUserOutputData.forSuccess(
                    taiKhoan.getMaTaiKhoan(),
                    taiKhoan.getEmail(),
                    taiKhoan.getTenDangNhap(),
                    taiKhoan.getSoDienThoai(),
                    taiKhoan.getVaiTro(),
                    taiKhoan.isHoatDong(),
                    taiKhoan.getNgayTao()
                );
            } catch (Exception e) {
                errorException = e;
            }
        }
        
        // Step 4: Handle error
        if (errorException != null) {
            String errorCode = extractErrorCode(errorException);
            outputData = AddUserOutputData.forError(errorCode, errorException.getMessage());
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
