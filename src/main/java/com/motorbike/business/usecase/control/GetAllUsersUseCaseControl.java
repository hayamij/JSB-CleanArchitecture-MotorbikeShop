package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.user.GetAllUsersOutputData;
import com.motorbike.business.dto.user.GetAllUsersOutputData.UserItem;
import com.motorbike.business.ports.repository.UserRepository;
import com.motorbike.business.usecase.input.GetAllUsersInputBoundary;
import com.motorbike.business.usecase.output.GetAllUsersOutputBoundary;
import com.motorbike.domain.entities.TaiKhoan;

import java.util.List;
import java.util.stream.Collectors;

public class GetAllUsersUseCaseControl implements GetAllUsersInputBoundary {

    private final GetAllUsersOutputBoundary outputBoundary;
    private final UserRepository userRepository;

    public GetAllUsersUseCaseControl(
            GetAllUsersOutputBoundary outputBoundary,
            UserRepository userRepository
    ) {
        this.outputBoundary = outputBoundary;
        this.userRepository = userRepository;
    }

    @Override
    public void execute(Void inputData) {
        GetAllUsersOutputData outputData = null;
        Exception errorException = null;

        try {
            List<TaiKhoan> allUsers = userRepository.findAll();

            List<UserItem> users = allUsers.stream()
                    .map(u -> new UserItem(
                            u.getMaTaiKhoan(),
                            u.getEmail(),
                            u.getTenDangNhap(),
                            u.getSoDienThoai(),
                            u.getDiaChi(),
                            u.getVaiTro(),
                            u.isHoatDong(),
                            u.getNgayTao(),
                            u.getNgayCapNhat(),
                            u.getLanDangNhapCuoi()
                    ))
                    .collect(Collectors.toList());

            outputData = new GetAllUsersOutputData(users);
        } catch (Exception e) {
            errorException = e;
        }

        if (errorException != null) {
            outputData = new GetAllUsersOutputData("SYSTEM_ERROR", errorException.getMessage());
        }

        outputBoundary.present(outputData);
    }
}
