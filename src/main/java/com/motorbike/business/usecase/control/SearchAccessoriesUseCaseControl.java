package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.accessory.SearchAccessoriesInputData;
import com.motorbike.business.dto.accessory.SearchAccessoriesOutputData;
import com.motorbike.business.dto.accessory.SearchAccessoriesOutputData.AccessoryItem;
import com.motorbike.business.ports.repository.ProductRepository;
import com.motorbike.business.usecase.output.SearchAccessoriesOutputBoundary;
import com.motorbike.domain.entities.SanPham;
import com.motorbike.domain.entities.PhuKienXeMay;

import java.util.List;
import java.util.stream.Collectors;

public class SearchAccessoriesUseCaseControl
    extends AbstractUseCaseControl<SearchAccessoriesInputData, SearchAccessoriesOutputBoundary>
    implements com.motorbike.business.usecase.input.SearchAccessoriesInputBoundary {

    private final ProductRepository productRepository;

    public SearchAccessoriesUseCaseControl(SearchAccessoriesOutputBoundary outputBoundary,
                                           ProductRepository productRepository) {
        super(outputBoundary);
        this.productRepository = productRepository;
    }

    @Override
    protected void validateInput(SearchAccessoriesInputData inputData) {
        // no strict validation; all fields optional
    }

    @Override
    protected void executeBusinessLogic(SearchAccessoriesInputData inputData) {
        try {
            List<SanPham> all = productRepository.findAll();

            List<AccessoryItem> result = all.stream()
                    .filter(p -> p instanceof PhuKienXeMay)
                    .map(p -> (PhuKienXeMay) p)
                    .filter(p -> matches(p, inputData))
                    .map(p -> new AccessoryItem(
                            p.getMaSanPham(),
                            p.getTenSanPham(),
                            p.getMoTa(),
                            p.getGia(),
                            p.getSoLuongTonKho(),
                            p.getHinhAnh(),
                            p.getLoaiPhuKien(),
                            p.getThuongHieu(),
                            p.getChatLieu(),
                            p.getKichThuoc()))
                    .collect(Collectors.toList());

            SearchAccessoriesOutputData out = new SearchAccessoriesOutputData(result);
            outputBoundary.present(out);

        } catch (Exception e) {
            SearchAccessoriesOutputData err = new SearchAccessoriesOutputData("SYSTEM_ERROR", e.getMessage());
            outputBoundary.present(err);
        }
    }

    private boolean matches(PhuKienXeMay p, SearchAccessoriesInputData q) {
        if (q == null) return true;
        if (q.keyword != null && !q.keyword.isEmpty()) {
            String kw = q.keyword.toLowerCase();
            if (!(p.getTenSanPham().toLowerCase().contains(kw) || p.getMoTa().toLowerCase().contains(kw))) {
                return false;
            }
        }
        if (q.type != null && !q.type.isEmpty()) {
            if (p.getLoaiPhuKien() == null || !p.getLoaiPhuKien().toLowerCase().contains(q.type.toLowerCase()))
                return false;
        }
        if (q.brand != null && !q.brand.isEmpty()) {
            if (p.getThuongHieu() == null || !p.getThuongHieu().toLowerCase().contains(q.brand.toLowerCase()))
                return false;
        }
        if (q.material != null && !q.material.isEmpty()) {
            if (p.getChatLieu() == null || !p.getChatLieu().toLowerCase().contains(q.material.toLowerCase()))
                return false;
        }
        if (q.minPrice != null) {
            if (p.getGia().doubleValue() < q.minPrice) return false;
        }
        if (q.maxPrice != null) {
            if (p.getGia().doubleValue() > q.maxPrice) return false;
        }

        return true;
    }

    @Override
    protected void handleValidationError(IllegalArgumentException e) {
        SearchAccessoriesOutputData err = new SearchAccessoriesOutputData("INVALID_INPUT", e.getMessage());
        outputBoundary.present(err);
    }

    @Override
    protected void handleSystemError(Exception e) {
        SearchAccessoriesOutputData err = new SearchAccessoriesOutputData("SYSTEM_ERROR", e.getMessage());
        outputBoundary.present(err);
    }
}
