package com.motorbike.business.usecase.impl;


import com.motorbike.domain.entities.ProductCategoryRegistry;
import com.motorbike.adapters.presenters.ProductDetailPresenter;
import com.motorbike.adapters.viewmodels.ProductDetailViewModel;
import com.motorbike.business.dto.productdetail.GetProductDetailInputData;
import com.motorbike.business.dto.productdetail.GetProductDetailOutputData;
import com.motorbike.business.ports.repository.ProductRepository;
import com.motorbike.business.usecase.GetProductDetailInputBoundary;
import com.motorbike.business.usecase.GetProductDetailOutputBoundary;
import com.motorbike.domain.entities.Product;
import com.motorbike.domain.entities.ProductCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration Test for Get Product Detail Use Case
 * Tests the complete flow: Use Case → Presenter → ViewModel
 */
@DisplayName("Get Product Detail Use Case Tests")
class GetProductDetailUseCaseImplTest {

    private GetProductDetailInputBoundary useCase;
    private ProductDetailViewModel viewModel;
    private ProductDetailPresenter presenter;
    private MockProductRepository mockRepository;

    @BeforeEach
    void setUp() {
        // Setup test dependencies
        mockRepository = new MockProductRepository();
        viewModel = new ProductDetailViewModel();
        presenter = new ProductDetailPresenter(viewModel);
        useCase = new GetProductDetailUseCaseImpl(presenter, mockRepository);
    }

    @Test
    @DisplayName("Should successfully get product detail when product exists")
    void testGetProductDetail_Success() {
        // Arrange
        Long productId = 1L;
        Product testProduct = new Product(
            productId,
            "Honda Wave RSX",
            "Xe số tiết kiệm nhiên liệu",
            new BigDecimal("38000000"),
            "/images/honda-wave-rsx.jpg",
            "{\"engine\":\"110cc\"}",
            ProductCategoryRegistry.motorcycle(),
            15,
            true,
            LocalDateTime.now(),
            LocalDateTime.now()
        );
        mockRepository.setProduct(testProduct);
        
        GetProductDetailInputData inputData = new GetProductDetailInputData(productId);

        // Act
        useCase.execute(inputData);

        // Assert
        assertFalse(viewModel.hasError, "Should not have error");
        assertEquals("1", viewModel.productId);
        assertEquals("Honda Wave RSX", viewModel.name);
        assertEquals("Xe số tiết kiệm nhiên liệu", viewModel.description);
        assertNotNull(viewModel.formattedPrice);
        assertTrue(viewModel.formattedPrice.contains("38"), "Price should contain 38 (million)");
        assertEquals("/images/honda-wave-rsx.jpg", viewModel.imageUrl);
        assertEquals("Xe máy", viewModel.categoryDisplay);
        assertEquals("Còn hàng", viewModel.availabilityStatus);
        assertEquals("GREEN", viewModel.stockStatusColor);
    }

    @Test
    @DisplayName("Should return error when product not found")
    void testGetProductDetail_ProductNotFound() {
        // Arrange
        Long productId = 999L;
        mockRepository.setProduct(null); // No product
        GetProductDetailInputData inputData = new GetProductDetailInputData(productId);

        // Act
        useCase.execute(inputData);

        // Assert
        assertTrue(viewModel.hasError, "Should have error");
        assertNotNull(viewModel.errorMessage);
        assertTrue(viewModel.errorMessage.contains("Không tìm thấy sản phẩm"), 
            "Error message should indicate product not found");
        assertEquals("RED", viewModel.errorColor);
    }

    @Test
    @DisplayName("Should return error when product ID is null")
    void testGetProductDetail_NullProductId() {
        // Arrange
        GetProductDetailInputData inputData = new GetProductDetailInputData(null);

        // Act
        useCase.execute(inputData);

        // Assert
        assertTrue(viewModel.hasError, "Should have error");
        assertNotNull(viewModel.errorMessage);
        assertTrue(viewModel.errorMessage.contains("không hợp lệ"), 
            "Error message should indicate invalid input");
    }

    @Test
    @DisplayName("Should show out of stock when stock quantity is 0")
    void testGetProductDetail_OutOfStock() {
        // Arrange
        Long productId = 2L;
        Product testProduct = new Product(
            productId,
            "Yamaha Exciter",
            "Xe côn tay",
            new BigDecimal("47000000"),
            "/images/exciter.jpg",
            "{}",
            ProductCategoryRegistry.motorcycle(),
            0, // Out of stock
            true,
            LocalDateTime.now(),
            LocalDateTime.now()
        );
        mockRepository.setProduct(testProduct);
        GetProductDetailInputData inputData = new GetProductDetailInputData(productId);

        // Act
        useCase.execute(inputData);

        // Assert
        assertFalse(viewModel.hasError);
        assertEquals("Hết hàng", viewModel.availabilityStatus);
        assertEquals("ORANGE", viewModel.stockStatusColor);
    }

    @Test
    @DisplayName("Should show unavailable when product is not available")
    void testGetProductDetail_NotAvailable() {
        // Arrange
        Long productId = 3L;
        Product testProduct = new Product(
            productId,
            "Test Product",
            "Description",
            new BigDecimal("1000000"),
            "/images/test.jpg",
            "{}",
            ProductCategoryRegistry.accessory(),
            5,
            false, // Not available
            LocalDateTime.now(),
            LocalDateTime.now()
        );
        mockRepository.setProduct(testProduct);
        GetProductDetailInputData inputData = new GetProductDetailInputData(productId);

        // Act
        useCase.execute(inputData);

        // Assert
        assertFalse(viewModel.hasError);
        assertEquals("Không có sẵn", viewModel.availabilityStatus);
        assertEquals("RED", viewModel.stockStatusColor);
    }

    @Test
    @DisplayName("Should format accessory category correctly")
    void testGetProductDetail_AccessoryCategory() {
        // Arrange
        Long productId = 4L;
        Product testProduct = new Product(
            productId,
            "Mũ bảo hiểm Royal",
            "Mũ fullface",
            new BigDecimal("850000"),
            "/images/helmet.jpg",
            "{}",
            ProductCategoryRegistry.accessory(),
            50,
            true,
            LocalDateTime.now(),
            LocalDateTime.now()
        );
        mockRepository.setProduct(testProduct);
        GetProductDetailInputData inputData = new GetProductDetailInputData(productId);

        // Act
        useCase.execute(inputData);

        // Assert
        assertFalse(viewModel.hasError);
        assertEquals("Phụ kiện", viewModel.categoryDisplay);
    }

    @Test
    @DisplayName("Should show warning when stock is low (< 5)")
    void testGetProductDetail_LowStock() {
        // Arrange
        Long productId = 5L;
        Product testProduct = new Product(
            productId,
            "Găng tay",
            "Găng tay đi xe",
            new BigDecimal("350000"),
            "/images/gloves.jpg",
            "{}",
            ProductCategoryRegistry.accessory(),
            3, // Low stock
            true,
            LocalDateTime.now(),
            LocalDateTime.now()
        );
        mockRepository.setProduct(testProduct);
        GetProductDetailInputData inputData = new GetProductDetailInputData(productId);

        // Act
        useCase.execute(inputData);

        // Assert
        assertFalse(viewModel.hasError);
        assertTrue(viewModel.stockQuantity.contains("Sắp hết"), 
            "Should show low stock warning");
    }

    @Test
    @DisplayName("Should handle null description gracefully")
    void testGetProductDetail_NullDescription() {
        // Arrange
        Long productId = 6L;
        Product testProduct = new Product(
            productId,
            "Product without description",
            null, // Null description
            new BigDecimal("1000000"),
            "/images/test.jpg",
            "{}",
            ProductCategoryRegistry.motorcycle(),
            10,
            true,
            LocalDateTime.now(),
            LocalDateTime.now()
        );
        mockRepository.setProduct(testProduct);
        GetProductDetailInputData inputData = new GetProductDetailInputData(productId);

        // Act
        useCase.execute(inputData);

        // Assert
        assertFalse(viewModel.hasError);
        assertEquals("No description available", viewModel.description);
    }

    @Test
    @DisplayName("Should handle null image URL gracefully")
    void testGetProductDetail_NullImageUrl() {
        // Arrange
        Long productId = 7L;
        Product testProduct = new Product(
            productId,
            "Product without image",
            "Description",
            new BigDecimal("1000000"),
            null, // Null image
            "{}",
            ProductCategoryRegistry.motorcycle(),
            10,
            true,
            LocalDateTime.now(),
            LocalDateTime.now()
        );
        mockRepository.setProduct(testProduct);
        GetProductDetailInputData inputData = new GetProductDetailInputData(productId);

        // Act
        useCase.execute(inputData);

        // Assert
        assertFalse(viewModel.hasError);
        assertEquals("/images/no-image.jpg", viewModel.imageUrl);
    }

    /**
     * Mock Product Repository for testing
     * Implements ProductRepository interface with in-memory data
     */
    private static class MockProductRepository implements ProductRepository {
        private Product product;

        public void setProduct(Product product) {
            this.product = product;
        }

        @Override
        public Optional<Product> findById(Long productId) {
            return Optional.ofNullable(product);
        }

        @Override
        public Product save(Product product) {
            this.product = product;
            return product;
        }

        @Override
        public boolean existsById(Long productId) {
            return product != null && product.getId().equals(productId);
        }
    }

    /**
     * Mock Output Boundary for direct testing (alternative approach)
     */
    private static class MockOutputBoundary implements GetProductDetailOutputBoundary {
        public GetProductDetailOutputData capturedOutput;
        public int presentCallCount = 0;

        @Override
        public void present(GetProductDetailOutputData outputData) {
            this.capturedOutput = outputData;
            this.presentCallCount++;
        }
    }

    @Test
    @DisplayName("Should call presenter exactly once")
    void testGetProductDetail_PresenterCalledOnce() {
        // Arrange
        MockOutputBoundary mockPresenter = new MockOutputBoundary();
        GetProductDetailInputBoundary testUseCase = 
            new GetProductDetailUseCaseImpl(mockPresenter, mockRepository);
        
        Product testProduct = new Product(
            1L, "Test", "Desc", new BigDecimal("1000000"),
            "/img.jpg", "{}", ProductCategoryRegistry.motorcycle(),
            10, true, LocalDateTime.now(), LocalDateTime.now()
        );
        mockRepository.setProduct(testProduct);
        GetProductDetailInputData inputData = new GetProductDetailInputData(1L);

        // Act
        testUseCase.execute(inputData);

        // Assert
        assertEquals(1, mockPresenter.presentCallCount, "Presenter should be called exactly once");
        assertNotNull(mockPresenter.capturedOutput, "Output data should not be null");
        assertTrue(mockPresenter.capturedOutput.success, "Output should indicate success");
    }
}
