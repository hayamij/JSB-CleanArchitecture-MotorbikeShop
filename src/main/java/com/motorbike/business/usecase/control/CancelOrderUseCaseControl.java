
package com.motorbike.business.usecase.control;
import com.motorbike.business.usecase.input.CancelOrderInputBoundary;

import java.math.BigDecimal;

import com.motorbike.business.dto.cancelorder.CancelOrderInputData;
import com.motorbike.business.dto.cancelorder.CancelOrderOutputData;
import com.motorbike.business.dto.validateordercancellation.ValidateOrderCancellationInputData;
import com.motorbike.business.dto.restorestock.RestoreProductStockInputData;
import com.motorbike.business.dto.updateorderstatus.UpdateOrderStatusInputData;
import com.motorbike.business.ports.repository.OrderRepository;
import com.motorbike.business.usecase.output.CancelOrderOutputBoundary;
import com.motorbike.business.usecase.input.ValidateOrderCancellationInputBoundary;
import com.motorbike.business.usecase.input.RestoreProductStockInputBoundary;
import com.motorbike.business.usecase.input.UpdateOrderStatusInputBoundary;
import com.motorbike.domain.entities.ChiTietDonHang;
import com.motorbike.domain.entities.DonHang;
import com.motorbike.domain.entities.TrangThaiDonHang;
import com.motorbike.domain.exceptions.DomainException;
import com.motorbike.domain.exceptions.ValidationException;

public class CancelOrderUseCaseControl implements CancelOrderInputBoundary {
    
    private final CancelOrderOutputBoundary outputBoundary;
    private final OrderRepository orderRepository;
    private final ValidateOrderCancellationInputBoundary validateCancellationUseCase;
    private final RestoreProductStockInputBoundary restoreStockUseCase;
    private final UpdateOrderStatusInputBoundary updateStatusUseCase;
    
    public CancelOrderUseCaseControl(
            CancelOrderOutputBoundary outputBoundary,
            OrderRepository orderRepository,
            ValidateOrderCancellationInputBoundary validateCancellationUseCase,
            RestoreProductStockInputBoundary restoreStockUseCase,
            UpdateOrderStatusInputBoundary updateStatusUseCase) {
        this.outputBoundary = outputBoundary;
        this.orderRepository = orderRepository;
        this.validateCancellationUseCase = validateCancellationUseCase;
        this.restoreStockUseCase = restoreStockUseCase;
        this.updateStatusUseCase = updateStatusUseCase;
    }
    
    // Constructor for tests with 3 params (outputBoundary, orderRepo, productRepo)
    public CancelOrderUseCaseControl(
            CancelOrderOutputBoundary outputBoundary,
            OrderRepository orderRepository,
            com.motorbike.business.ports.repository.ProductRepository productRepository) {
        this.outputBoundary = outputBoundary;
        this.orderRepository = orderRepository;
        this.validateCancellationUseCase = new ValidateOrderCancellationUseCaseControl(null);
        this.restoreStockUseCase = new RestoreProductStockUseCaseControl(null, productRepository);
        this.updateStatusUseCase = new UpdateOrderStatusUseCaseControl(null, orderRepository);
    }
    
    @Override
    public void execute(CancelOrderInputData inputData) {
        CancelOrderOutputData outputData = null;
        Exception errorException = null;
        
        try {
            if (inputData == null) {
                throw ValidationException.invalidInput();
            }
            com.motorbike.domain.entities.TaiKhoan.checkInput(inputData.getUserId());
            
            if (inputData.getOrderId() == null) {
                throw ValidationException.nullOrderId();
            }
        } catch (Exception e) {
            errorException = e;
        }
        
        DonHang donHang = null;
        if (errorException == null) {
            try {
                donHang = orderRepository.findById(inputData.getOrderId())
                    .orElseThrow(() -> DomainException.cannotCancelOrder(
                        "Không tìm thấy đơn hàng: " + inputData.getOrderId()
                    ));
                
                if (!donHang.getMaTaiKhoan().equals(inputData.getUserId())) {
                    throw DomainException.cannotCancelOrder(
                        "Bạn không có quyền hủy đơn hàng này"
                    );
                }
                
                // UC-47: Validate order cancellation
                ValidateOrderCancellationInputData validateInput = new ValidateOrderCancellationInputData(
                    donHang.getMaDonHang(),
                    donHang.getTrangThai()
                );
                var validationResult = ((ValidateOrderCancellationUseCaseControl) validateCancellationUseCase)
                    .validateInternal(validateInput);
                
                if (!validationResult.isSuccess()) {
                    throw new DomainException(validationResult.getErrorMessage(), validationResult.getErrorCode());
                }
                
                if (!validationResult.canCancel()) {
                    throw DomainException.cannotCancelOrder(validationResult.getReason());
                }
            } catch (Exception e) {
                errorException = e;
            }
        }
        
        if (errorException == null && donHang != null) {
            try {
                BigDecimal totalRefund = BigDecimal.ZERO;
                
                // UC-46: Restore product stock for all items
                for (ChiTietDonHang chiTiet : donHang.getDanhSachSanPham()) {
                    RestoreProductStockInputData restoreInput = new RestoreProductStockInputData(
                        chiTiet.getMaSanPham(),
                        chiTiet.getSoLuong()
                    );
                    var restoreResult = ((RestoreProductStockUseCaseControl) restoreStockUseCase)
                        .restoreStockInternal(restoreInput);
                    
                    if (!restoreResult.isSuccess()) {
                        throw new DomainException(restoreResult.getErrorMessage(), restoreResult.getErrorCode());
                    }
                    
                    totalRefund = totalRefund.add(chiTiet.getThanhTien());
                }
                
                // UC-48: Update order status to cancelled
                UpdateOrderStatusInputData updateInput = new UpdateOrderStatusInputData(
                    donHang.getMaDonHang(),
                    TrangThaiDonHang.DA_HUY
                );
                var updateResult = ((UpdateOrderStatusUseCaseControl) updateStatusUseCase)
                    .updateStatusInternal(updateInput);
                
                if (!updateResult.isSuccess()) {
                    throw new DomainException(updateResult.getErrorMessage(), updateResult.getErrorCode());
                }
                
                // Reload order to get updated status
                Long orderId = donHang.getMaDonHang();
                DonHang cancelledOrder = orderRepository.findById(orderId)
                    .orElseThrow(() -> DomainException.orderNotFound(orderId));
                
                outputData = CancelOrderOutputData.forSuccess(
                    cancelledOrder.getMaDonHang(),
                    cancelledOrder.getMaTaiKhoan(),
                    cancelledOrder.getTrangThai().name(),
                    totalRefund,
                    inputData.getCancelReason()
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
            } else if (errorException instanceof DomainException) {
                errorCode = ((DomainException) errorException).getErrorCode();
            } else if (errorException instanceof com.motorbike.domain.exceptions.SystemException) {
                errorCode = ((com.motorbike.domain.exceptions.SystemException) errorException).getErrorCode();
            }
            
            outputData = CancelOrderOutputData.forError(errorCode, message);
        }
        
        outputBoundary.present(outputData);
    }
}
