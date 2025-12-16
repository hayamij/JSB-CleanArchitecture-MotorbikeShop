package com.motorbike.adapters.viewmodels;

import org.springframework.stereotype.Component;
import org.springframework.http.ResponseEntity;

import com.motorbike.business.dto.listusers.ListUsersOutputData;
import com.motorbike.business.usecase.output.ListUsersOutputBoundary;
import com.motorbike.adapters.dto.response.ListUsersResponse;
import com.motorbike.adapters.dto.response.ListUsersResponse.UserItemResponse;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ListUsersViewModel implements ListUsersOutputBoundary {

    private ResponseEntity<ListUsersResponse> response;

    @Override
    public void present(ListUsersOutputData outputData) {
        if (outputData == null) {
            response = ResponseEntity.status(500).body(ListUsersResponse.error("SYSTEM_ERROR", "Null output"));
            return;
        }

        if (!outputData.isSuccess()) {
            response = ResponseEntity.badRequest().body(ListUsersResponse.error(outputData.getErrorCode(), outputData.getMessage()));
            return;
        }

        List<UserItemResponse> users = outputData.getUsers().stream()
                .map(u -> new UserItemResponse(
                        u.id,
                        u.email,
                        u.username,
                        u.role,
                        u.active,
                        u.createdAt,
                        u.updatedAt,
                        u.lastLogin
                ))
                .collect(Collectors.toList());

        response = ResponseEntity.ok(ListUsersResponse.success(users));
    }

    public ResponseEntity<ListUsersResponse> getResponse() {
        return response;
    }
}