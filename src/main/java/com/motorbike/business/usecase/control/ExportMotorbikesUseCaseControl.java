package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.motorbike.ExportMotorbikesInputData;
import com.motorbike.business.dto.motorbike.ExportMotorbikesOutputData;
import com.motorbike.business.ports.exporter.ExcelExporter;
import com.motorbike.business.ports.repository.MotorbikeRepository;
import com.motorbike.business.usecase.input.ExportMotorbikesInputBoundary;
import com.motorbike.business.usecase.output.ExportMotorbikesOutputBoundary;
import com.motorbike.business.dto.generateexcelfile.GenerateExcelFileInputData;
import com.motorbike.business.dto.formatdataforexport.FormatDataForExportInputData;
import com.motorbike.business.usecase.input.GenerateExcelFileInputBoundary;
import com.motorbike.business.usecase.input.FormatDataForExportInputBoundary;
import com.motorbike.domain.entities.XeMay;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Use Case Control: Export danh sách xe máy ra file Excel
 * 
 * Business logic:
 * 1. Lấy danh sách xe máy từ repository (có thể filter)
 * 2. Chuyển đổi entities thành rows data
 * 3. Sử dụng ExcelExporter để tạo file
 * 4. Trả về file Excel qua Output Boundary
 */
public class ExportMotorbikesUseCaseControl implements ExportMotorbikesInputBoundary {
    
    private final ExportMotorbikesOutputBoundary outputBoundary;
    private final MotorbikeRepository motorbikeRepository;
    private final ExcelExporter excelExporter;
    private final FormatDataForExportInputBoundary formatDataForExportUseCase;
    private final GenerateExcelFileInputBoundary generateExcelFileUseCase;
    
    public ExportMotorbikesUseCaseControl(
            ExportMotorbikesOutputBoundary outputBoundary,
            MotorbikeRepository motorbikeRepository,
            ExcelExporter excelExporter,
            FormatDataForExportInputBoundary formatDataForExportUseCase,
            GenerateExcelFileInputBoundary generateExcelFileUseCase
    ) {
        this.outputBoundary = outputBoundary;
        this.motorbikeRepository = motorbikeRepository;
        this.excelExporter = excelExporter;
        this.formatDataForExportUseCase = formatDataForExportUseCase;
        this.generateExcelFileUseCase = generateExcelFileUseCase;
    }

    public ExportMotorbikesUseCaseControl(
            ExportMotorbikesOutputBoundary outputBoundary,
            MotorbikeRepository motorbikeRepository,
            FormatDataForExportInputBoundary formatDataForExportUseCase,
            GenerateExcelFileInputBoundary generateExcelFileUseCase
    ) {
        this.outputBoundary = outputBoundary;
        this.motorbikeRepository = motorbikeRepository;
        this.excelExporter = null;
        this.formatDataForExportUseCase = formatDataForExportUseCase;
        this.generateExcelFileUseCase = generateExcelFileUseCase;
    }

    // Constructor with SanPhamRepository and 4 parameters (for backward compatibility)
    public ExportMotorbikesUseCaseControl(
            com.motorbike.business.ports.SanPhamRepository sanPhamRepository,
            GenerateExcelFileInputBoundary generateExcelFileUseCase,
            FormatDataForExportInputBoundary formatDataForExportUseCase,
            ExportMotorbikesOutputBoundary outputBoundary
    ) {
        this.outputBoundary = outputBoundary;
        this.motorbikeRepository = (MotorbikeRepository) sanPhamRepository;
        this.excelExporter = null;
        this.formatDataForExportUseCase = formatDataForExportUseCase;
        this.generateExcelFileUseCase = generateExcelFileUseCase;
    }
    
    @Override
    public void execute(ExportMotorbikesInputData inputData) {
        try {
            // Step 1: Lấy danh sách xe máy
            List<XeMay> motorbikes;
            
            if (inputData != null && hasFilters(inputData)) {
                // Nếu có filters, search theo keyword only (MotorbikeRepository chỉ có searchMotorbikes(String))
                motorbikes = motorbikeRepository.searchMotorbikes(inputData.getKeyword());
            } else {
                // Nếu không có filters, lấy tất cả
                motorbikes = motorbikeRepository.findAllMotorbikes();
            }
            
            // Step 2: Tạo headers
            List<String> headers = createHeaders();
            
            // Step 3: Convert entities sang rows
            List<List<String>> rows = convertMotorbikesToRows(motorbikes);
            
            // Step 4: Export ra Excel
            ByteArrayOutputStream outputStream = excelExporter.exportToExcel(headers, rows);
            
            // Step 5: Tạo filename với timestamp
            String fileName = generateFileName();
            
            // Step 6: Tạo output data
            ExportMotorbikesOutputData outputData = new ExportMotorbikesOutputData(
                outputStream.toByteArray(),
                fileName,
                motorbikes.size()
            );
            
            // Step 7: Present success
            outputBoundary.presentSuccess(outputData);
            
        } catch (Exception e) {
            // Present error
            outputBoundary.presentError("Lỗi khi export xe máy: " + e.getMessage());
        }
    }
    
    /**
     * Kiểm tra có filters hay không
     */
    private boolean hasFilters(ExportMotorbikesInputData inputData) {
        return (inputData.getKeyword() != null && !inputData.getKeyword().trim().isEmpty())
            || (inputData.getBrand() != null && !inputData.getBrand().trim().isEmpty())
            || (inputData.getModel() != null && !inputData.getModel().trim().isEmpty())
            || (inputData.getColor() != null && !inputData.getColor().trim().isEmpty());
    }
    
    /**
     * Tạo danh sách headers cho file Excel
     */
    private List<String> createHeaders() {
        List<String> headers = new ArrayList<>();
        headers.add("Tên SP");
        headers.add("Mô tả");
        headers.add("Giá");
        headers.add("Hình ảnh");
        headers.add("Tồn kho");
        headers.add("Hãng");
        headers.add("Dòng");
        headers.add("Màu");
        headers.add("Năm SX");
        headers.add("Dung tích");
        return headers;
    }
    
    /**
     * Convert danh sách XeMay entities sang rows data
     */
    private List<List<String>> convertMotorbikesToRows(List<XeMay> motorbikes) {
        List<List<String>> rows = new ArrayList<>();
        
        for (XeMay motorbike : motorbikes) {
            List<String> row = new ArrayList<>();
            
            // Lấy dữ liệu từ entity (từ cả XeMay và SanPham cha)
            row.add(motorbike.getTenSanPham() != null ? motorbike.getTenSanPham() : "");
            row.add(motorbike.getMoTa() != null ? motorbike.getMoTa() : "");
            row.add(motorbike.getGia() != null ? motorbike.getGia().toString() : "0");
            row.add(motorbike.getHinhAnh() != null ? motorbike.getHinhAnh() : "");
            row.add(String.valueOf(motorbike.getSoLuongTonKho()));
            row.add(motorbike.getHangXe() != null ? motorbike.getHangXe() : "");
            row.add(motorbike.getDongXe() != null ? motorbike.getDongXe() : "");
            row.add(motorbike.getMauSac() != null ? motorbike.getMauSac() : "");
            row.add(String.valueOf(motorbike.getNamSanXuat()));
            row.add(String.valueOf(motorbike.getDungTich()));
            
            rows.add(row);
        }
        
        return rows;
    }
    
    /**
     * Tạo tên file với timestamp
     */
    private String generateFileName() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));
        return "motorbikes-export-" + timestamp + ".xlsx";
    }
}
