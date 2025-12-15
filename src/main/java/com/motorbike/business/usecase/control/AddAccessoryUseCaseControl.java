package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.accessory.AddAccessoryInputData;
import com.motorbike.business.dto.accessory.AddAccessoryOutputData;
import com.motorbike.business.dto.accessory.AddAccessoryOutputData.AccessoryItem;
import com.motorbike.business.ports.repository.AccessoryRepository;
import com.motorbike.business.usecase.input.AddAccessoryInputBoundary;
import com.motorbike.business.usecase.output.AddAccessoryOutputBoundary;
import com.motorbike.domain.entities.PhuKienXeMay;

public class AddAccessoryUseCaseControl implements AddAccessoryInputBoundary {

    private final AddAccessoryOutputBoundary outputBoundary;
    private final AccessoryRepository accessoryRepository;

    public AddAccessoryUseCaseControl(
            AddAccessoryOutputBoundary outputBoundary,
            AccessoryRepository accessoryRepository
    ) {
        this.outputBoundary = outputBoundary;
        this.accessoryRepository = accessoryRepository;
    }

    @Override
    public void execute(AddAccessoryInputData input) {
        AddAccessoryOutputData outputData;

        try {
            // Validate
            if (input.name == null || input.name.isBlank()) {
                throw new IllegalArgumentException("Product name cannot be empty");
            }
            if (input.price == null || input.price.compareTo(new java.math.BigDecimal("0")) <= 0) {
                throw new IllegalArgumentException("Price must be > 0");
            }
            if (input.stock < 0) {
                throw new IllegalArgumentException("Stock must be >= 0");
            }

            // Tìm xem đã có phụ kiện trùng chưa
            PhuKienXeMay existing = accessoryRepository
                    .findAllAccessories()
                    .stream()
                    .filter(x ->
                            x.getTenSanPham().equalsIgnoreCase(input.name) &&
                            x.getLoaiPhuKien().equalsIgnoreCase(input.type) &&
                            x.getThuongHieu().equalsIgnoreCase(input.brand)
                    )
                    .findFirst()
                    .orElse(null);

            PhuKienXeMay saved;

            if (existing != null) {
                // ĐÃ CÓ → CỘNG THÊM TỒN KHO
                existing.setSoLuongTonKho(existing.getSoLuongTonKho() + input.stock);
                saved = accessoryRepository.save(existing);
            } else {
                // CHƯA CÓ → TẠO MỚI
                PhuKienXeMay accessory = new PhuKienXeMay(
                        input.name,
                        input.description,
                        input.price,
                        input.imageUrl,
                        input.stock,
                        input.type,
                        input.brand,
                        input.material,
                        input.size
                );

                saved = accessoryRepository.save(accessory);
            }

            // Map to output
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

            outputData = new AddAccessoryOutputData(item);

        } catch (IllegalArgumentException e) {
            outputData = new AddAccessoryOutputData("VALIDATION_ERROR", e.getMessage());
        } catch (Exception e) {
            outputData = new AddAccessoryOutputData("SYSTEM_ERROR", e.getMessage());
        }

        outputBoundary.present(outputData);
    }
}
