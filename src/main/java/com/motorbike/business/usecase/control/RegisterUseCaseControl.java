package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.register.RegisterInputData;
import com.motorbike.business.dto.register.RegisterOutputData;
import com.motorbike.business.ports.repository.UserRepository;
import com.motorbike.business.ports.repository.CartRepository;
import com.motorbike.business.usecase.output.RegisterOutputBoundary;
import com.motorbike.domain.entities.TaiKhoan;
import com.motorbike.domain.entities.GioHang;
import com.motorbike.domain.exceptions.DomainException;
import com.motorbike.domain.exceptions.ValidationException;
import com.motorbike.domain.exceptions.SystemException;

public class RegisterUseCaseControl {
    
    private final RegisterOutputBoundary outputBoundary;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    
    public RegisterUseCaseControl(
            RegisterOutputBoundary outputBoundary,
            UserRepository userRepository,
            CartRepository cartRepository) {
        this.outputBoundary = outputBoundary;
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
    }
    
    public void execute(RegisterInputData inputData) {
        RegisterOutputData outputData = null;
        Exception errorException = null;
        
        try {
            if (inputData == null) {
                throw ValidationException.invalidInput();
            }
            TaiKhoan.checkInputForRegister(
                inputData.getEmail(),
                inputData.getUsername(),
                inputData.getPassword(),
                inputData.getPhoneNumber()
            );
        } catch (Exception e) {
            errorException = e;
        }
        
        if (errorException == null) {
            try {
                if (userRepository.existsByEmail(inputData.getEmail())) {
                    throw DomainException.emailAlreadyExists(inputData.getEmail());
                }
            } catch (Exception e) {
                errorException = e;
            }
        }
        
        if (errorException == null) {
            try {
                TaiKhoan taiKhoan = new TaiKhoan(
                    inputData.getEmail(),
                    inputData.getUsername(),
                    inputData.getPassword(),
                    inputData.getPhoneNumber(),
                    inputData.getAddress()
                );
                
                TaiKhoan savedTaiKhoan = userRepository.save(taiKhoan);
                
                GioHang gioHang = new GioHang(savedTaiKhoan.getMaTaiKhoan());
                cartRepository.save(gioHang);
                
                outputData = RegisterOutputData.forSuccess(
                    savedTaiKhoan.getMaTaiKhoan(),
                    savedTaiKhoan.getEmail(),
                    savedTaiKhoan.getTenDangNhap(),
                    savedTaiKhoan.getVaiTro(),
                    savedTaiKhoan.getNgayTao()
                );
            } catch (Exception e) {
                errorException = e;
            }
        }
        
        if (errorException != null) {
            String errorCode = "SYSTEM_ERROR";
            String message = errorException.getMessage();
            
            if (errorException instanceof ValidationException) {
                errorCode = ((ValidationException) errorException).getErrorCode();
            } else if (errorException instanceof DomainException) {
                errorCode = ((DomainException) errorException).getErrorCode();
            } else if (errorException instanceof SystemException) {
                errorCode = ((SystemException) errorException).getErrorCode();
            }
            
            outputData = RegisterOutputData.forError(errorCode, message);
        }
        
        outputBoundary.present(outputData);
    }
}
