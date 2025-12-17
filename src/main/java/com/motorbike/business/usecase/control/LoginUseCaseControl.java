package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.login.LoginInputData;
import com.motorbike.business.dto.login.LoginOutputData;
import com.motorbike.business.ports.repository.UserRepository;
import com.motorbike.business.ports.repository.CartRepository;
import com.motorbike.business.usecase.output.LoginOutputBoundary;
import com.motorbike.business.dto.user.VerifyPasswordInputData;
import com.motorbike.business.usecase.input.VerifyPasswordInputBoundary;
import com.motorbike.domain.entities.TaiKhoan;
import com.motorbike.domain.entities.GioHang;
import com.motorbike.business.usecase.input.LoginInputBoundary;
import com.motorbike.domain.exceptions.*;
import java.util.Optional;

public class LoginUseCaseControl implements LoginInputBoundary{
    
    private final LoginOutputBoundary outputBoundary;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final VerifyPasswordInputBoundary verifyPasswordUseCase;
    
    public LoginUseCaseControl(
            LoginOutputBoundary outputBoundary,
            UserRepository userRepository,
            CartRepository cartRepository,
            VerifyPasswordInputBoundary verifyPasswordUseCase) {
        this.outputBoundary = outputBoundary;
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.verifyPasswordUseCase = verifyPasswordUseCase;
    }

    // Constructor with parameter order: outputBoundary first (for backward compatibility)
    public LoginUseCaseControl(
            LoginOutputBoundary outputBoundary,
            UserRepository userRepository,
            CartRepository cartRepository) {
        this.outputBoundary = outputBoundary;
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.verifyPasswordUseCase = new VerifyPasswordUseCaseControl(null);
    }
    
    public void execute(LoginInputData inputData) {
        LoginOutputData outputData = null;
        Exception errorException = null;
        TaiKhoan taiKhoan = null;
        
        try {
            if (inputData == null) {
                throw ValidationException.invalidInput();
            }
            // Validate login input (hỗ trợ email/username)
            TaiKhoan.checkInputForLogin(inputData.getUsername(), inputData.getPassword());
        } catch (Exception e) {
            errorException = e;
        }
        
        if (errorException == null) {
            // Step 2: Find user
            try {
                taiKhoan = userRepository.findByUsernameOrEmailOrPhone(inputData.getUsername())
                    .orElseThrow(() -> DomainException.userNotFound(inputData.getUsername()));
                
                // Step 3: UC-60 - Verify password
                VerifyPasswordInputData verifyInput = new VerifyPasswordInputData(
                    inputData.getPassword(),
                    taiKhoan.getMatKhau()  // hashed password from DB
                );
                var verifyResult = ((VerifyPasswordUseCaseControl) verifyPasswordUseCase)
                    .verifyInternal(verifyInput);
                
                if (!verifyResult.isValid()) {
                    throw DomainException.wrongPassword();
                }
                
                if (!taiKhoan.isHoatDong()) {
                    throw DomainException.accountLocked();
                }
            } catch (Exception e) {
                errorException = e;
            }
        }
        
        if (errorException == null && taiKhoan != null) {
            try {
                taiKhoan.dangNhapThanhCong();
                userRepository.save(taiKhoan);
                
                boolean cartMerged = false;
                int mergedItemsCount = 0;
                Long userCartId = null;
                
                Optional<GioHang> userCartOpt = cartRepository.findByUserId(taiKhoan.getMaTaiKhoan());
                final GioHang userCart;
                
                if (userCartOpt.isPresent()) {
                    userCart = userCartOpt.get();
                    userCartId = userCart.getMaGioHang();
                } else {
                    GioHang newCart = new GioHang(taiKhoan.getMaTaiKhoan());
                    userCart = cartRepository.save(newCart);
                    userCartId = userCart.getMaGioHang();
                }
                
                if (inputData.getGuestCartId() != null) {
                    Optional<GioHang> guestCartOpt = cartRepository.findById(inputData.getGuestCartId());
                    
                    if (guestCartOpt.isPresent()) {
                        GioHang guestCart = guestCartOpt.get();
                        mergedItemsCount = guestCart.getDanhSachSanPham().size();
                        
                        guestCart.getDanhSachSanPham().forEach(item -> {
                            userCart.themSanPham(item);
                        });
                        
                        cartRepository.save(userCart);
                        cartRepository.delete(guestCart.getMaGioHang());
                        cartMerged = true;
                    }
                }
                
                outputData = LoginOutputData.forSuccess(
                    taiKhoan.getMaTaiKhoan(),
                    taiKhoan.getEmail(),
                    taiKhoan.getTenDangNhap(),
                    taiKhoan.getSoDienThoai(),
                    taiKhoan.getDiaChi(),
                    taiKhoan.getVaiTro(),
                    taiKhoan.getLanDangNhapCuoi(),
                    null,
                    userCartId,
                    cartMerged,
                    mergedItemsCount
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
            
            outputData = LoginOutputData.forError(errorCode, message);
        }
        
        outputBoundary.present(outputData);
    }
}
