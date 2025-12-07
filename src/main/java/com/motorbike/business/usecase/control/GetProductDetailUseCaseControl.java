
package com.motorbike.business.usecase.control;

import com.motorbike.business.usecase.input.GetProductDetailInputBoundary;

import com.motorbike.business.dto.productdetail.GetProductDetailInputData;
import com.motorbike.business.dto.productdetail.GetProductDetailOutputData;
import com.motorbike.business.ports.repository.ProductRepository;
import com.motorbike.business.usecase.output.GetProductDetailOutputBoundary;
import com.motorbike.domain.entities.SanPham;
import com.motorbike.domain.exceptions.ValidationException;
import com.motorbike.domain.exceptions.DomainException;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class GetProductDetailUseCaseControl implements GetProductDetailInputBoundary {
    
    private final GetProductDetailOutputBoundary outputBoundary;
    private final ProductRepository productRepository;
    
    public GetProductDetailUseCaseControl(
            GetProductDetailOutputBoundary outputBoundary,
            ProductRepository productRepository) {
        this.outputBoundary = outputBoundary;
        this.productRepository = productRepository;
    }
    
    public void execute(GetProductDetailInputData inputData) {
        GetProductDetailOutputData outputData = null;
        Exception errorException = null;
        
        try {
            if (inputData == null) {
                throw ValidationException.invalidInput();
            }
            SanPham.checkInput(inputData.productId, 1);
        } catch (Exception e) {
            errorException = e;
        }
        
        SanPham sanPham = null;
        if (errorException == null) {
            try {
                sanPham = productRepository.findById(inputData.productId)
                    .orElseThrow(() -> DomainException.productNotFound(String.valueOf(inputData.productId)));
            } catch (Exception e) {
                errorException = e;
            }
        }
        
        if (errorException == null && sanPham != null) {
            try {
                String chiTiet = sanPham.layThongTinChiTiet();
                BigDecimal giaGoc = sanPham.getGia();
                BigDecimal giaSauKhuyenMai = sanPham.tinhGiaSauKhuyenMai();
                double phanTramGiam = giaGoc.subtract(giaSauKhuyenMai)
                    .divide(giaGoc, 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100))
                    .doubleValue();
                boolean conHang = sanPham.getSoLuongTonKho() > 0;
                
                // Determine category based on instance type
                String category = (sanPham instanceof com.motorbike.domain.entities.XeMay) 
                    ? "MOTORCYCLE" : "ACCESSORY";
                
                outputData = GetProductDetailOutputData.forSuccess(
                    sanPham.getMaSanPham(),
                    sanPham.getTenSanPham(),
                    sanPham.getMoTa(),
                    chiTiet,
                    giaGoc,
                    giaSauKhuyenMai,
                    phanTramGiam,
                    sanPham.getSoLuongTonKho(),
                    conHang,
                    sanPham.getHinhAnh(),
                    category
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
            
            outputData = GetProductDetailOutputData.forError(errorCode, message);
        }
        
        outputBoundary.present(outputData);
    }
}
