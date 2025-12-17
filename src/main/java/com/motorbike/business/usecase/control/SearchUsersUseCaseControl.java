package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.user.SearchUsersInputData;
import com.motorbike.business.dto.user.SearchUsersOutputData;
import com.motorbike.business.dto.user.SearchUsersOutputData.UserItem;
import com.motorbike.business.ports.repository.UserRepository;
import com.motorbike.business.usecase.input.SearchUsersInputBoundary;
import com.motorbike.business.usecase.output.SearchUsersOutputBoundary;
import com.motorbike.business.dto.buildsearchcriteria.BuildSearchCriteriaInputData;
import com.motorbike.business.dto.applysearchfilters.ApplySearchFiltersInputData;
import com.motorbike.business.dto.sortsearchresults.SortSearchResultsInputData;
import com.motorbike.business.usecase.input.BuildSearchCriteriaInputBoundary;
import com.motorbike.business.usecase.input.ApplySearchFiltersInputBoundary;
import com.motorbike.business.usecase.input.SortSearchResultsInputBoundary;
import com.motorbike.domain.entities.TaiKhoan;

import java.util.List;
import java.util.stream.Collectors;

public class SearchUsersUseCaseControl implements SearchUsersInputBoundary {

    private final SearchUsersOutputBoundary outputBoundary;
    private final UserRepository userRepository;
    private final BuildSearchCriteriaInputBoundary buildSearchCriteriaUseCase;
    private final ApplySearchFiltersInputBoundary applySearchFiltersUseCase;
    private final SortSearchResultsInputBoundary sortSearchResultsUseCase;

    public SearchUsersUseCaseControl(
            SearchUsersOutputBoundary outputBoundary,
            UserRepository userRepository,
            BuildSearchCriteriaInputBoundary buildSearchCriteriaUseCase,
            ApplySearchFiltersInputBoundary applySearchFiltersUseCase,
            SortSearchResultsInputBoundary sortSearchResultsUseCase
    ) {
        this.outputBoundary = outputBoundary;
        this.userRepository = userRepository;
        this.buildSearchCriteriaUseCase = buildSearchCriteriaUseCase;
        this.applySearchFiltersUseCase = applySearchFiltersUseCase;
        this.sortSearchResultsUseCase = sortSearchResultsUseCase;
    }

    // Constructor with 2 parameters (for backward compatibility)
    public SearchUsersUseCaseControl(
            SearchUsersOutputBoundary outputBoundary,
            UserRepository userRepository
    ) {
        this.outputBoundary = outputBoundary;
        this.userRepository = userRepository;
        this.buildSearchCriteriaUseCase = new BuildSearchCriteriaUseCaseControl(null);
        this.applySearchFiltersUseCase = new ApplySearchFiltersUseCaseControl(null);
        this.sortSearchResultsUseCase = new SortSearchResultsUseCaseControl(null);
    }

    @Override
    public void execute(SearchUsersInputData input) {
        SearchUsersOutputData outputData = null;
        Exception errorException = null;

        try {
            List<TaiKhoan> all = userRepository.findAll();

            List<UserItem> filtered = all.stream()
                    .filter(u -> input.keyword == null || 
                            u.getEmail().toLowerCase().contains(input.keyword.toLowerCase()) ||
                            u.getTenDangNhap().toLowerCase().contains(input.keyword.toLowerCase()) ||
                            (u.getSoDienThoai() != null && u.getSoDienThoai().contains(input.keyword)))
                    .filter(u -> input.vaiTro == null || u.getVaiTro() == input.vaiTro)
                    .filter(u -> input.hoatDong == null || u.isHoatDong() == input.hoatDong)
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

            outputData = new SearchUsersOutputData(filtered);
        } catch (Exception e) {
            errorException = e;
        }

        if (errorException != null) {
            outputData = new SearchUsersOutputData("SYSTEM_ERROR", errorException.getMessage());
        }

        outputBoundary.present(outputData);
    }
}
