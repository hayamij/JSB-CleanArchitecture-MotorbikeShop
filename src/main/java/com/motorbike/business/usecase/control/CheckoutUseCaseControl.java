package com.motorbike.business.usecase.control;

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
import com.motorbike.domain.exceptions.InvalidCartException;
import com.motorbike.domain.exceptions.InvalidOrderException;
import com.motorbike.domain.exceptions.EmptyCartException;
import com.motorbike.domain.exceptions.ProductNotFoundException;
import com.motorbike.domain.exceptions.InsufficientStockException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Checkout Use Case Control
 * Business Rules:
 * - Bắt buộc phải đăng nhập (chỉ customer/admin mới được thanh toán)
 * - Giỏ hàng phải có ít nhất 1 sản phẩm
 * - Kiểm tra lại số lượng tồn kho của từng sản phẩm trước khi thanh toán
 * - Tạo Order với trạng thái ban đầu "CHO_XAC_NHAN"
 * - Tạo các OrderItem tương ứng với các CartItem
 * - Trừ số lượng tồn kho của sản phẩm
 * - Xóa giỏ hàng sau khi thanh toán thành công
 * - Entity DonHang chịu trách nhiệm tính tổng tiền (quantity × price cho mỗi item)
 */
public class CheckoutUseCaseControl 
        extends AbstractUseCaseControl<CheckoutInputData, CheckoutOutputBoundary> {
    
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    
    public CheckoutUseCaseControl(
            CheckoutOutputBoundary outputBoundary,
            CartRepository cartRepository,
            ProductRepository productRepository,
            OrderRepository orderRepository) {
        super(outputBoundary);
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }
    
    @Override
    protected void executeBusinessLogic(CheckoutInputData inputData) throws Exception {
        try {
            // Business Rule: Giỏ hàng phải có ít nhất 1 sản phẩm
            GioHang gioHang = cartRepository.findByUserId(inputData.getUserId())
                .orElseThrow(() -> new EmptyCartException());
            
            if (gioHang.getDanhSachSanPham().isEmpty()) {
                throw new EmptyCartException();
            }
            
            // Business Rule: Kiểm tra lại số lượng tồn kho của từng sản phẩm trước khi thanh toán
            for (ChiTietGioHang item : gioHang.getDanhSachSanPham()) {
                SanPham sanPham = productRepository.findById(item.getMaSanPham())
                    .orElseThrow(() -> new ProductNotFoundException(
                        String.valueOf(item.getMaSanPham()), 
                        "Sản phẩm không tồn tại: " + item.getMaSanPham()));
                
                if (sanPham.getSoLuongTonKho() < item.getSoLuong()) {
                    throw new InsufficientStockException(
                        sanPham.getTenSanPham(), 
                        sanPham.getSoLuongTonKho());
                }
            }
            
            // Business Rule: Tạo Order với trạng thái ban đầu (CHO_XAC_NHAN)
            // Entity DonHang chịu trách nhiệm tính tổng tiền (quantity × price cho mỗi item)
            DonHang donHang = DonHang.fromGioHang(
                gioHang,
                inputData.getReceiverName(),
                inputData.getPhoneNumber(),
                inputData.getShippingAddress(),
                inputData.getNote()
            );
            
            // Business Rule: Trừ số lượng tồn kho của sản phẩm
            for (ChiTietGioHang item : gioHang.getDanhSachSanPham()) {
                SanPham sanPham = productRepository.findById(item.getMaSanPham()).get();
                sanPham.giamTonKho(item.getSoLuong());
                productRepository.save(sanPham);
            }
            
            // Lưu đơn hàng vào database
            DonHang savedOrder = orderRepository.save(donHang);
            
            // Business Rule: Xóa giỏ hàng sau khi thanh toán thành công
            gioHang.xoaToanBoGioHang();
            cartRepository.save(gioHang);
            
            // Prepare output data
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
            
            CheckoutOutputData outputData = CheckoutOutputData.forSuccess(
                savedOrder.getMaDonHang(),
                savedOrder.getMaTaiKhoan(),
                savedOrder.getTenNguoiNhan(),
                savedOrder.getSoDienThoai(),
                savedOrder.getDiaChiGiaoHang(),
                savedOrder.getTrangThai().name(),
                savedOrder.getTongTien(),
                savedOrder.getDanhSachSanPham().size(),
                orderItems
            );
            
            outputBoundary.present(outputData);
            
        } catch (InvalidCartException | InvalidOrderException | EmptyCartException | 
                 ProductNotFoundException | InsufficientStockException e) {
            throw e;
        }
    }
    
    @Override
    protected void validateInput(CheckoutInputData inputData) {
        if (inputData == null || inputData.getUserId() == null) {
            throw new IllegalArgumentException("User ID không hợp lệ");
        }
    }
    
    @Override
    protected void handleValidationError(IllegalArgumentException e) {
        CheckoutOutputData outputData = CheckoutOutputData.forError(
            "INVALID_INPUT",
            e.getMessage()
        );
        outputBoundary.present(outputData);
    }
    
    @Override
    protected void handleSystemError(Exception e) {
        String errorCode = "SYSTEM_ERROR";
        String message = "Đã xảy ra lỗi: " + e.getMessage();
        
        try {
            throw e;
        } catch (InvalidCartException ex) {
            errorCode = ex.getErrorCode();
            message = ex.getMessage();
        } catch (InvalidOrderException ex) {
            errorCode = ex.getErrorCode();
            message = ex.getMessage();
        } catch (EmptyCartException ex) {
            errorCode = ex.getErrorCode();
            message = ex.getMessage();
        } catch (ProductNotFoundException ex) {
            errorCode = ex.getErrorCode();
            message = ex.getMessage();
        } catch (InsufficientStockException ex) {
            errorCode = ex.getErrorCode();
            message = ex.getMessage();
        } catch (Exception ex) {
            // Keep default
        }
        
        CheckoutOutputData outputData = CheckoutOutputData.forError(errorCode, message);
        outputBoundary.present(outputData);
    }
}
