package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.productdetail.GetProductDetailInputData;
import com.motorbike.business.dto.productdetail.GetProductDetailOutputData;
import com.motorbike.business.ports.repository.ProductRepository;
import com.motorbike.business.usecase.output.GetProductDetailOutputBoundary;
import com.motorbike.domain.entities.SanPham;
import com.motorbike.domain.exceptions.ProductNotFoundException;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Get Product Detail Use Case Control
 * Extends AbstractUseCaseControl for common validation and error handling
 */
public class GetProductDetailUseCaseControl 
        extends AbstractUseCaseControl<GetProductDetailInputData, GetProductDetailOutputBoundary> {
    
    private final ProductRepository productRepository;
    
    public GetProductDetailUseCaseControl(
            GetProductDetailOutputBoundary outputBoundary,
            ProductRepository productRepository) {
        super(outputBoundary);
        this.productRepository = productRepository;
    }
    
    @Override
    protected void executeBusinessLogic(GetProductDetailInputData inputData) throws Exception {
        SanPham sanPham = productRepository.findById(inputData.productId)
            .orElseThrow(() -> new ProductNotFoundException(String.valueOf(inputData.productId)));
        String chiTiet = sanPham.layThongTinChiTiet();
        BigDecimal giaGoc = sanPham.getGia();
        BigDecimal giaSauKhuyenMai = sanPham.tinhGiaSauKhuyenMai();
        double phanTramGiam = giaGoc.subtract(giaSauKhuyenMai)
            .divide(giaGoc, 4, RoundingMode.HALF_UP)
            .multiply(BigDecimal.valueOf(100))
            .doubleValue();
        boolean conHang = sanPham.getSoLuongTonKho() > 0;
        
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
    }
    
    @Override
    protected void validateInput(GetProductDetailInputData inputData) {
        if (inputData == null || inputData.productId == null) {
            throw new IllegalArgumentException("Mã sản phẩm không hợp lệ");
        }
    }
    
    @Override
    protected void handleValidationError(IllegalArgumentException e) {
        GetProductDetailOutputData outputData = GetProductDetailOutputData.forError(
            "INVALID_INPUT",
            e.getMessage()
        );
        outputBoundary.present(outputData);
    }
    
    @Override
    protected void handleSystemError(Exception e) {
        String errorCode = "SYSTEM_ERROR";
        String message = "Đã xảy ra lỗi khi lấy thông tin sản phẩm: " + e.getMessage();
        
        try {
            throw e;
        } catch (ProductNotFoundException ex) {
            errorCode = ex.getErrorCode();
            message = ex.getMessage();
        } catch (Exception ex) {
            // Keep default
        }
        
        GetProductDetailOutputData outputData = GetProductDetailOutputData.forError(errorCode, message);
        outputBoundary.present(outputData);
    }
}
