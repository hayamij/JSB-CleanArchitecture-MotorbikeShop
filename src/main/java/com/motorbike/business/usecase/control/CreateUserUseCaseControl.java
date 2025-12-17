package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.user.AddUserInputData;
import com.motorbike.business.dto.user.CreateUserInputData;
import com.motorbike.business.dto.user.CreateUserOutputData;
import com.motorbike.business.ports.repository.UserRepository;
import com.motorbike.business.usecase.input.AddUserInputBoundary;
import com.motorbike.business.usecase.output.CreateUserOutputBoundary;
import com.motorbike.business.dto.validateuserregistration.ValidateUserRegistrationInputData;
import com.motorbike.business.dto.checkuserduplication.CheckUserDuplicationInputData;
import com.motorbike.business.dto.hashpassword.HashPasswordInputData;
import com.motorbike.business.usecase.input.ValidateUserRegistrationInputBoundary;
import com.motorbike.business.usecase.input.CheckUserDuplicationInputBoundary;
import com.motorbike.business.usecase.input.HashPasswordInputBoundary;
import com.motorbike.domain.entities.TaiKhoan;
// import com.motorbike.domain.entities.VaiTro;
import com.motorbike.domain.exceptions.*;

public class CreateUserUseCaseControl implements AddUserInputBoundary {
    
    private final CreateUserOutputBoundary outputBoundary;
    private final UserRepository userRepository;
    private final ValidateUserRegistrationInputBoundary validateUserRegistrationUseCase;
    private final CheckUserDuplicationInputBoundary checkUserDuplicationUseCase;
    private final HashPasswordInputBoundary hashPasswordUseCase;
    
    public CreateUserUseCaseControl(
            CreateUserOutputBoundary outputBoundary,
            UserRepository userRepository,
            ValidateUserRegistrationInputBoundary validateUserRegistrationUseCase,
            CheckUserDuplicationInputBoundary checkUserDuplicationUseCase,
            HashPasswordInputBoundary hashPasswordUseCase) {
        this.outputBoundary = outputBoundary;
        this.userRepository = userRepository;
        this.validateUserRegistrationUseCase = validateUserRegistrationUseCase;
        this.checkUserDuplicationUseCase = checkUserDuplicationUseCase;
        this.hashPasswordUseCase = hashPasswordUseCase;
    }
    
    // Constructor for tests with 2 params
    public CreateUserUseCaseControl(
            CreateUserOutputBoundary outputBoundary,
            UserRepository userRepository) {
        this.outputBoundary = outputBoundary;
        this.userRepository = userRepository;
        this.validateUserRegistrationUseCase = new ValidateUserRegistrationUseCaseControl(null);
        this.checkUserDuplicationUseCase = new CheckUserDuplicationUseCaseControl(null, userRepository);
        this.hashPasswordUseCase = new HashPasswordUseCaseControl(null);
    }
    
    @Override
    public void execute(AddUserInputData inputData) {
        if (inputData == null) {
            throw ValidationException.invalidInput();
        }
        // Convert AddUserInputData to CreateUserInputData
        CreateUserInputData createInput = new CreateUserInputData(
            inputData.getUsername(),
            inputData.getPassword(),
            inputData.getEmail(),
            inputData.getHoTen(),
            inputData.getSoDienThoai(),
            inputData.getVaiTro()
        );
        executeInternal(createInput);
    }
    
    private void executeInternal(CreateUserInputData inputData) {
        CreateUserOutputData outputData = null;
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
                ValidateUserRegistrationInputData validateInput = new ValidateUserRegistrationInputData(
                    inputData.getHoTen(),
                    inputData.getEmail(),
                    inputData.getTenDangNhap(),
                    inputData.getMatKhau(),
                    inputData.getSoDienThoai()
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
                    inputData.getTenDangNhap(),
                    null  // excludeUserId - not applicable for creation
                );
                var dupResult = ((CheckUserDuplicationUseCaseControl) checkUserDuplicationUseCase)
                    .checkInternal(checkDupInput);
                
                if (dupResult.isDuplicate()) {
                    if ("email".equals(dupResult.getDuplicatedField())) {
                        throw DomainException.emailAlreadyExists(inputData.getEmail());
                    } else if ("username".equals(dupResult.getDuplicatedField())) {
                        throw DomainException.usernameAlreadyExists(inputData.getTenDangNhap());
                    }
                }
            } catch (Exception e) {
                errorException = e;
            }
        }
        
        // Step 4: UC-59 - Hash password and create user
        if (errorException == null) {
            try {
                HashPasswordInputData hashInput = new HashPasswordInputData(inputData.getMatKhau());
                var hashResult = ((HashPasswordUseCaseControl) hashPasswordUseCase)
                    .hashInternal(hashInput);
                
                if (!hashResult.isSuccess()) {
                    throw new SystemException(hashResult.getErrorMessage(), hashResult.getErrorCode());
                }
                
                TaiKhoan taiKhoan = new TaiKhoan(
                    inputData.getHoTen(),
                    inputData.getEmail(),
                    inputData.getTenDangNhap(),
                    hashResult.getHashedPassword(),  // Use hashed password
                    inputData.getSoDienThoai(),
                    inputData.getDiaChi()
                );
                
                // Set role if specified
                if (inputData.getVaiTro() != null && inputData.getVaiTro().equalsIgnoreCase("ADMIN")) {
                    taiKhoan.thangCapAdmin();
                }
                
                TaiKhoan savedTaiKhoan = userRepository.save(taiKhoan);
                
                outputData = CreateUserOutputData.forSuccess(
                    savedTaiKhoan.getMaTaiKhoan(),
                    savedTaiKhoan.getEmail(),
                    savedTaiKhoan.getTenDangNhap()
                );
            } catch (Exception e) {
                errorException = e;
            }
        }
        
        // Step 5: Handle error
        if (errorException != null) {
            String errorCode = extractErrorCode(errorException);
            outputData = CreateUserOutputData.forError(errorCode, errorException.getMessage());
        }
        
        // Step 6: Present result
        outputBoundary.present(outputData);
    }
    
    private String extractErrorCode(Exception e) {
        if (e instanceof ValidationException) {
            return ((ValidationException) e).getErrorCode();
        } else if (e instanceof DomainException) {
            return ((DomainException) e).getErrorCode();
        } else if (e instanceof SystemException) {
            return ((SystemException) e).getErrorCode();
        }
        return "SYSTEM_ERROR";
    }
}
