package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.motorbike.ImportMotorbikesInputData;
import com.motorbike.business.dto.motorbike.ImportMotorbikesOutputData;
import com.motorbike.business.dto.motorbike.ImportMotorbikesOutputData.ImportError;
import com.motorbike.business.ports.parser.ExcelParser;
import com.motorbike.business.ports.repository.MotorbikeRepository;
import com.motorbike.business.usecase.input.ImportMotorbikesInputBoundary;
import com.motorbike.business.usecase.output.ImportMotorbikesOutputBoundary;
import com.motorbike.domain.entities.SanPham;
import com.motorbike.domain.entities.XeMay;
import com.motorbike.domain.exceptions.ValidationException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Use Case Control: Import xe máy hàng loạt từ file Excel.
 * 
 * Business logic:
 * 1. Parse file Excel
 * 2. Validate từng dòng dữ liệu
 * 3. Tạo entity XeMay cho các dòng hợp lệ
 * 4. Lưu hàng loạt vào database
 * 5. Thu thập lỗi để báo cáo
 */
public class ImportMotorbikesUseCaseControl implements ImportMotorbikesInputBoundary {
    
    private final ImportMotorbikesOutputBoundary outputBoundary;
    private final MotorbikeRepository motorbikeRepository;
    private final ExcelParser excelParser;
    
    public ImportMotorbikesUseCaseControl(
            ImportMotorbikesOutputBoundary outputBoundary,
            MotorbikeRepository motorbikeRepository,
            ExcelParser excelParser
    ) {
        this.outputBoundary = outputBoundary;
        this.motorbikeRepository = motorbikeRepository;
        this.excelParser = excelParser;
    }
    
    @Override
    public void execute(ImportMotorbikesInputData inputData) {
        ImportMotorbikesOutputData outputData;
        
        try {
            // Step 1: Validate input
            if (inputData == null || inputData.getFileInputStream() == null) {
                throw ValidationException.invalidInput();
            }
            
            validateFileExtension(inputData.getOriginalFilename());
            
            // Step 2: Parse Excel file
            List<List<String>> rows = excelParser.parseExcelFile(inputData.getFileInputStream());
            
            if (rows == null || rows.isEmpty()) {
                outputData = ImportMotorbikesOutputData.forError(
                    "EMPTY_FILE",
                    "File Excel không có dữ liệu"
                );
                outputBoundary.present(outputData);
                return;
            }
            
            // Step 3: Process rows (skip header row)
            List<XeMay> validMotorbikes = new ArrayList<>();
            List<ImportError> errors = new ArrayList<>();
            int totalRecords = rows.size() - 1; // Exclude header
            
            for (int i = 1; i < rows.size(); i++) {
                int rowNumber = i + 1; // Excel row number (1-based, accounting for header)
                List<String> row = rows.get(i);
                
                try {
                    XeMay xeMay = parseRowToMotorbike(row, rowNumber);
                    validMotorbikes.add(xeMay);
                } catch (Exception e) {
                    errors.add(new ImportError(rowNumber, "", e.getMessage()));
                }
            }
            
            // Step 4: Save valid motorbikes in bulk
            int successCount = 0;
            if (!validMotorbikes.isEmpty()) {
                try {
                    motorbikeRepository.saveAll(validMotorbikes);
                    successCount = validMotorbikes.size();
                } catch (Exception e) {
                    // If bulk save fails, try individual saves
                    for (int i = 0; i < validMotorbikes.size(); i++) {
                        try {
                            motorbikeRepository.save(validMotorbikes.get(i));
                            successCount++;
                        } catch (Exception ex) {
                            errors.add(new ImportError(
                                i + 2, // Approximate row number
                                "",
                                "Lỗi khi lưu: " + ex.getMessage()
                            ));
                        }
                    }
                }
            }
            
            int failureCount = totalRecords - successCount;
            
            // Step 5: Present result
            outputData = ImportMotorbikesOutputData.forSuccess(
                totalRecords,
                successCount,
                failureCount,
                errors
            );
            
        } catch (Exception e) {
            String errorCode = extractErrorCode(e);
            outputData = ImportMotorbikesOutputData.forError(errorCode, e.getMessage());
        }
        
        outputBoundary.present(outputData);
    }
    
    /**
     * Parse một dòng Excel thành XeMay entity
     * 
     * Cấu trúc Excel (các cột):
     * 0: Tên sản phẩm
     * 1: Mô tả
     * 2: Giá
     * 3: Hình ảnh
     * 4: Số lượng tồn kho
     * 5: Hãng xe
     * 6: Dòng xe
     * 7: Màu sắc
     * 8: Năm sản xuất
     * 9: Dung tích
     */
    private XeMay parseRowToMotorbike(List<String> row, int rowNumber) throws Exception {
        if (row.size() < 10) {
            throw new ValidationException("Dòng " + rowNumber + ": Không đủ cột (cần 10 cột)", "INVALID_ROW");
        }
        
        try {
            String tenSanPham = getStringValue(row, 0);
            String moTa = getStringValue(row, 1);
            BigDecimal gia = getBigDecimalValue(row, 2, rowNumber, "Giá");
            String hinhAnh = getStringValue(row, 3);
            int soLuongTonKho = getIntValue(row, 4, rowNumber, "Số lượng tồn kho");
            String hangXe = getStringValue(row, 5);
            String dongXe = getStringValue(row, 6);
            String mauSac = getStringValue(row, 7);
            int namSanXuat = getIntValue(row, 8, rowNumber, "Năm sản xuất");
            int dungTich = getIntValue(row, 9, rowNumber, "Dung tích");
            
            // Validate using domain rules
            SanPham.validateTenSanPham(tenSanPham);
            SanPham.validateGia(gia);
            SanPham.validateSoLuongTonKho(soLuongTonKho);
            
            validateMotorbikeFields(hangXe, dongXe, mauSac, namSanXuat, dungTich, rowNumber);
            
            // Create entity
            return new XeMay(
                tenSanPham, moTa, gia, hinhAnh, soLuongTonKho,
                hangXe, dongXe, mauSac, namSanXuat, dungTich
            );
            
        } catch (NumberFormatException e) {
            throw new ValidationException(
                "Dòng " + rowNumber + ": Lỗi định dạng số - " + e.getMessage(),
                "INVALID_NUMBER_FORMAT"
            );
        }
    }
    
    private String getStringValue(List<String> row, int index) {
        if (index >= row.size()) return "";
        String value = row.get(index);
        return value != null ? value.trim() : "";
    }
    
    private BigDecimal getBigDecimalValue(List<String> row, int index, int rowNumber, String fieldName) 
            throws ValidationException {
        String value = getStringValue(row, index);
        if (value.isEmpty()) {
            throw ValidationException.fieldRequired(fieldName + " (dòng " + rowNumber + ")");
        }
        try {
            return new BigDecimal(value);
        } catch (NumberFormatException e) {
            throw new ValidationException(
                fieldName + " không hợp lệ tại dòng " + rowNumber + ": " + value,
                "INVALID_PRICE"
            );
        }
    }
    
    private int getIntValue(List<String> row, int index, int rowNumber, String fieldName) 
            throws ValidationException {
        String value = getStringValue(row, index);
        if (value.isEmpty()) {
            throw ValidationException.fieldRequired(fieldName + " (dòng " + rowNumber + ")");
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new ValidationException(
                fieldName + " không hợp lệ tại dòng " + rowNumber + ": " + value,
                "INVALID_NUMBER"
            );
        }
    }
    
    private void validateMotorbikeFields(String hangXe, String dongXe, String mauSac, 
                                        int namSanXuat, int dungTich, int rowNumber) 
            throws ValidationException {
        if (hangXe == null || hangXe.trim().isEmpty()) {
            throw ValidationException.fieldRequired("Hãng xe (dòng " + rowNumber + ")");
        }
        if (dongXe == null || dongXe.trim().isEmpty()) {
            throw ValidationException.fieldRequired("Dòng xe (dòng " + rowNumber + ")");
        }
        if (mauSac == null || mauSac.trim().isEmpty()) {
            throw ValidationException.fieldRequired("Màu sắc (dòng " + rowNumber + ")");
        }
        if (namSanXuat < 1900 || namSanXuat > 2100) {
            throw new ValidationException(
                "Năm sản xuất không hợp lệ (dòng " + rowNumber + "): " + namSanXuat,
                "INVALID_YEAR"
            );
        }
        if (dungTich <= 0) {
            throw new ValidationException(
                "Dung tích phải lớn hơn 0 (dòng " + rowNumber + ")",
                "INVALID_DISPLACEMENT"
            );
        }
    }
    
    private void validateFileExtension(String filename) throws ValidationException {
        if (filename == null || filename.trim().isEmpty()) {
            throw new ValidationException("Tên file không hợp lệ", "INVALID_FILENAME");
        }
        
        String lowerFilename = filename.toLowerCase();
        if (!lowerFilename.endsWith(".xlsx") && !lowerFilename.endsWith(".xls")) {
            throw new ValidationException(
                "File không đúng định dạng. Chỉ chấp nhận file .xlsx hoặc .xls",
                "INVALID_FILE_FORMAT"
            );
        }
    }
    
    private String extractErrorCode(Exception e) {
        if (e instanceof ValidationException) {
            return "VALIDATION_ERROR";
        }
        return "IMPORT_ERROR";
    }
}
