package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.motorbike.DeleteMotorbikeInputData;
import com.motorbike.business.dto.motorbike.DeleteMotorbikeOutputData;
import com.motorbike.business.usecase.output.DeleteMotorbikeOutputBoundary;
import com.motorbike.business.ports.repository.MotorbikeRepository;
import com.motorbike.domain.entities.XeMay;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class DeleteMotorbikeUseCaseControlTest {

    private static class MockPresenter implements DeleteMotorbikeOutputBoundary {
        public DeleteMotorbikeOutputData received;

        @Override
        public void present(DeleteMotorbikeOutputData outputData) {
            this.received = outputData;
        }
    }

    private static class MockRepo implements MotorbikeRepository {

        public Map<Long, XeMay> store = new HashMap<>();
        public boolean throwException = false;

        @Override
        public Optional<XeMay> findById(Long id) {
            if (throwException) {
                throw new RuntimeException("DB find error");
            }
            return Optional.ofNullable(store.get(id));
        }

        @Override
        public void deleteById(Long id) {
            if (throwException) {
                throw new RuntimeException("DB delete error");
            }
            store.remove(id);
        }

        @Override
        public List<XeMay> findAllMotorbikes() {
            return new ArrayList<>(store.values());
        }

        @Override
        public XeMay save(XeMay xe) {
            store.put(xe.getMaSanPham(), xe);
            return xe;
        }
    }

    @Test
    void testDelete_Success() {
        MockPresenter presenter = new MockPresenter();
        MockRepo repo = new MockRepo();

        XeMay xe = new XeMay(
                "Test Name",
                "Test Desc",
                new BigDecimal("1000"),
                "img.jpg",
                5,
                "Honda",
                "Wave",
                "Đỏ",
                2023,
                110
        );
        xe.setMaSanPham(1L);
        repo.store.put(1L, xe);

        DeleteMotorbikeUseCaseControl control =
                new DeleteMotorbikeUseCaseControl(presenter, repo);

        DeleteMotorbikeInputData input = new DeleteMotorbikeInputData(1L);

        control.execute(input);

        assertNotNull(presenter.received);
        assertTrue(presenter.received.success);
        assertNull(presenter.received.errorCode);
        assertNull(presenter.received.errorMessage);
        assertFalse(repo.store.containsKey(1L));
    }

    @Test
    void testDelete_NotFound() {
        MockPresenter presenter = new MockPresenter();
        MockRepo repo = new MockRepo(); // store rỗng

        DeleteMotorbikeUseCaseControl control =
                new DeleteMotorbikeUseCaseControl(presenter, repo);

        DeleteMotorbikeInputData input = new DeleteMotorbikeInputData(999L);

        control.execute(input);

        assertFalse(presenter.received.success);
        assertEquals("NOT_FOUND", presenter.received.errorCode);
        assertEquals("Motorbike not found", presenter.received.errorMessage);
    }

    @Test
    void testDelete_RepositoryThrows() {
        MockPresenter presenter = new MockPresenter();
        MockRepo repo = new MockRepo();
        repo.throwException = true; // ép repo ném lỗi

        DeleteMotorbikeUseCaseControl control =
                new DeleteMotorbikeUseCaseControl(presenter, repo);

        DeleteMotorbikeInputData input = new DeleteMotorbikeInputData(1L);

        control.execute(input);

        assertFalse(presenter.received.success);
        assertEquals("SYSTEM_ERROR", presenter.received.errorCode);
        assertNotNull(presenter.received.errorMessage);
    }
}
