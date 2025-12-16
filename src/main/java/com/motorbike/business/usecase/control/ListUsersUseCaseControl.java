package com.motorbike.business.usecase.control;

import java.util.List;
import java.util.stream.Collectors;

import com.motorbike.business.dto.listusers.ListUsersInputData;
import com.motorbike.business.dto.listusers.ListUsersOutputData;
import com.motorbike.business.dto.listusers.ListUsersOutputData.UserItem;
import com.motorbike.business.ports.repository.UserRepository;
import com.motorbike.business.usecase.input.ListUsersInputBoundary;
import com.motorbike.business.usecase.output.ListUsersOutputBoundary;
import com.motorbike.domain.entities.TaiKhoan;
import com.motorbike.domain.exceptions.ValidationException;

public class ListUsersUseCaseControl implements ListUsersInputBoundary {

    private final ListUsersOutputBoundary outputBoundary;
    private final UserRepository userRepository;

    public ListUsersUseCaseControl(
            ListUsersOutputBoundary outputBoundary,
            UserRepository userRepository) {
        this.outputBoundary = outputBoundary;
        this.userRepository = userRepository;
    }

    @Override
    public void execute(ListUsersInputData inputData) {
        ListUsersOutputData outputData = null;
        Exception errorException = null;

        try {
            if (inputData == null || !inputData.isAdmin()) {
                throw ValidationException.invalidInput();
            }
        } catch (Exception e) {
            errorException = e;
        }

        List<TaiKhoan> allUsers = null;
        if (errorException == null) {
            try {
                allUsers = userRepository.findAll();
            } catch (Exception e) {
                errorException = e;
            }
        }

        if (errorException == null && allUsers != null) {
            try {
                String keyword = inputData.getKeyword();
                List<TaiKhoan> filtered = allUsers;
                if (keyword != null && !keyword.isBlank()) {
                    String kw = keyword.trim().toLowerCase();
                    filtered = allUsers.stream()
                            .filter(u -> {
                                boolean matchesEmail = u.getEmail() != null
                                        && u.getEmail().toLowerCase().contains(kw);
                                boolean matchesUsername = u.getTenDangNhap() != null
                                        && u.getTenDangNhap().toLowerCase().contains(kw);
                                boolean matchesRole = u.getVaiTro() != null
                                        && u.getVaiTro().name().toLowerCase().contains(kw);
                                return matchesEmail || matchesUsername || matchesRole;
                            })
                            .collect(Collectors.toList());
                }

                List<UserItem> items = filtered.stream()
                        .map(u -> new UserItem(
                                u.getMaTaiKhoan(),
                                u.getEmail(),
                                u.getTenDangNhap(),
                                u.getVaiTro() != null ? u.getVaiTro().name() : null,
                                u.isHoatDong(),
                                u.getNgayTao(),
                                u.getNgayCapNhat(),
                                u.getLanDangNhapCuoi()
                        ))
                        .collect(Collectors.toList());

                outputData = ListUsersOutputData.forSuccess(items);
            } catch (Exception e) {
                errorException = e;
            }
        }

        if (errorException != null) {
            String errorCode = "SYSTEM_ERROR";
            String message = errorException.getMessage();

            if (errorException instanceof ValidationException) {
                errorCode = ((ValidationException) errorException).getErrorCode();
            }

            outputData = ListUsersOutputData.forError(errorCode, message);
        }

        outputBoundary.present(outputData);
    }
}
