package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.user.UpdateUserInputData;
import com.motorbike.business.dto.user.UpdateUserOutputData;
import com.motorbike.business.ports.repository.UserRepository;
import com.motorbike.business.usecase.input.UpdateUserInputBoundary;
import com.motorbike.business.usecase.output.UpdateUserOutputBoundary;
import com.motorbike.domain.entities.TaiKhoan;
import com.motorbike.domain.entities.VaiTro;
import com.motorbike.domain.exceptions.DomainException;
import com.motorbike.domain.exceptions.ValidationException;
import com.motorbike.domain.exceptions.SystemException;

public class UpdateUserUseCaseControl implements UpdateUserInputBoundary {
    
    private final UpdateUserOutputBoundary outputBoundary;
    private final UserRepository userRepository;
    
    public UpdateUserUseCaseControl(UpdateUserOutputBoundary outputBoundary,
                                   UserRepository userRepository) {
        this.outputBoundary = outputBoundary;
        this.userRepository = userRepository;
    }
    
    @Override
    public void execute(UpdateUserInputData inputData) {
        UpdateUserOutputData outputData = null;
        Exception errorException = null;
        TaiKhoan taiKhoan = null;
        
        // Step 1: Validation
        try {
            if (inputData == null) {
                throw ValidationException.invalidInput();
            }
            
            TaiKhoan.checkInput(inputData.getMaTaiKhoan());
            TaiKhoan.validateHoTen(inputData.getHoTen());
            TaiKhoan.validateEmail(inputData.getEmail());
            TaiKhoan.validateSoDienThoai(inputData.getSoDienThoai());
            
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
        
        // Step 3: Update user entity
        if (errorException == null && taiKhoan != null) {
            try {
                // Update hoTen
                taiKhoan.setHoTen(inputData.getHoTen());
                
                // Check if email is changed and already exists
                if (!taiKhoan.getEmail().equals(inputData.getEmail())) {
                    if (userRepository.existsByEmail(inputData.getEmail())) {
                        throw DomainException.emailAlreadyExists(inputData.getEmail());
                    }
                    taiKhoan.setEmail(inputData.getEmail());
                }
                
                taiKhoan.setSoDienThoai(inputData.getSoDienThoai());
                taiKhoan.setDiaChi(inputData.getDiaChi());
                
                // Update role
                if (inputData.getVaiTro() != null && taiKhoan.getVaiTro() != inputData.getVaiTro()) {
                    if (inputData.getVaiTro() == VaiTro.ADMIN) {
                        taiKhoan.thangCapAdmin();
                    } else {
                        taiKhoan.haCapCustomer();
                    }
                }
                
                // Update status
                if (taiKhoan.isHoatDong() != inputData.isHoatDong()) {
                    if (inputData.isHoatDong()) {
                        taiKhoan.moKhoaTaiKhoan();
                    } else {
                        taiKhoan.khoaTaiKhoan();
                    }
                }
                
                taiKhoan = userRepository.save(taiKhoan);
                
                outputData = UpdateUserOutputData.forSuccess(
                    taiKhoan.getMaTaiKhoan(),
                    taiKhoan.getEmail(),
                    taiKhoan.getTenDangNhap(),
                    taiKhoan.getSoDienThoai(),
                    taiKhoan.getVaiTro(),
                    taiKhoan.isHoatDong(),
                    taiKhoan.getNgayCapNhat()
                );
            } catch (Exception e) {
                errorException = e;
            }
        }
        
        // Step 4: Handle error
        if (errorException != null) {
            String errorCode = extractErrorCode(errorException);
            outputData = UpdateUserOutputData.forError(errorCode, errorException.getMessage());
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
