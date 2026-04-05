package com.motorbike.business.ports.exporter;

import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * Port (Interface) cho việc xuất dữ liệu ra file CSV.
 * 
 * Business layer định nghĩa contract này.
 * Infrastructure layer sẽ implement.
 */
public interface CSVExporter {
    
    /**
     * Xuất dữ liệu ra file CSV dạng byte array
     * 
     * @param headers Danh sách header columns
     * @param rows Danh sách các row data, mỗi row là một List<String>
     * @return ByteArrayOutputStream chứa file CSV
     * @throws Exception nếu có lỗi khi tạo file
     */
    ByteArrayOutputStream exportToCSV(List<String> headers, List<List<String>> rows) throws Exception;
}
