package com.motorbike.business.usecase.impl;

import com.motorbike.business.dto.productdetail.ProductDetailInputData;
import com.motorbike.business.dto.productdetail.ProductDetailOutputData;
import com.motorbike.business.ports.repository.ProductRepository;
import com.motorbike.business.usecase.GetProductDetailInputBoundary;
import com.motorbike.business.usecase.GetProductDetailOutputBoundary;
import com.motorbike.domain.entities.SanPham;

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
    public void execute(ProductDetailInputData inputData) {
        try {
            // 1. Validate input
            if (inputData == null || inputData.getProductId() == null) {
                ProductDetailOutputData outputData = ProductDetailOutputData.forError(
                    "INVALID_INPUT",
                    "Mã sản phẩm không hợp lệ"
                );
                outputBoundary.present(outputData);
                return;
            }
            
            // 2. Find product
            Optional<SanPham> productOpt = productRepository.findById(inputData.getProductId());
            
            if (!productOpt.isPresent()) {
                ProductDetailOutputData outputData = ProductDetailOutputData.forError(
                    "PRODUCT_NOT_FOUND",
                    "Không tìm thấy sản phẩm với mã: " + inputData.getProductId()
                );
                outputBoundary.present(outputData);
                return;
            }
            
            SanPham sanPham = productOpt.get();
            
            // 3. Get product details using entity's method
            String chiTiet = sanPham.layThongTinChiTiet();
            
            // 4. Calculate final price (with discount)
            double giaGoc = sanPham.getGiaBan();
            double giaSauKhuyenMai = sanPham.tinhGiaSauKhuyenMai();
            double phanTramGiam = ((giaGoc - giaSauKhuyenMai) / giaGoc) * 100;
            
            // 5. Check availability
            boolean conHang = sanPham.getTonKho() > 0;
            
            // 6. Create success output
            ProductDetailOutputData outputData = ProductDetailOutputData.forSuccess(
                sanPham.getMaSanPham(),
                sanPham.getTenSanPham(),
                sanPham.getMoTa(),
                chiTiet,
                giaGoc,
                giaSauKhuyenMai,
                phanTramGiam,
                sanPham.getTonKho(),
                conHang
            );
            
            outputBoundary.present(outputData);
            
        } catch (Exception e) {
            ProductDetailOutputData outputData = ProductDetailOutputData.forError(
                "SYSTEM_ERROR",
                "Đã xảy ra lỗi khi lấy thông tin sản phẩm: " + e.getMessage()
            );
            outputBoundary.present(outputData);
        }
    }
}
