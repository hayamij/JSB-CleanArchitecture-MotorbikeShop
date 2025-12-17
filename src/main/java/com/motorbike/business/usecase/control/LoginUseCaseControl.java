package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.login.LoginInputData;
import com.motorbike.business.dto.login.LoginOutputData;
import com.motorbike.business.ports.repository.UserRepository;
import com.motorbike.business.ports.repository.CartRepository;
import com.motorbike.business.usecase.output.LoginOutputBoundary;
import com.motorbike.business.dto.user.VerifyPasswordInputData;
import com.motorbike.business.dto.cart.CreateUserCartInputData;
import com.motorbike.business.dto.cart.MergeGuestCartInputData;
import com.motorbike.business.usecase.input.VerifyPasswordInputBoundary;
import com.motorbike.business.usecase.input.CreateUserCartInputBoundary;
import com.motorbike.business.usecase.input.MergeGuestCartInputBoundary;
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
    private final CreateUserCartInputBoundary createUserCartUseCase;
    private final MergeGuestCartInputBoundary mergeGuestCartUseCase;
    
    public LoginUseCaseControl(
            LoginOutputBoundary outputBoundary,
            UserRepository userRepository,
            CartRepository cartRepository,
            VerifyPasswordInputBoundary verifyPasswordUseCase,
            CreateUserCartInputBoundary createUserCartUseCase,
            MergeGuestCartInputBoundary mergeGuestCartUseCase) {
        this.outputBoundary = outputBoundary;
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.verifyPasswordUseCase = verifyPasswordUseCase;
        this.createUserCartUseCase = createUserCartUseCase;
        this.mergeGuestCartUseCase = mergeGuestCartUseCase;
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
        this.createUserCartUseCase = new CreateUserCartUseCaseControl(null, cartRepository);
        this.mergeGuestCartUseCase = new MergeGuestCartUseCaseControl(null, cartRepository);
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
                // Step 4: Update last login
                taiKhoan.dangNhapThanhCong();
                userRepository.save(taiKhoan);
                
                boolean cartMerged = false;
                int mergedItemsCount = 0;
                Long userCartId = null;
                
                // Step 5: Get or create user cart
                Optional<GioHang> userCartOpt = cartRepository.findByUserId(taiKhoan.getMaTaiKhoan());
                
                if (userCartOpt.isPresent()) {
                    userCartId = userCartOpt.get().getMaGioHang();
                } else {
                    // UC-71 [CreateUserCartUseCaseControl] - Create new cart for user
                    CreateUserCartInputData createCartInput = new CreateUserCartInputData(taiKhoan.getMaTaiKhoan());
                    var createCartResult = ((CreateUserCartUseCaseControl) createUserCartUseCase)
                        .createInternal(createCartInput);
                    
                    if (!createCartResult.isSuccess()) {
                        throw new SystemException(createCartResult.getErrorMessage(), createCartResult.getErrorCode());
                    }
                    userCartId = createCartResult.getCartId();
                }
                
                // Step 6: Merge guest cart if exists
                if (inputData.getGuestCartId() != null && userCartId != null) {
                    // UC-72 [MergeGuestCartUseCaseControl] - Merge guest cart to user cart
                    MergeGuestCartInputData mergeInput = new MergeGuestCartInputData(
                        inputData.getGuestCartId(),
                        userCartId
                    );
                    var mergeResult = ((MergeGuestCartUseCaseControl) mergeGuestCartUseCase)
                        .mergeInternal(mergeInput);
                    
                    if (mergeResult.isSuccess()) {
                        cartMerged = mergeResult.isCartMerged();
                        mergedItemsCount = mergeResult.getMergedItemsCount();
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
