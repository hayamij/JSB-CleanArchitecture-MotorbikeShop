package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.accessory.GetAllAccessoriesOutputData;
import com.motorbike.business.dto.accessory.GetAllAccessoriesOutputData.AccessoryItem;
import com.motorbike.business.ports.repository.ProductRepository;
import com.motorbike.business.usecase.input.GetAllAccessoriesInputBoundary;
import com.motorbike.business.usecase.output.GetAllAccessoriesOutputBoundary;
import com.motorbike.domain.entities.SanPham;
import com.motorbike.domain.entities.PhuKienXeMay;

import java.util.List;
import java.util.stream.Collectors;

public class GetAllAccessoriesUseCaseControl
    extends AbstractUseCaseControl<Void, GetAllAccessoriesOutputBoundary> implements GetAllAccessoriesInputBoundary {

    private final ProductRepository productRepository;

    public GetAllAccessoriesUseCaseControl(GetAllAccessoriesOutputBoundary outputBoundary,
                                          ProductRepository productRepository) {
        super(outputBoundary);
        this.productRepository = productRepository;
    }

    @Override
    protected void validateInput(Void inputData) {
        // no input
    }

    @Override
    protected void executeBusinessLogic(Void inputData) {
        try {
            List<SanPham> allProducts = productRepository.findAll();

            List<AccessoryItem> accessories = allProducts.stream()
                    .filter(p -> p instanceof PhuKienXeMay)
                    .map(p -> mapToItem((PhuKienXeMay) p))
                    .collect(Collectors.toList());

            GetAllAccessoriesOutputData outputData = new GetAllAccessoriesOutputData(accessories);
            outputBoundary.present(outputData);

        } catch (Exception e) {
            GetAllAccessoriesOutputData error = new GetAllAccessoriesOutputData("SYSTEM_ERROR", e.getMessage());
            outputBoundary.present(error);
        }
    }

    private AccessoryItem mapToItem(PhuKienXeMay p) {
        return new AccessoryItem(
                p.getMaSanPham(),
                p.getTenSanPham(),
                p.getMoTa(),
                p.getGia(),
                p.getSoLuongTonKho(),
                p.getHinhAnh(),
                p.getLoaiPhuKien(),
                p.getThuongHieu(),
                p.getChatLieu(),
                p.getKichThuoc()
        );
    }

    @Override
    protected void handleValidationError(IllegalArgumentException e) {
        GetAllAccessoriesOutputData error = new GetAllAccessoriesOutputData("INVALID_INPUT", e.getMessage());
        outputBoundary.present(error);
    }

    @Override
    protected void handleSystemError(Exception e) {
        GetAllAccessoriesOutputData error = new GetAllAccessoriesOutputData("SYSTEM_ERROR", e.getMessage());
        outputBoundary.present(error);
    }
}
