package com.motorbike.business.usecase.input;

import com.motorbike.business.dto.parseexceldata.ParseExcelDataInputData;

public interface ParseExcelDataInputBoundary {
    void execute(ParseExcelDataInputData inputData);
}
