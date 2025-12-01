package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.motorbike.GetAllMotorbikesOutputData;
import com.motorbike.business.dto.motorbike.GetAllMotorbikesOutputData.MotorbikeItem;
import com.motorbike.business.ports.repository.ProductRepository;
import com.motorbike.business.usecase.input.GetAllMotorbikesInputBoundary;
import com.motorbike.business.usecase.output.GetAllMotorbikesOutputBoundary;
import com.motorbike.domain.entities.SanPham;
import com.motorbike.domain.entities.XeMay;

import java.util.List;
import java.util.stream.Collectors;

public class GetAllMotorbikesUseCaseControl
        extends AbstractUseCaseControl<Void, GetAllMotorbikesOutputBoundary> implements GetAllMotorbikesInputBoundary {

    private final ProductRepository productRepository;

    public GetAllMotorbikesUseCaseControl(
            GetAllMotorbikesOutputBoundary outputBoundary,
            ProductRepository productRepository
    ) {
        super(outputBoundary);
        this.productRepository = productRepository;
    }

    @Override
    protected void validateInput(Void inputData) {
        // Không có input nên không cần validate
    }

    @Override
    protected void executeBusinessLogic(Void inputData) {
        try {
            List<SanPham> allProducts = productRepository.findAll();

            List<MotorbikeItem> motorbikes = allProducts.stream()
                    .filter(p -> p instanceof XeMay)
                    .map(p -> mapToItem((XeMay) p))
                    .collect(Collectors.toList());

            GetAllMotorbikesOutputData outputData =
                    new GetAllMotorbikesOutputData(motorbikes);

            outputBoundary.present(outputData);

        } catch (Exception e) {
            GetAllMotorbikesOutputData error =
                    new GetAllMotorbikesOutputData("SYSTEM_ERROR", e.getMessage());

            outputBoundary.present(error);
        }
    }

    private MotorbikeItem mapToItem(XeMay x) {
        return new MotorbikeItem(
                x.getMaSanPham(),
                x.getTenSanPham(),
                x.getMoTa(),
                x.getGia(),
                x.getSoLuongTonKho(),
                x.getHinhAnh(),
                x.getHangXe(),
                x.getDongXe(),
                x.getMauSac(),
                x.getNamSanXuat(),
                x.getDungTich()
        );
    }

    @Override
    protected void handleValidationError(IllegalArgumentException e) {
        GetAllMotorbikesOutputData error =
                new GetAllMotorbikesOutputData("INVALID_INPUT", e.getMessage());
        outputBoundary.present(error);
    }

    @Override
    protected void handleSystemError(Exception e) {
        GetAllMotorbikesOutputData error =
                new GetAllMotorbikesOutputData("SYSTEM_ERROR", e.getMessage());
        outputBoundary.present(error);
    }
}
