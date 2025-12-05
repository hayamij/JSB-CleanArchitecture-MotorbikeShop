package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.accessory.SearchAccessoriesInputData;
import com.motorbike.business.dto.accessory.SearchAccessoriesOutputData;
import com.motorbike.business.dto.accessory.SearchAccessoriesOutputData.AccessoryItem;
import com.motorbike.business.ports.repository.ProductRepository;
import com.motorbike.business.usecase.input.SearchAccessoriesInputBoundary;
import com.motorbike.business.usecase.output.SearchAccessoriesOutputBoundary;
import com.motorbike.domain.entities.PhuKienXeMay;

import java.util.List;
import java.util.stream.Collectors;

public class SearchAccessoriesUseCaseControl implements SearchAccessoriesInputBoundary {

    private final SearchAccessoriesOutputBoundary outputBoundary;
    private final ProductRepository productRepository;

    public SearchAccessoriesUseCaseControl(
            SearchAccessoriesOutputBoundary outputBoundary,
            ProductRepository productRepository) {
        this.outputBoundary = outputBoundary;
        this.productRepository = productRepository;
    }

    @Override
    public void execute(SearchAccessoriesInputData input) {
        SearchAccessoriesOutputData outputData = null;
        Exception errorException = null;

        try {
            List<PhuKienXeMay> all = productRepository.findAllAccessories();

            List<AccessoryItem> filtered = all.stream()
                    .filter(pk -> input.keyword == null || pk.getTenSanPham().toLowerCase().contains(input.keyword.toLowerCase()))
                    .filter(pk -> input.loaiPhuKien == null || pk.getLoaiPhuKien().equalsIgnoreCase(input.loaiPhuKien))
                    .filter(pk -> input.thuongHieu == null || pk.getThuongHieu().equalsIgnoreCase(input.thuongHieu))
                    .filter(pk -> input.chatLieu == null || pk.getChatLieu().equalsIgnoreCase(input.chatLieu))
                    .filter(pk -> input.minPrice == null || pk.getGia().doubleValue() >= input.minPrice)
                    .filter(pk -> input.maxPrice == null || pk.getGia().doubleValue() <= input.maxPrice)
                    .map(pk -> new AccessoryItem(
                            pk.getMaSanPham(),
                            pk.getTenSanPham(),
                            pk.getMoTa(),
                            pk.getGia(),
                            pk.getSoLuongTonKho(),
                            pk.getHinhAnh(),
                            pk.getLoaiPhuKien(),
                            pk.getThuongHieu(),
                            pk.getChatLieu(),
                            pk.getKichThuoc()
                    ))
                    .collect(Collectors.toList());

            outputData = new SearchAccessoriesOutputData(filtered);
        } catch (Exception e) {
            errorException = e;
        }

        if (errorException != null) {
            outputData = new SearchAccessoriesOutputData("SYSTEM_ERROR", errorException.getMessage());
        }

        outputBoundary.present(outputData);
    }
}
