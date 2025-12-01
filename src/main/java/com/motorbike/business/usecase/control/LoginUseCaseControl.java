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
        
        LoginOutputData outputData = LoginOutputData.forSuccess(
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
        
        outputBoundary.present(outputData);
    }
    
    @Override
    protected void validateInput(LoginInputData inputData) {
        checkInputNotNull(inputData);
        
        if (inputData.getEmail() == null || inputData.getEmail().trim().isEmpty()) {
            throw new EmptyEmailException();
        }
        
        if (inputData.getPassword() == null || inputData.getPassword().isEmpty()) {
            throw new EmptyPasswordException();
        }
        
        if (!inputData.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new InvalidEmailException();
        }
    }
    
    @Override
    protected void handleValidationError(IllegalArgumentException e) {
        String errorCode = "INVALID_INPUT";
        if (e instanceof InvalidInputException) {
            errorCode = ((InvalidInputException) e).getErrorCode();
        }
        LoginOutputData outputData = LoginOutputData.forError(errorCode, e.getMessage());
        outputBoundary.present(outputData);
    }
    
    @Override
    protected void handleSystemError(Exception e) {
        String errorCode;
        String message;
        
        if (e instanceof UserNotFoundException) {
            UserNotFoundException ex = (UserNotFoundException) e;
            errorCode = ex.getErrorCode();
            message = ex.getMessage();
        } else if (e instanceof WrongPasswordException) {
            WrongPasswordException ex = (WrongPasswordException) e;
            errorCode = ex.getErrorCode();
            message = ex.getMessage();
        } else if (e instanceof AccountLockedException) {
            AccountLockedException ex = (AccountLockedException) e;
            errorCode = ex.getErrorCode();
            message = ex.getMessage();
        } else if (e instanceof SystemException) {
            SystemException ex = (SystemException) e;
            errorCode = ex.getErrorCode();
            message = ex.getMessage();
        } else {
            throw new SystemException(e);
        }
        
        LoginOutputData outputData = LoginOutputData.forError(errorCode, message);
        outputBoundary.present(outputData);
    }
}
