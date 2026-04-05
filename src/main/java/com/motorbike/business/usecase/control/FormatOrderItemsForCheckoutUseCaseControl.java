package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.formatorderitems.FormatOrderItemsForCheckoutInputData;
import com.motorbike.business.dto.formatorderitems.FormatOrderItemsForCheckoutOutputData;
import com.motorbike.business.dto.formatorderitems.FormatOrderItemsForCheckoutOutputData.OrderItemDisplay;
import com.motorbike.business.usecase.input.FormatOrderItemsForCheckoutInputBoundary;
import com.motorbike.business.usecase.output.FormatOrderItemsForCheckoutOutputBoundary;
import com.motorbike.domain.entities.ChiTietDonHang;

import java.util.List;
import java.util.stream.Collectors;

/**
 * UC-82: Format Order Items For Checkout Use Case
 * Responsibility: Format ChiTietDonHang entities into OrderItemDisplay DTOs for checkout response
 * - Maps product ID, name, price, quantity, subtotal
 * - Simple formatting without business logic
 */
public class FormatOrderItemsForCheckoutUseCaseControl implements FormatOrderItemsForCheckoutInputBoundary {
    
    private final FormatOrderItemsForCheckoutOutputBoundary outputBoundary;
    
    public FormatOrderItemsForCheckoutUseCaseControl(FormatOrderItemsForCheckoutOutputBoundary outputBoundary) {
        this.outputBoundary = outputBoundary;
    }
    
    @Override
    public FormatOrderItemsForCheckoutOutputData execute(FormatOrderItemsForCheckoutInputData inputData) {
        if (outputBoundary != null) {
            FormatOrderItemsForCheckoutOutputData outputData = formatInternal(inputData);
            outputBoundary.present(outputData);
            return outputData;
        } else {
            return formatInternal(inputData);
        }
    }
    
    /**
     * Internal method for direct delegation from other usecases
     * Returns output data without going through presenter
     */
    public FormatOrderItemsForCheckoutOutputData formatInternal(FormatOrderItemsForCheckoutInputData inputData) {
        try {
            if (inputData == null || inputData.getOrderItems() == null) {
                return FormatOrderItemsForCheckoutOutputData.forError("INVALID_INPUT", "Order items cannot be null");
            }
            
            List<OrderItemDisplay> formattedItems = inputData.getOrderItems().stream()
                    .map(item -> new OrderItemDisplay(
                            String.valueOf(item.getMaSanPham()),
                            item.getTenSanPham(),
                            item.getGiaBan(),
                            item.getSoLuong(),
                            item.getThanhTien()
                    ))
                    .collect(Collectors.toList());
            
            return FormatOrderItemsForCheckoutOutputData.forSuccess(formattedItems);
            
        } catch (Exception e) {
            return FormatOrderItemsForCheckoutOutputData.forError("FORMAT_ERROR", e.getMessage());
        }
    }
}
