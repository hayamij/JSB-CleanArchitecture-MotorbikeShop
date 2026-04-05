package com.motorbike.infrastructure.parser;

import com.motorbike.business.ports.parser.ExcelParser;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
public class ApachePOIExcelParser implements ExcelParser {
    
    @Override
    public List<List<String>> parseExcelFile(InputStream inputStream) throws Exception {
        List<List<String>> rows = new ArrayList<>();
        
        try (Workbook workbook = WorkbookFactory.create(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0); 
            
            for (Row row : sheet) {
                List<String> rowData = new ArrayList<>();
                
                for (Cell cell : row) {
                    rowData.add(getCellValueAsString(cell));
                }
                
                rows.add(rowData);
            }
        }
        
        return rows;
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }
        
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
                
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    double numValue = cell.getNumericCellValue();
                    if (numValue == (long) numValue) {
                        return String.valueOf((long) numValue);
                    } else {
                        return String.valueOf(numValue);
                    }
                }
                
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
                
            case FORMULA:
                return cell.getCellFormula();
                
            case BLANK:
                return "";
                
            default:
                return "";
        }
    }
}
