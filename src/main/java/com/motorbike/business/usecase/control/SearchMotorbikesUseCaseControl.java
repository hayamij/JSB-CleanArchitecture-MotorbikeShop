package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.motorbike.SearchMotorbikesInputData;
import com.motorbike.business.dto.motorbike.SearchMotorbikesOutputData;
import com.motorbike.business.dto.motorbike.SearchMotorbikesOutputData.MotorbikeItem;
import com.motorbike.business.ports.repository.MotorbikeRepository;
import com.motorbike.business.usecase.output.SearchMotorbikesOutputBoundary;
import com.motorbike.domain.entities.XeMay;
import com.motorbike.business.usecase.input.SearchMotorbikesInputBoundary;

import java.util.List;
import java.util.stream.Collectors;

public class SearchMotorbikesUseCaseControl implements SearchMotorbikesInputBoundary{

    private final SearchMotorbikesOutputBoundary outputBoundary;
    private final MotorbikeRepository motorbikeRepository;

    public SearchMotorbikesUseCaseControl(
            SearchMotorbikesOutputBoundary outputBoundary,
            MotorbikeRepository motorbikeRepository
    ) {
        this.outputBoundary = outputBoundary;
        this.motorbikeRepository = motorbikeRepository;
    }

    @Override
    public void execute(SearchMotorbikesInputData input) {
        SearchMotorbikesOutputData outputData = null;
        Exception errorException = null;

        try {
            List<XeMay> all = motorbikeRepository.findAllMotorbikes();

            List<MotorbikeItem> filtered = all.stream()
                    .filter(x -> input.keyword == null || x.getTenSanPham().toLowerCase().contains(input.keyword.toLowerCase()))
                    .filter(x -> input.brand == null || x.getHangXe().equalsIgnoreCase(input.brand))
                    .filter(x -> input.model == null || x.getDongXe().equalsIgnoreCase(input.model))
                    .filter(x -> input.color == null || x.getMauSac().equalsIgnoreCase(input.color))
                    .filter(x -> input.minCC == null || x.getDungTich() >= input.minCC)
                    .filter(x -> input.maxCC == null || x.getDungTich() <= input.maxCC)
                    .map(x -> new MotorbikeItem(
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
                    ))
                    .collect(Collectors.toList());

            outputData = new SearchMotorbikesOutputData(filtered);
        } catch (Exception e) {
            errorException = e;
        }

        if (errorException != null) {
            outputData = new SearchMotorbikesOutputData("SYSTEM_ERROR", errorException.getMessage());
        }

        outputBoundary.present(outputData);
    }
}
