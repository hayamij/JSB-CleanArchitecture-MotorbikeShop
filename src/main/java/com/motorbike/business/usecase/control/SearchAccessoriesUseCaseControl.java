package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.accessory.SearchAccessoriesInputData;
import com.motorbike.business.dto.accessory.SearchAccessoriesOutputData;
import com.motorbike.business.dto.accessory.SearchAccessoriesOutputData.AccessoryItem;
import com.motorbike.business.ports.repository.ProductRepository;
import com.motorbike.business.usecase.input.SearchAccessoriesInputBoundary;
import com.motorbike.business.usecase.output.SearchAccessoriesOutputBoundary;
import com.motorbike.business.dto.buildsearchcriteria.BuildSearchCriteriaInputData;
import com.motorbike.business.dto.applysearchfilters.ApplySearchFiltersInputData;
import com.motorbike.business.dto.sortsearchresults.SortSearchResultsInputData;
import com.motorbike.business.usecase.input.BuildSearchCriteriaInputBoundary;
import com.motorbike.business.usecase.input.ApplySearchFiltersInputBoundary;
import com.motorbike.business.usecase.input.SortSearchResultsInputBoundary;
import com.motorbike.domain.entities.PhuKienXeMay;
import com.motorbike.domain.entities.SanPham;

import java.util.List;
import java.util.stream.Collectors;

public class SearchAccessoriesUseCaseControl implements SearchAccessoriesInputBoundary {

    private final SearchAccessoriesOutputBoundary outputBoundary;
    private final ProductRepository productRepository;
    private final BuildSearchCriteriaInputBoundary buildSearchCriteriaUseCase;
    private final ApplySearchFiltersInputBoundary applySearchFiltersUseCase;
    private final SortSearchResultsInputBoundary sortSearchResultsUseCase;

    public SearchAccessoriesUseCaseControl(
            SearchAccessoriesOutputBoundary outputBoundary,
            ProductRepository productRepository,
            BuildSearchCriteriaInputBoundary buildSearchCriteriaUseCase,
            ApplySearchFiltersInputBoundary applySearchFiltersUseCase,
            SortSearchResultsInputBoundary sortSearchResultsUseCase
    ) {
        this.outputBoundary = outputBoundary;
        this.productRepository = productRepository;
        this.buildSearchCriteriaUseCase = buildSearchCriteriaUseCase;
        this.applySearchFiltersUseCase = applySearchFiltersUseCase;
        this.sortSearchResultsUseCase = sortSearchResultsUseCase;
    }

    // Constructor with 2 parameters (for backward compatibility)
    public SearchAccessoriesUseCaseControl(
            SearchAccessoriesOutputBoundary outputBoundary,
            ProductRepository productRepository
    ) {
        this.outputBoundary = outputBoundary;
        this.productRepository = productRepository;
        this.buildSearchCriteriaUseCase = new BuildSearchCriteriaUseCaseControl(null);
        this.applySearchFiltersUseCase = new ApplySearchFiltersUseCaseControl(null);
        this.sortSearchResultsUseCase = new SortSearchResultsUseCaseControl(null);
    }

    @Override
    public void execute(SearchAccessoriesInputData inputData) {
        SearchAccessoriesOutputData outputData = null;
        Exception errorException = null;

        try {
            List<SanPham> allProducts = productRepository.findAll();

            List<AccessoryItem> accessories = allProducts.stream()
                    .filter(p -> p instanceof PhuKienXeMay)
                    .map(p -> (PhuKienXeMay) p)
                    .filter(a -> inputData == null || inputData.keyword == null || 
                            a.getTenSanPham().toLowerCase().contains(inputData.keyword.toLowerCase()) ||
                            (a.getMoTa() != null && a.getMoTa().toLowerCase().contains(inputData.keyword.toLowerCase())))
                    .filter(a -> inputData == null || inputData.loaiPhuKien == null || 
                            (a.getLoaiPhuKien() != null && a.getLoaiPhuKien().equalsIgnoreCase(inputData.loaiPhuKien)))
                    .filter(a -> inputData == null || inputData.thuongHieu == null || 
                            (a.getThuongHieu() != null && a.getThuongHieu().equalsIgnoreCase(inputData.thuongHieu)))
                    .filter(a -> inputData == null || inputData.chatLieu == null || 
                            (a.getChatLieu() != null && a.getChatLieu().equalsIgnoreCase(inputData.chatLieu)))
                    .filter(a -> inputData == null || inputData.minPrice == null || a.getGia().doubleValue() >= inputData.minPrice)
                    .filter(a -> inputData == null || inputData.maxPrice == null || a.getGia().doubleValue() <= inputData.maxPrice)
                    .map(this::mapToAccessoryItem)
                    .collect(Collectors.toList());

            outputData = new SearchAccessoriesOutputData(accessories);
        } catch (Exception e) {
            errorException = e;
        }

        if (errorException != null) {
            outputData = new SearchAccessoriesOutputData("SYSTEM_ERROR", errorException.getMessage());
        }

        outputBoundary.present(outputData);
    }

    private AccessoryItem mapToAccessoryItem(PhuKienXeMay accessory) {
        return new AccessoryItem(
                accessory.getMaSanPham(),
                accessory.getTenSanPham(),
                accessory.getMoTa(),
                accessory.getGia(),
                accessory.getSoLuongTonKho(),
                accessory.getHinhAnh(),
                accessory.getLoaiPhuKien(),
                accessory.getThuongHieu(),
                accessory.getChatLieu(),
                accessory.getKichThuoc()
        );
    }
}
