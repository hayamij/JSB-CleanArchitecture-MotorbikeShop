package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.user.UpdateUserInputData;
import com.motorbike.business.dto.user.UpdateUserOutputData;
import com.motorbike.business.ports.repository.UserRepository;
import com.motorbike.business.usecase.input.UpdateUserInputBoundary;
import com.motorbike.business.usecase.output.UpdateUserOutputBoundary;
import com.motorbike.business.dto.checkuserduplication.CheckUserDuplicationInputData;
import com.motorbike.business.usecase.input.CheckUserDuplicationInputBoundary;
import com.motorbike.domain.entities.TaiKhoan;
import com.motorbike.domain.entities.VaiTro;
import com.motorbike.domain.exceptions.DomainException;
import com.motorbike.domain.exceptions.ValidationException;
import com.motorbike.domain.exceptions.SystemException;

public class UpdateUserUseCaseControl implements UpdateUserInputBoundary {
    
    private final UpdateUserOutputBoundary outputBoundary;
    private final UserRepository userRepository;
    private final CheckUserDuplicationInputBoundary checkUserDuplicationUseCase;
    
    public UpdateUserUseCaseControl(UpdateUserOutputBoundary outputBoundary,
                                   UserRepository userRepository,
                                   CheckUserDuplicationInputBoundary checkUserDuplicationUseCase) {
        this.outputBoundary = outputBoundary;
        this.userRepository = userRepository;
        this.checkUserDuplicationUseCase = checkUserDuplicationUseCase;
    }

    // Constructor with 2 parameters (for backward compatibility)
    public UpdateUserUseCaseControl(
            UpdateUserOutputBoundary outputBoundary,
            UserRepository userRepository
    ) {
        this.outputBoundary = outputBoundary;
        this.userRepository = userRepository;
        this.checkUserDuplicationUseCase = new CheckUserDuplicationUseCaseControl(null, userRepository);
    }
    
    @Override
    public void execute(UpdateUserInputData inputData) {
        UpdateUserOutputData outputData = null;
        Exception errorException = null;
        TaiKhoan taiKhoan = null;
        
        // Step 1: Validation
        try {
            if (inputData == null) {
                throw ValidationException.invalidInput();
            }
            
            TaiKhoan.checkInput(inputData.getMaTaiKhoan());
            
            // Only validate fields that are being updated (non-null)
            if (inputData.getTenDangNhap() != null) {
                TaiKhoan.validateTenDangNhap(inputData.getTenDangNhap());
            }
            if (inputData.getHoTen() != null) {
                TaiKhoan.validateHoTen(inputData.getHoTen());
            }
            if (inputData.getEmail() != null) {
                TaiKhoan.validateEmail(inputData.getEmail());
            }
            if (inputData.getSoDienThoai() != null) {
                TaiKhoan.validateSoDienThoai(inputData.getSoDienThoai());
            }
            
        } catch (Exception e) {
            errorException = e;
        }
        
        // Step 2: Check if user exists
        if (errorException == null) {
            try {
                taiKhoan = userRepository.findById(inputData.getMaTaiKhoan())
                    .orElseThrow(() -> DomainException.userNotFound(inputData.getMaTaiKhoan()));
            } catch (Exception e) {
                errorException = e;
            }
        }
        
        // Step 3: UC-58 - Check duplication for changed fields
        if (errorException == null && taiKhoan != null) {
            try {
                // Only check duplication if email or username is being changed
                String emailToCheck = (inputData.getEmail() != null && !taiKhoan.getEmail().equals(inputData.getEmail())) 
                    ? inputData.getEmail() : null;
                String usernameToCheck = (inputData.getTenDangNhap() != null && !taiKhoan.getTenDangNhap().equals(inputData.getTenDangNhap())) 
                    ? inputData.getTenDangNhap() : null;
                
                if (emailToCheck != null || usernameToCheck != null) {
                    CheckUserDuplicationInputData checkDupInput = new CheckUserDuplicationInputData(
                        emailToCheck,
                        usernameToCheck,
                        inputData.getMaTaiKhoan()  // Exclude current user from duplication check
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
                }
                
                // Update username (only if provided and passed duplication check)
                if (inputData.getTenDangNhap() != null && !taiKhoan.getTenDangNhap().equals(inputData.getTenDangNhap())) {
                    taiKhoan.setTenDangNhap(inputData.getTenDangNhap());
                }
                
                // Update hoTen (only if provided)
                if (inputData.getHoTen() != null) {
                    taiKhoan.setHoTen(inputData.getHoTen());
                }
                
                // Update email (only if provided and passed duplication check)
                if (inputData.getEmail() != null && !taiKhoan.getEmail().equals(inputData.getEmail())) {
                    taiKhoan.setEmail(inputData.getEmail());
                }
                
                // Update phone and address (only if provided)
                if (inputData.getSoDienThoai() != null) {
                    taiKhoan.setSoDienThoai(inputData.getSoDienThoai());
                }
                if (inputData.getDiaChi() != null) {
                    taiKhoan.setDiaChi(inputData.getDiaChi());
                }
                
                // Update role
                if (inputData.getVaiTro() != null && taiKhoan.getVaiTro() != inputData.getVaiTro()) {
                    if (inputData.getVaiTro() == VaiTro.ADMIN) {
                        taiKhoan.thangCapAdmin();
                    } else {
                        taiKhoan.haCapCustomer();
                    }
                }
                
                // Update status
                if (taiKhoan.isHoatDong() != inputData.isHoatDong()) {
                    if (inputData.isHoatDong()) {
                        taiKhoan.moKhoaTaiKhoan();
                    } else {
                        taiKhoan.khoaTaiKhoan();
                    }
                }
                
                taiKhoan = userRepository.save(taiKhoan);
                
                outputData = UpdateUserOutputData.forSuccess(
                    taiKhoan.getMaTaiKhoan(),
                    taiKhoan.getEmail(),
                    taiKhoan.getTenDangNhap(),
                    taiKhoan.getSoDienThoai(),
                    taiKhoan.getVaiTro(),
                    taiKhoan.isHoatDong(),
                    taiKhoan.getNgayCapNhat()
                );
            } catch (Exception e) {
                errorException = e;
            }
        }
        
        // Step 4: Handle error
        if (errorException != null) {
            String errorCode = extractErrorCode(errorException);
            outputData = UpdateUserOutputData.forError(errorCode, errorException.getMessage());
        }
        
        // Step 5: Present result
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
