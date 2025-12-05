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

public class GetAllMotorbikesUseCaseControl implements GetAllMotorbikesInputBoundary {

    private final GetAllMotorbikesOutputBoundary outputBoundary;
    private final ProductRepository productRepository;

    public GetAllMotorbikesUseCaseControl(
            GetAllMotorbikesOutputBoundary outputBoundary,
            ProductRepository productRepository
    ) {
        this.outputBoundary = outputBoundary;
        this.productRepository = productRepository;
    }

    @Override
    public void execute() {
        GetAllMotorbikesOutputData outputData = null;
        Exception errorException = null;

        try {
            List<SanPham> allProducts = productRepository.findAll();

            List<MotorbikeItem> motorbikes = allProducts.stream()
                    .filter(p -> p instanceof XeMay)
                    .map(p -> {
                        XeMay x = (XeMay) p;
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
                    })
                    .collect(Collectors.toList());

            outputData = new GetAllMotorbikesOutputData(motorbikes);
        } catch (Exception e) {
            errorException = e;
        }

        if (errorException != null) {
            outputData = new GetAllMotorbikesOutputData("SYSTEM_ERROR", errorException.getMessage());
        }

        outputBoundary.present(outputData);
    }
}
