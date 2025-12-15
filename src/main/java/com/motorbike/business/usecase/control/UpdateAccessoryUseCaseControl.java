package com.motorbike.business.usecase.control;

import java.util.Optional;

import com.motorbike.business.dto.accessory.UpdateAccessoryInputData;
import com.motorbike.business.dto.accessory.UpdateAccessoryOutputData;
import com.motorbike.business.dto.accessory.UpdateAccessoryOutputData.AccessoryItem;
import com.motorbike.business.ports.repository.AccessoryRepository;
import com.motorbike.business.usecase.input.UpdateAccessoryInputBoundary;
import com.motorbike.business.usecase.output.UpdateAccessoryOutputBoundary;
import com.motorbike.domain.entities.PhuKienXeMay;

public class UpdateAccessoryUseCaseControl implements UpdateAccessoryInputBoundary {

    private final UpdateAccessoryOutputBoundary outputBoundary;
    private final AccessoryRepository accessoryRepository;

    public UpdateAccessoryUseCaseControl(
            UpdateAccessoryOutputBoundary outputBoundary,
            AccessoryRepository accessoryRepository
    ) {
        this.outputBoundary = outputBoundary;
        this.accessoryRepository = accessoryRepository;
    }

    @Override
    public void execute(UpdateAccessoryInputData input) {
        UpdateAccessoryOutputData outputData;

        try {
            // Validate
            if (input.id == null) {
                throw new IllegalArgumentException("ID cannot be null");
            }

            Optional<PhuKienXeMay> optionalAccessory = accessoryRepository.findById(input.id);
            if (optionalAccessory.isEmpty()) {
                throw new IllegalArgumentException("Accessory not found with ID: " + input.id);
            }

            PhuKienXeMay accessory = optionalAccessory.get();

            // Update fields
            if (input.name != null && !input.name.isBlank()) {
                accessory.setTenSanPham(input.name);
            }
            if (input.description != null) {
                accessory.setMoTa(input.description);
            }
            if (input.price != null && input.price.compareTo(new java.math.BigDecimal("0")) > 0) {
                accessory.setGia(input.price);
            }
            if (input.imageUrl != null) {
                accessory.setHinhAnh(input.imageUrl);
            }
            if (input.stock >= 0) {
                accessory.setSoLuongTonKho(input.stock);
            }
            if (input.type != null) {
                accessory.setLoaiPhuKien(input.type);
            }
            if (input.brand != null) {
                accessory.setThuongHieu(input.brand);
            }
            if (input.material != null) {
                accessory.setChatLieu(input.material);
            }
            if (input.size != null) {
                accessory.setKichThuoc(input.size);
            }

            PhuKienXeMay saved = accessoryRepository.save(accessory);

            AccessoryItem item = new AccessoryItem(
                    saved.getMaSanPham(),
                    saved.getTenSanPham(),
                    saved.getMoTa(),
                    saved.getGia(),
                    saved.getSoLuongTonKho(),
                    saved.getHinhAnh(),
                    saved.getLoaiPhuKien(),
                    saved.getThuongHieu(),
                    saved.getChatLieu(),
                    saved.getKichThuoc()
            );

            outputData = new UpdateAccessoryOutputData(item);

        } catch (IllegalArgumentException e) {
            outputData = new UpdateAccessoryOutputData("VALIDATION_ERROR", e.getMessage());
        } catch (Exception e) {
            outputData = new UpdateAccessoryOutputData("SYSTEM_ERROR", e.getMessage());
        }

        outputBoundary.present(outputData);
    }
}
