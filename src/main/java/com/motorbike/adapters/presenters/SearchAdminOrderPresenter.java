package com.motorbike.adapters.presenters;

import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import com.motorbike.adapters.viewmodels.SearchAdminOrderViewModel;
import com.motorbike.business.dto.searchadminorder.SearchAdminOrderOutputData;
import com.motorbike.business.usecase.output.SearchAdminOrderOutputBoundary;

public class SearchAdminOrderPresenter implements SearchAdminOrderOutputBoundary {

    private final SearchAdminOrderViewModel viewModel;
    private final NumberFormat currencyFormat;
    private final DateTimeFormatter dateFormatter;

    public SearchAdminOrderPresenter(SearchAdminOrderViewModel viewModel) {
        this.viewModel = viewModel;
        this.currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        this.dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    }

    @Override
    public void present(SearchAdminOrderOutputData outputData) {
        if (outputData.isSuccess()) {
            viewModel.success = true;
            viewModel.message = outputData.getMessage();
            viewModel.errorCode = null;
            viewModel.errorMessage = null;

            if (outputData.getOrders() != null) {
                viewModel.orders = outputData.getOrders().stream()
                    .map(order -> {
                        SearchAdminOrderViewModel.OrderItemViewModel item = new SearchAdminOrderViewModel.OrderItemViewModel();
                        item.orderId = order.getOrderId();
                        item.customerId = order.getCustomerId();
                        item.customerName = order.getCustomerName();
                        item.customerPhone = order.getCustomerPhone();
                        item.shippingAddress = order.getShippingAddress();
                        item.orderStatus = order.getOrderStatus();
                        item.formattedTotalAmount = currencyFormat.format(order.getTotalAmount());
                        item.totalItems = order.getTotalItems();
                        item.totalQuantity = order.getTotalQuantity();
                        item.formattedOrderDate = order.getOrderDate().format(dateFormatter);
                        item.note = order.getNote();
                        item.statusColor = getStatusColor(order.getOrderStatus());
                        return item;
                    })
                    .collect(java.util.stream.Collectors.toList());
            }
        } else {
            viewModel.success = false;
            viewModel.message = outputData.getMessage();
            viewModel.errorCode = outputData.getErrorCode();
            viewModel.errorMessage = outputData.getErrorMessage();
            viewModel.orders = null;
        }
    }

    private String getStatusColor(String status) {
        return switch (status) {
            case "CHO_XAC_NHAN" -> "#FFF4E6";
            case "DA_XAC_NHAN" -> "#E3F2FD";
            case "DANG_GIAO" -> "#FFF9C4";
            case "DA_GIAO" -> "#E8F5E9";
            case "DA_HUY" -> "#FFEBEE";
            default -> "#F5F5F5";
        };
    }
}
