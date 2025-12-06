package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.product.GetAllProductsOutputData;
import com.motorbike.business.dto.product.GetAllProductsOutputData.ProductInfo;
import com.motorbike.business.ports.repository.ProductRepository;
import com.motorbike.business.usecase.input.GetAllProductsInputBoundary;
import com.motorbike.business.usecase.output.GetAllProductsOutputBoundary;
import com.motorbike.domain.entities.SanPham;
import com.motorbike.domain.entities.XeMay;
import com.motorbike.domain.entities.PhuKienXeMay;

import java.util.List;
import java.util.stream.Collectors;

public class GetAllProductsUseCaseControl implements GetAllProductsInputBoundary {

    private final GetAllProductsOutputBoundary outputBoundary;
    private final ProductRepository productRepository;

    public GetAllProductsUseCaseControl(
            GetAllProductsOutputBoundary outputBoundary,
            ProductRepository productRepository
    ) {
        this.outputBoundary = outputBoundary;
        this.productRepository = productRepository;
    }

    @Override
    public void execute() {
        GetAllProductsOutputData outputData = null;
        Exception errorException = null;

        // Business logic
        try {
            List<SanPham> allProducts = productRepository.findAll();

            List<ProductInfo> products = allProducts.stream()
                    .map(this::mapToProductInfo)
                    .collect(Collectors.toList());

            outputData = GetAllProductsOutputData.forSuccess(products);
        } catch (Exception e) {
            errorException = e;
        }

        // Error handling
        if (errorException != null) {
            String errorCode = "GET_ALL_PRODUCTS_ERROR";
            outputData = GetAllProductsOutputData.forError(errorCode, errorException.getMessage());
        }

        // Present
        outputBoundary.present(outputData);
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
