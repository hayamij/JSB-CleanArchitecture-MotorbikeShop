
package com.motorbike.business.usecase.control;

import com.motorbike.business.usecase.input.CheckoutInputBoundary;

import com.motorbike.business.dto.checkout.CheckoutInputData;
import com.motorbike.business.dto.checkout.CheckoutOutputData;
import com.motorbike.business.ports.repository.CartRepository;
import com.motorbike.business.ports.repository.ProductRepository;
import com.motorbike.business.ports.repository.OrderRepository;
import com.motorbike.business.usecase.output.CheckoutOutputBoundary;
import com.motorbike.domain.entities.GioHang;
import com.motorbike.domain.entities.ChiTietGioHang;
import com.motorbike.domain.entities.DonHang;
import com.motorbike.domain.entities.SanPham;
import com.motorbike.domain.entities.PhuongThucThanhToan;
import com.motorbike.domain.exceptions.ValidationException;
import com.motorbike.domain.exceptions.DomainException;
import java.util.List;
import java.util.stream.Collectors;

public class CheckoutUseCaseControl implements CheckoutInputBoundary {
    
    private final CheckoutOutputBoundary outputBoundary;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    
    public CheckoutUseCaseControl(
            CheckoutOutputBoundary outputBoundary,
            CartRepository cartRepository,
            ProductRepository productRepository,
            OrderRepository orderRepository) {
        this.outputBoundary = outputBoundary;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }
    
    public void execute(CheckoutInputData inputData) {
        CheckoutOutputData outputData = null;
        Exception errorException = null;
        
        try {
            if (inputData == null) {
                throw ValidationException.invalidInput();
            }
            DonHang.checkInput(
                inputData.getUserId(),
                inputData.getReceiverName(),
                inputData.getPhoneNumber(),
                inputData.getShippingAddress()
            );
        } catch (Exception e) {
            errorException = e;
        }
        
        GioHang gioHang = null;
        if (errorException == null) {
            try {
                gioHang = cartRepository.findByUserId(inputData.getUserId())
                    .orElseThrow(DomainException::emptyCart);
                
                if (gioHang.getDanhSachSanPham().isEmpty()) {
                    throw DomainException.emptyCart();
                }
                
                for (ChiTietGioHang item : gioHang.getDanhSachSanPham()) {
                    SanPham sanPham = productRepository.findById(item.getMaSanPham())
                        .orElseThrow(() -> DomainException.productNotFound(String.valueOf(item.getMaSanPham())));
                    
                    if (sanPham.getSoLuongTonKho() < item.getSoLuong()) {
                        throw DomainException.insufficientStock(
                            sanPham.getTenSanPham(),
                            sanPham.getSoLuongTonKho());
                    }
                }
            } catch (Exception e) {
                errorException = e;
            }
        }
        
        if (errorException == null && gioHang != null) {
            try {
                PhuongThucThanhToan phuongThucThanhToan = PhuongThucThanhToan.THANH_TOAN_TRUC_TIEP;
                if (inputData.getPaymentMethod() != null) {
                    try {
                        phuongThucThanhToan = PhuongThucThanhToan.valueOf(inputData.getPaymentMethod());
                    } catch (IllegalArgumentException e) {
                        // Default to COD if invalid payment method
                    }
                }
                
                DonHang donHang = DonHang.fromGioHang(
                    gioHang,
                    inputData.getReceiverName(),
                    inputData.getPhoneNumber(),
                    inputData.getShippingAddress(),
                    inputData.getNote(),
                    phuongThucThanhToan
                );
                
                for (ChiTietGioHang item : gioHang.getDanhSachSanPham()) {
                    SanPham sanPham = productRepository.findById(item.getMaSanPham()).get();
                    sanPham.giamTonKho(item.getSoLuong());
                    productRepository.save(sanPham);
                }
                
                DonHang savedOrder = orderRepository.save(donHang);
                
                gioHang.xoaToanBoGioHang();
                cartRepository.save(gioHang);
                
                List<CheckoutOutputData.OrderItemData> orderItems = savedOrder.getDanhSachSanPham()
                    .stream()
                    .map(item -> new CheckoutOutputData.OrderItemData(
                        item.getMaSanPham(),
                        item.getTenSanPham(),
                        item.getGiaBan(),
                        item.getSoLuong(),
                        item.getThanhTien()
                    ))
                    .collect(Collectors.toList());
                
                outputData = CheckoutOutputData.forSuccess(
                    savedOrder.getMaDonHang(),
                    savedOrder.getMaTaiKhoan(),
                    savedOrder.getTenNguoiNhan(),
                    savedOrder.getSoDienThoai(),
                    savedOrder.getDiaChiGiaoHang(),
                    savedOrder.getTrangThai().name(),
                    savedOrder.getTongTien(),
                    savedOrder.getDanhSachSanPham().size(),
                    orderItems,
                    savedOrder.getPhuongThucThanhToan().name(),
                    savedOrder.getPhuongThucThanhToan().getMoTa()
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
            
            outputData = CheckoutOutputData.forError(errorCode, message);
        }
        
        outputBoundary.present(outputData);
    }
}
