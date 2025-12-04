package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.login.LoginInputData;
import com.motorbike.business.dto.login.LoginOutputData;
import com.motorbike.business.ports.repository.UserRepository;
import com.motorbike.business.ports.repository.CartRepository;
import com.motorbike.business.usecase.output.LoginOutputBoundary;
import com.motorbike.domain.entities.TaiKhoan;
import com.motorbike.domain.entities.GioHang;
import com.motorbike.domain.exceptions.*;
import java.util.Optional;

public class LoginUseCaseControl {
    
    private final LoginOutputBoundary outputBoundary;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    
    public LoginUseCaseControl(
            LoginOutputBoundary outputBoundary,
            UserRepository userRepository,
            CartRepository cartRepository) {
        this.outputBoundary = outputBoundary;
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
    }
    
    public void execute(LoginInputData inputData) {
        LoginOutputData outputData = null;
        Exception errorException = null;
        TaiKhoan taiKhoan = null;
        
        try {
            if (inputData == null) {
                throw ValidationException.invalidInput();
            }
            TaiKhoan.checkInputForLogin(inputData.getEmail(), inputData.getPassword());
        } catch (Exception e) {
            errorException = e;
        }
        
        if (errorException == null) {
            try {
                taiKhoan = userRepository.findByEmail(inputData.getEmail())
                    .orElseThrow(() -> DomainException.userNotFound(inputData.getEmail()));
                
                if (!taiKhoan.kiemTraMatKhau(inputData.getPassword())) {
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
