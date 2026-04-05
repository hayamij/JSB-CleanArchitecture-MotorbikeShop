package com.motorbike.business.usecase.input;

import com.motorbike.business.dto.formatdataforexport.FormatDataForExportInputData;

public interface FormatDataForExportInputBoundary {
    void execute(FormatDataForExportInputData inputData);
}
