package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.motorbike.AddMotorbikeInputData;
import com.motorbike.business.dto.motorbike.AddMotorbikeOutputData;
import com.motorbike.business.dto.motorbike.AddMotorbikeOutputData.MotorbikeItem;
import com.motorbike.business.ports.repository.MotorbikeRepository;
import com.motorbike.business.usecase.input.AddMotorbikeInputBoundary;
import com.motorbike.business.usecase.output.AddMotorbikeOutputBoundary;

import com.motorbike.domain.entities.XeMay;

public class AddMotorbikeUseCaseControl implements AddMotorbikeInputBoundary {

    private final AddMotorbikeOutputBoundary outputBoundary;
    private final MotorbikeRepository motorbikeRepository;

    public AddMotorbikeUseCaseControl(
            AddMotorbikeOutputBoundary outputBoundary,
            MotorbikeRepository motorbikeRepository
    ) {
        this.outputBoundary = outputBoundary;
        this.motorbikeRepository = motorbikeRepository;
    }

    @Override
    public void execute(AddMotorbikeInputData input) {
        AddMotorbikeOutputData outputData;
    
        try {
            if (input.name == null || input.name.isBlank()) {
                throw new IllegalArgumentException("Product name cannot be empty");
            }
            if (input.price == null || input.price.compareTo(new java.math.BigDecimal("0")) <= 0) {
                throw new IllegalArgumentException("Price must be > 0");
            }
            if (input.stock < 0) {
                throw new IllegalArgumentException("Stock must be >= 0");
            }
    
            XeMay existing = motorbikeRepository
                    .findAllMotorbikes()
                    .stream()
                    .filter(x ->
                            x.getTenSanPham().equalsIgnoreCase(input.name) &&
                            x.getHangXe().equalsIgnoreCase(input.brand) &&
                            x.getDongXe().equalsIgnoreCase(input.model) &&
                            x.getMauSac().equalsIgnoreCase(input.color) &&
                            x.getNamSanXuat() == input.year &&
                            x.getDungTich() == input.displacement
                    )
                    .findFirst()
                    .orElse(null);
    
            XeMay saved;
    
            if (existing != null) {
                existing.setSoLuongTonKho(existing.getSoLuongTonKho() + input.stock);
                saved = motorbikeRepository.save(existing);
    
            } else {
                XeMay xeMay = new XeMay(
                        input.name,
                        input.description,
                        input.price,
                        input.imageUrl,
                        input.stock,
                        input.brand,
                        input.model,
                        input.color,
                        input.year,
                        input.displacement
                );
    
                saved = motorbikeRepository.save(xeMay);
            }
    
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
    
            outputData = new AddMotorbikeOutputData(item);
    
        } catch (Exception e) {
            outputData = new AddMotorbikeOutputData("SYSTEM_ERROR", e.getMessage());
        }
    
        outputBoundary.present(outputData);
    }
    
}
