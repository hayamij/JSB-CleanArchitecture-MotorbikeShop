package com.motorbike.business.usecase.impl;

import com.motorbike.business.dto.register.RegisterInputData;
import com.motorbike.business.dto.register.RegisterOutputData;
import com.motorbike.business.ports.repository.UserRepository;
import com.motorbike.business.ports.repository.CartRepository;
import com.motorbike.business.usecase.RegisterInputBoundary;
import com.motorbike.business.usecase.RegisterOutputBoundary;
import com.motorbike.domain.entities.TaiKhoan;
import com.motorbike.domain.entities.GioHang;
import com.motorbike.domain.exceptions.InvalidUserException;

/**
 * Register Use Case Implementation
 * Orchestrates user registration business flow
 */
public class RegisterUseCaseImpl implements RegisterInputBoundary {
    
    private final RegisterOutputBoundary outputBoundary;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    
    public RegisterUseCaseImpl(
            RegisterOutputBoundary outputBoundary,
            UserRepository userRepository,
            CartRepository cartRepository) {
        this.outputBoundary = outputBoundary;
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
    }
    
    @Override
    public void execute(RegisterInputData inputData) {
        try {
            // 1. Validate input
            validateInput(inputData);
            
            // 2. Check if email already exists
            if (userRepository.existsByEmail(inputData.getEmail())) {
                RegisterOutputData outputData = RegisterOutputData.forError(
                    "EMAIL_EXISTS",
                    "Email đã được sử dụng: " + inputData.getEmail()
                );
                outputBoundary.present(outputData);
                return;
            }
            
            // 3. Create new TaiKhoan - entity handles validation
            TaiKhoan taiKhoan = new TaiKhoan(
                inputData.getEmail(),
                inputData.getUsername(),
                inputData.getPassword(), // Will be hashed by entity
                inputData.getPhoneNumber(),
                inputData.getAddress()
            );
            
            // 4. Save user
            TaiKhoan savedTaiKhoan = userRepository.save(taiKhoan);
            
            // 5. Create shopping cart for new user
            GioHang gioHang = new GioHang(savedTaiKhoan.getMaTaiKhoan());
            cartRepository.save(gioHang);
            
            // 6. Create success output
            RegisterOutputData outputData = RegisterOutputData.forSuccess(
                savedTaiKhoan.getMaTaiKhoan(),
                savedTaiKhoan.getEmail(),
                savedTaiKhoan.getTenDangNhap(),
                savedTaiKhoan.getVaiTro(),
                savedTaiKhoan.getNgayTao()
            );
            
            outputBoundary.present(outputData);
            
        } catch (InvalidUserException e) {
            // Business validation error from entity
            RegisterOutputData outputData = RegisterOutputData.forError(
                e.getErrorCode(),
                e.getMessage()
            );
            outputBoundary.present(outputData);
            
        } catch (IllegalArgumentException e) {
            // Input validation error
            RegisterOutputData outputData = RegisterOutputData.forError(
                "INVALID_INPUT",
                e.getMessage()
            );
            outputBoundary.present(outputData);
            
        } catch (Exception e) {
            // Unexpected error
            RegisterOutputData outputData = RegisterOutputData.forError(
                "SYSTEM_ERROR",
                "Đã xảy ra lỗi hệ thống: " + e.getMessage()
            );
            outputBoundary.present(outputData);
        }
    }
    
    private void validateInput(RegisterInputData inputData) {
        if (inputData == null) {
            throw new IllegalArgumentException("Input data không được null");
        }
        
        if (inputData.getPassword() == null || inputData.getConfirmPassword() == null) {
            throw new IllegalArgumentException("Mật khẩu không được để trống");
        }
        
        if (!inputData.getPassword().equals(inputData.getConfirmPassword())) {
            throw new IllegalArgumentException("Mật khẩu xác nhận không khớp");
        }
    }
}
