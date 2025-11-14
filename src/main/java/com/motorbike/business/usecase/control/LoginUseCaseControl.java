package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.login.LoginInputData;
import com.motorbike.business.dto.login.LoginOutputData;
import com.motorbike.business.ports.repository.UserRepository;
import com.motorbike.business.ports.repository.CartRepository;
import com.motorbike.business.usecase.output.LoginOutputBoundary;
import com.motorbike.domain.entities.TaiKhoan;
import com.motorbike.domain.entities.GioHang;
import com.motorbike.domain.exceptions.UserNotFoundException;
import com.motorbike.domain.exceptions.WrongPasswordException;
import com.motorbike.domain.exceptions.AccountLockedException;
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
        TaiKhoan taiKhoan = userRepository.findByEmail(inputData.getEmail())
            .orElseThrow(() -> new UserNotFoundException(inputData.getEmail()));
        
        // Simple if-checks with throw - cleaner than nested try-catch
        if (!taiKhoan.kiemTraMatKhau(inputData.getPassword())) {
            throw new WrongPasswordException();
        }
        
        if (!taiKhoan.isHoatDong()) {
            throw new AccountLockedException();
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
        String errorCode = "SYSTEM_ERROR";
        String message = "Đã xảy ra lỗi hệ thống: " + e.getMessage();
        
        try {
            throw e;
        } catch (UserNotFoundException ex) {
            errorCode = ex.getErrorCode();
            message = ex.getMessage();
        } catch (WrongPasswordException ex) {
            errorCode = ex.getErrorCode();
            message = ex.getMessage();
        } catch (AccountLockedException ex) {
            errorCode = ex.getErrorCode();
            message = ex.getMessage();
        } catch (Exception ex) {
            // Keep default SYSTEM_ERROR
        }
        
        LoginOutputData outputData = LoginOutputData.forError(errorCode, message);
        outputBoundary.present(outputData);
    }
}
