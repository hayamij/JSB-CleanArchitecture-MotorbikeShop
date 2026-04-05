package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.formatproductfordisplay.FormatProductForDisplayInputData;
import com.motorbike.business.dto.formatproductfordisplay.FormatProductForDisplayOutputData;
import com.motorbike.business.usecase.input.FormatProductForDisplayInputBoundary;
import com.motorbike.business.usecase.output.FormatProductForDisplayOutputBoundary;
import com.motorbike.domain.entities.SanPham;
import com.motorbike.domain.exceptions.ValidationException;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class FormatProductForDisplayUseCaseControl implements FormatProductForDisplayInputBoundary {
    private final FormatProductForDisplayOutputBoundary outputBoundary;

    public FormatProductForDisplayUseCaseControl(FormatProductForDisplayOutputBoundary outputBoundary) {
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void execute(FormatProductForDisplayInputData inputData) {
        FormatProductForDisplayOutputData outputData = formatInternal(inputData);
        outputBoundary.present(outputData);
    }

    public FormatProductForDisplayOutputData formatInternal(FormatProductForDisplayInputData inputData) {
        FormatProductForDisplayOutputData outputData = null;
        Exception errorException = null;

        // Step 1: Validation
        try {
            if (inputData == null) {
                throw ValidationException.invalidInput("FormatProductForDisplay");
            }
            if (inputData.getProduct() == null) {
                throw ValidationException.invalidInput("Product is required");
            }
        } catch (Exception e) {
            errorException = e;
        }

        // Step 2: Business logic - format product for display
        if (errorException == null) {
            try {
                SanPham product = inputData.getProduct();
                NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

                // Format prices
                String formattedPrice = currencyFormat.format(product.getGiaBan());
                
                // Calculate and format discount price if applicable
                String formattedDiscountPrice = null;
                if (product.getPhanTramGiamGia() != null && product.getPhanTramGiamGia().compareTo(BigDecimal.ZERO) > 0) {
                    BigDecimal discountAmount = product.getGiaBan()
                            .multiply(product.getPhanTramGiamGia())
                            .divide(BigDecimal.valueOf(100), 2, java.math.RoundingMode.HALF_UP);
                    BigDecimal finalPrice = product.getGiaBan().subtract(discountAmount);
                    formattedDiscountPrice = currencyFormat.format(finalPrice);
                }

                // Determine stock status
                String stockStatus;
                String stockStatusColor;
                int stock = product.getSoLuongTonKho();
                
                if (stock == 0) {
                    stockStatus = "Hết hàng";
                    stockStatusColor = "danger";
                } else if (stock < 10) {
                    stockStatus = "Sắp hết (" + stock + " sản phẩm)";
                    stockStatusColor = "warning";
                } else if (stock < 50) {
                    stockStatus = "Còn hàng (" + stock + " sản phẩm)";
                    stockStatusColor = "info";
                } else {
                    stockStatus = "Còn hàng";
                    stockStatusColor = "success";
                }

                // Format discount badge
                String discountBadge = null;
                if (product.getPhanTramGiamGia() != null && product.getPhanTramGiamGia().compareTo(BigDecimal.ZERO) > 0) {
                    discountBadge = "-" + product.getPhanTramGiamGia() + "%";
                }

                // Format category display
                String categoryDisplay = formatCategoryDisplay(product.getLoaiSanPham());

                outputData = new FormatProductForDisplayOutputData(
                        product.getMaSanPham(),
                        product.getTenSanPham(),
                        String.valueOf(product.getMaSanPham()),
                        formattedPrice,
                        formattedDiscountPrice,
                        stockStatus,
                        stockStatusColor,
                        discountBadge,
                        categoryDisplay
                );

            } catch (Exception e) {
                errorException = e;
            }
        }

        // Step 3: Handle error
        if (errorException != null) {
            String errorCode = errorException instanceof ValidationException
                    ? ((ValidationException) errorException).getErrorCode()
                    : "FORMAT_ERROR";
            outputData = FormatProductForDisplayOutputData.forError(errorCode, errorException.getMessage());
        }

        // Step 4: Return result
        return outputData;
    }

    private String formatCategoryDisplay(String category) {
        if (category == null || category.trim().isEmpty()) {
            return "Chưa phân loại";
        }
        
        // Capitalize first letter of each word
        String[] words = category.toLowerCase().split("\\s+");
        StringBuilder result = new StringBuilder();
        
        for (String word : words) {
            if (word.length() > 0) {
                result.append(Character.toUpperCase(word.charAt(0)))
                      .append(word.substring(1))
                      .append(" ");
            }
        }
        
        return result.toString().trim();
    }
}
