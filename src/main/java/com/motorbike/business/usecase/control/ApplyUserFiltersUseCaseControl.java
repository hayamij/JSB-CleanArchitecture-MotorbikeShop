package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.user.ApplyUserFiltersInputData;
import com.motorbike.business.dto.user.ApplyUserFiltersOutputData;
import com.motorbike.business.usecase.input.ApplyUserFiltersInputBoundary;
import com.motorbike.business.usecase.output.ApplyUserFiltersOutputBoundary;
import com.motorbike.domain.entities.TaiKhoan;

import java.util.List;
import java.util.stream.Collectors;

/**
 * UC-73: Apply User Filters
 * Secondary usecase to filter user list by keyword, role, and active status
 */
public class ApplyUserFiltersUseCaseControl implements ApplyUserFiltersInputBoundary {

    private final ApplyUserFiltersOutputBoundary outputBoundary;

    public ApplyUserFiltersUseCaseControl(ApplyUserFiltersOutputBoundary outputBoundary) {
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void execute(ApplyUserFiltersInputData inputData) {
        ApplyUserFiltersOutputData outputData = filterInternal(inputData);
        if (outputBoundary != null) {
            outputBoundary.present(outputData);
        }
    }

    /**
     * Internal method for composition by other usecases
     */
    public ApplyUserFiltersOutputData filterInternal(ApplyUserFiltersInputData inputData) {
        try {
            List<TaiKhoan> filtered = inputData.getUsers().stream()
                    .filter(u -> inputData.getKeyword() == null || 
                            u.getEmail().toLowerCase().contains(inputData.getKeyword().toLowerCase()) ||
                            u.getTenDangNhap().toLowerCase().contains(inputData.getKeyword().toLowerCase()) ||
                            (u.getSoDienThoai() != null && u.getSoDienThoai().contains(inputData.getKeyword())))
                    .filter(u -> inputData.getVaiTro() == null || u.getVaiTro().equals(inputData.getVaiTro()))
                    .filter(u -> inputData.getHoatDong() == null || u.isHoatDong() == inputData.getHoatDong())
                    .collect(Collectors.toList());

            return ApplyUserFiltersOutputData.forSuccess(filtered);
        } catch (Exception e) {
            return ApplyUserFiltersOutputData.forError(
                "FILTER_ERROR", 
                "Lỗi khi lọc danh sách người dùng: " + e.getMessage()
            );
        }
    }
}
