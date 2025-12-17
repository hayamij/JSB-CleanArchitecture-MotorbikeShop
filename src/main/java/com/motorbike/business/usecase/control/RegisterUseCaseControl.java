package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.register.RegisterInputData;
import com.motorbike.business.dto.register.RegisterOutputData;
import com.motorbike.business.ports.repository.UserRepository;
import com.motorbike.business.ports.repository.CartRepository;
import com.motorbike.business.usecase.output.RegisterOutputBoundary;
import com.motorbike.business.dto.user.ValidateUserRegistrationInputData;
import com.motorbike.business.dto.user.CheckUserDuplicationInputData;
import com.motorbike.business.dto.user.HashPasswordInputData;
import com.motorbike.business.usecase.input.ValidateUserRegistrationInputBoundary;
import com.motorbike.business.usecase.input.CheckUserDuplicationInputBoundary;
import com.motorbike.business.usecase.input.HashPasswordInputBoundary;
import com.motorbike.domain.entities.TaiKhoan;
import com.motorbike.domain.entities.GioHang;
import com.motorbike.domain.exceptions.DomainException;
import com.motorbike.domain.exceptions.ValidationException;
import com.motorbike.business.usecase.input.RegisterInputBoundary;
import com.motorbike.domain.exceptions.SystemException;

public class RegisterUseCaseControl implements RegisterInputBoundary {
    
    private final RegisterOutputBoundary outputBoundary;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final ValidateUserRegistrationInputBoundary validateUserRegistrationUseCase;
    private final CheckUserDuplicationInputBoundary checkUserDuplicationUseCase;
    private final HashPasswordInputBoundary hashPasswordUseCase;
    
    public RegisterUseCaseControl(
            RegisterOutputBoundary outputBoundary,
            UserRepository userRepository,
            CartRepository cartRepository,
            ValidateUserRegistrationInputBoundary validateUserRegistrationUseCase,
            CheckUserDuplicationInputBoundary checkUserDuplicationUseCase,
            HashPasswordInputBoundary hashPasswordUseCase) {
        this.outputBoundary = outputBoundary;
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.validateUserRegistrationUseCase = validateUserRegistrationUseCase;
        this.checkUserDuplicationUseCase = checkUserDuplicationUseCase;
        this.hashPasswordUseCase = hashPasswordUseCase;
    }

    // Constructor with parameter order: outputBoundary first (for backward compatibility)
    public RegisterUseCaseControl(
            RegisterOutputBoundary outputBoundary,
            UserRepository userRepository,
            CartRepository cartRepository) {
        this.outputBoundary = outputBoundary;
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.validateUserRegistrationUseCase = new ValidateUserRegistrationUseCaseControl(null);
        this.checkUserDuplicationUseCase = new CheckUserDuplicationUseCaseControl(null, userRepository);
        this.hashPasswordUseCase = new HashPasswordUseCaseControl(null);
    }
    
    public void execute(RegisterInputData inputData) {
        RegisterOutputData outputData = null;
        Exception errorException = null;
        
        // Step 1: Basic validation
        try {
            if (inputData == null) {
                throw ValidationException.invalidInput();
            }
        } catch (Exception e) {
            errorException = e;
        }
        
        // Step 2: UC-57 - Validate user registration data
        if (errorException == null) {
            try {
                // Using Vietnamese DTO: hoTen, email, tenDangNhap, matKhau, soDienThoai
                ValidateUserRegistrationInputData validateInput = new ValidateUserRegistrationInputData(
                    inputData.getName(),        // hoTen (fullName)
                    inputData.getEmail(),       // email
                    inputData.getUsername(),    // tenDangNhap (username)
                    inputData.getPassword(),    // matKhau (password)
                    inputData.getPhoneNumber()  // soDienThoai (phoneNumber)
                );
                var validateResult = ((ValidateUserRegistrationUseCaseControl) validateUserRegistrationUseCase)
                    .validateInternal(validateInput);
                
                if (!validateResult.isValid()) {
                    throw new ValidationException(
                        String.join("; ", validateResult.getErrors()),
                        "VALIDATION_ERROR"
                    );
                }
            } catch (Exception e) {
                errorException = e;
            }
        }
        
        // Step 3: UC-58 - Check user duplication
        if (errorException == null) {
            try {
                CheckUserDuplicationInputData checkDupInput = new CheckUserDuplicationInputData(
                    inputData.getEmail(),
                    inputData.getUsername(),
                    null  // excludeUserId - not applicable for registration
                );
                var dupResult = ((CheckUserDuplicationUseCaseControl) checkUserDuplicationUseCase)
                    .checkInternal(checkDupInput);
                
                if (dupResult.isDuplicate()) {
                    if ("email".equals(dupResult.getDuplicatedField())) {
                        throw DomainException.emailAlreadyExists(inputData.getEmail());
                    } else if ("username".equals(dupResult.getDuplicatedField())) {
                        throw DomainException.usernameAlreadyExists(inputData.getUsername());
                    }
                }
            } catch (Exception e) {
                errorException = e;
            }
        }
        
        // Step 4: UC-59 - Hash password and create user
        if (errorException == null) {
            try {
                HashPasswordInputData hashInput = new HashPasswordInputData(inputData.getPassword());
                var hashResult = ((HashPasswordUseCaseControl) hashPasswordUseCase)
                    .hashInternal(hashInput);
                
                if (!hashResult.isSuccess()) {
                    throw new SystemException(hashResult.getErrorMessage(), hashResult.getErrorCode());
                }
                
                TaiKhoan taiKhoan = new TaiKhoan(
                    inputData.getName(),
                    inputData.getEmail(),
                    inputData.getUsername(),
                    hashResult.getHashedPassword(),  // Use hashed password
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
                    savedTaiKhoan.getSoDienThoai(),
                    savedTaiKhoan.getDiaChi(),
                    savedTaiKhoan.getVaiTro(),
                    savedTaiKhoan.getNgayTao()
                );
            } catch (Exception e) {
                errorException = e;
            }
        }
        
        // Step 5: Handle error
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
