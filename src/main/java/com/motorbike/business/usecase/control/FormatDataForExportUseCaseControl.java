package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.formatdataforexport.FormatDataForExportInputData;
import com.motorbike.business.dto.formatdataforexport.FormatDataForExportOutputData;
import com.motorbike.business.usecase.input.FormatDataForExportInputBoundary;
import com.motorbike.business.usecase.output.FormatDataForExportOutputBoundary;
import com.motorbike.domain.entities.*;
import com.motorbike.domain.exceptions.ValidationException;

import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class FormatDataForExportUseCaseControl implements FormatDataForExportInputBoundary {
    private final FormatDataForExportOutputBoundary outputBoundary;

    public FormatDataForExportUseCaseControl(FormatDataForExportOutputBoundary outputBoundary) {
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void execute(FormatDataForExportInputData inputData) {
        FormatDataForExportOutputData outputData = formatInternal(inputData);
        outputBoundary.present(outputData);
    }

    public FormatDataForExportOutputData formatInternal(FormatDataForExportInputData inputData) {
        FormatDataForExportOutputData outputData = null;
        Exception errorException = null;

        // Step 1: Validation
        try {
            if (inputData == null) {
                throw ValidationException.invalidInput("FormatDataForExport");
            }
            if (inputData.getRawData() == null) {
                throw ValidationException.invalidInput("Raw data is required");
            }
        } catch (Exception e) {
            errorException = e;
        }

        // Step 2: Business logic - format data for export
        if (errorException == null) {
            try {
                List<?> rawData = inputData.getRawData();
                String exportType = inputData.getExportType();

                List<Map<String, Object>> formattedData;
                List<String> headers;

                // Format based on export type
                if ("motorbike".equalsIgnoreCase(exportType) || "xe_may".equalsIgnoreCase(exportType)) {
                    formattedData = formatMotorbikeData(rawData);
                    headers = getMotorbikeHeaders();
                } else if ("accessory".equalsIgnoreCase(exportType) || "phu_tung".equalsIgnoreCase(exportType)) {
                    formattedData = formatAccessoryData(rawData);
                    headers = getAccessoryHeaders();
                } else if ("user".equalsIgnoreCase(exportType) || "nguoi_dung".equalsIgnoreCase(exportType)) {
                    formattedData = formatUserData(rawData);
                    headers = getUserHeaders();
                } else if ("order".equalsIgnoreCase(exportType) || "don_hang".equalsIgnoreCase(exportType)) {
                    formattedData = formatOrderData(rawData);
                    headers = getOrderHeaders();
                } else {
                    // Generic formatting
                    formattedData = formatGenericData(rawData);
                    headers = getGenericHeaders(formattedData);
                }

                outputData = new FormatDataForExportOutputData(formattedData, headers);

            } catch (Exception e) {
                errorException = e;
            }
        }

        // Step 3: Handle error
        if (errorException != null) {
            String errorCode = errorException instanceof ValidationException
                    ? ((ValidationException) errorException).getErrorCode()
                    : "FORMAT_ERROR";
            outputData = FormatDataForExportOutputData.forError(errorCode, errorException.getMessage());
        }

        // Step 4: Return result
        return outputData;
    }

    private List<Map<String, Object>> formatMotorbikeData(List<?> rawData) {
        List<Map<String, Object>> formatted = new ArrayList<>();
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

        for (Object item : rawData) {
            if (item instanceof SanPham) {
                SanPham product = (SanPham) item;
                Map<String, Object> row = new LinkedHashMap<>();
                row.put("Mã sản phẩm", product.getMaSP());
                row.put("Tên xe", product.getTenSP());
                row.put("Giá bán", currencyFormat.format(product.getGia()));
                row.put("Số lượng tồn", product.getSoLuongTonKho());
                row.put("Giảm giá (%)", 0);
                row.put("Loại", product instanceof com.motorbike.domain.entities.XeMay ? "Xe máy" : "Phụ kiện");
                formatted.add(row);
            }
        }

        return formatted;
    }

    private List<String> getMotorbikeHeaders() {
        return Arrays.asList("Mã sản phẩm", "Tên xe", "Giá bán", "Số lượng tồn", "Giảm giá (%)", "Loại");
    }

    private List<Map<String, Object>> formatAccessoryData(List<?> rawData) {
        // Similar to motorbike
        return formatMotorbikeData(rawData);
    }

    private List<String> getAccessoryHeaders() {
        return Arrays.asList("Mã sản phẩm", "Tên phụ tùng", "Giá bán", "Số lượng tồn", "Giảm giá (%)", "Loại");
    }

    private List<Map<String, Object>> formatUserData(List<?> rawData) {
        List<Map<String, Object>> formatted = new ArrayList<>();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        for (Object item : rawData) {
            if (item instanceof NguoiDung) {
                NguoiDung user = (NguoiDung) item;
                Map<String, Object> row = new LinkedHashMap<>();
                row.put("Mã người dùng", user.getMaNguoiDung());
                row.put("Tên đăng nhập", user.getTenDangNhap());
                row.put("Email", user.getEmail());
                row.put("Họ tên", user.getHoTen());
                row.put("Số điện thoại", user.getSoDienThoai() != null ? user.getSoDienThoai() : "");
                row.put("Vai trò", user.getVaiTro().name());
                row.put("Ngày tạo", user.getNgayTao() != null ? user.getNgayTao().format(dateFormatter) : "");
                formatted.add(row);
            }
        }

        return formatted;
    }

    private List<String> getUserHeaders() {
        return Arrays.asList("Mã người dùng", "Tên đăng nhập", "Email", "Họ tên", "Số điện thoại", "Vai trò", "Ngày tạo");
    }

    private List<Map<String, Object>> formatOrderData(List<?> rawData) {
        List<Map<String, Object>> formatted = new ArrayList<>();
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        for (Object item : rawData) {
            if (item instanceof DonHang) {
                DonHang order = (DonHang) item;
                Map<String, Object> row = new LinkedHashMap<>();
                row.put("Mã đơn hàng", order.getMaDonHang());
                row.put("Người đặt", order.getNguoiDung().getHoTen());
                row.put("Tổng tiền", currencyFormat.format(order.getTongTien()));
                row.put("Trạng thái", order.getTrangThai().name());
                row.put("Ngày đặt", order.getNgayDatHang().format(dateFormatter));
                row.put("Phương thức thanh toán", order.getPhuongThucThanhToan());
                formatted.add(row);
            }
        }

        return formatted;
    }

    private List<String> getOrderHeaders() {
        return Arrays.asList("Mã đơn hàng", "Người đặt", "Tổng tiền", "Trạng thái", "Ngày đặt", "Phương thức thanh toán");
    }

    private List<Map<String, Object>> formatGenericData(List<?> rawData) {
        List<Map<String, Object>> formatted = new ArrayList<>();

        for (Object item : rawData) {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("Data", item.toString());
            formatted.add(row);
        }

        return formatted;
    }

    private List<String> getGenericHeaders(List<Map<String, Object>> formattedData) {
        if (formattedData.isEmpty()) {
            return Collections.singletonList("Data");
        }
        return new ArrayList<>(formattedData.get(0).keySet());
    }
}
