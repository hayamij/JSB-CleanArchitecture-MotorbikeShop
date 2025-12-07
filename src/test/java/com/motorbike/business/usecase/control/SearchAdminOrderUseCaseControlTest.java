package com.motorbike.business.usecase.control;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.motorbike.business.dto.searchadminorder.SearchAdminOrderInputData;
import com.motorbike.business.dto.searchadminorder.SearchAdminOrderOutputData;
import com.motorbike.business.ports.repository.OrderRepository;
import com.motorbike.business.usecase.output.SearchAdminOrderOutputBoundary;
import com.motorbike.domain.entities.ChiTietDonHang;
import com.motorbike.domain.entities.DonHang;
import com.motorbike.domain.entities.TrangThaiDonHang;

class SearchAdminOrderUseCaseControlTest {

    private SearchAdminOrderUseCaseControl useCase;
    private SearchAdminOrderOutputBoundary outputBoundary;
    private OrderRepository orderRepository;

    @BeforeEach
    void setUp() {
        outputBoundary = mock(SearchAdminOrderOutputBoundary.class);
        orderRepository = mock(OrderRepository.class);
        useCase = new SearchAdminOrderUseCaseControl(outputBoundary, orderRepository);
    }

    @Test
    void testSearchOrder_WithValidAdminQuery_ShouldReturnMatchingOrders() {
        // Arrange
        String searchQuery = "nguyen";
        
        DonHang order1 = createTestOrder(1L, 1L, "Nguyen Van A", "0123456789", TrangThaiDonHang.CHO_XAC_NHAN);
        DonHang order2 = createTestOrder(2L, 2L, "Tran Van B", "0987654321", TrangThaiDonHang.DA_XAC_NHAN);
        DonHang order3 = createTestOrder(3L, 3L, "Nguyen Thi C", "0123999888", TrangThaiDonHang.DANG_GIAO);
        
        when(orderRepository.searchForAdmin(searchQuery)).thenReturn(Arrays.asList(order1, order3));
        
        SearchAdminOrderInputData inputData = SearchAdminOrderInputData.forAdmin(searchQuery);
        
        // Act
        useCase.execute(inputData);
        
        // Assert
        ArgumentCaptor<SearchAdminOrderOutputData> captor = ArgumentCaptor.forClass(SearchAdminOrderOutputData.class);
        verify(outputBoundary).present(captor.capture());
        
        SearchAdminOrderOutputData result = captor.getValue();
        assertTrue(result.isSuccess());
        assertEquals(2, result.getOrders().size());
    }

    @Test
    void testSearchOrder_WithEmptyQuery_ShouldReturnValidationError() {
        // Arrange
        SearchAdminOrderInputData inputData = SearchAdminOrderInputData.forAdmin("");
        
        // Act
        useCase.execute(inputData);
        
        // Assert
        ArgumentCaptor<SearchAdminOrderOutputData> captor = ArgumentCaptor.forClass(SearchAdminOrderOutputData.class);
        verify(outputBoundary).present(captor.capture());
        
        SearchAdminOrderOutputData result = captor.getValue();
        assertFalse(result.isSuccess());
        assertNotNull(result.getErrorCode());
    }

    @Test
    void testSearchOrder_WithNullQuery_ShouldReturnValidationError() {
        // Arrange
        SearchAdminOrderInputData inputData = SearchAdminOrderInputData.forAdmin(null);
        
        // Act
        useCase.execute(inputData);
        
        // Assert
        ArgumentCaptor<SearchAdminOrderOutputData> captor = ArgumentCaptor.forClass(SearchAdminOrderOutputData.class);
        verify(outputBoundary).present(captor.capture());
        
        SearchAdminOrderOutputData result = captor.getValue();
        assertFalse(result.isSuccess());
        assertNotNull(result.getErrorCode());
    }

    @Test
    void testSearchOrder_NoMatches_ShouldReturnEmptyList() {
        // Arrange
        String searchQuery = "xyz123notfound";
        
        when(orderRepository.searchForAdmin(searchQuery)).thenReturn(List.of());
        
        SearchAdminOrderInputData inputData = SearchAdminOrderInputData.forAdmin(searchQuery);
        
        // Act
        useCase.execute(inputData);
        
        // Assert
        ArgumentCaptor<SearchAdminOrderOutputData> captor = ArgumentCaptor.forClass(SearchAdminOrderOutputData.class);
        verify(outputBoundary).present(captor.capture());
        
        SearchAdminOrderOutputData result = captor.getValue();
        assertTrue(result.isSuccess());
        assertEquals(0, result.getOrders().size());
    }

    @Test
    void testSearchOrder_ByPhoneNumber_ShouldReturnMatchingOrders() {
        // Arrange
        String searchQuery = "0123";
        
        DonHang order1 = createTestOrder(1L, 1L, "Nguyen Van A", "0123456789", TrangThaiDonHang.CHO_XAC_NHAN);
        DonHang order2 = createTestOrder(2L, 2L, "Tran Van B", "0987654321", TrangThaiDonHang.DA_XAC_NHAN);
        DonHang order3 = createTestOrder(3L, 3L, "Le Van C", "0123999888", TrangThaiDonHang.DANG_GIAO);
        
        when(orderRepository.searchForAdmin(searchQuery)).thenReturn(Arrays.asList(order1, order3));
        
        SearchAdminOrderInputData inputData = SearchAdminOrderInputData.forAdmin(searchQuery);
        
        // Act
        useCase.execute(inputData);
        
        // Assert
        ArgumentCaptor<SearchAdminOrderOutputData> captor = ArgumentCaptor.forClass(SearchAdminOrderOutputData.class);
        verify(outputBoundary).present(captor.capture());
        
        SearchAdminOrderOutputData result = captor.getValue();
        assertTrue(result.isSuccess());
        assertEquals(2, result.getOrders().size());
    }

    @Test
    void testSearchOrder_ByProductName_ShouldReturnMatchingOrders() {
        // Arrange
        String searchQuery = "honda";
        
        List<ChiTietDonHang> items = new ArrayList<>();
        ChiTietDonHang item = new ChiTietDonHang(1L, 1L, 1L, "Honda Wave", new BigDecimal("20000000"), 1, new BigDecimal("20000000"));
        items.add(item);
        
        DonHang order1 = new DonHang(
            1L,
            1L,
            items,
            new BigDecimal("20000000"),
            TrangThaiDonHang.CHO_XAC_NHAN,
            "Nguyen Van A",
            "0123456789",
            "123 Test Street",
            "Test note",
            LocalDateTime.now(),
            LocalDateTime.now()
        );
        
        DonHang order2 = createTestOrder(2L, 2L, "Tran Van B", "0987654321", TrangThaiDonHang.DA_XAC_NHAN);
        
        when(orderRepository.searchForAdmin(searchQuery)).thenReturn(Arrays.asList(order1));
        
        SearchAdminOrderInputData inputData = SearchAdminOrderInputData.forAdmin(searchQuery);
        
        // Act
        useCase.execute(inputData);
        
        // Assert
        ArgumentCaptor<SearchAdminOrderOutputData> captor = ArgumentCaptor.forClass(SearchAdminOrderOutputData.class);
        verify(outputBoundary).present(captor.capture());
        
        SearchAdminOrderOutputData result = captor.getValue();
        assertTrue(result.isSuccess());
        assertEquals(1, result.getOrders().size());
    }

    @Test
    void testSearchOrder_ByStatus_ShouldReturnMatchingOrders() {
        // Arrange
        String searchQuery = "cho_xac_nhan";
        
        DonHang order1 = createTestOrder(1L, 1L, "Nguyen Van A", "0123456789", TrangThaiDonHang.CHO_XAC_NHAN);
        DonHang order2 = createTestOrder(2L, 2L, "Tran Van B", "0987654321", TrangThaiDonHang.DA_XAC_NHAN);
        DonHang order3 = createTestOrder(3L, 3L, "Le Van C", "0123999888", TrangThaiDonHang.CHO_XAC_NHAN);
        
        when(orderRepository.searchForAdmin(searchQuery)).thenReturn(Arrays.asList(order1, order3));
        
        SearchAdminOrderInputData inputData = SearchAdminOrderInputData.forAdmin(searchQuery);
        
        // Act
        useCase.execute(inputData);
        
        // Assert
        ArgumentCaptor<SearchAdminOrderOutputData> captor = ArgumentCaptor.forClass(SearchAdminOrderOutputData.class);
        verify(outputBoundary).present(captor.capture());
        
        SearchAdminOrderOutputData result = captor.getValue();
        assertTrue(result.isSuccess());
        assertEquals(2, result.getOrders().size());
    }

    @Test
    void testSearchOrder_ByOrderId_ShouldReturnMatchingOrders() {
        // Arrange
        String searchQuery = "1";
        
        DonHang order1 = createTestOrder(1L, 1L, "Nguyen Van A", "0923456789", TrangThaiDonHang.CHO_XAC_NHAN);
        DonHang order2 = createTestOrder(2L, 2L, "Tran Van B", "0987654322", TrangThaiDonHang.DA_XAC_NHAN);
        DonHang order3 = createTestOrder(10L, 3L, "Le Van C", "0923999888", TrangThaiDonHang.DANG_GIAO);
        
        when(orderRepository.searchForAdmin(searchQuery)).thenReturn(Arrays.asList(order1, order2, order3));
        
        SearchAdminOrderInputData inputData = SearchAdminOrderInputData.forAdmin(searchQuery);
        
        // Act
        useCase.execute(inputData);
        
        // Assert
        ArgumentCaptor<SearchAdminOrderOutputData> captor = ArgumentCaptor.forClass(SearchAdminOrderOutputData.class);
        verify(outputBoundary).present(captor.capture());
        
        SearchAdminOrderOutputData result = captor.getValue();
        assertTrue(result.isSuccess());
        assertEquals(3, result.getOrders().size()); // Order 1 (id=1, userId=1), 10 (id contains 1), phone contains 1
    }

    @Test
    void testSearchOrder_ByCustomerId_ShouldReturnMatchingOrders() {
        // Arrange
        String searchQuery = "2";
        
        DonHang order1 = createTestOrder(1L, 1L, "Nguyen Van A", "0123456789", TrangThaiDonHang.CHO_XAC_NHAN);
        DonHang order2 = createTestOrder(2L, 2L, "Tran Van B", "0987654321", TrangThaiDonHang.DA_XAC_NHAN);
        DonHang order3 = createTestOrder(3L, 20L, "Le Van C", "0123999888", TrangThaiDonHang.DANG_GIAO);
        
        when(orderRepository.searchForAdmin(searchQuery)).thenReturn(Arrays.asList(order1, order2, order3));
        
        SearchAdminOrderInputData inputData = SearchAdminOrderInputData.forAdmin(searchQuery);
        
        // Act
        useCase.execute(inputData);
        
        // Assert
        ArgumentCaptor<SearchAdminOrderOutputData> captor = ArgumentCaptor.forClass(SearchAdminOrderOutputData.class);
        verify(outputBoundary).present(captor.capture());
        
        SearchAdminOrderOutputData result = captor.getValue();
        assertTrue(result.isSuccess());
        assertEquals(3, result.getOrders().size()); // All contain "2" in order/customer id or phone
    }

    private DonHang createTestOrder(Long orderId, Long userId, String receiverName, 
                                    String phone, TrangThaiDonHang status) {
        DonHang order = new DonHang(
            orderId,
            userId,
            new ArrayList<>(),
            new BigDecimal("50000000"),
            status,
            receiverName,
            phone,
            "123 Test Street",
            "Test note",
            LocalDateTime.now(),
            LocalDateTime.now()
        );
        return order;
    }
}
