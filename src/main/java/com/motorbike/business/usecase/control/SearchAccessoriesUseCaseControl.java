package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.accessory.SearchAccessoriesInputData;
import com.motorbike.business.dto.accessory.SearchAccessoriesOutputData;
import com.motorbike.business.dto.accessory.SearchAccessoriesOutputData.AccessoryItem;
import com.motorbike.business.ports.repository.ProductRepository;
import com.motorbike.business.usecase.input.SearchAccessoriesInputBoundary;
import com.motorbike.business.usecase.output.SearchAccessoriesOutputBoundary;
import com.motorbike.domain.entities.PhuKienXeMay;
import com.motorbike.domain.entities.SanPham;

import java.util.List;
import java.util.stream.Collectors;

public class SearchAccessoriesUseCaseControl implements SearchAccessoriesInputBoundary {

    private final SearchAccessoriesOutputBoundary outputBoundary;
    private final ProductRepository productRepository;

    public SearchAccessoriesUseCaseControl(
            SearchAccessoriesOutputBoundary outputBoundary,
            ProductRepository productRepository
    ) {
        this.outputBoundary = outputBoundary;
        this.productRepository = productRepository;
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
                    .filter(p -> matchesSearchCriteria(p, inputData))
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

    private boolean matchesSearchCriteria(PhuKienXeMay accessory, SearchAccessoriesInputData criteria) {
        if (criteria == null) {
            return true;
        }

        // Tìm kiếm theo keyword trong tên hoặc mô tả
        if (criteria.keyword != null && !criteria.keyword.isEmpty()) {
            String keyword = criteria.keyword.toLowerCase();
            boolean matchesName = accessory.getTenSanPham().toLowerCase().contains(keyword);
            boolean matchesDescription = accessory.getMoTa() != null && 
                                        accessory.getMoTa().toLowerCase().contains(keyword);
            if (!matchesName && !matchesDescription) {
                return false;
            }
        }

        // Lọc theo loại phụ kiện
        if (criteria.type != null && !criteria.type.isEmpty()) {
            if (accessory.getLoaiPhuKien() == null || 
                !accessory.getLoaiPhuKien().equalsIgnoreCase(criteria.type)) {
                return false;
            }
        }

        // Lọc theo thương hiệu
        if (criteria.brand != null && !criteria.brand.isEmpty()) {
            if (accessory.getThuongHieu() == null || 
                !accessory.getThuongHieu().equalsIgnoreCase(criteria.brand)) {
                return false;
            }
        }

        // Lọc theo chất liệu
        if (criteria.material != null && !criteria.material.isEmpty()) {
            if (accessory.getChatLieu() == null || 
                !accessory.getChatLieu().equalsIgnoreCase(criteria.material)) {
                return false;
            }
        }

        // Lọc theo khoảng giá tối thiểu
        if (criteria.minPrice != null) {
            if (accessory.getGia().doubleValue() < criteria.minPrice) {
                return false;
            }
        }

        // Lọc theo khoảng giá tối đa
        if (criteria.maxPrice != null) {
            if (accessory.getGia().doubleValue() > criteria.maxPrice) {
                return false;
            }
        }

        return true;
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
