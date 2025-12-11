package com.motorbike.infrastructure.exporter;

import com.motorbike.business.ports.exporter.ExcelExporter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Component
public class ApachePOIExcelExporter implements ExcelExporter {
    
    @Override
    public ByteArrayOutputStream exportToExcel(List<String> headers, List<List<String>> rows) throws Exception {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Data");
            
            CellStyle headerStyle = createHeaderStyle(workbook);
            
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.size(); i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers.get(i));
                cell.setCellStyle(headerStyle);
            }
            
            for (int i = 0; i < rows.size(); i++) {
                Row row = sheet.createRow(i + 1);
                List<String> rowData = rows.get(i);
                
                for (int j = 0; j < rowData.size(); j++) {
                    Cell cell = row.createCell(j);
                    cell.setCellValue(rowData.get(j));
                }
            }
            
            for (int i = 0; i < headers.size(); i++) {
                sheet.autoSizeColumn(i);
            }
            
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            
            return outputStream;
        }
    }
    
    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        
        // Set background color
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        
        // Set borders
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        
        return style;
    }
}
