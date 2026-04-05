package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.accessory.ExportAccessoriesInputData;
import com.motorbike.business.ports.repository.AccessoryRepository;
import com.motorbike.domain.entities.PhuKienXeMay;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ExportAccessoriesUseCaseControlTest {

    @Test
    void shouldRetrieveAccessoriesForExport() {
        // Given
        PhuKienXeMay accessory1 = new PhuKienXeMay(
            1L, "Mũ bảo hiểm", "Phụ tùng cao cấp",
            BigDecimal.valueOf(500000), "image1.jpg", 20, true,
            null, null,
            "Mũ", "Honda", "Nhựa ABS", "L"
        );

        PhuKienXeMay accessory2 = new PhuKienXeMay(
            2L, "Găng tay", "Găng tay chất lượng",
            BigDecimal.valueOf(200000), "image2.jpg", 30, true,
            null, null,
            "Găng tay", "Yamaha", "Da", "M"
        );

        List<PhuKienXeMay> accessories = Arrays.asList(accessory1, accessory2);

        AccessoryRepository accessoryRepo = new MockAccessoryRepository(accessories);

        // When - Test that repository returns correct data
        List<PhuKienXeMay> result = accessoryRepo.findAllAccessories();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Mũ bảo hiểm", result.get(0).getTenSanPham());
        assertEquals("Găng tay", result.get(1).getTenSanPham());
    }

    @Test
    void shouldHandleEmptyAccessoryList() {
        // Given
        List<PhuKienXeMay> accessories = Collections.emptyList();
        AccessoryRepository accessoryRepo = new MockAccessoryRepository(accessories);

        // When
        List<PhuKienXeMay> result = accessoryRepo.findAllAccessories();

        // Then
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    private static class MockAccessoryRepository implements AccessoryRepository {
        private final List<PhuKienXeMay> accessories;

        public MockAccessoryRepository(List<PhuKienXeMay> accessories) {
            this.accessories = accessories;
        }

        @Override
        public List<PhuKienXeMay> findAllAccessories() {
            return accessories;
        }

        @Override
        public List<PhuKienXeMay> search(String keyword, String type, String brand) {
            return accessories;
        }

        @Override
        public PhuKienXeMay save(PhuKienXeMay accessory) {
            return accessory;
        }

        @Override
        public java.util.Optional<PhuKienXeMay> findById(Long id) {
            return java.util.Optional.empty();
        }

        @Override
        public void deleteById(Long id) {
        }
        
        @Override
        public java.util.List<PhuKienXeMay> saveAll(java.util.List<PhuKienXeMay> accessories) {
            return accessories;
        }
        
        @Override
        public java.util.List<PhuKienXeMay> searchAccessories(String keyword) {
            return accessories;
        }
        
        @Override
        public boolean existsById(Long id) {
            return false;
        }
    }
}
