package com.motorbike.business.usecase.input;

import com.motorbike.business.dto.archiveproduct.ArchiveProductInputData;

public interface ArchiveProductInputBoundary {
    void execute(ArchiveProductInputData inputData);
}
