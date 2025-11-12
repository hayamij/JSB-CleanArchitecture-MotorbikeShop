package com.motorbike.business.usecase.impl;

import com.motorbike.business.dto.productdetail.GetProductDetailInputData;
import com.motorbike.business.dto.productdetail.GetProductDetailOutputData;
import com.motorbike.business.ports.repository.ProductRepository;
import com.motorbike.business.usecase.GetProductDetailInputBoundary;
import com.motorbike.business.usecase.GetProductDetailOutputBoundary;
import com.motorbike.domain.entities.SanPham;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Get Product Detail Use Case Implementation
 * Retrieves detailed information about a specific product
 */
public class GetProductDetailUseCaseImpl implements GetProductDetailInputBoundary {
    
    private final GetProductDetailOutputBoundary outputBoundary;
    private final ProductRepository productRepository;
    
    public GetProductDetailUseCaseImpl(
            GetProductDetailOutputBoundary outputBoundary,
            ProductRepository productRepository) {
        this.outputBoundary = outputBoundary;
        this.productRepository = productRepository;
    }
    
    @Override
    public void execute(GetProductDetailInputData inputData) {
        try {
            // 1. Validate input
            if (inputData == null || inputData.productId == null) {
                GetProductDetailOutputData outputData = GetProductDetailOutputData.forError(
                    "INVALID_INPUT",
                    "Mã sản phẩm không hợp lệ"
                );
                outputBoundary.present(outputData);
                return;
            }
            
            // 2. Find product
            Optional<SanPham> productOpt = productRepository.findById(inputData.productId);
            
            if (!productOpt.isPresent()) {
                GetProductDetailOutputData outputData = GetProductDetailOutputData.forError(
                    "PRODUCT_NOT_FOUND",
                    "Không tìm thấy sản phẩm với mã: " + inputData.productId
                );
                outputBoundary.present(outputData);
                return;
            }
            
            SanPham sanPham = productOpt.get();
            
            // 3. Get product details using entity's method
            String chiTiet = sanPham.layThongTinChiTiet();
            
            // 4. Calculate final price (with discount)
            BigDecimal giaGoc = sanPham.getGia();
            BigDecimal giaSauKhuyenMai = sanPham.tinhGiaSauKhuyenMai();
            double phanTramGiam = giaGoc.subtract(giaSauKhuyenMai)
                .divide(giaGoc, 4, BigDecimal.ROUND_HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .doubleValue();
            
            // 5. Check availability
            boolean conHang = sanPham.getSoLuongTonKho() > 0;
            
            // 6. Create success output
            GetProductDetailOutputData outputData = GetProductDetailOutputData.forSuccess(
                sanPham.getMaSanPham(),
                sanPham.getTenSanPham(),
                sanPham.getMoTa(),
                chiTiet,
                giaGoc,
                giaSauKhuyenMai,
                phanTramGiam,
                sanPham.getSoLuongTonKho(),
                conHang
            );
            
            outputBoundary.present(outputData);
            
        } catch (Exception e) {
            GetProductDetailOutputData outputData = GetProductDetailOutputData.forError(
                "SYSTEM_ERROR",
                "Đã xảy ra lỗi khi lấy thông tin sản phẩm: " + e.getMessage()
            );
            outputBoundary.present(outputData);
        }
    }
}
