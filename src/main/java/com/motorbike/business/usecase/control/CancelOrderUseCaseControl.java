package com.motorbike.business.usecase.control;

import java.math.BigDecimal;

import com.motorbike.business.dto.cancelorder.CancelOrderInputData;
import com.motorbike.business.dto.cancelorder.CancelOrderOutputData;
import com.motorbike.business.ports.repository.OrderRepository;
import com.motorbike.business.ports.repository.ProductRepository;
import com.motorbike.business.usecase.output.CancelOrderOutputBoundary;
import com.motorbike.domain.entities.ChiTietDonHang;
import com.motorbike.domain.entities.DonHang;
import com.motorbike.domain.entities.SanPham;
import com.motorbike.domain.entities.TrangThaiDonHang;
import com.motorbike.domain.exceptions.DomainException;
import com.motorbike.domain.exceptions.ValidationException;

public class CancelOrderUseCaseControl
        extends AbstractUseCaseControl<CancelOrderInputData, CancelOrderOutputBoundary> {
    
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    
    public CancelOrderUseCaseControl(
            CancelOrderOutputBoundary outputBoundary,
            OrderRepository orderRepository,
            ProductRepository productRepository) {
        super(outputBoundary);
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }
    
    @Override
    protected void executeBusinessLogic(CancelOrderInputData inputData) throws Exception {
        try {
            DonHang donHang = orderRepository.findById(inputData.getOrderId())
                .orElseThrow(() -> DomainException.cannotCancelOrder(
                    "Không tìm thấy đơn hàng: " + inputData.getOrderId()
                ));
            
            if (!donHang.getMaTaiKhoan().equals(inputData.getUserId())) {
                throw DomainException.cannotCancelOrder(
                    "Bạn không có quyền hủy đơn hàng này"
                );
            }
            
            if (donHang.getTrangThai() != TrangThaiDonHang.CHO_XAC_NHAN) {
                throw DomainException.cannotCancelOrder(
                    "Chỉ có thể hủy đơn hàng ở trạng thái 'Chờ xác nhận'. " +
                    "Trạng thái hiện tại: " + donHang.getTrangThai().getMoTa()
                );
            }
            
            BigDecimal totalRefund = BigDecimal.ZERO;
            for (ChiTietDonHang chiTiet : donHang.getDanhSachSanPham()) {
                SanPham sanPham = productRepository.findById(chiTiet.getMaSanPham())
                    .orElseThrow(() -> DomainException.productNotFound(
                        "Sản phẩm không tồn tại: " + chiTiet.getMaSanPham()
                    ));
                
                sanPham.tangTonKho(chiTiet.getSoLuong());
                productRepository.save(sanPham);
                
                totalRefund = totalRefund.add(chiTiet.getThanhTien());
            }
            
            donHang.huyDonHang();
            DonHang cancelledOrder = orderRepository.save(donHang);
            
            CancelOrderOutputData outputData = CancelOrderOutputData.forSuccess(
                cancelledOrder.getMaDonHang(),
                cancelledOrder.getMaTaiKhoan(),
                cancelledOrder.getTrangThai().name(),
                totalRefund,
                inputData.getCancelReason()
            );
            
            outputBoundary.present(outputData);
            
        } catch (ValidationException | DomainException e) {
            throw e;
        }
    }
    
    @Override
    protected void validateInput(CancelOrderInputData inputData) {
        checkInputNotNull(inputData);
        com.motorbike.domain.entities.TaiKhoan.checkInput(inputData.getUserId());
        
        if (inputData.getOrderId() == null) {
            throw ValidationException.nullOrderId();
        }
    }
    
    @Override
    protected void handleValidationError(IllegalArgumentException e) {
        String errorCode = "INVALID_INPUT";
        if (e instanceof ValidationException) {
            errorCode = ((ValidationException) e).getErrorCode();
        }
        CancelOrderOutputData outputData = CancelOrderOutputData.forError(
            errorCode,
            e.getMessage()
        );
        outputBoundary.present(outputData);
    }
    
    @Override
    protected void handleSystemError(Exception e) {
        String errorCode;
        String message;
        
        if (e instanceof ValidationException) {
            ValidationException ex = (ValidationException) e;
            errorCode = ex.getErrorCode();
            message = ex.getMessage();
        } else if (e instanceof DomainException) {
            DomainException ex = (DomainException) e;
            errorCode = ex.getErrorCode();
            message = ex.getMessage();
        } else if (e instanceof com.motorbike.domain.exceptions.SystemException) {
            com.motorbike.domain.exceptions.SystemException ex = (com.motorbike.domain.exceptions.SystemException) e;
            errorCode = ex.getErrorCode();
            message = ex.getMessage();
        } else {
            throw new com.motorbike.domain.exceptions.SystemException(e);
        }
        
        CancelOrderOutputData outputData = CancelOrderOutputData.forError(errorCode, message);
        outputBoundary.present(outputData);
    }
}
