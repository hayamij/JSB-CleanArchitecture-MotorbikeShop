package com.motorbike.business.usecase.output;

import com.motorbike.business.dto.parseexceldata.ParseExcelDataOutputData;

public interface ParseExcelDataOutputBoundary {
    void present(ParseExcelDataOutputData outputData);
}
