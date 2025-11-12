package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.register.RegisterInputData;
import com.motorbike.business.dto.register.RegisterOutputData;
import com.motorbike.business.ports.repository.UserRepository;
import com.motorbike.business.ports.repository.CartRepository;
import com.motorbike.business.usecase.output.RegisterOutputBoundary;
import com.motorbike.domain.entities.TaiKhoan;
import com.motorbike.domain.entities.GioHang;
import com.motorbike.domain.exceptions.InvalidUserException;
import com.motorbike.domain.exceptions.EmailAlreadyExistsException;

/**
 * Register Use Case Control
 * Extends AbstractUseCaseControl for common validation and error handling
 */
public class RegisterUseCaseControl 
        extends AbstractUseCaseControl<RegisterInputData, RegisterOutputBoundary> {
    
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    
    public RegisterUseCaseControl(
            RegisterOutputBoundary outputBoundary,
            UserRepository userRepository,
            CartRepository cartRepository) {
        super(outputBoundary);
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
    }
    
    @Override
    protected void executeBusinessLogic(RegisterInputData inputData) throws Exception {
        try {
            // Simple if-check with throw
            if (userRepository.existsByEmail(inputData.getEmail())) {
                throw new EmailAlreadyExistsException(inputData.getEmail());
            }
            
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
            
            RegisterOutputData outputData = RegisterOutputData.forSuccess(
                savedTaiKhoan.getMaTaiKhoan(),
                savedTaiKhoan.getEmail(),
                savedTaiKhoan.getTenDangNhap(),
                savedTaiKhoan.getVaiTro(),
                savedTaiKhoan.getNgayTao()
            );
            
            outputBoundary.present(outputData);
            
        } catch (InvalidUserException | EmailAlreadyExistsException e) {
            throw e;
        }
    }
    
    @Override
    protected void validateInput(RegisterInputData inputData) {
        checkInputNotNull(inputData);
        
        if (inputData.getPassword() == null || inputData.getConfirmPassword() == null) {
            throw new IllegalArgumentException("Mật khẩu không được để trống");
        }
        
        if (!inputData.getPassword().equals(inputData.getConfirmPassword())) {
            throw new IllegalArgumentException("Mật khẩu xác nhận không khớp");
        }
    }
    
    @Override
    protected void handleValidationError(IllegalArgumentException e) {
        RegisterOutputData outputData = RegisterOutputData.forError(
            "INVALID_INPUT",
            e.getMessage()
        );
        outputBoundary.present(outputData);
    }
    
    @Override
    protected void handleSystemError(Exception e) {
        String errorCode = "SYSTEM_ERROR";
        String message = "Đã xảy ra lỗi hệ thống: " + e.getMessage();
        
        try {
            throw e;
        } catch (InvalidUserException ex) {
            errorCode = ex.getErrorCode();
            message = ex.getMessage();
        } catch (EmailAlreadyExistsException ex) {
            errorCode = ex.getErrorCode();
            message = ex.getMessage();
        } catch (Exception ex) {
            // Keep default
        }
        
        RegisterOutputData outputData = RegisterOutputData.forError(errorCode, message);
        outputBoundary.present(outputData);
    }
}
