package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.accessory.SearchAccessoriesInputData;
import com.motorbike.business.dto.accessory.SearchAccessoriesOutputData;
import com.motorbike.business.dto.accessory.SearchAccessoriesOutputData.AccessoryItem;
import com.motorbike.business.dto.accessory.FormatAccessoriesForDisplayInputData;
import com.motorbike.business.ports.repository.ProductRepository;
import com.motorbike.business.usecase.input.SearchAccessoriesInputBoundary;
import com.motorbike.business.usecase.input.FormatAccessoriesForDisplayInputBoundary;
import com.motorbike.business.usecase.output.SearchAccessoriesOutputBoundary;

import com.motorbike.domain.entities.PhuKienXeMay;
import com.motorbike.domain.entities.SanPham;
import com.motorbike.domain.exceptions.SystemException;

import java.util.List;
import java.util.stream.Collectors;

public class SearchAccessoriesUseCaseControl implements SearchAccessoriesInputBoundary {

    private final SearchAccessoriesOutputBoundary outputBoundary;
    private final ProductRepository productRepository;
    private final FormatAccessoriesForDisplayInputBoundary formatAccessoriesUseCase;

    public SearchAccessoriesUseCaseControl(
            SearchAccessoriesOutputBoundary outputBoundary,
            ProductRepository productRepository,
            FormatAccessoriesForDisplayInputBoundary formatAccessoriesUseCase
    ) {
        this.outputBoundary = outputBoundary;
        this.productRepository = productRepository;
        this.formatAccessoriesUseCase = formatAccessoriesUseCase;
    }

    // Backward compatibility constructor
    public SearchAccessoriesUseCaseControl(
            SearchAccessoriesOutputBoundary outputBoundary,
            ProductRepository productRepository
    ) {
        this.outputBoundary = outputBoundary;
        this.productRepository = productRepository;
        this.formatAccessoriesUseCase = new FormatAccessoriesForDisplayUseCaseControl(null);
    }

    @Override
    public void execute(SearchAccessoriesInputData inputData) {
        SearchAccessoriesOutputData outputData = null;
        Exception errorException = null;

        try {
            // Step 1: Get all products and filter to accessories only
            List<SanPham> allProducts = productRepository.findAll();

            List<PhuKienXeMay> filteredAccessories = allProducts.stream()
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
                    .collect(Collectors.toList());

            // Step 2: UC-76 Format accessories for display
            FormatAccessoriesForDisplayInputData formatInput = new FormatAccessoriesForDisplayInputData(filteredAccessories);
            var formatResult = ((FormatAccessoriesForDisplayUseCaseControl) formatAccessoriesUseCase).formatInternal(formatInput);
            
            if (!formatResult.isSuccess()) {
                throw new SystemException(formatResult.getErrorMessage(), formatResult.getErrorCode());
            }

            // Convert from GetAllAccessoriesOutputData.AccessoryItem to SearchAccessoriesOutputData.AccessoryItem
            List<AccessoryItem> accessories = formatResult.getAccessoryItems().stream()
                .map(item -> new AccessoryItem(
                    item.id,
                    item.name,
                    item.description,
                    item.price,
                    item.stock,
                    item.imageUrl,
                    item.loaiPhuKien,
                    item.thuongHieu,
                    item.chatLieu,
                    item.kichThuoc
                ))
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
}
