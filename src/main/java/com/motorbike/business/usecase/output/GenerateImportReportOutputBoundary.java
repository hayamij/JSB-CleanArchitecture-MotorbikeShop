package com.motorbike.business.usecase.output;

import com.motorbike.business.dto.generateimportreport.GenerateImportReportOutputData;

public interface GenerateImportReportOutputBoundary {
    void present(GenerateImportReportOutputData outputData);
}
