package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.productdetail.GetProductDetailInputData;
import com.motorbike.business.dto.productdetail.GetProductDetailOutputData;
import com.motorbike.business.ports.repository.ProductRepository;
import com.motorbike.business.usecase.output.GetProductDetailOutputBoundary;
import com.motorbike.domain.entities.SanPham;
import com.motorbike.domain.exceptions.ValidationException;
import com.motorbike.domain.exceptions.DomainException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

public class GetProductDetailUseCaseControl {
    
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
                String chiTietString = sanPham.layThongTinChiTiet();
                Map<String, String> specifications = parseSpecifications(chiTietString);
                BigDecimal giaGoc = sanPham.getGia();
                BigDecimal giaSauKhuyenMai = sanPham.tinhGiaSauKhuyenMai();
                double phanTramGiam = giaGoc.subtract(giaSauKhuyenMai)
                    .divide(giaGoc, 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100))
                    .doubleValue();
                boolean conHang = sanPham.getSoLuongTonKho() > 0;
                
                outputData = GetProductDetailOutputData.forSuccess(
                    sanPham.getMaSanPham(),
                    sanPham.getTenSanPham(),
                    sanPham.getMoTa(),
                    specifications,
                    giaGoc,
                    giaSauKhuyenMai,
                    phanTramGiam,
                    sanPham.getSoLuongTonKho(),
                    conHang
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
    
    private Map<String, String> parseSpecifications(String specString) {
        Map<String, String> specs = new HashMap<>();
        if (specString == null || specString.isEmpty()) {
            return specs;
        }
        
        // Parse the specifications string into key-value pairs
        String[] lines = specString.split("\n");
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;
            
            int colonIndex = line.indexOf(":");
            if (colonIndex > 0) {
                String key = line.substring(0, colonIndex).trim();
                String value = line.substring(colonIndex + 1).trim();
                specs.put(key, value);
            }
        }
        
        return specs;
    }
}
