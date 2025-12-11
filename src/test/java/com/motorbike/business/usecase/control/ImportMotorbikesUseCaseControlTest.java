package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.motorbike.ImportMotorbikesInputData;
import com.motorbike.business.dto.motorbike.ImportMotorbikesOutputData;
import com.motorbike.business.ports.parser.ExcelParser;
import com.motorbike.business.ports.repository.MotorbikeRepository;
import com.motorbike.business.usecase.output.ImportMotorbikesOutputBoundary;
import com.motorbike.domain.entities.XeMay;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ImportMotorbikesUseCaseControlTest {

    private static class MockPresenter implements ImportMotorbikesOutputBoundary {
        public ImportMotorbikesOutputData receivedData;

        @Override
        public void present(ImportMotorbikesOutputData outputData) {
            this.receivedData = outputData;
        }
    }

    private static class MockExcelParser implements ExcelParser {
        private List<List<String>> dataToReturn;
        private Exception exceptionToThrow;

        public void setDataToReturn(List<List<String>> data) {
            this.dataToReturn = data;
        }

        public void setExceptionToThrow(Exception e) {
            this.exceptionToThrow = e;
        }

        @Override
        public List<List<String>> parseExcelFile(InputStream inputStream) throws Exception {
            if (exceptionToThrow != null) {
                throw exceptionToThrow;
            }
            return dataToReturn;
        }
    }

    private static class MockMotorbikeRepository implements MotorbikeRepository {
        public List<XeMay> savedMotorbikes = new ArrayList<>();
        public Exception exceptionToThrow;

        @Override
        public List<XeMay> findAllMotorbikes() {
            return null;
        }

        @Override
        public java.util.Optional<XeMay> findById(Long id) {
            return java.util.Optional.empty();
        }

        @Override
        public XeMay save(XeMay xeMay) {
            if (exceptionToThrow != null) {
                throw new RuntimeException(exceptionToThrow);
            }
            savedMotorbikes.add(xeMay);
            return xeMay;
        }

        @Override
        public void deleteById(Long id) {
        }

        @Override
        public boolean existsById(Long id) {
            return false;
        }

        @Override
        public List<XeMay> searchMotorbikes(String keyword) {
            return null;
        }

        @Override
        public List<XeMay> saveAll(List<XeMay> xeMayList) {
            if (exceptionToThrow != null) {
                throw new RuntimeException(exceptionToThrow);
            }
            savedMotorbikes.addAll(xeMayList);
            return xeMayList;
        }
    }

    private MockPresenter presenter;
    private MockMotorbikeRepository repository;
    private MockExcelParser excelParser;
    private ImportMotorbikesUseCaseControl useCase;

    @BeforeEach
    void setUp() {
        presenter = new MockPresenter();
        repository = new MockMotorbikeRepository();
        excelParser = new MockExcelParser();
        useCase = new ImportMotorbikesUseCaseControl(presenter, repository, excelParser);
    }

    @Test
    void testImportMotorbikes_Success() {
        // Arrange - Tạo dữ liệu Excel hợp lệ
        List<List<String>> excelData = Arrays.asList(
            Arrays.asList("Tên SP", "Mô tả", "Giá", "Hình ảnh", "Tồn kho", "Hãng", "Dòng", "Màu", "Năm SX", "Dung tích"), // Header
            Arrays.asList("Wave Alpha", "Xe số tiết kiệm", "30000000", "wave.jpg", "10", "Honda", "Wave", "Đỏ", "2024", "110"),
            Arrays.asList("Vision", "Xe tay ga cao cấp", "35000000", "vision.jpg", "5", "Honda", "Vision", "Trắng", "2024", "125")
        );
        excelParser.setDataToReturn(excelData);

        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ImportMotorbikesInputData inputData = new ImportMotorbikesInputData(inputStream, "motorbikes.xlsx");

        // Act
        useCase.execute(inputData);

        // Assert
        assertNotNull(presenter.receivedData);
        assertFalse(presenter.receivedData.hasError());
        assertEquals(2, presenter.receivedData.getTotalRecords());
        assertEquals(2, presenter.receivedData.getSuccessCount());
        assertEquals(0, presenter.receivedData.getFailureCount());
        assertEquals(2, repository.savedMotorbikes.size());
    }

    @Test
    void testImportMotorbikes_WithValidationErrors() {
        // Arrange - Dữ liệu có một dòng hợp lệ và một dòng lỗi
        List<List<String>> excelData = Arrays.asList(
            Arrays.asList("Tên SP", "Mô tả", "Giá", "Hình ảnh", "Tồn kho", "Hãng", "Dòng", "Màu", "Năm SX", "Dung tích"),
            Arrays.asList("Wave Alpha", "Xe số", "30000000", "wave.jpg", "10", "Honda", "Wave", "Đỏ", "2024", "110"), // Hợp lệ
            Arrays.asList("", "Xe tay ga", "35000000", "vision.jpg", "5", "Honda", "Vision", "Trắng", "2024", "125") // Tên rỗng - Lỗi
        );
        excelParser.setDataToReturn(excelData);

        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ImportMotorbikesInputData inputData = new ImportMotorbikesInputData(inputStream, "motorbikes.xlsx");

        // Act
        useCase.execute(inputData);

        // Assert
        assertNotNull(presenter.receivedData);
        assertFalse(presenter.receivedData.hasError());
        assertEquals(2, presenter.receivedData.getTotalRecords());
        assertEquals(1, presenter.receivedData.getSuccessCount());
        assertEquals(1, presenter.receivedData.getFailureCount());
        assertEquals(1, presenter.receivedData.getErrors().size());
        assertEquals(1, repository.savedMotorbikes.size());
    }

    @Test
    void testImportMotorbikes_EmptyFile() {
        // Arrange - File rỗng
        List<List<String>> emptyData = new ArrayList<>();
        excelParser.setDataToReturn(emptyData);

        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ImportMotorbikesInputData inputData = new ImportMotorbikesInputData(inputStream, "empty.xlsx");

        // Act
        useCase.execute(inputData);

        // Assert
        assertNotNull(presenter.receivedData);
        assertTrue(presenter.receivedData.hasError());
        assertEquals("EMPTY_FILE", presenter.receivedData.getErrorCode());
    }

    @Test
    void testImportMotorbikes_InvalidFileExtension() {
        // Arrange
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ImportMotorbikesInputData inputData = new ImportMotorbikesInputData(inputStream, "document.pdf");

        // Act
        useCase.execute(inputData);

        // Assert
        assertNotNull(presenter.receivedData);
        assertTrue(presenter.receivedData.hasError());
        assertEquals("VALIDATION_ERROR", presenter.receivedData.getErrorCode());
        assertTrue(presenter.receivedData.getErrorMessage().contains("định dạng"));
    }

    @Test
    void testImportMotorbikes_NullInput() {
        // Act
        useCase.execute(null);

        // Assert
        assertNotNull(presenter.receivedData);
        assertTrue(presenter.receivedData.hasError());
        assertEquals("VALIDATION_ERROR", presenter.receivedData.getErrorCode());
    }

    @Test
    void testImportMotorbikes_InvalidPriceFormat() {
        // Arrange - Giá không hợp lệ
        List<List<String>> excelData = Arrays.asList(
            Arrays.asList("Tên SP", "Mô tả", "Giá", "Hình ảnh", "Tồn kho", "Hãng", "Dòng", "Màu", "Năm SX", "Dung tích"),
            Arrays.asList("Wave Alpha", "Xe số", "ABC", "wave.jpg", "10", "Honda", "Wave", "Đỏ", "2024", "110") // Giá không phải số
        );
        excelParser.setDataToReturn(excelData);

        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ImportMotorbikesInputData inputData = new ImportMotorbikesInputData(inputStream, "motorbikes.xlsx");

        // Act
        useCase.execute(inputData);

        // Assert
        assertNotNull(presenter.receivedData);
        assertFalse(presenter.receivedData.hasError());
        assertEquals(1, presenter.receivedData.getTotalRecords());
        assertEquals(0, presenter.receivedData.getSuccessCount());
        assertEquals(1, presenter.receivedData.getFailureCount());
        assertEquals(1, presenter.receivedData.getErrors().size());
    }

    @Test
    void testImportMotorbikes_MissingColumns() {
        // Arrange - Không đủ cột
        List<List<String>> excelData = Arrays.asList(
            Arrays.asList("Tên SP", "Mô tả", "Giá"),
            Arrays.asList("Wave Alpha", "Xe số", "30000000") // Chỉ có 3 cột thay vì 10
        );
        excelParser.setDataToReturn(excelData);

        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ImportMotorbikesInputData inputData = new ImportMotorbikesInputData(inputStream, "motorbikes.xlsx");

        // Act
        useCase.execute(inputData);

        // Assert
        assertNotNull(presenter.receivedData);
        assertFalse(presenter.receivedData.hasError());
        assertEquals(1, presenter.receivedData.getTotalRecords());
        assertEquals(0, presenter.receivedData.getSuccessCount());
        assertEquals(1, presenter.receivedData.getFailureCount());
    }

    @Test
    void testImportMotorbikes_ParserException() {
        // Arrange - Parser ném exception
        excelParser.setExceptionToThrow(new Exception("File corrupted"));

        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ImportMotorbikesInputData inputData = new ImportMotorbikesInputData(inputStream, "motorbikes.xlsx");

        // Act
        useCase.execute(inputData);

        // Assert
        assertNotNull(presenter.receivedData);
        assertTrue(presenter.receivedData.hasError());
        assertEquals("IMPORT_ERROR", presenter.receivedData.getErrorCode());
    }

    @Test
    void testImportMotorbikes_RepositorySaveAllException() {
        // Arrange
        List<List<String>> excelData = Arrays.asList(
            Arrays.asList("Tên SP", "Mô tả", "Giá", "Hình ảnh", "Tồn kho", "Hãng", "Dòng", "Màu", "Năm SX", "Dung tích"),
            Arrays.asList("Wave Alpha", "Xe số", "30000000", "wave.jpg", "10", "Honda", "Wave", "Đỏ", "2024", "110"),
            Arrays.asList("Vision", "Xe tay ga", "35000000", "vision.jpg", "5", "Honda", "Vision", "Trắng", "2024", "125")
        );
        excelParser.setDataToReturn(excelData);
        repository.exceptionToThrow = new Exception("Database connection failed");

        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ImportMotorbikesInputData inputData = new ImportMotorbikesInputData(inputStream, "motorbikes.xlsx");

        // Act
        useCase.execute(inputData);

        // Assert
        assertNotNull(presenter.receivedData);
        assertFalse(presenter.receivedData.hasError()); // Bulk save fails, but individual saves are attempted
        // Since mock doesn't actually do individual saves after bulk failure in this simple implementation,
        // we check that at least the attempt was made
        assertEquals(2, presenter.receivedData.getTotalRecords());
    }

    @Test
    void testImportMotorbikes_InvalidYearFormat() {
        // Arrange
        List<List<String>> excelData = Arrays.asList(
            Arrays.asList("Tên SP", "Mô tả", "Giá", "Hình ảnh", "Tồn kho", "Hãng", "Dòng", "Màu", "Năm SX", "Dung tích"),
            Arrays.asList("Wave Alpha", "Xe số", "30000000", "wave.jpg", "10", "Honda", "Wave", "Đỏ", "ABC", "110") // Năm không hợp lệ
        );
        excelParser.setDataToReturn(excelData);

        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ImportMotorbikesInputData inputData = new ImportMotorbikesInputData(inputStream, "motorbikes.xlsx");

        // Act
        useCase.execute(inputData);

        // Assert
        assertNotNull(presenter.receivedData);
        assertFalse(presenter.receivedData.hasError());
        assertEquals(1, presenter.receivedData.getFailureCount());
        assertTrue(presenter.receivedData.getErrors().get(0).getErrorMessage().contains("Năm sản xuất"));
    }
}
