package com.motorbike.infrastructure.exporter;

import com.motorbike.business.ports.exporter.CSVExporter;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Implementation của CSVExporter sử dụng Java native API.
 * 
 * Infrastructure layer implement interface từ Business layer.
 */
@Component
public class SimpleCSVExporter implements CSVExporter {
    
    @Override
    public ByteArrayOutputStream exportToCSV(List<String> headers, List<List<String>> rows) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        
        try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8))) {
            // Write BOM for Excel UTF-8 recognition
            outputStream.write(new byte[]{(byte)0xEF, (byte)0xBB, (byte)0xBF});
            
            // Write headers
            writer.println(escapeCsvLine(headers));
            
            // Write data rows
            for (List<String> row : rows) {
                writer.println(escapeCsvLine(row));
            }
            
            writer.flush();
        }
        
        return outputStream;
    }
    
    /**
     * Escape CSV line - handle commas, quotes, and newlines
     */
    private String escapeCsvLine(List<String> values) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < values.size(); i++) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append(escapeCsvValue(values.get(i)));
        }
        return sb.toString();
    }
    
    /**
     * Escape single CSV value
     */
    private String escapeCsvValue(String value) {
        if (value == null) {
            return "";
        }
        
        // If value contains comma, quote, or newline, wrap in quotes and escape quotes
        if (value.contains(",") || value.contains("\"") || value.contains("\n") || value.contains("\r")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        
        return value;
    }
}
