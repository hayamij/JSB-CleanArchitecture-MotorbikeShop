package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.adduser.AddUserInputData;
import com.motorbike.business.dto.adduser.AddUserOutputData;
import com.motorbike.business.ports.repository.CartRepository;
import com.motorbike.business.ports.repository.UserRepository;
import com.motorbike.business.usecase.input.AddUserInputBoundary;
import com.motorbike.business.usecase.output.AddUserOutputBoundary;
import com.motorbike.domain.entities.GioHang;
import com.motorbike.domain.entities.TaiKhoan;
import com.motorbike.domain.entities.VaiTro;
import com.motorbike.domain.exceptions.DomainException;
import com.motorbike.domain.exceptions.ValidationException;

public class AddUserUseCaseControl implements AddUserInputBoundary {

    private final AddUserOutputBoundary outputBoundary;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;

    public AddUserUseCaseControl(AddUserOutputBoundary outputBoundary,
                                 UserRepository userRepository,
                                 CartRepository cartRepository) {
        this.outputBoundary = outputBoundary;
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
    }

    @Override
    public void execute(AddUserInputData input) {
        AddUserOutputData outputData = null;
        Exception errorException = null;

        try {
            if (input == null) throw ValidationException.invalidInput();
            TaiKhoan.checkInputForRegister(
                    input.email,
                    input.username,
                    input.password,
                    input.phoneNumber
            );
        } catch (ValidationException e) {
            errorException = e;
        }

        if (errorException == null) {
            try {
                if (userRepository.existsByEmail(input.email)) {
                    throw DomainException.emailAlreadyExists(input.email);
                }
            } catch (DomainException e) {
                errorException = e;
            }
        }

        if (errorException == null) {
            try {
                TaiKhoan taiKhoan = new TaiKhoan(
                        input.email,
                        input.username,
                        input.password,
                        input.phoneNumber,
                        input.address
                );

                if (input.role != null && !input.role.isBlank()) {
                    try {
                        VaiTro vt = VaiTro.valueOf(input.role);
                        taiKhoan.setVaiTro(vt);
                    } catch (IllegalArgumentException ex) {
                        // ignore invalid role
                    }
                }

                if (input.active != null) {
                    taiKhoan.setHoatDong(input.active);
                }

                TaiKhoan saved = userRepository.save(taiKhoan);

                GioHang gioHang = new GioHang(saved.getMaTaiKhoan());
                cartRepository.save(gioHang);

                outputData = AddUserOutputData.forSuccess(
                        saved.getMaTaiKhoan(),
                        saved.getEmail(),
                        saved.getTenDangNhap(),
                        saved.getVaiTro() != null ? saved.getVaiTro().name() : null,
                        saved.isHoatDong(),
                        saved.getNgayTao()
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
            }

            outputData = AddUserOutputData.forError(errorCode, message);
        }

        outputBoundary.present(outputData);
    }
}
