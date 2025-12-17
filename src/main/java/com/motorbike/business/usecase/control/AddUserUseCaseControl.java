package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.user.AddUserInputData;
import com.motorbike.business.dto.user.AddUserOutputData;
import com.motorbike.business.dto.user.CheckUserDuplicationInputData;
import com.motorbike.business.ports.repository.UserRepository;
import com.motorbike.business.usecase.input.AddUserInputBoundary;
import com.motorbike.business.usecase.input.CheckUserDuplicationInputBoundary;
import com.motorbike.business.usecase.output.AddUserOutputBoundary;
import com.motorbike.domain.entities.TaiKhoan;
import com.motorbike.domain.entities.VaiTro;
import com.motorbike.domain.exceptions.DomainException;
import com.motorbike.domain.exceptions.ValidationException;
import com.motorbike.domain.exceptions.SystemException;

public class AddUserUseCaseControl implements AddUserInputBoundary {
    
    private final AddUserOutputBoundary outputBoundary;
    private final UserRepository userRepository;
    private final CheckUserDuplicationInputBoundary checkUserDuplicationUseCase;
    
    public AddUserUseCaseControl(
            AddUserOutputBoundary outputBoundary,
            UserRepository userRepository,
            CheckUserDuplicationInputBoundary checkUserDuplicationUseCase) {
        this.outputBoundary = outputBoundary;
        this.userRepository = userRepository;
        this.checkUserDuplicationUseCase = checkUserDuplicationUseCase;
    }

    // Backward compatibility constructor
    public AddUserUseCaseControl(
            AddUserOutputBoundary outputBoundary,
            UserRepository userRepository) {
        this.outputBoundary = outputBoundary;
        this.userRepository = userRepository;
        this.checkUserDuplicationUseCase = new CheckUserDuplicationUseCaseControl(null, userRepository);
    }
    
    @Override
    public void execute(AddUserInputData inputData) {
        AddUserOutputData outputData = null;
        Exception errorException = null;
        TaiKhoan taiKhoan = null;
        
        // Step 1: Validation
        try {
            if (inputData == null) {
                throw ValidationException.invalidInput();
            }
            
            TaiKhoan.checkInputForRegister(
                inputData.getHoTen(),
                inputData.getEmail(),
                inputData.getTenDangNhap(),
                inputData.getMatKhau(),
                inputData.getSoDienThoai()
            );
            
        } catch (Exception e) {
            errorException = e;
        }
        
        // Step 2: UC-58 Check user duplication
        if (errorException == null) {
            try {
                CheckUserDuplicationInputData checkDupInput = new CheckUserDuplicationInputData(
                    inputData.getEmail(),
                    inputData.getTenDangNhap(),
                    null  // No exclusion for new user
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
        
        // Step 3: Create user entity
        if (errorException == null) {
            try {
                taiKhoan = new TaiKhoan(
                    inputData.getHoTen(),
                    inputData.getEmail(),
                    inputData.getTenDangNhap(),
                    inputData.getMatKhau(),
                    inputData.getSoDienThoai(),
                    inputData.getDiaChi()
                );
                
                // Set role and status if provided by admin
                if (inputData.getVaiTro() != null) {
                    if (inputData.getVaiTro() == VaiTro.ADMIN) {
                        taiKhoan.thangCapAdmin();
                    }
                }
                
                if (!inputData.isHoatDong()) {
                    taiKhoan.khoaTaiKhoan();
                }
                
            } catch (Exception e) {
                errorException = e;
            }
        }
        
        // Step 4: Save to database
        if (errorException == null && taiKhoan != null) {
            try {
                taiKhoan = userRepository.save(taiKhoan);
                
                outputData = AddUserOutputData.forSuccess(
                    taiKhoan.getMaTaiKhoan(),
                    taiKhoan.getEmail(),
                    taiKhoan.getTenDangNhap(),
                    taiKhoan.getSoDienThoai(),
                    taiKhoan.getVaiTro(),
                    taiKhoan.isHoatDong(),
                    taiKhoan.getNgayTao()
                );
            } catch (Exception e) {
                errorException = e;
            }
        }
        
        // Step 5: Handle error
        if (errorException != null) {
            String errorCode = extractErrorCode(errorException);
            outputData = AddUserOutputData.forError(errorCode, errorException.getMessage());
        }
        
        // Step 6: Present result
        outputBoundary.present(outputData);
    }
    
    private String extractErrorCode(Exception exception) {
        if (exception instanceof ValidationException) {
            return ((ValidationException) exception).getErrorCode();
        } else if (exception instanceof DomainException) {
            return ((DomainException) exception).getErrorCode();
        } else if (exception instanceof SystemException) {
            return ((SystemException) exception).getErrorCode();
        }
        return "SYSTEM_ERROR";
    }
}
