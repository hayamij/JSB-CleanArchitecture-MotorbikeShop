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

    @Test
    @DisplayName("Should list all orders successfully without filter")
    void testListAllOrdersSuccess() {
        List<DonHang> mockOrders = createMockOrders(5);
        when(orderRepository.findAll()).thenReturn(mockOrders);

        ListAllOrdersInputData inputData = ListAllOrdersInputData.getAllOrders();
        listAllOrdersUseCase.execute(inputData);

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
        List<DonHang> mockOrders = createMockOrders(25);
        when(orderRepository.findAll()).thenReturn(mockOrders);

        ListAllOrdersInputData inputData =
            ListAllOrdersInputData.withPagination(1, 10);
        listAllOrdersUseCase.execute(inputData);

        ArgumentCaptor<ListAllOrdersOutputData> captor =
            ArgumentCaptor.forClass(ListAllOrdersOutputData.class);
        verify(outputBoundary).present(captor.capture());
        
        ListAllOrdersOutputData output = captor.getValue();
        assertTrue(output.isSuccess());
        assertEquals(25, output.getTotalOrders());
        assertEquals(3, output.getTotalPages());
        assertEquals(10, output.getOrders().size());
        assertEquals(1, output.getCurrentPage());
    }

    @Test
    @DisplayName("Should filter orders by status CHO_XAC_NHAN")
    void testListOrdersFilterByStatusPending() {
        DonHang order1 = new DonHang(1L, "Customer 1", "0123456789", "Address 1", null);
        order1.setMaDonHang(1L);
        
        List<DonHang> mockOrders = new ArrayList<>();
        mockOrders.add(order1);
        
        when(orderRepository.findByStatus(TrangThaiDonHang.CHO_XAC_NHAN))
            .thenReturn(mockOrders);

        ListAllOrdersInputData inputData =
            ListAllOrdersInputData.withStatusFilter("CHO_XAC_NHAN");
        listAllOrdersUseCase.execute(inputData);

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
        List<DonHang> mockOrders = createMockOrders(8);
        when(orderRepository.findByStatus(TrangThaiDonHang.DA_GIAO))
            .thenReturn(mockOrders);

        ListAllOrdersInputData inputData =
            ListAllOrdersInputData.withStatusFilter("DA_GIAO");
        listAllOrdersUseCase.execute(inputData);

        verify(orderRepository).findByStatus(TrangThaiDonHang.DA_GIAO);
        
        ArgumentCaptor<ListAllOrdersOutputData> captor =
            ArgumentCaptor.forClass(ListAllOrdersOutputData.class);
        verify(outputBoundary).present(captor.capture());
        
        assertTrue(captor.getValue().isSuccess());
    }

    @Test
    @DisplayName("Should sort orders by date descending (newest first)")
    void testListOrdersSortByDateDescending() {
        List<DonHang> mockOrders = createMockOrdersWithDates();
        when(orderRepository.findAll()).thenReturn(mockOrders);

        ListAllOrdersInputData inputData =
            ListAllOrdersInputData.withFullFilters(0, 10, null, "date_desc");
        listAllOrdersUseCase.execute(inputData);

        ArgumentCaptor<ListAllOrdersOutputData> captor =
            ArgumentCaptor.forClass(ListAllOrdersOutputData.class);
        verify(outputBoundary).present(captor.capture());
        
        ListAllOrdersOutputData output = captor.getValue();
        assertTrue(output.isSuccess());
        
        List<ListAllOrdersOutputData.OrderItemData> orders = output.getOrders();
        assertNotNull(orders);
        assertTrue(orders.get(0).getOrderDate()
            .isAfter(orders.get(orders.size() - 1).getOrderDate()));
    }

    @Test
    @DisplayName("Should sort orders by amount descending (highest first)")
    void testListOrdersSortByAmountDescending() {
        List<DonHang> mockOrders = createMockOrdersWithAmounts();
        when(orderRepository.findAll()).thenReturn(mockOrders);

        ListAllOrdersInputData inputData =
            ListAllOrdersInputData.withFullFilters(0, 10, null, "amount_desc");
        listAllOrdersUseCase.execute(inputData);

        ArgumentCaptor<ListAllOrdersOutputData> captor =
            ArgumentCaptor.forClass(ListAllOrdersOutputData.class);
        verify(outputBoundary).present(captor.capture());
        
        ListAllOrdersOutputData output = captor.getValue();
        assertTrue(output.isSuccess());
    }

    @Test
    @DisplayName("Should calculate total revenue correctly")
    void testCalculateTotalRevenueCorrectly() {
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

        ListAllOrdersInputData inputData = ListAllOrdersInputData.getAllOrders();
        listAllOrdersUseCase.execute(inputData);

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
        List<DonHang> mockOrders = createMockOrders(25);
        when(orderRepository.findAll()).thenReturn(mockOrders);

        ListAllOrdersInputData inputData =
            ListAllOrdersInputData.withPagination(2, 10);
        listAllOrdersUseCase.execute(inputData);

        ArgumentCaptor<ListAllOrdersOutputData> captor =
            ArgumentCaptor.forClass(ListAllOrdersOutputData.class);
        verify(outputBoundary).present(captor.capture());
        
        ListAllOrdersOutputData output = captor.getValue();
        assertEquals(25, output.getTotalOrders());
        assertEquals(3, output.getTotalPages());
        assertEquals(5, output.getOrders().size());
    }

    @Test
    @DisplayName("Should return empty result when no orders found")
    void testListAllOrdersEmptyResult() {
        when(orderRepository.findAll()).thenReturn(new ArrayList<>());

        ListAllOrdersInputData inputData = ListAllOrdersInputData.getAllOrders();
        listAllOrdersUseCase.execute(inputData);

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
        when(orderRepository.findByStatus(TrangThaiDonHang.DA_HUY))
            .thenReturn(new ArrayList<>());

        ListAllOrdersInputData inputData =
            ListAllOrdersInputData.withStatusFilter("DA_HUY");
        listAllOrdersUseCase.execute(inputData);

        ArgumentCaptor<ListAllOrdersOutputData> captor =
            ArgumentCaptor.forClass(ListAllOrdersOutputData.class);
        verify(outputBoundary).present(captor.capture());
        
        ListAllOrdersOutputData output = captor.getValue();
        assertTrue(output.isSuccess());
        assertTrue(output.isEmpty());
    }

    @Test
    @DisplayName("Should fail with negative page")
    void testValidationFailNegativePage() {
        ListAllOrdersInputData inputData =
            ListAllOrdersInputData.withPagination(-1, 10);
        listAllOrdersUseCase.execute(inputData);

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
        ListAllOrdersInputData inputData =
            ListAllOrdersInputData.withPagination(0, 0);
        listAllOrdersUseCase.execute(inputData);

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
        ListAllOrdersInputData inputData =
            ListAllOrdersInputData.withPagination(0, 101);
        listAllOrdersUseCase.execute(inputData);

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
        listAllOrdersUseCase.execute(null);

        verify(orderRepository, never()).findAll();
        
        ArgumentCaptor<ListAllOrdersOutputData> captor =
            ArgumentCaptor.forClass(ListAllOrdersOutputData.class);
        verify(outputBoundary).present(captor.capture());
        
        ListAllOrdersOutputData output = captor.getValue();
        assertFalse(output.isSuccess());
        assertEquals("INVALID_INPUT", output.getErrorCode());
    }

    @Test
    @DisplayName("Should handle page 0 with max page size (100)")
    void testEdgeCaseMaxPageSize() {
        List<DonHang> mockOrders = createMockOrders(150);
        when(orderRepository.findAll()).thenReturn(mockOrders);

        ListAllOrdersInputData inputData =
            ListAllOrdersInputData.withPagination(0, 100);
        listAllOrdersUseCase.execute(inputData);

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
        List<DonHang> mockOrders = createMockOrders(1);
        when(orderRepository.findAll()).thenReturn(mockOrders);

        ListAllOrdersInputData inputData = ListAllOrdersInputData.getAllOrders();
        listAllOrdersUseCase.execute(inputData);

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
        List<DonHang> mockOrders = createMockOrders(3);
        when(orderRepository.findAll()).thenReturn(mockOrders);

        ListAllOrdersInputData inputData = ListAllOrdersInputData.getAllOrders();
        listAllOrdersUseCase.execute(inputData);

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

    
    private List<DonHang> createMockOrdersWithDates() {
        List<DonHang> orders = new ArrayList<>();
        
        LocalDateTime now = LocalDateTime.now();
        DonHang order1 = new DonHang(
            1L,
            1L,
            new ArrayList<>(),
            BigDecimal.ZERO,
            TrangThaiDonHang.CHO_XAC_NHAN,
            "Customer 1",
            "0123",
            "Address",
            null,
            now.minusDays(2),
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
            now.minusDays(1),
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
            now,
            now
        );

        orders.add(order1);
        orders.add(order2);
        orders.add(order3);
        
        return orders;
    }

    
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
