package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.motorbike.UpdateMotorbikeInputData;
import com.motorbike.business.dto.motorbike.UpdateMotorbikeOutputData;
import com.motorbike.business.dto.motorbike.UpdateMotorbikeOutputData.MotorbikeItem;
import com.motorbike.business.ports.repository.MotorbikeRepository;
import com.motorbike.business.usecase.input.UpdateMotorbikeInputBoundary;
import com.motorbike.business.usecase.output.UpdateMotorbikeOutputBoundary;
import com.motorbike.domain.entities.XeMay;

public class UpdateMotorbikeUseCaseControl implements UpdateMotorbikeInputBoundary {

    private final UpdateMotorbikeOutputBoundary outputBoundary;
    private final MotorbikeRepository motorbikeRepository;

    public UpdateMotorbikeUseCaseControl(
            UpdateMotorbikeOutputBoundary outputBoundary,
            MotorbikeRepository motorbikeRepository
    ) {
        this.outputBoundary = outputBoundary;
        this.motorbikeRepository = motorbikeRepository;
    }

    @Override
    public void execute(UpdateMotorbikeInputData input) {
        UpdateMotorbikeOutputData output;

        try {
            XeMay existing = motorbikeRepository.findById(input.id).orElse(null);

            if (existing == null) {
                output = new UpdateMotorbikeOutputData("NOT_FOUND", "Motorbike not found");
                outputBoundary.present(output);
                return;
            }

            if (input.name != null) existing.setTenSanPham(input.name);
            if (input.description != null) existing.setMoTa(input.description);
            if (input.price != null) existing.setGia(input.price);
            if (input.imageUrl != null) existing.setHinhAnh(input.imageUrl);
            if (input.stock != null) existing.setSoLuongTonKho(input.stock);
            if (input.brand != null) existing.setHangXe(input.brand);
            if (input.model != null) existing.setDongXe(input.model);
            if (input.color != null) existing.setMauSac(input.color);
            if (input.year != null) existing.setNamSanXuat(input.year);
            if (input.displacement != null) existing.setDungTich(input.displacement);

            XeMay saved = motorbikeRepository.save(existing);

            MotorbikeItem item = new MotorbikeItem(
                    saved.getMaSanPham(),
                    saved.getTenSanPham(),
                    saved.getMoTa(),
                    saved.getGia(),
                    saved.getSoLuongTonKho(),
                    saved.getHinhAnh(),
                    saved.getHangXe(),
                    saved.getDongXe(),
                    saved.getMauSac(),
                    saved.getNamSanXuat(),
                    saved.getDungTich()
            );

            output = new UpdateMotorbikeOutputData(item);

        } catch (Exception e) {
            output = new UpdateMotorbikeOutputData("SYSTEM_ERROR", e.getMessage());
        }

        outputBoundary.present(output);
    }

}
