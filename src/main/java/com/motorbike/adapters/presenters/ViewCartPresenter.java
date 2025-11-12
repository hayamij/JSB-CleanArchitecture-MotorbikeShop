package com.motorbike.adapters.presenters;

import com.motorbike.adapters.viewmodels.ViewCartViewModel;
import com.motorbike.adapters.viewmodels.ViewCartViewModel.CartItemViewModel;
import com.motorbike.business.dto.viewcart.ViewCartOutputData;
import com.motorbike.business.usecase.ViewCartOutputBoundary;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Presenter: ViewCartPresenter
 * Converts ViewCartOutputData to ViewCartViewModel
 * Formats data for UI presentation
 */
public class ViewCartPresenter implements ViewCartOutputBoundary {
    private ViewCartViewModel viewModel;
    private static final NumberFormat VND_FORMAT = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

    @Override
    public void present(ViewCartOutputData outputData) {
        if (!outputData.isSuccess()) {
            // Error case
            viewModel = new ViewCartViewModel(
                false,
                null,
                null,
                0,
                0,
                "0 ₫",
                null,
                true,
                false,
                null,
                outputData.getErrorMessage()
            );
            return;
        }

        // Empty cart case
        if (outputData.isEmpty()) {
            viewModel = new ViewCartViewModel(
                true,
                outputData.getCartId(),
                outputData.getUserId(),
                0,
                0,
                "0 ₫",
                new ArrayList<>(),
                true,
                false,
                "Giỏ hàng của bạn đang trống",
                null
            );
            return;
        }

        // Format cart items
        List<CartItemViewModel> itemViewModels = new ArrayList<>();
        if (outputData.getItems() != null) {
            for (ViewCartOutputData.CartItemData item : outputData.getItems()) {
                String stockStatus = getStockStatus(item);
                String stockWarningColor = item.hasStockWarning() ? "#dc3545" : null; // Red for warnings

                itemViewModels.add(new CartItemViewModel(
                    item.getProductId(),
                    item.getProductName(),
                    item.getProductImageUrl(),
                    VND_FORMAT.format(item.getUnitPrice()),
                    item.getQuantity(),
                    VND_FORMAT.format(item.getSubtotal()),
                    item.getAvailableStock(),
                    stockStatus,
                    item.hasStockWarning(),
                    item.getStockWarningMessage(),
                    stockWarningColor
                ));
            }
        }

        // Format total amount
        String formattedTotalAmount = VND_FORMAT.format(outputData.getTotalAmount());

        // Build cart summary message
        String cartSummary = String.format(
            "Giỏ hàng có %d sản phẩm (%d món)",
            outputData.getTotalItems(),
            outputData.getTotalQuantity()
        );

        // Stock warning message
        String stockWarningMessage = outputData.hasStockWarnings() ?
            "Một số sản phẩm trong giỏ hàng có cảnh báo về số lượng tồn kho" : null;

        viewModel = new ViewCartViewModel(
            true,
            outputData.getCartId(),
            outputData.getUserId(),
            outputData.getTotalItems(),
            outputData.getTotalQuantity(),
            formattedTotalAmount,
            itemViewModels,
            false,
            outputData.hasStockWarnings(),
            cartSummary,
            stockWarningMessage
        );
    }

    public ViewCartViewModel getViewModel() {
        return viewModel;
    }

    private String getStockStatus(ViewCartOutputData.CartItemData item) {
        int stock = item.getAvailableStock();
        
        // Check stock level first for specific statuses
        if (stock == 0) {
            return "out_of_stock";
        } else if (item.hasStockWarning()) {
            return "warning";
        } else if (stock < 10) {
            return "low_stock";
        } else {
            return "in_stock";
        }
    }
}
