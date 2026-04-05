package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.motorbike.ExportMotorbikesInputData;
import com.motorbike.business.ports.repository.MotorbikeRepository;
import com.motorbike.domain.entities.XeMay;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ExportMotorbikesUseCaseControlTest {

    @Test
    void shouldRetrieveMotorbikesForExport() {
        // Given
        XeMay motorbike1 = new XeMay(
            1L, "Yamaha Exciter", "Xe thể thao",
            BigDecimal.valueOf(45000000), "image1.jpg", 10, true,
            null, null,
            "Yamaha", "Exciter", "Đỏ", 2024, 150
        );

        XeMay motorbike2 = new XeMay(
            2L, "Honda Wave", "Xe số",
            BigDecimal.valueOf(30000000), "image2.jpg", 15, true,
            null, null,
            "Honda", "Wave", "Xanh", 2024, 110
        );

        List<XeMay> motorbikes = Arrays.asList(motorbike1, motorbike2);

        MotorbikeRepository motorbikeRepo = new MockMotorbikeRepository(motorbikes);

        // When - Test that repository returns correct data
        List<XeMay> result = motorbikeRepo.findAllMotorbikes();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Yamaha Exciter", result.get(0).getTenSanPham());
        assertEquals("Honda Wave", result.get(1).getTenSanPham());
    }

    @Test
    void shouldHandleEmptyMotorbikeList() {
        // Given
        List<XeMay> motorbikes = Collections.emptyList();
        MotorbikeRepository motorbikeRepo = new MockMotorbikeRepository(motorbikes);

        // When
        List<XeMay> result = motorbikeRepo.findAllMotorbikes();

        // Then
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    private static class MockMotorbikeRepository implements MotorbikeRepository {
        private final List<XeMay> motorbikes;

        public MockMotorbikeRepository(List<XeMay> motorbikes) {
            this.motorbikes = motorbikes;
        }

        @Override
        public List<XeMay> findAllMotorbikes() {
            return motorbikes;
        }

        public List<XeMay> search(String keyword, String brand, String model) {
            return motorbikes;
        }

        @Override
        public XeMay save(XeMay motorbike) {
            return motorbike;
        }

        @Override
        public java.util.Optional<XeMay> findById(Long id) {
            return java.util.Optional.empty();
        }

        @Override
        public void deleteById(Long id) {
        }
        
        @Override
        public java.util.List<XeMay> saveAll(java.util.List<XeMay> motorbikes) {
            return motorbikes;
        }
        
        @Override
        public java.util.List<XeMay> searchMotorbikes(String keyword) {
            return motorbikes;
        }
        
        @Override
        public boolean existsById(Long id) {
            return false;
        }
    }
}
