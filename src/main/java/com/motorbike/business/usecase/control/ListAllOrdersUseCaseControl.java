package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.listallorders.ListAllOrdersInputData;
import com.motorbike.business.dto.listallorders.ListAllOrdersOutputData;
import com.motorbike.business.dto.sortorders.SortOrdersByDateInputData;
import com.motorbike.business.dto.formatordersforlist.FormatOrdersForListInputData;
import com.motorbike.business.ports.repository.OrderRepository;
import com.motorbike.business.usecase.output.ListAllOrdersOutputBoundary;
import com.motorbike.business.usecase.input.SortOrdersByDateInputBoundary;
import com.motorbike.business.usecase.input.FormatOrdersForListInputBoundary;
import com.motorbike.domain.entities.DonHang;
import com.motorbike.domain.exceptions.ValidationException;
import com.motorbike.domain.exceptions.SystemException;
import com.motorbike.business.usecase.input.ListAllOrdersInputBoundary;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ListAllOrdersUseCaseControl implements ListAllOrdersInputBoundary {
    
    private final ListAllOrdersOutputBoundary outputBoundary;
    private final OrderRepository orderRepository;
    private final SortOrdersByDateInputBoundary sortOrdersUseCase;
    private final FormatOrdersForListInputBoundary formatOrdersUseCase;

    public ListAllOrdersUseCaseControl(
            ListAllOrdersOutputBoundary outputBoundary,
            OrderRepository orderRepository,
            SortOrdersByDateInputBoundary sortOrdersUseCase,
            FormatOrdersForListInputBoundary formatOrdersUseCase) {
        this.outputBoundary = outputBoundary;
        this.orderRepository = orderRepository;
        this.sortOrdersUseCase = sortOrdersUseCase;
        this.formatOrdersUseCase = formatOrdersUseCase;
    }

    // Backward compatibility constructor
    public ListAllOrdersUseCaseControl(
            ListAllOrdersOutputBoundary outputBoundary,
            OrderRepository orderRepository) {
        this.outputBoundary = outputBoundary;
        this.orderRepository = orderRepository;
        this.sortOrdersUseCase = new SortOrdersByDateUseCaseControl(null);
        this.formatOrdersUseCase = new FormatOrdersForListUseCaseControl(null);
    }

    public void execute(ListAllOrdersInputData inputData) {
        ListAllOrdersOutputData outputData = null;
        Exception errorException = null;

        try {
            if (inputData == null) {
                throw ValidationException.invalidInput();
            }
        } catch (Exception e) {
            errorException = e;
        }

        List<DonHang> allOrders = null;
        if (errorException == null) {
            try {
                // Step 1: Get all orders from repository
                allOrders = orderRepository.findAll();
            } catch (Exception e) {
                errorException = e;
            }
        }

        if (errorException == null && allOrders != null) {
            try {
                // Step 2: UC-79 Sort orders by date (newest first)
                SortOrdersByDateInputData sortInput = new SortOrdersByDateInputData(allOrders, true);
                var sortResult = ((SortOrdersByDateUseCaseControl) sortOrdersUseCase).sortInternal(sortInput);
                
                if (!sortResult.isSuccess()) {
                    throw new SystemException(sortResult.getErrorMessage(), sortResult.getErrorCode());
                }
                
                // Step 3: UC-81 Format orders for list display
                FormatOrdersForListInputData formatInput = new FormatOrdersForListInputData(sortResult.getSortedOrders());
                var formatResult = ((FormatOrdersForListUseCaseControl) formatOrdersUseCase).formatInternal(formatInput);
                
                if (!formatResult.isSuccess()) {
                    throw new SystemException(formatResult.getErrorMessage(), formatResult.getErrorCode());
                }
                
                // Convert FormatOrdersForListOutputData.OrderListItem to ListAllOrdersOutputData.OrderItemData
                List<ListAllOrdersOutputData.OrderItemData> orderItems = formatResult.getOrderItems().stream()
                        .map(item -> new ListAllOrdersOutputData.OrderItemData(
                                item.getOrderId(),
                                Long.parseLong(item.getUserId()),
                                item.getReceiverName(),
                                item.getPhoneNumber(),
                                item.getShippingAddress(),
                                item.getStatus(),
                                item.getTotalAmount(),
                                item.getProductCount(),
                                item.getTotalQuantity(),
                                item.getOrderDate(),
                                item.getNote(),
                                item.getPaymentMethod()
                        ))
                        .collect(Collectors.toList());

                outputData = ListAllOrdersOutputData.forSuccess(orderItems);
            } catch (Exception e) {
                errorException = e;
            }
        }

        if (errorException != null) {
            String errorCode = "SYSTEM_ERROR";
            String message = errorException.getMessage();

            if (errorException instanceof ValidationException) {
                errorCode = ((ValidationException) errorException).getErrorCode();
            }

            outputData = ListAllOrdersOutputData.forError(errorCode, message);
        }

        outputBoundary.present(outputData);
    }
}
