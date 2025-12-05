package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.accessory.GetAllAccessoriesOutputData;
import com.motorbike.business.dto.accessory.GetAllAccessoriesOutputData.AccessoryItem;
import com.motorbike.business.ports.repository.ProductRepository;
import com.motorbike.business.usecase.input.GetAllAccessoriesInputBoundary;
import com.motorbike.business.usecase.output.GetAllAccessoriesOutputBoundary;
import com.motorbike.domain.entities.PhuKienXeMay;

import java.util.List;
import java.util.stream.Collectors;

public class GetAllAccessoriesUseCaseControl implements GetAllAccessoriesInputBoundary {

    private final GetAllAccessoriesOutputBoundary outputBoundary;
    private final ProductRepository productRepository;

    public GetAllAccessoriesUseCaseControl(
            GetAllAccessoriesOutputBoundary outputBoundary,
            ProductRepository productRepository
    ) {
        this.outputBoundary = outputBoundary;
        this.productRepository = productRepository;
    }

    @Override
    public void execute() {
        GetAllAccessoriesOutputData outputData = null;
        Exception errorException = null;

        try {
            List<PhuKienXeMay> all = productRepository.findAllAccessories();

            List<AccessoryItem> items = all.stream()
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

            outputData = new GetAllAccessoriesOutputData(items);
        } catch (Exception e) {
            errorException = e;
        }

        if (errorException != null) {
            outputData = new GetAllAccessoriesOutputData("SYSTEM_ERROR", errorException.getMessage());
        }

        outputBoundary.present(outputData);
    }
}
