package com.motorbike.business.usecase.input;

import com.motorbike.business.dto.generateimportreport.GenerateImportReportInputData;

public interface GenerateImportReportInputBoundary {
    void execute(GenerateImportReportInputData inputData);
}
