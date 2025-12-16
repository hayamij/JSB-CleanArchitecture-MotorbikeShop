package com.motorbike.adapters.viewmodels;

import org.springframework.stereotype.Component;
import org.springframework.http.ResponseEntity;

import com.motorbike.business.dto.adduser.AddUserOutputData;
import com.motorbike.business.usecase.output.AddUserOutputBoundary;
import com.motorbike.adapters.dto.response.AddUserResponse;

@Component
public class AddUserViewModel implements AddUserOutputBoundary {

    private ResponseEntity<AddUserResponse> response;

    @Override
    public void present(AddUserOutputData outputData) {
        if (outputData == null) {
            response = ResponseEntity.status(500).body(AddUserResponse.error("SYSTEM_ERROR", "Null output"));
            return;
        }

        if (!outputData.isSuccess()) {
            response = ResponseEntity.badRequest().body(AddUserResponse.error(outputData.getErrorCode(), outputData.getMessage()));
            return;
        }

        AddUserResponse.SuccessData s = new AddUserResponse.SuccessData(
                outputData.getId(),
                outputData.getEmail(),
                outputData.getUsername(),
                outputData.getRole(),
                outputData.isActive(),
                outputData.getCreatedAt()
        );

        response = ResponseEntity.ok(AddUserResponse.success(s));
    }

    public ResponseEntity<AddUserResponse> getResponse() {
        return response;
    }
}