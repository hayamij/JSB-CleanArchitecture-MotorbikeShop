package com.motorbike.business.ports.parser;

import java.io.InputStream;
import java.util.List;

/**
 * Port (Interface) cho việc đọc file Excel.
 * 
 * Business layer định nghĩa contract này.
 * Infrastructure layer sẽ implement (ví dụ: dùng Apache POI).
 */
public interface ExcelParser {
    
    /**
     * Parse file Excel và trả về danh sách các row data
     * 
     * @param inputStream Stream của file Excel
     * @return Danh sách các row, mỗi row là một List<String>
     * @throws Exception nếu file không hợp lệ hoặc không đọc được
     */
    List<List<String>> parseExcelFile(InputStream inputStream) throws Exception;
}
