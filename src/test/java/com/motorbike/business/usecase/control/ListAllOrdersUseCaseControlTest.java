package com.motorbike.business.usecase.control;

import java.math.BigDecimal;
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
import com.motorbike.domain.entities.PhuongThucThanhToan;

@DisplayName("List All Orders Use Case Tests (no pagination/status/sort/revenue)")
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
    @DisplayName("Should list all orders successfully and return all items")
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
        assertNotNull(output.getOrders());
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
        assertEquals(0, output.getOrders().size());
    }

    @Test
    @DisplayName("Should calculate total amount by summing returned order items")
    void testCalculateTotalAmountFromOrderItems() {
        List<DonHang> mockOrders = new ArrayList<>();
        
        DonHang order1 = new DonHang(1L, "Customer 1", "0123", "Address", null, PhuongThucThanhToan.THANH_TOAN_TRUC_TIEP);
        order1.setMaDonHang(1L);
        order1.themSanPham(new ChiTietDonHang(1L, "Product 1",
            BigDecimal.valueOf(10000000), 1));
        
        DonHang order2 = new DonHang(2L, "Customer 2", "0456", "Address", null, PhuongThucThanhToan.THANH_TOAN_TRUC_TIEP);
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
        // Sum total amounts from returned order items
        BigDecimal sum = BigDecimal.ZERO;
        for (ListAllOrdersOutputData.OrderItemData oi : output.getOrders()) {
            sum = sum.add(oi.getTotalAmount());
        }
        assertEquals(0, BigDecimal.valueOf(30000000).compareTo(sum));
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
        assertEquals(1, output.getOrders().size());
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
        assertTrue(output.getOrders().size() > 0);
    }

    
    private List<DonHang> createMockOrders(int count) {
        List<DonHang> orders = new ArrayList<>();
        
        for (int i = 1; i <= count; i++) {
            DonHang order = new DonHang(
                (long) i,
                "Customer " + i,
                "012345678" + i,
                "Address " + i,
                "Note " + i,
                PhuongThucThanhToan.THANH_TOAN_TRUC_TIEP
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
}
