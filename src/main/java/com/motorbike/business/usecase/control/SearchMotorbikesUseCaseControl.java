package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.motorbike.SearchMotorbikesInputData;
import com.motorbike.business.dto.motorbike.SearchMotorbikesOutputData;
import com.motorbike.business.dto.motorbike.SearchMotorbikesOutputData.MotorbikeItem;
import com.motorbike.business.ports.repository.MotorbikeRepository;
import com.motorbike.business.usecase.output.SearchMotorbikesOutputBoundary;
import com.motorbike.domain.entities.XeMay;

import java.util.List;
import java.util.stream.Collectors;

public class SearchMotorbikesUseCaseControl
        extends AbstractUseCaseControl<SearchMotorbikesInputData, SearchMotorbikesOutputBoundary>
        implements com.motorbike.business.usecase.input.SearchMotorbikesInputBoundary {

    private final MotorbikeRepository motorbikeRepository;

    public SearchMotorbikesUseCaseControl(
            SearchMotorbikesOutputBoundary outputBoundary,
            MotorbikeRepository motorbikeRepository
    ) {
        super(outputBoundary);
        this.motorbikeRepository = motorbikeRepository;
    }

    @Override
    protected void validateInput(SearchMotorbikesInputData input) {
        // không có rule bắt buộc -> không validate
    }

    @Override
    protected void executeBusinessLogic(SearchMotorbikesInputData input) {
        try {
            List<XeMay> all = motorbikeRepository.findAllMotorbikes();

            // Lọc xe máy theo tiêu chí
            List<MotorbikeItem> filtered = all.stream()
                    .filter(x -> input.keyword == null || x.getTenSanPham().toLowerCase().contains(input.keyword.toLowerCase()))
                    .filter(x -> input.brand == null || x.getHangXe().equalsIgnoreCase(input.brand))
                    .filter(x -> input.model == null || x.getDongXe().equalsIgnoreCase(input.model))
                    .filter(x -> input.color == null || x.getMauSac().equalsIgnoreCase(input.color))
                    .filter(x -> input.minCC == null || x.getDungTich() >= input.minCC)
                    .filter(x -> input.maxCC == null || x.getDungTich() <= input.maxCC)
                    .map(this::mapToItem)
                    .collect(Collectors.toList());

            SearchMotorbikesOutputData output = new SearchMotorbikesOutputData(filtered);
            outputBoundary.present(output);

        } catch (Exception e) {
            SearchMotorbikesOutputData error = new SearchMotorbikesOutputData(
                    "SYSTEM_ERROR", e.getMessage()
            );
            outputBoundary.present(error);
        }
    }

    private MotorbikeItem mapToItem(XeMay x) {
      return new MotorbikeItem(
          x.getMaSanPham(),           // id
          x.getTenSanPham(),          // name
          x.getMoTa(),                // description
          x.getGia(),                 // price (BigDecimal)
          x.getSoLuongTonKho(),       // stock
          x.getHinhAnh(),             // imageUrl
          x.getHangXe(),              // brand
          x.getDongXe(),              // model
          x.getMauSac(),              // color
          x.getNamSanXuat(),          // year
          x.getDungTich()             // displacement
      );
  }


    @Override
    protected void handleValidationError(IllegalArgumentException e) {
        SearchMotorbikesOutputData error =
                new SearchMotorbikesOutputData("INVALID_INPUT", e.getMessage());
        outputBoundary.present(error);
    }

    @Override
    protected void handleSystemError(Exception e) {
        SearchMotorbikesOutputData error =
                new SearchMotorbikesOutputData("SYSTEM_ERROR", e.getMessage());
        outputBoundary.present(error);
    }
}
