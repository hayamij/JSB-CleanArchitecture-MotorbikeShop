package com.motorbike.business.ports.repository;

import com.motorbike.domain.entities.XeMay;
import java.util.List;
import java.util.Optional;

public interface MotorbikeRepository {

    // Lấy tất cả xe máy
    List<XeMay> findAllMotorbikes();

    // Tìm xe máy theo ID
    Optional<XeMay> findById(Long id);

    // Thêm mới hoặc cập nhật xe máy
    XeMay save(XeMay xeMay);

    // Xóa xe máy
    void deleteById(Long id);
    
    // Kiểm tra xe máy có tồn tại không
    boolean existsById(Long id);
    
    // Tìm kiếm xe máy theo keyword
    List<XeMay> searchMotorbikes(String keyword);
    
    // Thêm hàng loạt xe máy (bulk insert)
    List<XeMay> saveAll(List<XeMay> xeMayList);
}
