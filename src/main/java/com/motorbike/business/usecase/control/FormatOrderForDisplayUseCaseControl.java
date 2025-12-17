package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.formatorderfordisplay.FormatOrderForDisplayInputData;
import com.motorbike.business.dto.formatorderfordisplay.FormatOrderForDisplayOutputData;
import com.motorbike.business.usecase.input.FormatOrderForDisplayInputBoundary;
import com.motorbike.business.usecase.output.FormatOrderForDisplayOutputBoundary;
import com.motorbike.domain.entities.DonHang;
import com.motorbike.domain.entities.TrangThaiDonHang;
import com.motorbike.domain.exceptions.ValidationException;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class FormatOrderForDisplayUseCaseControl implements FormatOrderForDisplayInputBoundary {
    
    private final FormatOrderForDisplayOutputBoundary outputBoundary;
    
    public FormatOrderForDisplayUseCaseControl(FormatOrderForDisplayOutputBoundary outputBoundary) {
        this.outputBoundary = outputBoundary;
    }
    
    @Override
    public void execute(FormatOrderForDisplayInputData inputData) {
        FormatOrderForDisplayOutputData outputData = formatInternal(inputData);
        outputBoundary.present(outputData);
    }
    
    // Overload to accept DTO from order package
    public FormatOrderForDisplayOutputData formatInternal(com.motorbike.business.dto.order.FormatOrderForDisplayInputData inputData) {
        // Convert to the expected DTO type
        FormatOrderForDisplayInputData convertedInput = new FormatOrderForDisplayInputData(inputData.getDonHang());
        return formatInternal(convertedInput);
    }
    
    public FormatOrderForDisplayOutputData formatInternal(FormatOrderForDisplayInputData inputData) {
        FormatOrderForDisplayOutputData outputData = null;
        Exception errorException = null;
        
        try {
            if (inputData == null || inputData.getOrder() == null) {
                throw ValidationException.invalidInput();
            }
        } catch (Exception e) {
            errorException = e;
        }
        
        if (errorException == null) {
            try {
                DonHang order = inputData.getOrder();
                
                // Format status text
                String statusText = formatStatusText(order.getTrangThai());
                String statusColor = getStatusColor(order.getTrangThai());
                
                // Format amount
                NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
                String formattedAmount = currencyFormat.format(order.getTongTien());
                
                // Format date
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                String formattedDate = order.getNgayDatHang().format(dateFormatter);
                
                // Payment method text
                String paymentMethodText = order.getPhuongThucThanhToan().getMoTa();
                
                // Can cancel?
                boolean canCancel = order.getTrangThai() == TrangThaiDonHang.CHO_XAC_NHAN 
                                 || order.getTrangThai() == TrangThaiDonHang.DA_XAC_NHAN;
                
                outputData = new FormatOrderForDisplayOutputData(
                    order.getMaDonHang(),
                    statusText,
                    statusColor,
                    formattedAmount,
                    formattedDate,
                    paymentMethodText,
                    canCancel
                );
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
            
            outputData = FormatOrderForDisplayOutputData.forError(errorCode, message);
        }
        
        return outputData;
    }
    
    private String formatStatusText(TrangThaiDonHang status) {
        switch (status) {
            case CHO_XAC_NHAN: return "Chờ xác nhận";
            case DA_XAC_NHAN: return "Đã xác nhận";
            case DANG_GIAO_HANG: return "Đang giao hàng";
            case DA_GIAO_HANG: return "Đã giao hàng";
            case DA_HUY: return "Đã hủy";
            default: return "Không xác định";
        }
    }
    
    private String getStatusColor(TrangThaiDonHang status) {
        switch (status) {
            case CHO_XAC_NHAN: return "warning";
            case DA_XAC_NHAN: return "info";
            case DANG_GIAO_HANG: return "primary";
            case DA_GIAO_HANG: return "success";
            case DA_HUY: return "danger";
            default: return "secondary";
        }
    }
}
