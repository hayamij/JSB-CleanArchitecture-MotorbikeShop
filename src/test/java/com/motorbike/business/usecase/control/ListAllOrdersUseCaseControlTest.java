package com.motorbike.business.usecase.control;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.motorbike.business.dto.listallorders.ListAllOrdersInputData;
import com.motorbike.business.dto.listallorders.ListAllOrdersOutputData;
import com.motorbike.business.ports.repository.OrderRepository;
import com.motorbike.business.usecase.output.ListAllOrdersOutputBoundary;
import com.motorbike.domain.entities.ChiTietDonHang;
import com.motorbike.domain.entities.DonHang;
import com.motorbike.domain.entities.TrangThaiDonHang;

/**
 * Unit Tests for ListAllOrdersUseCaseControl
 * 
 * Test Cases:
 * 1. Lấy tất cả đơn hàng thành công
 * 2. Phân trang đúng cách
 * 3. Lọc theo trạng thái
 * 4. Sắp xếp theo ngày
 * 5. Sắp xếp theo giá
 * 6. Tính tổng doanh thu
 * 7. Kết quả trống
 * 8. Validate input
 * 9. Error handling
 */
@DisplayName("List All Orders Use Case Tests")
class ListAllOrdersUseCaseControlTest {

    private ListAllOrdersUseCaseControl listAllOrdersUseCase;
    private OrderRepository orderRepository;
    private ListAllOrdersOutputBoundary outputBoundary;

    @BeforeEach
    void setUp() {
        orderRepository = mock(OrderRepository.class);
        outputBoundary = mock(ListAllOrdersOutputBoundary.class);
        listAllOrdersUseCase = new ListAllOrdersUseCaseControl(outputBoundary, orderRepository);
    }

    // ==================== 1. SUCCESS CASES ====================

    @Test
    @DisplayName("Should list all orders successfully without filter")
    void testListAllOrdersSuccess() {
        // Given
        List<DonHang> mockOrders = createMockOrders(5);
        when(orderRepository.findAll()).thenReturn(mockOrders);

        // Act
        ListAllOrdersInputData inputData = ListAllOrdersInputData.getAllOrders();
        listAllOrdersUseCase.execute(inputData);

        // Assert
        verify(orderRepository).findAll();
        
        ArgumentCaptor<ListAllOrdersOutputData> captor = 
            ArgumentCaptor.forClass(ListAllOrdersOutputData.class);
        verify(outputBoundary).present(captor.capture());
        
        ListAllOrdersOutputData output = captor.getValue();
        assertTrue(output.isSuccess());
        assertEquals(5, output.getTotalOrders());
        assertEquals(1, output.getTotalPages());
        assertNotNull(output.getTotalRevenue());
    }

    @Test
    @DisplayName("Should list all orders with pagination")
    void testListAllOrdersWithPagination() {
        // Given
        List<DonHang> mockOrders = createMockOrders(25);
        when(orderRepository.findAll()).thenReturn(mockOrders);

        // Act
        ListAllOrdersInputData inputData = 
            ListAllOrdersInputData.withPagination(1, 10);
        listAllOrdersUseCase.execute(inputData);

        // Assert
        ArgumentCaptor<ListAllOrdersOutputData> captor = 
            ArgumentCaptor.forClass(ListAllOrdersOutputData.class);
        verify(outputBoundary).present(captor.capture());
        
        ListAllOrdersOutputData output = captor.getValue();
        assertTrue(output.isSuccess());
        assertEquals(25, output.getTotalOrders());
        assertEquals(3, output.getTotalPages()); // (25 + 10 - 1) / 10 = 3
        assertEquals(10, output.getOrders().size()); // Trang 1 (index 10-19)
        assertEquals(1, output.getCurrentPage());
    }

    @Test
    @DisplayName("Should filter orders by status CHO_XAC_NHAN")
    void testListOrdersFilterByStatusPending() {
        // Given
        DonHang order1 = new DonHang(1L, "Customer 1", "0123456789", "Address 1", null);
        order1.setMaDonHang(1L);
        
        List<DonHang> mockOrders = new ArrayList<>();
        mockOrders.add(order1);
        
        when(orderRepository.findByStatus(TrangThaiDonHang.CHO_XAC_NHAN))
            .thenReturn(mockOrders);

        // Act
        ListAllOrdersInputData inputData = 
            ListAllOrdersInputData.withStatusFilter("CHO_XAC_NHAN");
        listAllOrdersUseCase.execute(inputData);

        // Assert
        verify(orderRepository).findByStatus(TrangThaiDonHang.CHO_XAC_NHAN);
        
        ArgumentCaptor<ListAllOrdersOutputData> captor = 
            ArgumentCaptor.forClass(ListAllOrdersOutputData.class);
        verify(outputBoundary).present(captor.capture());
        
        ListAllOrdersOutputData output = captor.getValue();
        assertTrue(output.isSuccess());
        assertEquals(1, output.getTotalOrders());
    }

    @Test
    @DisplayName("Should filter orders by status DA_GIAO")
    void testListOrdersFilterByStatusDelivered() {
        // Given
        List<DonHang> mockOrders = createMockOrders(8);
        when(orderRepository.findByStatus(TrangThaiDonHang.DA_GIAO))
            .thenReturn(mockOrders);

        // Act
        ListAllOrdersInputData inputData = 
            ListAllOrdersInputData.withStatusFilter("DA_GIAO");
        listAllOrdersUseCase.execute(inputData);

        // Assert
        verify(orderRepository).findByStatus(TrangThaiDonHang.DA_GIAO);
        
        ArgumentCaptor<ListAllOrdersOutputData> captor = 
            ArgumentCaptor.forClass(ListAllOrdersOutputData.class);
        verify(outputBoundary).present(captor.capture());
        
        assertTrue(captor.getValue().isSuccess());
    }

    @Test
    @DisplayName("Should sort orders by date descending (newest first)")
    void testListOrdersSortByDateDescending() {
        // Given
        List<DonHang> mockOrders = createMockOrdersWithDates();
        when(orderRepository.findAll()).thenReturn(mockOrders);

        // Act
        ListAllOrdersInputData inputData = 
            ListAllOrdersInputData.withFullFilters(0, 10, null, "date_desc");
        listAllOrdersUseCase.execute(inputData);

        // Assert
        ArgumentCaptor<ListAllOrdersOutputData> captor = 
            ArgumentCaptor.forClass(ListAllOrdersOutputData.class);
        verify(outputBoundary).present(captor.capture());
        
        ListAllOrdersOutputData output = captor.getValue();
        assertTrue(output.isSuccess());
        
        // Verify first order is newest
        List<ListAllOrdersOutputData.OrderItemData> orders = output.getOrders();
        assertNotNull(orders);
        assertTrue(orders.get(0).getOrderDate()
            .isAfter(orders.get(orders.size() - 1).getOrderDate()));
    }

    @Test
    @DisplayName("Should sort orders by amount descending (highest first)")
    void testListOrdersSortByAmountDescending() {
        // Given
        List<DonHang> mockOrders = createMockOrdersWithAmounts();
        when(orderRepository.findAll()).thenReturn(mockOrders);

        // Act
        ListAllOrdersInputData inputData = 
            ListAllOrdersInputData.withFullFilters(0, 10, null, "amount_desc");
        listAllOrdersUseCase.execute(inputData);

        // Assert
        ArgumentCaptor<ListAllOrdersOutputData> captor = 
            ArgumentCaptor.forClass(ListAllOrdersOutputData.class);
        verify(outputBoundary).present(captor.capture());
        
        ListAllOrdersOutputData output = captor.getValue();
        assertTrue(output.isSuccess());
    }

    @Test
    @DisplayName("Should calculate total revenue correctly")
    void testCalculateTotalRevenueCorrectly() {
        // Given
        List<DonHang> mockOrders = new ArrayList<>();
        
        DonHang order1 = new DonHang(1L, "Customer 1", "0123", "Address", null);
        order1.setMaDonHang(1L);
        order1.themSanPham(new ChiTietDonHang(1L, "Product 1", 
            BigDecimal.valueOf(10000000), 1));
        
        DonHang order2 = new DonHang(2L, "Customer 2", "0456", "Address", null);
        order2.setMaDonHang(2L);
        order2.themSanPham(new ChiTietDonHang(2L, "Product 2", 
            BigDecimal.valueOf(20000000), 1));
        
        mockOrders.add(order1);
        mockOrders.add(order2);
        
        when(orderRepository.findAll()).thenReturn(mockOrders);

        // Act
        ListAllOrdersInputData inputData = ListAllOrdersInputData.getAllOrders();
        listAllOrdersUseCase.execute(inputData);

        // Assert
        ArgumentCaptor<ListAllOrdersOutputData> captor = 
            ArgumentCaptor.forClass(ListAllOrdersOutputData.class);
        verify(outputBoundary).present(captor.capture());
        
        ListAllOrdersOutputData output = captor.getValue();
        assertEquals(0, BigDecimal.valueOf(30000000)
            .compareTo(output.getTotalRevenue()));
    }

    @Test
    @DisplayName("Should handle pagination correctly on last page")
    void testPaginationLastPage() {
        // Given
        List<DonHang> mockOrders = createMockOrders(25);
        when(orderRepository.findAll()).thenReturn(mockOrders);

        // Act - Trang 2 (index từ 20-24, chỉ 5 items)
        ListAllOrdersInputData inputData = 
            ListAllOrdersInputData.withPagination(2, 10);
        listAllOrdersUseCase.execute(inputData);

        // Assert
        ArgumentCaptor<ListAllOrdersOutputData> captor = 
            ArgumentCaptor.forClass(ListAllOrdersOutputData.class);
        verify(outputBoundary).present(captor.capture());
        
        ListAllOrdersOutputData output = captor.getValue();
        assertEquals(25, output.getTotalOrders());
        assertEquals(3, output.getTotalPages());
        assertEquals(5, output.getOrders().size()); // Last page
    }

    // ==================== 2. EMPTY CASES ====================

    @Test
    @DisplayName("Should return empty result when no orders found")
    void testListAllOrdersEmptyResult() {
        // Given
        when(orderRepository.findAll()).thenReturn(new ArrayList<>());

        // Act
        ListAllOrdersInputData inputData = ListAllOrdersInputData.getAllOrders();
        listAllOrdersUseCase.execute(inputData);

        // Assert
        ArgumentCaptor<ListAllOrdersOutputData> captor = 
            ArgumentCaptor.forClass(ListAllOrdersOutputData.class);
        verify(outputBoundary).present(captor.capture());
        
        ListAllOrdersOutputData output = captor.getValue();
        assertTrue(output.isSuccess());
        assertTrue(output.isEmpty());
        assertEquals(0, output.getTotalOrders());
    }

    @Test
    @DisplayName("Should return empty result when status filter returns no orders")
    void testListOrdersEmptyResultWithFilter() {
        // Given
        when(orderRepository.findByStatus(TrangThaiDonHang.DA_HUY))
            .thenReturn(new ArrayList<>());

        // Act
        ListAllOrdersInputData inputData = 
            ListAllOrdersInputData.withStatusFilter("DA_HUY");
        listAllOrdersUseCase.execute(inputData);

        // Assert
        ArgumentCaptor<ListAllOrdersOutputData> captor = 
            ArgumentCaptor.forClass(ListAllOrdersOutputData.class);
        verify(outputBoundary).present(captor.capture());
        
        ListAllOrdersOutputData output = captor.getValue();
        assertTrue(output.isSuccess());
        assertTrue(output.isEmpty());
    }

    // ==================== 3. VALIDATION CASES ====================

    @Test
    @DisplayName("Should fail with negative page")
    void testValidationFailNegativePage() {
        // Act
        ListAllOrdersInputData inputData = 
            ListAllOrdersInputData.withPagination(-1, 10);
        listAllOrdersUseCase.execute(inputData);

        // Assert
        verify(orderRepository, never()).findAll();
        
        ArgumentCaptor<ListAllOrdersOutputData> captor = 
            ArgumentCaptor.forClass(ListAllOrdersOutputData.class);
        verify(outputBoundary).present(captor.capture());
        
        ListAllOrdersOutputData output = captor.getValue();
        assertFalse(output.isSuccess());
        assertEquals("INVALID_INPUT", output.getErrorCode());
    }

    @Test
    @DisplayName("Should fail with zero page size")
    void testValidationFailZeroPageSize() {
        // Act
        ListAllOrdersInputData inputData = 
            ListAllOrdersInputData.withPagination(0, 0);
        listAllOrdersUseCase.execute(inputData);

        // Assert
        verify(orderRepository, never()).findAll();
        
        ArgumentCaptor<ListAllOrdersOutputData> captor = 
            ArgumentCaptor.forClass(ListAllOrdersOutputData.class);
        verify(outputBoundary).present(captor.capture());
        
        ListAllOrdersOutputData output = captor.getValue();
        assertFalse(output.isSuccess());
    }

    @Test
    @DisplayName("Should fail with page size greater than max (100)")
    void testValidationFailPageSizeExceedsMax() {
        // Act
        ListAllOrdersInputData inputData = 
            ListAllOrdersInputData.withPagination(0, 101);
        listAllOrdersUseCase.execute(inputData);

        // Assert
        verify(orderRepository, never()).findAll();
        
        ArgumentCaptor<ListAllOrdersOutputData> captor = 
            ArgumentCaptor.forClass(ListAllOrdersOutputData.class);
        verify(outputBoundary).present(captor.capture());
        
        ListAllOrdersOutputData output = captor.getValue();
        assertFalse(output.isSuccess());
    }

    @Test
    @DisplayName("Should fail with null input")
    void testValidationFailNullInput() {
        // Act
        listAllOrdersUseCase.execute(null);

        // Assert
        verify(orderRepository, never()).findAll();
        
        ArgumentCaptor<ListAllOrdersOutputData> captor = 
            ArgumentCaptor.forClass(ListAllOrdersOutputData.class);
        verify(outputBoundary).present(captor.capture());
        
        ListAllOrdersOutputData output = captor.getValue();
        assertFalse(output.isSuccess());
        assertEquals("INVALID_INPUT", output.getErrorCode());
    }

    // ==================== 4. EDGE CASES ====================

    @Test
    @DisplayName("Should handle page 0 with max page size (100)")
    void testEdgeCaseMaxPageSize() {
        // Given
        List<DonHang> mockOrders = createMockOrders(150);
        when(orderRepository.findAll()).thenReturn(mockOrders);

        // Act
        ListAllOrdersInputData inputData = 
            ListAllOrdersInputData.withPagination(0, 100);
        listAllOrdersUseCase.execute(inputData);

        // Assert
        ArgumentCaptor<ListAllOrdersOutputData> captor = 
            ArgumentCaptor.forClass(ListAllOrdersOutputData.class);
        verify(outputBoundary).present(captor.capture());
        
        ListAllOrdersOutputData output = captor.getValue();
        assertTrue(output.isSuccess());
        assertEquals(100, output.getOrders().size());
    }

    @Test
    @DisplayName("Should handle single order")
    void testEdgeCaseSingleOrder() {
        // Given
        List<DonHang> mockOrders = createMockOrders(1);
        when(orderRepository.findAll()).thenReturn(mockOrders);

        // Act
        ListAllOrdersInputData inputData = ListAllOrdersInputData.getAllOrders();
        listAllOrdersUseCase.execute(inputData);

        // Assert
        ArgumentCaptor<ListAllOrdersOutputData> captor = 
            ArgumentCaptor.forClass(ListAllOrdersOutputData.class);
        verify(outputBoundary).present(captor.capture());
        
        ListAllOrdersOutputData output = captor.getValue();
        assertTrue(output.isSuccess());
        assertEquals(1, output.getTotalOrders());
        assertEquals(1, output.getTotalPages());
    }

    @Test
    @DisplayName("Should verify output data structure")
    void testOutputDataStructure() {
        // Given
        List<DonHang> mockOrders = createMockOrders(3);
        when(orderRepository.findAll()).thenReturn(mockOrders);

        // Act
        ListAllOrdersInputData inputData = ListAllOrdersInputData.getAllOrders();
        listAllOrdersUseCase.execute(inputData);

        // Assert
        ArgumentCaptor<ListAllOrdersOutputData> captor = 
            ArgumentCaptor.forClass(ListAllOrdersOutputData.class);
        verify(outputBoundary).present(captor.capture());
        
        ListAllOrdersOutputData output = captor.getValue();
        assertTrue(output.isSuccess());
        assertNotNull(output.getOrders());
        assertTrue(output.getTotalOrders() > 0);
        assertTrue(output.getTotalPages() > 0);
        assertNotNull(output.getTotalRevenue());
    }

    // ==================== HELPER METHODS ====================

    /**
     * Tạo mock orders cho testing
     */
    private List<DonHang> createMockOrders(int count) {
        List<DonHang> orders = new ArrayList<>();
        
        for (int i = 1; i <= count; i++) {
            DonHang order = new DonHang(
                (long) i,
                "Customer " + i,
                "012345678" + i,
                "Address " + i,
                "Note " + i
            );
            order.setMaDonHang((long) i);
            
            // Add sample item
            ChiTietDonHang item = new ChiTietDonHang(
                (long) i,
                "Product " + i,
                BigDecimal.valueOf(1000000 * i),
                1
            );
            order.themSanPham(item);
            
            orders.add(order);
        }
        
        return orders;
    }

    /**
     * Tạo orders với dates khác nhau
     */
    private List<DonHang> createMockOrdersWithDates() {
        List<DonHang> orders = new ArrayList<>();
        
        // Create orders with explicit distinct dates to make sorting deterministic
        LocalDateTime now = LocalDateTime.now();
        DonHang order1 = new DonHang(
            1L, // maDonHang
            1L, // maTaiKhoan
            new ArrayList<>(), // items
            BigDecimal.ZERO, // tongTien
            TrangThaiDonHang.CHO_XAC_NHAN,
            "Customer 1",
            "0123",
            "Address",
            null,
            now.minusDays(2), // oldest
            now.minusDays(2)
        );

        DonHang order2 = new DonHang(
            2L,
            2L,
            new ArrayList<>(),
            BigDecimal.ZERO,
            TrangThaiDonHang.CHO_XAC_NHAN,
            "Customer 2",
            "0456",
            "Address",
            null,
            now.minusDays(1), // middle
            now.minusDays(1)
        );

        DonHang order3 = new DonHang(
            3L,
            3L,
            new ArrayList<>(),
            BigDecimal.ZERO,
            TrangThaiDonHang.CHO_XAC_NHAN,
            "Customer 3",
            "0789",
            "Address",
            null,
            now, // newest
            now
        );

        orders.add(order1); // oldest
        orders.add(order2); // middle
        orders.add(order3); // newest
        
        return orders;
    }

    /**
     * Tạo orders với amounts khác nhau
     */
    private List<DonHang> createMockOrdersWithAmounts() {
        List<DonHang> orders = new ArrayList<>();
        
        DonHang order1 = new DonHang(1L, "Customer 1", "0123", "Address", null);
        order1.setMaDonHang(1L);
        order1.themSanPham(new ChiTietDonHang(1L, "Product 1", 
            BigDecimal.valueOf(10000000), 1));
        
        DonHang order2 = new DonHang(2L, "Customer 2", "0456", "Address", null);
        order2.setMaDonHang(2L);
        order2.themSanPham(new ChiTietDonHang(2L, "Product 2", 
            BigDecimal.valueOf(50000000), 1));
        
        DonHang order3 = new DonHang(3L, "Customer 3", "0789", "Address", null);
        order3.setMaDonHang(3L);
        order3.themSanPham(new ChiTietDonHang(3L, "Product 3", 
            BigDecimal.valueOf(30000000), 1));
        
        orders.add(order1);
        orders.add(order2);
        orders.add(order3);
        
        return orders;
    }
}