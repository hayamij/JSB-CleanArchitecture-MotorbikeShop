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
            .orElseThrow(() -> DomainException.productNotFound(String.valueOf(inputData.productId)));
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
        checkInputNotNull(inputData);
        SanPham.checkInput(inputData.productId, 1);
    }
    
    @Override
    protected void handleValidationError(IllegalArgumentException e) {
        String errorCode = "INVALID_INPUT";
        if (e instanceof ValidationException) {
            errorCode = ((ValidationException) e).getErrorCode();
        }
        GetProductDetailOutputData outputData = GetProductDetailOutputData.forError(errorCode, e.getMessage());
        outputBoundary.present(outputData);
    }
    
    @Override
    protected void handleSystemError(Exception e) {
        String errorCode;
        String message;
        
        if (e instanceof DomainException) {
            DomainException ex = (DomainException) e;
            errorCode = ex.getErrorCode();
            message = ex.getMessage();
        } else if (e instanceof com.motorbike.domain.exceptions.SystemException) {
            com.motorbike.domain.exceptions.SystemException ex = (com.motorbike.domain.exceptions.SystemException) e;
            errorCode = ex.getErrorCode();
            message = ex.getMessage();
        } else {
            throw new com.motorbike.domain.exceptions.SystemException(e);
        }
        
        GetProductDetailOutputData outputData = GetProductDetailOutputData.forError(errorCode, message);
        outputBoundary.present(outputData);
    }
}
