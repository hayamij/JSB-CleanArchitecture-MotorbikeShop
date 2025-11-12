package com.motorbike.business.usecase.impl;

import com.motorbike.business.dto.login.LoginInputData;
import com.motorbike.business.dto.login.LoginOutputData;
import com.motorbike.business.ports.repository.UserRepository;
import com.motorbike.business.ports.repository.CartRepository;
import com.motorbike.business.usecase.LoginInputBoundary;
import com.motorbike.business.usecase.LoginOutputBoundary;
import com.motorbike.domain.entities.TaiKhoan;
import com.motorbike.domain.entities.GioHang;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Login Use Case Implementation
 * Orchestrates the login business flow
 * Follows Clean Architecture - depends only on interfaces and domain entities
 */
public class LoginUseCaseImpl implements LoginInputBoundary {
    
    private final LoginOutputBoundary outputBoundary;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    
    /**
     * Constructor injection - Dependency Inversion Principle
     */
    public LoginUseCaseImpl(
            LoginOutputBoundary outputBoundary,
            UserRepository userRepository,
            CartRepository cartRepository) {
        this.outputBoundary = outputBoundary;
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
    }
    
    @Override
    public void execute(LoginInputData inputData) {
        try {
            // 1. Validate input
            validateInput(inputData);
            
            // 2. Find user by email
            Optional<TaiKhoan> userOptional = userRepository.findByEmail(inputData.getEmail());
            
            if (!userOptional.isPresent()) {
                // User not found
                LoginOutputData outputData = LoginOutputData.forError(
                    "USER_NOT_FOUND",
                    "Không tìm thấy tài khoản với email: " + inputData.getEmail()
                );
                outputBoundary.present(outputData);
                return;
            }
            
            TaiKhoan taiKhoan = userOptional.get();
            
            // 3. Verify password using entity business logic
            if (!taiKhoan.kiemTraMatKhau(inputData.getPassword())) {
                // Wrong password
                LoginOutputData outputData = LoginOutputData.forError(
                    "WRONG_PASSWORD",
                    "Mật khẩu không đúng"
                );
                outputBoundary.present(outputData);
                return;
            }
            
            // 4. Check if account is active
            if (!taiKhoan.isHoatDong()) {
                LoginOutputData outputData = LoginOutputData.forError(
                    "ACCOUNT_LOCKED",
                    "Tài khoản đã bị khóa. Vui lòng liên hệ admin."
                );
                outputBoundary.present(outputData);
                return;
            }
            
            // 5. Update last login time - business logic
            taiKhoan.dangNhapThanhCong();
            userRepository.save(taiKhoan);
            
            // 6. Handle cart merging if guest had cart
            boolean cartMerged = false;
            int mergedItemsCount = 0;
            
            if (inputData.getGuestCartId() != null) {
                Optional<GioHang> guestCartOpt = cartRepository.findById(inputData.getGuestCartId());
                Optional<GioHang> userCartOpt = cartRepository.findByUserId(taiKhoan.getMaTaiKhoan());
                
                if (guestCartOpt.isPresent()) {
                    GioHang guestCart = guestCartOpt.get();
                    
                    if (userCartOpt.isPresent()) {
                        // Merge guest cart into user cart
                        GioHang userCart = userCartOpt.get();
                        mergedItemsCount = guestCart.getDanhSachSanPham().size();
                        
                        // Add all items from guest cart to user cart
                        guestCart.getDanhSachSanPham().forEach(item -> {
                            userCart.themSanPham(item);
                        });
                        
                        cartRepository.save(userCart);
                        cartRepository.delete(guestCart.getMaGioHang());
                        cartMerged = true;
                    } else {
                        // Just assign guest cart to user
                        guestCart.setMaTaiKhoan(taiKhoan.getMaTaiKhoan());
                        cartRepository.save(guestCart);
                        mergedItemsCount = guestCart.getDanhSachSanPham().size();
                        cartMerged = true;
                    }
                }
            }
            
            // 7. Create success output data
            LoginOutputData outputData = LoginOutputData.forSuccess(
                taiKhoan.getMaTaiKhoan(),
                taiKhoan.getEmail(),
                taiKhoan.getTenDangNhap(),
                taiKhoan.getVaiTro(),
                taiKhoan.getLanDangNhapCuoi(),
                null, // sessionToken - for future implementation
                cartMerged,
                mergedItemsCount
            );
            
            // 8. Present output
            outputBoundary.present(outputData);
            
        } catch (IllegalArgumentException e) {
            // Input validation error
            LoginOutputData outputData = LoginOutputData.forError(
                "INVALID_INPUT",
                e.getMessage()
            );
            outputBoundary.present(outputData);
            
        } catch (Exception e) {
            // Unexpected error
            LoginOutputData outputData = LoginOutputData.forError(
                "SYSTEM_ERROR",
                "Đã xảy ra lỗi hệ thống: " + e.getMessage()
            );
            outputBoundary.present(outputData);
        }
    }
    
    /**
     * Validate input data
     * @param inputData Input data to validate
     * @throws IllegalArgumentException if validation fails
     */
    private void validateInput(LoginInputData inputData) {
        if (inputData == null) {
            throw new IllegalArgumentException("Input data không được null");
        }
        
        if (inputData.getEmail() == null || inputData.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email không được để trống");
        }
        
        if (inputData.getPassword() == null || inputData.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Mật khẩu không được để trống");
        }
        
        // Basic email format validation
        if (!inputData.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Email không hợp lệ");
        }
    }
}
