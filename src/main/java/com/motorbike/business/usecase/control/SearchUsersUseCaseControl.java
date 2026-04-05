package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.user.SearchUsersInputData;
import com.motorbike.business.dto.user.SearchUsersOutputData;
import com.motorbike.business.dto.user.SearchUsersOutputData.UserItem;
import com.motorbike.business.dto.user.ApplyUserFiltersInputData;
import com.motorbike.business.ports.repository.UserRepository;
import com.motorbike.business.usecase.input.SearchUsersInputBoundary;
import com.motorbike.business.usecase.input.ApplyUserFiltersInputBoundary;
import com.motorbike.business.usecase.output.SearchUsersOutputBoundary;
import com.motorbike.domain.entities.TaiKhoan;
import com.motorbike.domain.exceptions.SystemException;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class SearchUsersUseCaseControl implements SearchUsersInputBoundary {

    private final SearchUsersOutputBoundary outputBoundary;
    private final UserRepository userRepository;
    private final ApplyUserFiltersInputBoundary applyUserFiltersUseCase;

    public SearchUsersUseCaseControl(
            SearchUsersOutputBoundary outputBoundary,
            UserRepository userRepository,
            ApplyUserFiltersInputBoundary applyUserFiltersUseCase
    ) {
        this.outputBoundary = outputBoundary;
        this.userRepository = userRepository;
        this.applyUserFiltersUseCase = applyUserFiltersUseCase;
    }

    // Backward compatibility constructor
    public SearchUsersUseCaseControl(
            SearchUsersOutputBoundary outputBoundary,
            UserRepository userRepository
    ) {
        this.outputBoundary = outputBoundary;
        this.userRepository = userRepository;
        this.applyUserFiltersUseCase = new ApplyUserFiltersUseCaseControl(null);
    }

    @Override
    public void execute(SearchUsersInputData input) {
        SearchUsersOutputData outputData = null;
        Exception errorException = null;

        try {
            // Step 1: Get all users from repository
            List<TaiKhoan> all = userRepository.findAll();

            // Step 2: UC-73 Apply filters
            ApplyUserFiltersInputData filterInput = new ApplyUserFiltersInputData(
                all, input.keyword, input.vaiTro, input.hoatDong
            );
            var filterResult = ((ApplyUserFiltersUseCaseControl) applyUserFiltersUseCase).filterInternal(filterInput);
            if (!filterResult.isSuccess()) {
                throw new SystemException(filterResult.getErrorMessage(), filterResult.getErrorCode());
            }

            // Step 3: Map to UserItem DTOs
            List<UserItem> userItems = filterResult.getFilteredUsers().stream()
                    .map(u -> new UserItem(
                            u.getMaTaiKhoan(),
                            u.getHoTen(),
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

            outputData = new SearchUsersOutputData(userItems);
        } catch (Exception e) {
            errorException = e;
        }

        if (errorException != null) {
            outputData = new SearchUsersOutputData("SYSTEM_ERROR", errorException.getMessage());
        }

        outputBoundary.present(outputData);
    }
}
