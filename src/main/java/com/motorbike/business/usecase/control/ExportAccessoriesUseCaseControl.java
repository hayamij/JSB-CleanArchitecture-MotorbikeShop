package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.accessory.ExportAccessoriesInputData;
import com.motorbike.business.dto.accessory.ExportAccessoriesOutputData;
import com.motorbike.business.ports.exporter.CSVExporter;
import com.motorbike.business.ports.repository.AccessoryRepository;
import com.motorbike.business.usecase.input.ExportAccessoriesInputBoundary;
import com.motorbike.business.usecase.output.ExportAccessoriesOutputBoundary;
import com.motorbike.domain.entities.PhuKienXeMay;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Use Case Control: Export danh sách phụ kiện ra file Excel
 */
public class ExportAccessoriesUseCaseControl implements ExportAccessoriesInputBoundary {
    
    private final ExportAccessoriesOutputBoundary outputBoundary;
    private final AccessoryRepository accessoryRepository;
    private final CSVExporter csvExporter;
    
    public ExportAccessoriesUseCaseControl(
            ExportAccessoriesOutputBoundary outputBoundary,
            AccessoryRepository accessoryRepository,
            CSVExporter csvExporter
    ) {
        this.outputBoundary = outputBoundary;
        this.accessoryRepository = accessoryRepository;
        this.csvExporter = csvExporter;
    }
    
    @Override
    public void execute(ExportAccessoriesInputData inputData) {
        try {
            // Step 1: Lấy danh sách phụ kiện
            List<PhuKienXeMay> accessories;
            
            if (inputData != null && hasFilters(inputData)) {
                // Nếu có filters, search theo filters
                accessories = accessoryRepository.search(
                    inputData.getKeyword(),
                    inputData.getType(),
                    inputData.getBrand()
                );
            } else {
                // Nếu không có filters, lấy tất cả
                accessories = accessoryRepository.findAllAccessories();
            }
            
            // Step 2: Tạo headers
            List<String> headers = createHeaders();
            
            // Step 3: Convert entities sang rows
            List<List<String>> rows = convertAccessoriesToRows(accessories);
            
            // Step 4: Export ra CSV
            ByteArrayOutputStream outputStream = csvExporter.exportToCSV(headers, rows);
            
            // Step 5: Tạo filename với timestamp
            String fileName = generateFileName();
            
            // Step 6: Tạo output data
            ExportAccessoriesOutputData outputData = new ExportAccessoriesOutputData(
                outputStream.toByteArray(),
                fileName,
                accessories.size()
            );
            
            // Step 7: Present success
            outputBoundary.presentSuccess(outputData);
            
        } catch (Exception e) {
            outputBoundary.presentError("Lỗi khi export phụ kiện: " + e.getMessage());
        }
    }
    
    private boolean hasFilters(ExportAccessoriesInputData inputData) {
        return (inputData.getKeyword() != null && !inputData.getKeyword().trim().isEmpty())
            || (inputData.getType() != null && !inputData.getType().trim().isEmpty())
            || (inputData.getBrand() != null && !inputData.getBrand().trim().isEmpty())
            || (inputData.getMaterial() != null && !inputData.getMaterial().trim().isEmpty());
    }
    
    private List<String> createHeaders() {
        List<String> headers = new ArrayList<>();
        headers.add("Tên SP");
        headers.add("Mô tả");
        headers.add("Giá");
        headers.add("Hình ảnh");
        headers.add("Tồn kho");
        headers.add("Loại");
        headers.add("Thương hiệu");
        headers.add("Chất liệu");
        return headers;
    }
    
    private List<List<String>> convertAccessoriesToRows(List<PhuKienXeMay> accessories) {
        List<List<String>> rows = new ArrayList<>();
        
        for (PhuKienXeMay accessory : accessories) {
            List<String> row = new ArrayList<>();
            
            row.add(accessory.getTenSanPham() != null ? accessory.getTenSanPham() : "");
            row.add(accessory.getMoTa() != null ? accessory.getMoTa() : "");
            row.add(accessory.getGia() != null ? accessory.getGia().toString() : "0");
            row.add(accessory.getHinhAnh() != null ? accessory.getHinhAnh() : "");
            row.add(String.valueOf(accessory.getSoLuongTonKho()));
            row.add(accessory.getLoaiPhuKien() != null ? accessory.getLoaiPhuKien() : "");
            row.add(accessory.getThuongHieu() != null ? accessory.getThuongHieu() : "");
            row.add(accessory.getChatLieu() != null ? accessory.getChatLieu() : "");
            
            rows.add(row);
        }
        
        return rows;
    }
    
    private String generateFileName() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));
        return "accessories-export-" + timestamp + ".csv";
    }
}
