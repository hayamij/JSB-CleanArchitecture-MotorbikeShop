package com.motorbike.business.ports.repository;

import java.util.List;
import java.util.Optional;

import com.motorbike.domain.entities.DonHang;
import com.motorbike.domain.entities.ProductSalesStats;
import com.motorbike.domain.entities.TrangThaiDonHang;

public interface OrderRepository {
    
    
    DonHang save(DonHang donHang);
    
    
    Optional<DonHang> findById(Long orderId);
    
    
    List<DonHang> findByUserId(Long userId);
    
    
    List<DonHang> findByStatus(TrangThaiDonHang trangThai);
    
    
    List<DonHang> findByUserIdAndStatus(Long userId, TrangThaiDonHang trangThai);

    
    List<DonHang> findAll();
    
    
    void deleteById(Long orderId);
    
    
    boolean existsById(Long orderId);
    
    
    List<DonHang> searchOrders(String keyword);
    
    /**
     * Lấy thống kê sản phẩm bán chạy từ các đơn hàng đã xác nhận
     * @param limit Số lượng sản phẩm tối đa cần lấy
     * @return Danh sách thống kê sản phẩm đã sắp xếp theo số lượng bán giảm dần
     */
    List<ProductSalesStats> getTopSellingProducts(int limit);
}
