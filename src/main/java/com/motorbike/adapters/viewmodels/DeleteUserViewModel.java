package com.motorbike.adapters.viewmodels;

import org.springframework.stereotype.Component;
import org.springframework.http.ResponseEntity;

import com.motorbike.business.dto.deleteuser.DeleteUserOutputData;
import com.motorbike.business.usecase.output.DeleteUserOutputBoundary;
import com.motorbike.adapters.dto.response.DeleteUserResponse;

@Component
public class DeleteUserViewModel implements DeleteUserOutputBoundary {

    private ResponseEntity<DeleteUserResponse> response;

    @Override
    public void present(DeleteUserOutputData outputData) {
        if (outputData == null) {
            response = ResponseEntity.status(500).body(DeleteUserResponse.error("SYSTEM_ERROR", "Null output"));
            return;
        }

        if (!outputData.isSuccess()) {
            response = ResponseEntity.badRequest().body(DeleteUserResponse.error(outputData.getErrorCode(), outputData.getMessage()));
            return;
        }

        response = ResponseEntity.ok(DeleteUserResponse.success());
    }

    public ResponseEntity<DeleteUserResponse> getResponse() {
        return response;
    }
}