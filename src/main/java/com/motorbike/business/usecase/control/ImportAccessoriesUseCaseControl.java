package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.accessory.ImportAccessoriesInputData;
import com.motorbike.business.dto.accessory.ImportAccessoriesOutputData;
import com.motorbike.business.dto.accessory.ImportAccessoriesOutputData.ImportError;
import com.motorbike.business.ports.parser.ExcelParser;
import com.motorbike.business.ports.repository.AccessoryRepository;
import com.motorbike.business.usecase.input.ImportAccessoriesInputBoundary;
import com.motorbike.business.usecase.output.ImportAccessoriesOutputBoundary;
import com.motorbike.business.dto.validateexcelfile.ValidateExcelFileInputData;
import com.motorbike.business.dto.parseexceldata.ParseExcelDataInputData;
import com.motorbike.business.dto.validateimportrow.ValidateImportRowInputData;
import com.motorbike.business.dto.generateimportreport.GenerateImportReportInputData;
import com.motorbike.business.usecase.input.ValidateExcelFileInputBoundary;
import com.motorbike.business.usecase.input.ParseExcelDataInputBoundary;
import com.motorbike.business.usecase.input.ValidateImportRowInputBoundary;
import com.motorbike.business.usecase.input.GenerateImportReportInputBoundary;
import com.motorbike.domain.entities.PhuKienXeMay;
import com.motorbike.domain.entities.SanPham;
import com.motorbike.domain.exceptions.ValidationException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Use Case Control: Import phụ kiện hàng loạt từ file Excel/CSV.
 * 
 * Business logic:
 * 1. Parse file Excel/CSV
 * 2. Validate từng dòng dữ liệu
 * 3. Tạo entity PhuKienXeMay cho các dòng hợp lệ
 * 4. Lưu hàng loạt vào database
 * 5. Thu thập lỗi để báo cáo
 */
public class ImportAccessoriesUseCaseControl implements ImportAccessoriesInputBoundary {
    
    private final ImportAccessoriesOutputBoundary outputBoundary;
    private final AccessoryRepository accessoryRepository;
    private final ExcelParser excelParser;
    private final ValidateExcelFileInputBoundary validateExcelFileUseCase;
    private final ParseExcelDataInputBoundary parseExcelDataUseCase;
    private final ValidateImportRowInputBoundary validateImportRowUseCase;
    private final GenerateImportReportInputBoundary generateImportReportUseCase;
    
    public ImportAccessoriesUseCaseControl(
            ImportAccessoriesOutputBoundary outputBoundary,
            AccessoryRepository accessoryRepository,
            ExcelParser excelParser,
            ValidateExcelFileInputBoundary validateExcelFileUseCase,
            ParseExcelDataInputBoundary parseExcelDataUseCase,
            ValidateImportRowInputBoundary validateImportRowUseCase,
            GenerateImportReportInputBoundary generateImportReportUseCase
    ) {
        this.outputBoundary = outputBoundary;
        this.accessoryRepository = accessoryRepository;
        this.excelParser = excelParser;
        this.validateExcelFileUseCase = validateExcelFileUseCase;
        this.parseExcelDataUseCase = parseExcelDataUseCase;
        this.validateImportRowUseCase = validateImportRowUseCase;
        this.generateImportReportUseCase = generateImportReportUseCase;
    }

    public ImportAccessoriesUseCaseControl(
            ImportAccessoriesOutputBoundary outputBoundary,
            AccessoryRepository accessoryRepository,
            ValidateExcelFileInputBoundary validateExcelFileUseCase,
            ParseExcelDataInputBoundary parseExcelDataUseCase,
            ValidateImportRowInputBoundary validateImportRowUseCase
    ) {
        this.outputBoundary = outputBoundary;
        this.accessoryRepository = accessoryRepository;
        this.excelParser = null;
        this.validateExcelFileUseCase = validateExcelFileUseCase;
        this.parseExcelDataUseCase = parseExcelDataUseCase;
        this.validateImportRowUseCase = validateImportRowUseCase;
        this.generateImportReportUseCase = null;
    }

    // Constructor with 5 parameters (for backward compatibility)
    public ImportAccessoriesUseCaseControl(
            ValidateExcelFileInputBoundary validateExcelFileUseCase,
            ParseExcelDataInputBoundary parseExcelDataUseCase,
            ValidateImportRowInputBoundary validateImportRowUseCase,
            GenerateImportReportInputBoundary generateImportReportUseCase,
            ImportAccessoriesOutputBoundary outputBoundary
    ) {
        this.outputBoundary = outputBoundary;
        this.accessoryRepository = null;
        this.excelParser = null;
        this.validateExcelFileUseCase = validateExcelFileUseCase;
        this.parseExcelDataUseCase = parseExcelDataUseCase;
        this.validateImportRowUseCase = validateImportRowUseCase;
        this.generateImportReportUseCase = generateImportReportUseCase;
    }
    
    @Override
    public void execute(ImportAccessoriesInputData inputData) {
        ImportAccessoriesOutputData outputData;
        
        try {
            // Step 1: Basic validation
            if (inputData == null || inputData.getFileInputStream() == null) {
                throw ValidationException.invalidInput();
            }
            
            // Step 2: UC-62 - Validate Excel file
            List<String> expectedColumns = List.of(
                "Tên sản phẩm", "Mô tả", "Giá", "Hình ảnh", "Số lượng tồn kho",
                "Loại phụ kiện", "Thương hiệu", "Chất liệu"
            );
            ValidateExcelFileInputData validateFileInput = new ValidateExcelFileInputData(
                inputData.getFileInputStream(),
                inputData.getOriginalFilename(),
                expectedColumns
            );
            var validateFileResult = ((ValidateExcelFileUseCaseControl) validateExcelFileUseCase)
                .validateInternal(validateFileInput);
            
            if (!validateFileResult.isValid()) {
                outputData = ImportAccessoriesOutputData.forError(
                    validateFileResult.getErrorCode(),
                    String.join("; ", validateFileResult.getErrors())
                );
                outputBoundary.present(outputData);
                return;
            }
            
            // Step 3: UC-63 - Parse Excel file
            ParseExcelDataInputData parseInput = new ParseExcelDataInputData(
                inputData.getFileInputStream()
            );
            var parseResult = ((ParseExcelDataUseCaseControl) parseExcelDataUseCase)
                .parseInternal(parseInput);
            
            if (!parseResult.isSuccess()) {
                outputData = ImportAccessoriesOutputData.forError(
                    parseResult.getErrorCode(),
                    parseResult.getErrorMessage()
                );
                outputBoundary.present(outputData);
                return;
            }
            
            var rows = parseResult.getRows();
            
            if (rows == null || rows.isEmpty()) {
                outputData = ImportAccessoriesOutputData.forError(
                    "EMPTY_FILE",
                    "File không có dữ liệu"
                );
                outputBoundary.present(outputData);
                return;
            }
            
            // Step 3: Process rows (skip header row)
            List<PhuKienXeMay> validAccessories = new ArrayList<>();
            List<ImportError> errors = new ArrayList<>();
            int totalRecords = rows.size() - 1; // Exclude header
            
            for (int i = 1; i < rows.size(); i++) {
                int rowNumber = i + 1; // Excel row number (1-based, accounting for header)
                var rowMap = rows.get(i);
                List<String> row = new java.util.ArrayList<>(rowMap.values());
                
                try {
                    PhuKienXeMay phuKien = parseRowToAccessory(row, rowNumber);
                    validAccessories.add(phuKien);
                } catch (Exception e) {
                    errors.add(new ImportError(rowNumber, "", e.getMessage()));
                }
            }
            
            // Step 4: Save valid accessories in bulk
            int successCount = 0;
            if (!validAccessories.isEmpty()) {
                try {
                    accessoryRepository.saveAll(validAccessories);
                    successCount = validAccessories.size();
                } catch (Exception e) {
                    // If bulk save fails, try individual saves
                    for (int i = 0; i < validAccessories.size(); i++) {
                        try {
                            accessoryRepository.save(validAccessories.get(i));
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
            outputData = ImportAccessoriesOutputData.forSuccess(
                totalRecords,
                successCount,
                failureCount,
                errors
            );
            
        } catch (Exception e) {
            String errorCode = extractErrorCode(e);
            outputData = ImportAccessoriesOutputData.forError(errorCode, e.getMessage());
        }
        
        outputBoundary.present(outputData);
    }
    
    /**
     * Parse một dòng Excel thành PhuKienXeMay entity
     * 
     * Cấu trúc Excel (8 cột):
     * 0: Tên sản phẩm
     * 1: Mô tả
     * 2: Giá
     * 3: Hình ảnh
     * 4: Số lượng tồn kho
     * 5: Loại phụ kiện
     * 6: Thương hiệu
     * 7: Chất liệu
     */
    private PhuKienXeMay parseRowToAccessory(List<String> row, int rowNumber) throws Exception {
        if (row.size() < 8) {
            throw new ValidationException("Dòng " + rowNumber + ": Không đủ cột (cần 8 cột)", "INVALID_ROW");
        }
        
        try {
            String tenSanPham = getStringValue(row, 0);
            String moTa = getStringValue(row, 1);
            BigDecimal gia = getBigDecimalValue(row, 2, rowNumber, "Giá");
            String hinhAnh = getStringValue(row, 3);
            int soLuongTonKho = getIntValue(row, 4, rowNumber, "Số lượng tồn kho");
            String loaiPhuKien = getStringValue(row, 5);
            String thuongHieu = getStringValue(row, 6);
            String chatLieu = getStringValue(row, 7);
            
            // Validate using domain rules
            SanPham.validateTenSanPham(tenSanPham);
            SanPham.validateGia(gia);
            SanPham.validateSoLuongTonKho(soLuongTonKho);
            
            validateAccessoryFields(loaiPhuKien, thuongHieu, chatLieu, rowNumber);
            
            // Create entity - note: kichThuoc is optional, set to empty string
            return new PhuKienXeMay(
                tenSanPham, moTa, gia, hinhAnh, soLuongTonKho,
                loaiPhuKien, thuongHieu, chatLieu, ""
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
    
    private void validateAccessoryFields(String loaiPhuKien, String thuongHieu, String chatLieu, int rowNumber) 
            throws ValidationException {
        if (loaiPhuKien == null || loaiPhuKien.trim().isEmpty()) {
            throw ValidationException.fieldRequired("Loại phụ kiện (dòng " + rowNumber + ")");
        }
        if (thuongHieu == null || thuongHieu.trim().isEmpty()) {
            throw ValidationException.fieldRequired("Thương hiệu (dòng " + rowNumber + ")");
        }
        if (chatLieu == null || chatLieu.trim().isEmpty()) {
            throw ValidationException.fieldRequired("Chất liệu (dòng " + rowNumber + ")");
        }
    }
    
    private void validateFileExtension(String filename) throws ValidationException {
        if (filename == null || filename.trim().isEmpty()) {
            throw new ValidationException("Tên file không hợp lệ", "INVALID_FILENAME");
        }
        
        String lowerFilename = filename.toLowerCase();
        if (!lowerFilename.endsWith(".xlsx") && !lowerFilename.endsWith(".xls") && !lowerFilename.endsWith(".csv")) {
            throw new ValidationException(
                "File không đúng định dạng. Chỉ chấp nhận file .xlsx, .xls hoặc .csv",
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
