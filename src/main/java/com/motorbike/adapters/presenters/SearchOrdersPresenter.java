package com.motorbike.adapters.presenters;

import com.motorbike.adapters.viewmodels.SearchOrdersViewModel;
import com.motorbike.business.dto.order.SearchOrdersOutputData;
import com.motorbike.business.usecase.output.SearchOrdersOutputBoundary;

import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

public class SearchOrdersPresenter implements SearchOrdersOutputBoundary {
    
    private final SearchOrdersViewModel viewModel;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    
    public SearchOrdersPresenter(SearchOrdersViewModel viewModel) {
        this.viewModel = viewModel;
    }
    
    @Override
    public void present(SearchOrdersOutputData outputData) {
        if (outputData.isSuccess()) {
            presentSuccess(outputData);
        } else {
            presentError(outputData);
        }
    }
    
    private void presentSuccess(SearchOrdersOutputData outputData) {
        viewModel.success = true;
        viewModel.hasError = false;
        viewModel.orders = outputData.getOrders().stream()
            .map(item -> {
                SearchOrdersViewModel.OrderItem vmItem = new SearchOrdersViewModel.OrderItem();
                vmItem.maDonHang = item.getMaDonHang();
                vmItem.maTaiKhoan = item.getMaTaiKhoan();
                vmItem.emailKhachHang = item.getEmailKhachHang();
                vmItem.tenNguoiNhan = item.getTenNguoiNhan();
                vmItem.soDienThoai = item.getSoDienThoai();
                vmItem.diaChiGiaoHang = item.getDiaChiGiaoHang();
                vmItem.tongTien = item.getTongTien();
                vmItem.trangThai = item.getTrangThai();
                vmItem.ngayDat = item.getNgayDat() != null ? item.getNgayDat().format(DATE_FORMATTER) : "";
                vmItem.ngayCapNhat = item.getNgayCapNhat() != null ? item.getNgayCapNhat().format(DATE_FORMATTER) : "";
                vmItem.soMatHang = item.getSoMatHang();
                vmItem.sanPham = item.getSanPham().stream()
                    .map(product -> {
                        SearchOrdersViewModel.ProductItem vmProduct = new SearchOrdersViewModel.ProductItem();
                        vmProduct.maSanPham = product.getMaSanPham();
                        vmProduct.tenSanPham = product.getTenSanPham();
                        vmProduct.giaBan = product.getGiaBan();
                        vmProduct.soLuong = product.getSoLuong();
                        vmProduct.thanhTien = product.getThanhTien();
                        return vmProduct;
                    })
                    .collect(Collectors.toList());
                return vmItem;
            })
            .collect(Collectors.toList());
    }
    
    private void presentError(SearchOrdersOutputData outputData) {
        viewModel.success = false;
        viewModel.hasError = true;
        viewModel.errorCode = outputData.getErrorCode();
        viewModel.errorMessage = outputData.getErrorMessage();
    }
}
