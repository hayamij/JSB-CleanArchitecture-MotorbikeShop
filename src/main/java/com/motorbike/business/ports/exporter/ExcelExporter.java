package com.motorbike.business.ports.exporter;

import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * Port (Interface) cho việc xuất dữ liệu ra file Excel.
 * 
 * Business layer định nghĩa contract này.
 * Infrastructure layer sẽ implement (ví dụ: dùng Apache POI).
 */
public interface ExcelExporter {
    
    /**
     * Xuất dữ liệu ra file Excel dạng byte array
     * 
     * @param headers Danh sách header columns
     * @param rows Danh sách các row data, mỗi row là một List<String>
     * @return ByteArrayOutputStream chứa file Excel
     * @throws Exception nếu có lỗi khi tạo file
     */
    ByteArrayOutputStream exportToExcel(List<String> headers, List<List<String>> rows) throws Exception;
}
