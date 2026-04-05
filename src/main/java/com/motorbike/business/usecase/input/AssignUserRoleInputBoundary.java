package com.motorbike.business.usecase.input;

import com.motorbike.business.dto.assignuserrole.AssignUserRoleInputData;

public interface AssignUserRoleInputBoundary {
    
    void execute(AssignUserRoleInputData inputData);
}
