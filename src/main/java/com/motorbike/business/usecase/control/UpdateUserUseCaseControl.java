package com.motorbike.business.usecase.control;

import java.time.LocalDateTime;
import java.util.Optional;

import com.motorbike.business.dto.updateuser.UpdateUserInputData;
import com.motorbike.business.dto.updateuser.UpdateUserOutputData;
import com.motorbike.business.ports.repository.UserRepository;
import com.motorbike.business.usecase.input.UpdateUserInputBoundary;
import com.motorbike.business.usecase.output.UpdateUserOutputBoundary;
import com.motorbike.domain.entities.TaiKhoan;
import com.motorbike.domain.entities.VaiTro;
import com.motorbike.domain.exceptions.DomainException;
import com.motorbike.domain.exceptions.ValidationException;

public class UpdateUserUseCaseControl implements UpdateUserInputBoundary {

    private final UpdateUserOutputBoundary outputBoundary;
    private final UserRepository userRepository;

    public UpdateUserUseCaseControl(UpdateUserOutputBoundary outputBoundary,
                                    UserRepository userRepository) {
        this.outputBoundary = outputBoundary;
        this.userRepository = userRepository;
    }

    @Override
    public void execute(UpdateUserInputData input) {
        UpdateUserOutputData outputData = null;
        Exception errorException = null;

        try {
            if (input == null || !input.isAdmin()) {
                throw ValidationException.invalidInput();
            }
            if (input.getUserId() == null) {
                throw ValidationException.invalidUserId();
            }
        } catch (Exception e) {
            errorException = e;
        }

        TaiKhoan existing = null;
        if (errorException == null) {
            try {
                Optional<TaiKhoan> opt = userRepository.findById(input.getUserId());
                existing = opt.orElseThrow();
            } catch (Exception e) {
                errorException = e;
            }
        }

        if (errorException == null && existing != null) {
            try {
                if (input.email != null && !input.email.isBlank()) {
                    existing.setEmail(input.email);
                }
                if (input.username != null && !input.username.isBlank()) {
                    existing.setTenDangNhap(input.username);
                }
                if (input.password != null && !input.password.isBlank()) {
                    existing.setMatKhau(input.password);
                }
                if (input.phoneNumber != null) {
                    existing.setSoDienThoai(input.phoneNumber);
                }
                if (input.address != null) {
                    existing.setDiaChi(input.address);
                }
                if (input.role != null && !input.role.isBlank()) {
                    try {
                        VaiTro vt = VaiTro.valueOf(input.role);
                        existing.setVaiTro(vt);
                    } catch (IllegalArgumentException ex) {
                        // ignore invalid role
                    }
                }
                if (input.active != null) {
                    existing.setHoatDong(input.active);
                }

                existing.setNgayCapNhat(LocalDateTime.now());
                TaiKhoan saved = userRepository.save(existing);

                outputData = UpdateUserOutputData.forSuccess(
                        saved.getMaTaiKhoan(),
                        saved.getEmail(),
                        saved.getTenDangNhap(),
                        saved.getVaiTro() != null ? saved.getVaiTro().name() : null,
                        saved.isHoatDong(),
                        saved.getNgayCapNhat()
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
            outputData = UpdateUserOutputData.forError(errorCode, message);
        }

        outputBoundary.present(outputData);
    }
}
