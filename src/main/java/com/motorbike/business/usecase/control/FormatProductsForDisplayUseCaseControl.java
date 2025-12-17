package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.product.FormatProductsForDisplayInputData;
import com.motorbike.business.dto.product.FormatProductsForDisplayOutputData;
import com.motorbike.business.dto.product.GetAllProductsOutputData.ProductInfo;
import com.motorbike.business.usecase.input.FormatProductsForDisplayInputBoundary;
import com.motorbike.business.usecase.output.FormatProductsForDisplayOutputBoundary;
import com.motorbike.domain.entities.SanPham;
import com.motorbike.domain.entities.XeMay;
import com.motorbike.domain.entities.PhuKienXeMay;

import java.util.List;
import java.util.stream.Collectors;

/**
 * UC-77: Format Products For Display
 * Secondary UseCase that maps SanPham entities to ProductInfo DTOs
 * Handles polymorphic mapping for XeMay and PhuKienXeMay
 * Used by: GetAllProducts
 */
public class FormatProductsForDisplayUseCaseControl implements FormatProductsForDisplayInputBoundary {

    private final FormatProductsForDisplayOutputBoundary outputBoundary;

    public FormatProductsForDisplayUseCaseControl(FormatProductsForDisplayOutputBoundary outputBoundary) {
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void execute(FormatProductsForDisplayInputData inputData) {
        FormatProductsForDisplayOutputData outputData = formatInternal(inputData);
        if (outputBoundary != null) {
            outputBoundary.present(outputData);
        }
    }

    /**
     * Internal method for direct delegation from parent usecases
     */
    public FormatProductsForDisplayOutputData formatInternal(FormatProductsForDisplayInputData inputData) {
        try {
            if (inputData == null || inputData.getProducts() == null) {
                return FormatProductsForDisplayOutputData.forError("INVALID_INPUT", "Input data cannot be null");
            }

            List<ProductInfo> products = inputData.getProducts().stream()
                    .map(this::mapToProductInfo)
                    .collect(Collectors.toList());

            return FormatProductsForDisplayOutputData.forSuccess(products);
        } catch (Exception e) {
            return FormatProductsForDisplayOutputData.forError("FORMAT_ERROR", e.getMessage());
        }
    }

    private ProductInfo mapToProductInfo(SanPham product) {
        if (product instanceof XeMay) {
            XeMay xeMay = (XeMay) product;
            return new ProductInfo(
                xeMay.getMaSanPham(),
                xeMay.getTenSanPham(),
                xeMay.getMoTa(),
                xeMay.getGia(),
                xeMay.getSoLuongTonKho(),
                xeMay.getHinhAnh(),
                xeMay.getNgayTao(),
                xeMay.isConHang(),
                xeMay.getHangXe(),
                xeMay.getDongXe(),
                xeMay.getMauSac(),
                xeMay.getNamSanXuat(),
                xeMay.getDungTich()
            );
        } else if (product instanceof PhuKienXeMay) {
            PhuKienXeMay phuKien = (PhuKienXeMay) product;
            return new ProductInfo(
                phuKien.getMaSanPham(),
                phuKien.getTenSanPham(),
                phuKien.getMoTa(),
                phuKien.getGia(),
                phuKien.getSoLuongTonKho(),
                phuKien.getHinhAnh(),
                phuKien.getNgayTao(),
                phuKien.isConHang(),
                phuKien.getLoaiPhuKien(),
                phuKien.getThuongHieu(),
                phuKien.getChatLieu(),
                phuKien.getKichThuoc()
            );
        } else {
            // Generic product (should not happen in this domain, but handle gracefully)
            return new ProductInfo(
                product.getMaSanPham(),
                product.getTenSanPham(),
                product.getMoTa(),
                product.getGia(),
                product.getSoLuongTonKho(),
                product.getHinhAnh(),
                product.getNgayTao(),
                product.isConHang(),
                null, null, null, null
            );
        }
    }
}
