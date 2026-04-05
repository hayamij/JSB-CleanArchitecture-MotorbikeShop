package com.motorbike.adapters.presenters;

import com.motorbike.business.dto.order.GetOrderDetailOutputData;
import com.motorbike.business.dto.order.GetOrderDetailOutputData.OrderDetailInfo;
import com.motorbike.business.dto.order.GetOrderDetailOutputData.OrderItemInfo;
import com.motorbike.business.usecase.output.GetOrderDetailOutputBoundary;
import com.motorbike.adapters.viewmodels.GetOrderDetailViewModel;
import com.motorbike.adapters.viewmodels.GetOrderDetailViewModel.OrderDetailDisplay;
import com.motorbike.adapters.viewmodels.GetOrderDetailViewModel.OrderItemDisplay;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class GetOrderDetailPresenter implements GetOrderDetailOutputBoundary {

    private final GetOrderDetailViewModel viewModel;
    private final DateTimeFormatter dateFormatter;

    public GetOrderDetailPresenter(GetOrderDetailViewModel viewModel) {
        this.viewModel = viewModel;
        this.dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    }

    @Override
    public void present(GetOrderDetailOutputData outputData) {
        if (outputData.isSuccess()) {
            viewModel.success = true;
            viewModel.orderDetail = mapToViewModel(outputData.getOrderDetail());
            viewModel.errorCode = null;
            viewModel.errorMessage = null;
        } else {
            viewModel.success = false;
            viewModel.orderDetail = null;
            viewModel.errorCode = outputData.getErrorCode();
            viewModel.errorMessage = outputData.getErrorMessage();
        }
    }

    private OrderDetailDisplay mapToViewModel(OrderDetailInfo info) {
        OrderDetailDisplay display = new OrderDetailDisplay();
        display.orderId = info.getOrderId();
        display.customerId = info.getCustomerId();
        display.customerName = info.getCustomerName();
        display.customerPhone = info.getCustomerPhone();
        display.shippingAddress = info.getShippingAddress();
        display.orderStatus = info.getOrderStatus();
        display.orderStatusCode = info.getOrderStatusCode();
        display.totalAmount = info.getTotalAmount();
        display.formattedOrderDate = info.getOrderDate().format(dateFormatter);
        display.note = info.getNote();
        display.paymentMethod = info.getPaymentMethod();
        display.paymentMethodText = formatPaymentMethod(info.getPaymentMethod());
        
        display.items = info.getItems().stream()
                .map(this::mapItemToViewModel)
                .collect(Collectors.toList());
        display.totalItems = info.getItems().size();
        
        return display;
    }

    private OrderItemDisplay mapItemToViewModel(OrderItemInfo itemInfo) {
        OrderItemDisplay item = new OrderItemDisplay();
        item.productId = itemInfo.getProductId();
        item.productName = itemInfo.getProductName();
        item.quantity = itemInfo.getQuantity();
        item.price = itemInfo.getPrice();
        item.subtotal = itemInfo.getSubtotal();
        return item;
    }

    private String formatPaymentMethod(String paymentMethod) {
        if ("THANH_TOAN_TRUC_TIEP".equals(paymentMethod)) {
            return "COD";
        } else if ("CHUYEN_KHOAN".equals(paymentMethod)) {
            return "Chuyển khoản";
        }
        return paymentMethod;
    }
}
