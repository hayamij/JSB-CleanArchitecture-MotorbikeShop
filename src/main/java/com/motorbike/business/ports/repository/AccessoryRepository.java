package com.motorbike.business.ports.repository;

import com.motorbike.domain.entities.PhuKienXeMay;
import java.util.List;
import java.util.Optional;

public interface AccessoryRepository {

    // Lấy tất cả phụ kiện
    List<PhuKienXeMay> findAllAccessories();

    // Tìm phụ kiện theo ID
    Optional<PhuKienXeMay> findById(Long id);

    // Thêm mới hoặc cập nhật phụ kiện
    PhuKienXeMay save(PhuKienXeMay phuKien);

    // Xóa phụ kiện
    void deleteById(Long id);
    
    // Kiểm tra phụ kiện có tồn tại không
    boolean existsById(Long id);
    
    // Tìm kiếm phụ kiện theo keyword
    List<PhuKienXeMay> searchAccessories(String keyword);
    
    // Tìm kiếm phụ kiện với các tiêu chí
    List<PhuKienXeMay> search(String keyword, String loaiPhuKien, String thuongHieu);
    
    // Thêm hàng loạt phụ kiện (bulk insert)
    List<PhuKienXeMay> saveAll(List<PhuKienXeMay> phuKienList);
}
