package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.login.LoginInputData;
import com.motorbike.business.dto.login.LoginOutputData;
import com.motorbike.business.ports.repository.UserRepository;
import com.motorbike.business.ports.repository.CartRepository;
import com.motorbike.business.usecase.output.LoginOutputBoundary;
import com.motorbike.domain.entities.TaiKhoan;
import com.motorbike.domain.entities.GioHang;
import java.util.Optional;

/**
 * Login Use Case Control
 * Extends AbstractUseCaseControl for common validation and error handling
 */
public class LoginUseCaseControl 
        extends AbstractUseCaseControl<LoginInputData, LoginOutputBoundary> {
    
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    
    public LoginUseCaseControl(
            LoginOutputBoundary outputBoundary,
            UserRepository userRepository,
            CartRepository cartRepository) {
        super(outputBoundary);
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
    }
    
    @Override
    protected void executeBusinessLogic(LoginInputData inputData) throws Exception {
        Optional<TaiKhoan> userOptional = userRepository.findByEmail(inputData.getEmail());
        
        if (!userOptional.isPresent()) {
            LoginOutputData outputData = LoginOutputData.forError(
                "USER_NOT_FOUND",
                "Không tìm thấy tài khoản với email: " + inputData.getEmail()
            );
            outputBoundary.present(outputData);
            return;
        }
        
        TaiKhoan taiKhoan = userOptional.get();
        
        if (!taiKhoan.kiemTraMatKhau(inputData.getPassword())) {
            LoginOutputData outputData = LoginOutputData.forError(
                "WRONG_PASSWORD",
                "Mật khẩu không đúng"
            );
            outputBoundary.present(outputData);
            return;
        }
        
        if (!taiKhoan.isHoatDong()) {
            LoginOutputData outputData = LoginOutputData.forError(
                "ACCOUNT_LOCKED",
                "Tài khoản đã bị khóa. Vui lòng liên hệ admin."
            );
            outputBoundary.present(outputData);
            return;
        }
        
        taiKhoan.dangNhapThanhCong();
        userRepository.save(taiKhoan);
        
        boolean cartMerged = false;
        int mergedItemsCount = 0;
        
        if (inputData.getGuestCartId() != null) {
            Optional<GioHang> guestCartOpt = cartRepository.findById(inputData.getGuestCartId());
            Optional<GioHang> userCartOpt = cartRepository.findByUserId(taiKhoan.getMaTaiKhoan());
            
            if (guestCartOpt.isPresent()) {
                GioHang guestCart = guestCartOpt.get();
                
                if (userCartOpt.isPresent()) {
                    GioHang userCart = userCartOpt.get();
                    mergedItemsCount = guestCart.getDanhSachSanPham().size();
                    
                    guestCart.getDanhSachSanPham().forEach(item -> {
                        userCart.themSanPham(item);
                    });
                    
                    cartRepository.save(userCart);
                    cartRepository.delete(guestCart.getMaGioHang());
                    cartMerged = true;
                } else {
                    guestCart.setMaTaiKhoan(taiKhoan.getMaTaiKhoan());
                    cartRepository.save(guestCart);
                    mergedItemsCount = guestCart.getDanhSachSanPham().size();
                    cartMerged = true;
                }
            }
        }
        
        LoginOutputData outputData = LoginOutputData.forSuccess(
            taiKhoan.getMaTaiKhoan(),
            taiKhoan.getEmail(),
            taiKhoan.getTenDangNhap(),
            taiKhoan.getVaiTro(),
            taiKhoan.getLanDangNhapCuoi(),
            null,
            cartMerged,
            mergedItemsCount
        );
        
        outputBoundary.present(outputData);
    }
    
    @Override
    protected void validateInput(LoginInputData inputData) {
        checkInputNotNull(inputData);
        
        if (inputData.getEmail() == null || inputData.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email không được để trống");
        }
        
        if (inputData.getPassword() == null || inputData.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Mật khẩu không được để trống");
        }
        
        if (!inputData.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Email không hợp lệ");
        }
    }
    
    @Override
    protected void handleValidationError(IllegalArgumentException e) {
        LoginOutputData outputData = LoginOutputData.forError(
            "INVALID_INPUT",
            e.getMessage()
        );
        outputBoundary.present(outputData);
    }
    
    @Override
    protected void handleSystemError(Exception e) {
        LoginOutputData outputData = LoginOutputData.forError(
            "SYSTEM_ERROR",
            "Đã xảy ra lỗi hệ thống: " + e.getMessage()
        );
        outputBoundary.present(outputData);
    }
}
