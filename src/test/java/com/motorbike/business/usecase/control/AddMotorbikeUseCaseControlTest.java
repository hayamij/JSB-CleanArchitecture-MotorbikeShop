package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.motorbike.AddMotorbikeInputData;
import com.motorbike.business.dto.motorbike.AddMotorbikeOutputData;
import com.motorbike.business.ports.repository.MotorbikeRepository;
import com.motorbike.business.usecase.output.AddMotorbikeOutputBoundary;
import com.motorbike.domain.entities.XeMay;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AddMotorbikeUseCaseControlTest {

    // =======================
    // Mock Presenter
    // =======================
    private static class MockPresenter implements AddMotorbikeOutputBoundary {
        public AddMotorbikeOutputData receivedData;

        @Override
        public void present(AddMotorbikeOutputData outputData) {
            this.receivedData = outputData;
        }
    }

    // =======================
    // Mock MotorbikeRepository
    // =======================
    private static class MockMotorbikeRepository implements MotorbikeRepository {

        public final List<XeMay> store = new ArrayList<>();
        private final boolean throwException;
        private long idCounter = 1L;

        public MockMotorbikeRepository() {
            this(false);
        }

        public MockMotorbikeRepository(boolean throwException) {
            this.throwException = throwException;
        }

        @Override
        public List<XeMay> findAllMotorbikes() {
            if (throwException) {
                throw new RuntimeException("DB error");
            }
            return store;
        }

        @Override
        public XeMay save(XeMay xe) {
            if (throwException) {
                throw new RuntimeException("Cannot save");
            }

            if (xe.getMaSanPham() == null) {
                xe.setMaSanPham(idCounter++);
            }

            store.removeIf(x -> x.getMaSanPham().equals(xe.getMaSanPham()));
            store.add(xe);
            return xe;
        }

        @Override
        public java.util.Optional<XeMay> findById(Long id) {
            return java.util.Optional.empty();
        }

        @Override
        public void deleteById(Long id) {}
    }

    // ----------------------------------------------------------
    // 1) Thêm xe mới thành công
    // ----------------------------------------------------------
    @Test
    void testAddMotorbike_New_Success() {
        MockPresenter presenter = new MockPresenter();
        MockMotorbikeRepository repo = new MockMotorbikeRepository();

        AddMotorbikeUseCaseControl control =
                new AddMotorbikeUseCaseControl(presenter, repo);

        AddMotorbikeInputData input = new AddMotorbikeInputData(
                "Winner X",
                "Sport",
                new BigDecimal("45000000"),
                "img.jpg",
                10,
                "Honda",
                "Winner X",
                "Đỏ",
                2024,
                150,
                "motorbike"
        );

        control.execute(input);

        assertNotNull(presenter.receivedData);
        assertTrue(presenter.receivedData.isSuccess());
        assertNull(presenter.receivedData.getErrorCode());
        assertNotNull(presenter.receivedData.getMotorbike());
        assertEquals("Winner X", presenter.receivedData.getMotorbike().name);
    }

    // ----------------------------------------------------------
    // 2) Đã tồn tại xe → cộng thêm tồn kho
    // ----------------------------------------------------------
    @Test
    void testAddMotorbike_Existing_UpdateStock() {
        MockPresenter presenter = new MockPresenter();
        MockMotorbikeRepository repo = new MockMotorbikeRepository();

        XeMay existing = new XeMay(
                "Winner X",
                "Sport",
                new BigDecimal("45000000"),
                "img.jpg",
                5,
                "Honda",
                "Winner X",
                "Đỏ",
                2024,
                150
        );
        existing.setMaSanPham(1L);
        repo.store.add(existing);

        AddMotorbikeUseCaseControl control =
                new AddMotorbikeUseCaseControl(presenter, repo);

        AddMotorbikeInputData input = new AddMotorbikeInputData(
                "Winner X",
                "Sport",
                new BigDecimal("45000000"),
                "img.jpg",
                7,
                "Honda",
                "Winner X",
                "Đỏ",
                2024,
                150,
                "motorbike"
        );

        control.execute(input);

        assertTrue(presenter.receivedData.isSuccess());
        assertEquals(12, presenter.receivedData.getMotorbike().stock);
    }

    // ----------------------------------------------------------
    // 3) Name không hợp lệ → lỗi validate
    // ----------------------------------------------------------
    @Test
    void testAddMotorbike_InvalidName() {
        MockPresenter presenter = new MockPresenter();
        MockMotorbikeRepository repo = new MockMotorbikeRepository();

        AddMotorbikeUseCaseControl control =
                new AddMotorbikeUseCaseControl(presenter, repo);

        AddMotorbikeInputData input = new AddMotorbikeInputData(
                null,
                "desc",
                new BigDecimal("1000"),
                "img",
                1,
                "Honda",
                "Wave",
                "Đỏ",
                2023,
                110,
                "motorbike"
        );

        control.execute(input);

        assertFalse(presenter.receivedData.isSuccess());
        assertEquals("SYSTEM_ERROR", presenter.receivedData.getErrorCode());
        assertNotNull(presenter.receivedData.getErrorMessage());
    }

    // ----------------------------------------------------------
    // 4) Price <= 0 → lỗi validate
    // ----------------------------------------------------------
    @Test
    void testAddMotorbike_InvalidPrice() {
        MockPresenter presenter = new MockPresenter();
        MockMotorbikeRepository repo = new MockMotorbikeRepository();

        AddMotorbikeUseCaseControl control =
                new AddMotorbikeUseCaseControl(presenter, repo);

        AddMotorbikeInputData input = new AddMotorbikeInputData(
                "Xe Test",
                "desc",
                BigDecimal.ZERO,
                "img",
                1,
                "Honda",
                "Wave",
                "Đỏ",
                2023,
                110,
                "motorbike"
        );

        control.execute(input);

        assertFalse(presenter.receivedData.isSuccess());
        assertEquals("SYSTEM_ERROR", presenter.receivedData.getErrorCode());
        assertNotNull(presenter.receivedData.getErrorMessage());
    }

    // ----------------------------------------------------------
    // 5) Repository ném exception → SYSTEM_ERROR
    // ----------------------------------------------------------
    @Test
    void testAddMotorbike_RepositoryThrows() {
        MockPresenter presenter = new MockPresenter();
        MockMotorbikeRepository repo = new MockMotorbikeRepository(true);

        AddMotorbikeUseCaseControl control =
                new AddMotorbikeUseCaseControl(presenter, repo);

        AddMotorbikeInputData input = new AddMotorbikeInputData(
                "Xe Test",
                "desc",
                new BigDecimal("1000"),
                "img",
                1,
                "Honda",
                "Wave",
                "Đỏ",
                2023,
                110,
                "motorbike"
        );

        control.execute(input);

        assertFalse(presenter.receivedData.isSuccess());
        assertEquals("SYSTEM_ERROR", presenter.receivedData.getErrorCode());
        assertNotNull(presenter.receivedData.getErrorMessage());
    }
}
