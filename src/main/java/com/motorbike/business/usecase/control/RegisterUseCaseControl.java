package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.register.RegisterInputData;
import com.motorbike.business.dto.register.RegisterOutputData;
import com.motorbike.business.ports.repository.UserRepository;
import com.motorbike.business.ports.repository.CartRepository;
import com.motorbike.business.usecase.output.RegisterOutputBoundary;
import com.motorbike.domain.entities.TaiKhoan;
import com.motorbike.domain.entities.GioHang;
import com.motorbike.domain.exceptions.DomainException;
import com.motorbike.domain.exceptions.ValidationException;
import com.motorbike.domain.exceptions.SystemException;

public class RegisterUseCaseControl
        extends AbstractUseCaseControl<RegisterInputData, RegisterOutputBoundary> {
    
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    
    public RegisterUseCaseControl(
            RegisterOutputBoundary outputBoundary,
            UserRepository userRepository,
            CartRepository cartRepository) {
        super(outputBoundary);
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
    }
    
    @Override
    protected void executeBusinessLogic(RegisterInputData inputData) throws Exception {
        try {
            if (userRepository.existsByEmail(inputData.getEmail())) {
                throw DomainException.emailAlreadyExists(inputData.getEmail());
            }
            
            TaiKhoan taiKhoan = new TaiKhoan(
                inputData.getEmail(),
                inputData.getUsername(),
                inputData.getPassword(),
                inputData.getPhoneNumber(),
                inputData.getAddress()
            );
            
            TaiKhoan savedTaiKhoan = userRepository.save(taiKhoan);
            
            GioHang gioHang = new GioHang(savedTaiKhoan.getMaTaiKhoan());
            cartRepository.save(gioHang);
            
            RegisterOutputData outputData = RegisterOutputData.forSuccess(
                savedTaiKhoan.getMaTaiKhoan(),
                savedTaiKhoan.getEmail(),
                savedTaiKhoan.getTenDangNhap(),
                savedTaiKhoan.getVaiTro(),
                savedTaiKhoan.getNgayTao()
            );
            
            outputBoundary.present(outputData);
            
        } catch (DomainException e) {
            throw e;
        }
    }
    
    @Override
    protected void validateInput(RegisterInputData inputData) {
        checkInputNotNull(inputData);
        TaiKhoan.checkInputForRegister(
            inputData.getEmail(),
            inputData.getUsername(),
            inputData.getPassword(),
            inputData.getPhoneNumber()
        );


    }
    
    @Override
    protected void handleValidationError(IllegalArgumentException e) {
        String errorCode = "INVALID_INPUT";
        if (e instanceof ValidationException) {
            errorCode = ((ValidationException) e).getErrorCode();
        }
        RegisterOutputData outputData = RegisterOutputData.forError(errorCode, e.getMessage());
        outputBoundary.present(outputData);
    }
    
    @Override
    protected void handleSystemError(Exception e) {
        String errorCode;
        String message;
        
        if (e instanceof DomainException) {
            DomainException ex = (DomainException) e;
            errorCode = ex.getErrorCode();
            message = ex.getMessage();
        } else if (e instanceof SystemException) {
            SystemException ex = (SystemException) e;
            errorCode = ex.getErrorCode();
            message = ex.getMessage();
        } else {
            throw new SystemException(e);
        }
        
        RegisterOutputData outputData = RegisterOutputData.forError(errorCode, message);
        outputBoundary.present(outputData);
    }
}
